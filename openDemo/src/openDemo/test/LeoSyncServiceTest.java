package openDemo.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.sync.LeoOuInfoModel;
import openDemo.entity.sync.LeoPositionModel;
import openDemo.entity.sync.LeoResEmpData;
import openDemo.entity.sync.LeoResJsonModel;
import openDemo.entity.sync.LeoResOrgData;
import openDemo.entity.sync.LeoResPosData;
import openDemo.entity.sync.LeoUserInfoModel;
import openDemo.utils.HttpClientUtil4Sync;

public class LeoSyncServiceTest {
	private static final SimpleDateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	public static void main(String[] args) throws UnsupportedOperationException, IOException {
		// getToken();
		// getEmps();
		// getOrgs();
		// getPoss();
		readJson();
	}

	static void readJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setDateFormat(JSON_DATE_FORMAT);

		LeoResJsonModel<LeoResEmpData> empModel = mapper.readValue(
				new File("D:\\Repository\\GitRemote\\temp\\openDemo\\src\\test1.json"),
				new TypeReference<LeoResJsonModel<LeoResEmpData>>() {
				});
		List<LeoUserInfoModel> empList = empModel.getData().getDataList();
		for (LeoUserInfoModel user : empList) {
			System.out.println(user.getID() + "=" + user.getUserName() + "=" + user.getCnName() + "=" + user.getSex()
					+ "=" + user.getOrgOuCode() + "=" + user.getMail() + "=" + user.getBirthday());
		}

		LeoResJsonModel<LeoResOrgData> orgModel = mapper.readValue(
				new File("D:\\Repository\\GitRemote\\temp\\openDemo\\src\\test2.json"),
				new TypeReference<LeoResJsonModel<LeoResOrgData>>() {
				});
		List<LeoOuInfoModel> orgList = orgModel.getData().getDataList();
		for (LeoOuInfoModel org : orgList) {
			System.out.println(org.getID() + "=" + org.getOuName() + "=" + org.getParentID());
		}

		LeoResJsonModel<LeoResPosData> posModel = mapper.readValue(
				new File("D:\\Repository\\GitRemote\\temp\\openDemo\\src\\test3.json"),
				new TypeReference<LeoResJsonModel<LeoResPosData>>() {
				});
		List<LeoPositionModel> posList = posModel.getData().getDataList();
		for (LeoPositionModel pos : posList) {
			System.out.println(pos.getpNo() + "=" + pos.getpNames());
		}
	}

	static void getEmps() throws IOException {
		String url = "https://open.leo.cn/v1/hr/employees/last-updated?from=";

		Map<String, Object> map = new HashMap<>();
		map.put("from", "1501571786");
		System.out.println(HttpClientUtil4Sync.doGet(url, map, getAuthHeader()));
	}

	static void getOrgs() throws IOException {
		String url = "https://open.leo.cn/v1/hr/origizations/last-updated?from=";

		Map<String, Object> map = new HashMap<>();
		map.put("from", "1501571786");
		System.out.println(HttpClientUtil4Sync.doGet(url, map, getAuthHeader()));
	}

	static void getPoss() throws IOException {
		String url = "https://open.leo.cn/v1/hr/job-positions/last-updated?from=";

		Map<String, Object> map = new HashMap<>();
		map.put("from", "1501571786");
		System.out.println(HttpClientUtil4Sync.doGet(url, map, getAuthHeader()));
	}

	static void getToken() throws IOException {
		String url = "https://open.leo.cn/v1/authentication/oauth2/get-token";

		Map<String, Object> map = new HashMap<>();
		map.put("access_key", "oleo_42db6ee396eb8765435e44446befad8e");
		map.put("secret_key", "5f81f9a50e7c4043efece652b7a82be2d0d90839b9b550b66c1fb865480a6aad");

		System.out.println(HttpClientUtil4Sync.doPost(url, map));
	}

	private static List<Header> getAuthHeader() {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Authorization",
				"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJvbGVvXzQyZGI2ZWUzOTZlYjg3NjU0MzVlNDQ0NDZiZWZhZDhlIiwiaWF0IjoxNTAzOTc1MTQ1LCJleHAiOjE1MDM5Nzg3NDV9.1H-SBikY3uSMB7mKH1eJTRjZtAggp9TtHJPgajWMPJk"));
		return headers;
	}
}
