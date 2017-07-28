package openDemo.test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.sync.OpOuInfoModel;
import openDemo.entity.sync.OpReqJsonModle;
import openDemo.service.sync.OpSyncService;

public class OpSyncServiceTest {

	public static void main(String[] args) throws IOException, ReflectiveOperationException, SQLException {
		Date startDate = new Date();
		System.out.println("同步中......");

		OpSyncService opSyncService = new OpSyncService();
		try {
			opSyncService.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// String query = "QueryEmpInfo";// QueryOrgInfo
		// String mode = "3";
		// System.out.println(opSyncService.getJsonPost(query, mode));
		// printOpOuInfoModel(opSyncService.getJsonPost(query, mode));
		// opSyncService.opOrgSync(query, mode, false);

		System.out.println("同步时间：" + calcMinutesBetween(startDate, new Date()));
	}

	static void printOpOuInfoModel(String jsonString) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setDateFormat(new SimpleDateFormat("yyyyMMdd"));

		OpReqJsonModle<OpOuInfoModel> modle = new OpReqJsonModle<>();
		modle = mapper.readValue(jsonString, new TypeReference<OpReqJsonModle<OpOuInfoModel>>() {
		});

		for (OpOuInfoModel model : modle.getEsbResData().get("SapMiddleOrg")) {
			System.out.println(model.getID() + "==" + model.getOuName());
		}
	}

	private static long calcMinutesBetween(Date d1, Date d2) {
		return Math.abs((d2.getTime() - d1.getTime())) / 1000;
	}

}
