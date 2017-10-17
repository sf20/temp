package openDemo.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.sync.LeoOuInfoModel;
import openDemo.entity.sync.LeoPositionModel;
import openDemo.entity.sync.LeoResJsonModel;
import openDemo.entity.sync.LeoUserInfoModel;
import openDemo.service.sync.LeoSyncService;
import openDemo.utils.HttpClientUtil4Sync;

public class LeoSyncServiceTest {
	private static final SimpleDateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final int FROM_TIMESTAMP = 1501516800;
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setDateFormat(JSON_DATE_FORMAT);
	}

	public static void main(String[] args) throws UnsupportedOperationException, IOException {
		// getEmps();
		// getOrgs();
		// getPoss();
		// readJson();
		leoSyncServiceTest();
	}

	static void leoSyncServiceTest() {
		Date startDate = new Date();
		System.out.println("同步中......");

		LeoSyncService leoSyncService = new LeoSyncService();
		try {
			leoSyncService.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("同步时间：" + calcMinutesBetween(startDate, new Date()));
	}

	private static long calcMinutesBetween(Date d1, Date d2) {
		return Math.abs((d2.getTime() - d1.getTime())) / 1000;
	}

	static void readJson() throws JsonParseException, JsonMappingException, IOException {

		LeoResJsonModel<LeoUserInfoModel> empModel = mapper.readValue(
				new File("D:\\Repository\\GitRemote\\temp\\openDemo\\src\\test1.json"),
				new TypeReference<LeoResJsonModel<LeoUserInfoModel>>() {
				});
		System.out.println(empModel.getData().getTotal());
		List<LeoUserInfoModel> empList = empModel.getData().getEmployees();
		for (LeoUserInfoModel user : empList) {
			System.out.println(user.getID() + "=" + user.getUserName() + "=" + user.getCnName() + "=" + user.getSex()
					+ "=" + user.getOrgOuCode() + "=" + user.getMail() + "=" + user.getBirthday());
		}

		LeoResJsonModel<LeoOuInfoModel> orgModel = mapper.readValue(
				new File("D:\\Repository\\GitRemote\\temp\\openDemo\\src\\test2.json"),
				new TypeReference<LeoResJsonModel<LeoOuInfoModel>>() {
				});
		System.out.println(empModel.getData().getTotal());
		List<LeoOuInfoModel> orgList = orgModel.getData().getOrigizations();
		for (LeoOuInfoModel org : orgList) {
			System.out.println(org.getID() + "=" + org.getOuName() + "=" + org.getParentID());
		}

		LeoResJsonModel<LeoPositionModel> posModel = mapper.readValue(
				new File("D:\\Repository\\GitRemote\\temp\\openDemo\\src\\test3.json"),
				new TypeReference<LeoResJsonModel<LeoPositionModel>>() {
				});
		System.out.println(empModel.getData().getTotal());
		List<LeoPositionModel> posList = posModel.getData().getJobPositions();
		for (LeoPositionModel pos : posList) {
			System.out.println(pos.getpNo() + "=" + pos.getpNames());
		}
	}

	static void getEmps() throws IOException {
		String url = "https://open.leo.cn/v1/hr/employees/last-updated";

		Map<String, Object> map = new HashMap<>();
		map.put("from", FROM_TIMESTAMP);
		System.out.println(HttpClientUtil4Sync.doGet(url, map, getAuthHeader()));
	}

	static void getOrgs() throws IOException {
		String url = "https://open.leo.cn/v1/hr/origizations/last-updated";

		Map<String, Object> map = new HashMap<>();
		map.put("from", FROM_TIMESTAMP);
		System.out.println(HttpClientUtil4Sync.doGet(url, map, getAuthHeader()));
	}

	static void getPoss() throws IOException {
		String url = "https://open.leo.cn/v1/hr/job-positions/last-updated";

		Map<String, Object> map = new HashMap<>();
		map.put("from", FROM_TIMESTAMP);
		System.out.println(HttpClientUtil4Sync.doGet(url, map, getAuthHeader()));
	}

	private static String getToken() throws IOException {
		String url = "https://open.leo.cn/v1/authentication/oauth2/get-token";

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("access_key", "oleo_42db6ee396eb8765435e44446befad8e");
		paramMap.put("secret_key", "5f81f9a50e7c4043efece652b7a82be2d0d90839b9b550b66c1fb865480a6aad");

		JsonNode jsonNode = mapper.readTree(HttpClientUtil4Sync.doPost(url, paramMap));
		String token = jsonNode.get("data").get("token").asText();
		String exp = jsonNode.get("data").get("exp").asText();
		System.out.println(exp);

		return token;
	}

	private static List<Header> getAuthHeader() throws IOException {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Authorization", "Bearer " + getToken()));
		return headers;
	}
}
