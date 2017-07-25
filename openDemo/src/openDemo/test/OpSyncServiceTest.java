package openDemo.test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.sync.OpOuInfoModel;
import openDemo.entity.sync.OpReqJsonModle;
import openDemo.service.sync.OpSyncService;

public class OpSyncServiceTest {
	private static final Logger logger = LogManager.getLogger(OpSyncServiceTest.class);

	public static void main(String[] args) throws IOException {
		Date startDate = new Date();
		System.out.println("同步中......");

		OpSyncService opSyncService = new OpSyncService();
		try {
			opSyncService.sync();
		} catch (IOException e) {
			logger.error("同步出现异常...");
			logger.error(e.getMessage());
		} catch (ReflectiveOperationException e) {
			logger.error("同步出现异常...");
			logger.error(e.getMessage());
		} catch (SQLException e) {
			logger.error("同步出现异常...");
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error("同步出现异常...");
			logger.error(e.getMessage());
		}
		// String query = "QueryOrgInfo";// QueryEmpInfo
		// String mode = "1";
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