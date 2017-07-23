package openDemo.service.sync;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.dao.OuInfoDao;
import openDemo.dao.UserInfoDao;
import openDemo.entity.OuInfoEntity;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoEntity;
import openDemo.entity.sync.OpOuInfoModel;
import openDemo.entity.sync.OpReqJsonModle;
import openDemo.entity.sync.OpUserInfoModel;
import openDemo.service.OrgService;
import openDemo.service.UserService;

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

	private static final String SERVICEOPERATION_EMP = "QueryEmpInfo";
	private static final String SERVICEOPERATION_ORG = "QueryOrgInfo";
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";

	private static String REPLACE_FROM = "&";
	private static String REPLACE_TO = " ";

	private static String MAPKEY_USER_SYNC_ADD = "userSyncAdd";
	private static String MAPKEY_USER_SYNC_UPDATE = "userSyncUpdate";
	private static String MAPKEY_USER_SYNC_DELETE = "userSyncDelete";
	// private static String MAPKEY_USER_SYNC_ENABLE = "userSyncEnable";
	// private static String MAPKEY_USER_SYNC_DISABLE = "userSyncDisable";
	private static String MAPKEY_ORG_SYNC_ADD = "orgSyncAdd";
	private static String MAPKEY_ORG_SYNC_UPDATE = "orgSyncUpdate";
	private static String MAPKEY_ORG_SYNC_DELETE = "orgSyncDelete";

	private static String SYNC_CODE_SUCCESS = "0";

	private static SimpleDateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat JAVA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private OrgService orgService = new OrgService();
	private UserService userService = new UserService();
	private UserInfoDao userInfoDao = new UserInfoDao();
	private OuInfoDao ouInfoDao = new OuInfoDao();
	private ObjectMapper mapper;

	private static final Logger logger = LogManager.getLogger(OpSyncService.class);

	public OpSyncService() {
		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// json字符串的日期格式
		mapper.setDateFormat(JSON_DATE_FORMAT);
	}

	/**
	 * 对外提供的同步方法
	 * 
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 * @throws SQLException
	 */
	public void sync() throws IOException, ReflectiveOperationException, SQLException {
		int orgCount = ouInfoDao.getAllCount();
		if (orgCount > 0) {
			// 组织增量同步
			logger.info("组织增量同步开始...");
			opOrgSync(SERVICEOPERATION_ORG, MODE_UPDATE, false);
			logger.info("组织增量同步结束");
		} else {
			// 组织全量同步
			logger.info("组织全量同步开始...");
			opOrgSync(SERVICEOPERATION_ORG, MODE_FULL, false);
			logger.info("组织全量同步结束");
		}

		int userCount = userInfoDao.getAllCount();
		if (userCount > 0) {
			// 用户增量同步
			logger.info("用户增量同步开始...");
			opUserSync(SERVICEOPERATION_EMP, MODE_UPDATE, true);
			logger.info("用户增量同步结束");
		} else {
			// 用户全量同步
			logger.info("用户全量同步开始...");
			opUserSync(SERVICEOPERATION_EMP, MODE_FULL, true);
			logger.info("用户全量同步结束");
		}

		// TODO 岗位同步

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
	 * 
	 * @param serviceOperation
	 * @param mode
	 * @param isBaseInfo
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 * @throws SQLException
	 */
	public void opOrgSync(String serviceOperation, String mode, boolean isBaseInfo)
			throws IOException, ReflectiveOperationException, SQLException {
		String jsonString = getJsonPost(serviceOperation, mode);

		OpReqJsonModle<OpOuInfoModel> modle = mapper.readValue(jsonString,
				new TypeReference<OpReqJsonModle<OpOuInfoModel>>() {
				});

		List<OuInfoEntity> newList = copyCreateEntityList(modle.getEsbResData().get(ORG_RES_DATA_KEY),
				OuInfoEntity.class);

		replaceIllegalChar(newList);

		// 全量模式
		if (MODE_FULL.equals(mode)) {
			removeExpiredOrgs(newList);
			syncAddOrgOneByOne(newList, isBaseInfo);
		}
		// 增量模式
		else {
			// 获取数据库全量list
			List<OuInfoEntity> allList = ouInfoDao.getAll();

			Map<String, List<OuInfoEntity>> map = compareOrgList(allList, newList);
			List<OuInfoEntity> orgsToSyncAdd = map.get(MAPKEY_ORG_SYNC_ADD);
			if (orgsToSyncAdd.size() > 0) {
				syncAddOrgOneByOne(orgsToSyncAdd, isBaseInfo);
			}

			List<OuInfoEntity> orgsToSyncUpdate = map.get(MAPKEY_ORG_SYNC_UPDATE);
			if (orgsToSyncUpdate.size() > 0) {
				syncUpdateOrgOneByOne(orgsToSyncUpdate, isBaseInfo);
			}

			List<OuInfoEntity> orgsToSyncDelete = map.get(MAPKEY_ORG_SYNC_DELETE);
			if (orgsToSyncDelete.size() > 0) {
				syncDeleteOrgOneByOne(orgsToSyncDelete, isBaseInfo);
			}
		}
	}

	/**
	 * 去除过期组织
	 * 
	 * @param list
	 */
	private void removeExpiredOrgs(List<OuInfoEntity> list) {
		for (Iterator<OuInfoEntity> iterator = list.iterator(); iterator.hasNext();) {
			if (isOrgExpired(iterator.next())) {
				iterator.remove();
			}
		}
	}

	/**
	 * 逐个组织同步删除
	 * 
	 * @param orgsToSyncDelete
	 * @param isBaseInfo
	 * @throws SQLException
	 */
	private void syncDeleteOrgOneByOne(List<OuInfoEntity> orgsToSyncDelete, boolean isBaseInfo) throws SQLException {
		List<String> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (OuInfoEntity org : orgsToSyncDelete) {
			String orgId = org.getID();
			tempList.add(orgId);

			resultEntity = orgService.deleteous(tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				ouInfoDao.deleteById(orgId);
			} else {
				printLog("组织同步删除失败", org.getID(), resultEntity);
			}

			tempList.clear();
		}

	}

	/**
	 * 逐个组织同步更新
	 * 
	 * @param orgsToSyncUpdate
	 * @param isBaseInfo
	 * @throws SQLException
	 */
	private void syncUpdateOrgOneByOne(List<OuInfoEntity> orgsToSyncUpdate, boolean isBaseInfo) throws SQLException {
		List<OuInfoEntity> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (OuInfoEntity org : orgsToSyncUpdate) {
			tempList.add(org);

			resultEntity = orgService.ous(isBaseInfo, tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				ouInfoDao.update(org);
			} else {
				printLog("组织同步更新失败", org.getID(), resultEntity);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个组织同步新增
	 * 
	 * @param orgsToSyncAdd
	 * @param isBaseInfo
	 * @throws SQLException
	 */
	private void syncAddOrgOneByOne(List<OuInfoEntity> orgsToSyncAdd, boolean isBaseInfo) throws SQLException {
		List<OuInfoEntity> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (OuInfoEntity org : orgsToSyncAdd) {
			tempList.add(org);

			resultEntity = orgService.ous(isBaseInfo, tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				ouInfoDao.insert(org);
			} else {
				printLog("组织同步新增失败", org.getID(), resultEntity);
			}

			tempList.clear();
		}
	}

	/**
	 * 通过复制属性值的方法将json数据模型集合转换为同步用的对象集合
	 * 
	 * @param fromList
	 *            json数据模型集合
	 * @param toListClassType
	 *            复制目标对象的类型
	 * @return 复制后的对象集合
	 * @throws ReflectiveOperationException
	 */
	private <E, T> List<T> copyCreateEntityList(List<E> fromList, Class<T> toListClassType)
			throws ReflectiveOperationException {
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
	 * 
	 * @param serviceOperation
	 * @param mode
	 * @param islink
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 * @throws SQLException
	 */
	public void opUserSync(String serviceOperation, String mode, boolean islink)
			throws IOException, ReflectiveOperationException, SQLException {
		String jsonString = getJsonPost(serviceOperation, mode);

		OpReqJsonModle<OpUserInfoModel> modle = mapper.readValue(jsonString,
				new TypeReference<OpReqJsonModle<OpUserInfoModel>>() {
				});

		List<OpUserInfoModel> modeList = modle.getEsbResData().get(EMP_RES_DATA_KEY);
		List<UserInfoEntity> newList = copyCreateEntityList(modeList, UserInfoEntity.class);

		// TODO
		tempFixProblem(newList);
		changeDateFormatAndSex(modeList, newList);

		// syncMethod(newList, mode, islink);
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			logger.info("用户同步Total Size: " + newList.size());
			if (newList.size() > 0) {
				removeExpiredUser(newList);
				syncAddOneByOne(newList, islink);
			}
		}
		// 增量模式
		else {
			// 获取数据库全量list
			List<UserInfoEntity> allList = userInfoDao.getAll();
			// 与增量list进行比较
			Map<String, List<UserInfoEntity>> map = compareUserList(allList, newList);

			List<UserInfoEntity> usersToSyncAdd = map.get(MAPKEY_USER_SYNC_ADD);
			if (usersToSyncAdd.size() > 0) {
				syncAddOneByOne(usersToSyncAdd, islink);
			}

			List<UserInfoEntity> usersToSyncUpdate = map.get(MAPKEY_USER_SYNC_UPDATE);
			if (usersToSyncUpdate.size() > 0) {
				syncUpdateOneByOne(usersToSyncUpdate, islink);
			}

			List<UserInfoEntity> usersToDelete = map.get(MAPKEY_USER_SYNC_DELETE);
			if (usersToDelete.size() > 0) {
				syncDeleteOneByOne(usersToDelete);
			}
		}

	}

	/**
	 * 去除过期员工
	 * 
	 * @param newList
	 */
	private void removeExpiredUser(List<UserInfoEntity> newList) {
		for (Iterator<UserInfoEntity> iterator = newList.iterator(); iterator.hasNext();) {
			// 有leavedate离职日期的删除
			if (iterator.next().getExpireDate() != null) {
				iterator.remove();
			}
		}
	}

	/**
	 * 将json模型对象的日期进行格式化(yyyy-MM-dd)后赋值给对应的java同步对象
	 * 
	 * @param fromList
	 *            json模型对象集合
	 * @param toList
	 *            java同步对象集合
	 */
	private void changeDateFormatAndSex(List<OpUserInfoModel> fromList, List<UserInfoEntity> toList) {
		int listSize = toList.size();

		for (int i = 0; i < listSize; i++) {
			Date entryTime = fromList.get(i).getEntryTime();
			if (entryTime != null) {
				toList.get(i).setEntryTime(JAVA_DATE_FORMAT.format(entryTime));
			}

			Date expireDate = fromList.get(i).getExpireDate();
			if (expireDate != null) {
				toList.get(i).setExpireDate(JAVA_DATE_FORMAT.format(expireDate));
			}

			// 性别字符串转换 1：男 2：女
			String sex = fromList.get(i).getSex();
			if ("1".equals(sex)) {
				toList.get(i).setSex("男");
			} else if ("2".equals(sex)) {
				toList.get(i).setSex("女");
			}
		}
	}

	/**
	 * 用于解决返回json字符串中组织没有ID的问题
	 * 
	 * @param newList
	 */
	private void tempFixProblem(List<UserInfoEntity> newList) {
		for (Iterator<UserInfoEntity> iterator = newList.iterator(); iterator.hasNext();) {
			UserInfoEntity userInfoEntity = iterator.next();
			// ID = userName
			userInfoEntity.setID(userInfoEntity.getUserName());
		}
	}

	/**
	 * 逐个用户同步新增
	 * 
	 * @param usersToSyncAdd
	 * @param islink
	 * @throws SQLException
	 */
	private void syncAddOneByOne(List<UserInfoEntity> usersToSyncAdd, boolean islink) throws SQLException {
		List<UserInfoEntity> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (UserInfoEntity user : usersToSyncAdd) {
			tempList.add(user);

			resultEntity = userService.userSync(islink, tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				userInfoDao.insert(user);
			} else {
				printLog("用户同步新增失败", user.getID(), resultEntity);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个用户同步更新
	 * 
	 * @param usersToSyncUpdate
	 * @param islink
	 * @throws SQLException
	 */
	private void syncUpdateOneByOne(List<UserInfoEntity> usersToSyncUpdate, boolean islink) throws SQLException {
		List<UserInfoEntity> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;

		for (UserInfoEntity user : usersToSyncUpdate) {
			tempList.add(user);

			resultEntity = userService.userSync(islink, tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				userInfoDao.update(user);
			} else {
				printLog("用户同步更新失败", user.getID(), resultEntity);
			}

			tempList.clear();
		}

	}

	/**
	 * 逐个用户同步删除
	 * 
	 * @param usersToDelete
	 * @throws SQLException
	 */
	private void syncDeleteOneByOne(List<UserInfoEntity> usersToDelete) throws SQLException {
		List<String> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (UserInfoEntity user : usersToDelete) {
			tempList.add(user.getUserName());

			resultEntity = userService.deletedusersSync(tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				userInfoDao.deleteById(user.getID());
			} else {
				printLog("用户同步删除失败", user.getID(), resultEntity);
			}

			tempList.clear();
		}
	}

	/**
	 * 数据库组织表数据集合与最新获取组织数据集合进行比较
	 * 
	 * @param fullList
	 *            数据库组织表数据集合
	 * @param newList
	 *            最新获取组织数据集合
	 * @return 包含 同步新增、更新、 删除等组织集合的Map对象
	 */
	private Map<String, List<OuInfoEntity>> compareOrgList(List<OuInfoEntity> fullList, List<OuInfoEntity> newList) {
		Map<String, List<OuInfoEntity>> map = new HashMap<>();

		List<OuInfoEntity> orgsToSyncAdd = new ArrayList<>();
		List<OuInfoEntity> orgsToSyncUpdate = new ArrayList<>();
		List<OuInfoEntity> orgsToSyncDelete = new ArrayList<>();

		for (OuInfoEntity fullOrg : fullList) {
			for (OuInfoEntity newOrg : newList) {
				// 已经存在的组织比较
				if (fullOrg.equals(newOrg)) {
					// 组织过期待删除
					if (isOrgExpired(newOrg)) {
						orgsToSyncDelete.add(newOrg);
					} else {
						String fullOrgName = fullOrg.getOuName();
						String newOrgName = newOrg.getOuName();
						// 组织名有变更
						if (fullOrgName == null) {
							if (newOrgName != null) {
								orgsToSyncUpdate.add(newOrg);
							}
						} else {
							if (newOrgName == null) {
								orgsToSyncUpdate.add(newOrg);
							} else {
								if (!newOrgName.equals(fullOrgName)) {
									orgsToSyncUpdate.add(newOrg);
								}
							}
						}
					}
				}
			}
		}

		// 待新增组织
		for (OuInfoEntity org : newList) {
			// 非过期组织
			if (!fullList.contains(org) && !isOrgExpired(org)) {
				orgsToSyncAdd.add(org);
			}
		}

		map.put(MAPKEY_ORG_SYNC_ADD, orgsToSyncAdd);
		map.put(MAPKEY_ORG_SYNC_UPDATE, orgsToSyncUpdate);
		map.put(MAPKEY_ORG_SYNC_DELETE, orgsToSyncDelete);

		logger.info("组织同步Total Size: " + newList.size());
		logger.info("组织同步新增Size: " + orgsToSyncAdd.size());
		logger.info("组织同步更新Size: " + orgsToSyncUpdate.size());
		logger.info("组织同步删除Size: " + orgsToSyncDelete.size());

		return map;
	}

	/**
	 * 判断组织是否过期
	 * 
	 * @param org
	 * @return
	 */
	private boolean isOrgExpired(OuInfoEntity org) {
		Date endDate = org.getEndDate();
		if (endDate == null) {
			return true;
		}

		return endDate.compareTo(new Date()) < 0;
	}

	/**
	 * 数据库用户表数据集合与最新获取用户数据集合进行比较
	 * 
	 * @param fullList
	 *            数据库用户表数据集合
	 * @param newList
	 *            最新获取用户数据集合
	 * @return 包含 同步新增、更新、删除等用户集合的Map对象
	 */
	private Map<String, List<UserInfoEntity>> compareUserList(List<UserInfoEntity> fullList,
			List<UserInfoEntity> newList) {
		Map<String, List<UserInfoEntity>> map = new HashMap<>();

		List<UserInfoEntity> usersToSyncAdd = new ArrayList<>();
		List<UserInfoEntity> usersToSyncUpdate = new ArrayList<>();
		List<UserInfoEntity> usersToDelete = new ArrayList<>();

		for (UserInfoEntity fullUser : fullList) {
			for (UserInfoEntity newUser : newList) {
				// 已经存在的用户比较
				if (fullUser.equals(newUser)) {
					// 用户离职待删除
					if (newUser.getExpireDate() != null) {
						usersToDelete.add(newUser);
					} else {
						// 存在用户更新
						usersToSyncUpdate.add(newUser);
					}
				}
			}
		}

		// 待新增用户
		for (UserInfoEntity user : newList) {
			if (!fullList.contains(user) && user.getExpireDate() == null) {
				usersToSyncAdd.add(user);
			}
		}

		map.put(MAPKEY_USER_SYNC_ADD, usersToSyncAdd);
		map.put(MAPKEY_USER_SYNC_UPDATE, usersToSyncUpdate);
		map.put(MAPKEY_USER_SYNC_DELETE, usersToDelete);

		logger.info("用户同步Total Size: " + newList.size());
		logger.info("用户同步新增Size: " + usersToSyncAdd.size());
		logger.info("用户同步更新Size: " + usersToSyncUpdate.size());
		logger.info("用户同步删除Size: " + usersToDelete.size());

		return map;
	}

	/**
	 * 将组织名中的"&"替换为" "
	 * 
	 * @param list
	 */
	private void replaceIllegalChar(List<OuInfoEntity> list) {
		for (Iterator<OuInfoEntity> iterator = list.iterator(); iterator.hasNext();) {
			OuInfoEntity entity = iterator.next();
			String ouName = entity.getOuName();
			if (ouName != null && ouName.contains(REPLACE_FROM)) {
				entity.setOuName(ouName.replaceAll(REPLACE_FROM, REPLACE_TO));
			}
		}
	}

	/**
	 * 同步返回错误信息日志记录
	 * 
	 * @param type
	 * @param resultEntity
	 */
	private void printLog(String type, String id, ResultEntity resultEntity) {
		// TODO
		logger.error(type + "ID:" + id + " ErrMsg:" + resultEntity.getCode() + "-" + resultEntity.getMessage());
	}

}
