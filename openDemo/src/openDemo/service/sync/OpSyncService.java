package openDemo.service.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.OuInfoEntity;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoEntity;
import openDemo.entity.sync.OpOuInfoModel;
import openDemo.entity.sync.OpReqJsonModle;
import openDemo.entity.sync.OpUserInfoModel;
import openDemo.service.OrgService;
import openDemo.service.UserService;
import openDemo.test.EsbServiceTest;

public class OpSyncService {

	private static final String REQUESTID = "RequestId";
	private static final String SERVICENAME = "ServiceName";
	private static final String SERVICEOPERATION = "ServiceOperation";
	private static final String SERVICEVERSION = "ServiceVersion";
	private static final String MODE = "Mode";
	private static final String ESBREQHEAD = "EsbReqHead";
	private static final String ESBREQDATA = "EsbReqData";

	private static final String REQUEST_URL = "http://esbuat.opple.com:50831/esb_emp/json";
	private static final String USERNAME = "yxtuser";
	private static final String PASSWORD = "yxtpwd";
	private static final String SERVICE_NAME = "YXT_ESB_EmpOrgQuery";
	private static final String SERVICE_VERSION = "1.0";

	private static final String CHARSET_UTF8 = "UTF-8";

	// 客户提供接口返回的json数据中组织数据和员工数据的key
	private static final String ORG_RES_DATA_KEY = "SapMiddleOrg";
	private static final String EMP_RES_DATA_KEY = "SapMiddleEmp";

	private static String REPLACE_FROM = "&";
	private static String REPLACE_TO = " ";
	private static int SIZE_PER_SYNC = 1500;// 每次同步的数量 当每次同步数量过大(同步时间超过5分钟)会导致网关超时504错误

	private static SimpleDateFormat javaDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyyMMdd");

	private OrgService orgService = new OrgService();
	private UserService userService = new UserService();
	private ObjectMapper mapper;

	public OpSyncService() {
		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// json字符串的日期格式
		mapper.setDateFormat(jsonDateFormat);
	}

	/**
	 * 向客户提供的接口发送POST请求并获取json数据
	 * 
	 * @param serviceOperation
	 *            可在QueryEmpInfo（员工数据）和QueryOrgInfo（组织架构）中二选一
	 * @param mode
	 *            可在1（全量）和2（增量）中二选一。EMP拥有1和2两种模式。Org只有1，全量模式。
	 * @return 响应的json字符串
	 */
	public String getJsonPost(String serviceOperation, String mode) {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(REQUEST_URL);

		// 请求header中增加Auth部分
		httpPost.addHeader("Authorization", getBasicAuthHeader(USERNAME, PASSWORD));

		// 构建消息实体 发送Json格式的数据
		StringEntity entity = new StringEntity(buildReqJson(serviceOperation, mode), ContentType.APPLICATION_JSON);
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
			// TODO
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
	private String buildReqJson(String serviceOperation, String mode) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();

		Map<String, Object> reqHeadMap = new HashMap<>();
		reqHeadMap.put(REQUESTID, UUID.randomUUID().toString());
		reqHeadMap.put(SERVICENAME, SERVICE_NAME);
		reqHeadMap.put(SERVICEOPERATION, serviceOperation);
		reqHeadMap.put(SERVICEVERSION, SERVICE_VERSION);
		map.put(ESBREQHEAD, reqHeadMap);

		Map<String, Object> reqDataMap = new HashMap<>();
		reqDataMap.put(MODE, mode);
		map.put(ESBREQDATA, reqDataMap);

		String str = "";
		try {
			str = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 组织同步 用时：80-90s
	 */
	public void opOrgSync() {
		// String jsonString = service.getJsonPost("QueryOrgInfo", "1");
		// System.out.println(jsonString);

		String jsonString = getFileStr("/new_depts.txt");

		OpReqJsonModle<OpOuInfoModel> modle = new OpReqJsonModle<>();
		try {
			modle = mapper.readValue(jsonString, new TypeReference<OpReqJsonModle<OpOuInfoModel>>() {
			});
		} catch (IOException e1) {
			// TODO
			e1.printStackTrace();
		}

		List<OuInfoEntity> list = null;
		try {
			list = copyCreateEntityList(modle.getEsbResData().get(ORG_RES_DATA_KEY), OuInfoEntity.class);
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			// TODO
			e.printStackTrace();
		}

		replaceIllegalChar(list);

		// TODO to delete
		printOrgInfo(list);
		ResultEntity resultEntity = orgService.ous(false, list);
		print("同步组织", resultEntity);
	}

	/**
	 * 通过复制属性值的方法将json数据模型集合转换为同步用的对象集合
	 * 
	 * @param fromList
	 *            json数据模型集合
	 * @param toListClassType
	 *            复制目标对象的类型
	 * @return 复制后的对象集合
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private <E, T> List<T> copyCreateEntityList(List<E> fromList, Class<T> toListClassType)
			throws IllegalAccessException, InvocationTargetException, InstantiationException {
		List<T> entityList = null;

		int listSize = fromList.size();
		if (fromList != null && listSize > 0) {
			entityList = new ArrayList<T>(listSize);

			for (int i = 0; i < listSize; i++) {
				T instance = toListClassType.newInstance();
				BeanUtils.copyProperties(instance, fromList.get(i));
				entityList.add(instance);
			}
		}

		return entityList;
	}

	/**
	 * 用户同步
	 */
	public void opUserSync() {
		// String jsonString = service.getJsonPost("QueryEmpInfo", "2");
		// System.out.println(jsonString);
		String jsonString = getFileStr("/userM2.json");

		OpReqJsonModle<OpUserInfoModel> modle = new OpReqJsonModle<>();
		try {
			modle = mapper.readValue(jsonString, new TypeReference<OpReqJsonModle<OpUserInfoModel>>() {
			});
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}

		List<UserInfoEntity> list = null;
		try {
			list = copyCreateEntityList(modle.getEsbResData().get(EMP_RES_DATA_KEY), UserInfoEntity.class);
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			// TODO
			e.printStackTrace();
		}

		// TODO to delete
		printUserInfo(list);

		ResultEntity resultEntity = new ResultEntity();
		int listSize = list.size();
		int syncTimes = calcSyncTimes(listSize, SIZE_PER_SYNC);
		for (int count = 0; count < syncTimes; count++) {
			// 最后一次同步
			if (count == syncTimes - 1) {
				resultEntity = userService.userSync(false, list.subList(count * SIZE_PER_SYNC, listSize));
			} else {
				resultEntity = userService.userSync(false,
						list.subList(count * SIZE_PER_SYNC, (count + 1) * SIZE_PER_SYNC));
			}

			// TODO to delete
			print("用户同步" + (count + 1), resultEntity);
		}
	}

	/**
	 * 将组织名中的"&"替换为" "
	 * 
	 * @param list
	 */
	private void replaceIllegalChar(List<OuInfoEntity> list) {
		for (OuInfoEntity entity : list) {
			String ouName = entity.getOuName();
			if (ouName != null && ouName.contains(REPLACE_FROM)) {
				entity.setOuName(ouName.replaceAll(REPLACE_FROM, REPLACE_TO));
			}
		}
	}

	/**
	 * 根据同步总量和每次同步数量计算同步次数
	 * 
	 * @param totalSize
	 *            同步总量
	 * @param sizePerSync
	 *            每次同步数量
	 * @return
	 */
	private int calcSyncTimes(int totalSize, int sizePerSync) {
		if (totalSize <= sizePerSync) {
			return 1;
		} else {
			return totalSize / sizePerSync + (totalSize % sizePerSync > 0 ? 1 : 0);
		}
	}

	// TODO====================测试用方法======================
	private void printOrgInfo(List<OuInfoEntity> list) {
		System.out.println("ID == OuName == ParentID == Description == Users == isSub");
		for (OuInfoEntity org : list) {
			System.out.println(org.getID() + " == " + org.getOuName() + " == " + org.getParentID() + " == "
					+ org.getDescription() + " == " + org.getUsers() + " == " + org.getIsSub());
		}
		System.out.println("total size: " + list.size());
	}

	private void printUserInfo(List<UserInfoEntity> list) {
		System.out.println(
				"ID == UserName == CnName == Password == Sex == Mobile == Mail == OrgOuCode == PostionNo == entryTime == Spare1");
		for (UserInfoEntity user : list) {
			Date entryTime = user.getEntryTime();
			String entryTimeStr = null;
			if (entryTime != null) {
				entryTimeStr = javaDateFormat.format(entryTime);
			}

			System.out.println(user.getID() + " == " + user.getUserName() + " == " + user.getCnName() + " == "
					+ user.getPassword() + " == " + user.getSex() + " == " + user.getMobile() + " == " + user.getMail()
					+ " == " + user.getOrgOuCode() + " == " + user.getPostionNo() + " == " + entryTimeStr + " == "
					+ user.getSpare1());
		}
		System.out.println("total size: " + list.size());
	}

	private void print(String name, ResultEntity resultEntity) {
		System.out.println(name + ":" + resultEntity.getCode() + "==" + resultEntity.getData() + "=="
				+ resultEntity.getMessage() + "==" + resultEntity.getTotalCount());
	}

	private String getFileStr(String filePath) {
		InputStream inputStream = EsbServiceTest.class.getResourceAsStream(filePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));

		StringBuffer buffer = new StringBuffer();
		String temp = null;
		try {
			while ((temp = reader.readLine()) != null) {
				buffer.append(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return buffer.toString();
	}

}
