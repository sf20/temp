package openDemo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EsbService {

	private final String REQUESTID = "RequestId";
	private final String SERVICENAME = "ServiceName";
	private final String SERVICEOPERATION = "ServiceOperation";
	private final String SERVICEVERSION = "ServiceVersion";
	private final String MODE = "Mode";
	private final String ESBREQHEAD = "EsbReqHead";
	private final String ESBREQDATA = "EsbReqData";

	private final String REQUEST_URL = "http://58.211.235.246:50831/hcm_emp/json";
	private final String USERNAME = "yxtuser";
	private final String PASSWORD = "yxtpwd";
	private final String SERVICE_NAME = "YXT_HCM_EmployeeMasterData";
	private final String SERVICE_VERSION = "1.0";

	private final String CHARSET_UTF8 = "UTF-8";

	public String getJsonPost(String serviceOperation, String mode) {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(REQUEST_URL);

		// 请求header中增加Auth部分
		httpPost.addHeader("Authorization", getBasicAuthHeader(USERNAME, PASSWORD));

		// 构建消息实体 发送Json格式的数据
		StringEntity entity = new StringEntity(getJson(serviceOperation, mode), ContentType.APPLICATION_JSON);
		entity.setContentEncoding(CHARSET_UTF8);
		httpPost.setEntity(entity);

		HttpResponse httpResponse = null;
		String responseStr = null;
		try {
			// 发送post请求
			httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				responseStr = EntityUtils.toString(httpResponse.getEntity(), CHARSET_UTF8);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
				HttpClientUtils.closeQuietly(httpClient);
			}

			if (httpResponse != null) {
				HttpClientUtils.closeQuietly(httpResponse);
			}
		}

		return responseStr;
	}

	/**
	 * 请求header中增加Auth部分 Auth类型：Basic
	 * 
	 * @param username
	 * @param password
	 * @return Auth请求头内容
	 */
	private String getBasicAuthHeader(String username, String password) {
		String auth = username + ":" + password;
		byte[] encodedAuth = null;
		String authHeader = null;
		try {
			encodedAuth = Base64.encodeBase64(auth.getBytes(CHARSET_UTF8));
			authHeader = "Basic " + new String(encodedAuth, CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return authHeader;
	}

	/**
	 * 构造符合客户要求的请求报文
	 * 
	 * @param serviceOperation
	 *            可在QueryEmpInfo（员工数据）和QueryOrgInfo（组织架构）中二选一
	 * @param mode
	 *            可在1（全量）和2（增量）中二选一。EMP拥有1和2两种模式。Org只有1，全量模式。
	 * @return
	 */
	private String getJson(String serviceOperation, String mode) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();

		Map<String, Object> reqHeadMap = new HashMap<>();
		reqHeadMap.put(REQUESTID, UUID.randomUUID().toString());
		reqHeadMap.put(SERVICENAME, SERVICE_NAME);
		reqHeadMap.put(SERVICEOPERATION, serviceOperation);
		reqHeadMap.put(SERVICEVERSION, SERVICE_VERSION);

		Map<String, Object> reqDataMap = new HashMap<>();
		reqDataMap.put(MODE, mode);

		map.put(ESBREQHEAD, reqHeadMap);
		map.put(ESBREQDATA, reqDataMap);

		String str = "";
		try {
			str = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return str;
	}
}
