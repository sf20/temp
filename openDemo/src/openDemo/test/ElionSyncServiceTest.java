package openDemo.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.service.sync.LeoSyncService;
import openDemo.utils.HttpClientUtil4Sync;

public class ElionSyncServiceTest {
	private static final String REQUEST_URL = "http://119.61.11.215:8080/PSIGW/PeopleSoftServiceListeningConnector/PSFT_HR/EL_INT_JOBCD_FULLSYNC_SVC.1.wsdl?Username='EL_INTERFACE'&Password='interface'";
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final SimpleDateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final int FROM_TIMESTAMP = 1501516800;
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setDateFormat(JSON_DATE_FORMAT);
	}

	public static void main(String[] args) throws UnsupportedOperationException, IOException {
		// leoSyncServiceTest();
		// ElionSyncServiceTest.class.getResource("");

		getPoss();
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

	static void getPoss() throws IOException {
		String requestEntity = FileUtils.readFileToString(
				new File("D:\\Repository\\GitRemote\\temp\\openDemo\\src\\openDemo\\test\\pos.xml"), "utf-8");

		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("SOAPAction", "EL_INT_JOBCD_FULLSYNC_OP.v1"));
		// headers.add(new BasicHeader("Authorization", getBasicAuthHeader("EL_INTERFACE", "interface")));

		System.out.println(HttpClientUtil4Sync.doPost(REQUEST_URL, requestEntity, headers));
	}

	static String getBasicAuthHeader(String username, String password) throws UnsupportedEncodingException {
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(CHARSET_UTF8));
		String authHeader = "Basic " + new String(encodedAuth, CHARSET_UTF8);

		return authHeader;
	}
}
