package openDemo.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.sync.OpOuInfoModel;
import openDemo.entity.sync.OpReqJsonModle;
import openDemo.service.sync.OppleSyncService;

public class OppleSyncServiceTest {

	public static void main(String[] args) throws Exception {
		// opSyncServiceTest();

		postGetJsonTest();
	}

	static void postGetJsonTest() throws Exception {
		 String query = "QueryEmpInfo";// QueryOrgInfo
		 String mode = "1";
		 System.out.println(OppleSyncService.getJsonPost(query, mode));
	}

	static void opSyncServiceTest() {
		Date startDate = new Date();
		System.out.println("同步中......");

		OppleSyncService opSyncService = new OppleSyncService();
		try {
			opSyncService.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("同步时间：" + calcMinutesBetween(startDate, new Date()));
	}

	void printOpOuInfoModel(String jsonString) throws IOException {
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
