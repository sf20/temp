package openDemo.service.sync;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
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
import openDemo.dao.PositionDao;
import openDemo.dao.UserInfoDao;
import openDemo.entity.OuInfoEntity;
import openDemo.entity.PositionEntity;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoEntity;
import openDemo.entity.sync.OpOuInfoModel;
import openDemo.entity.sync.OpReqJsonModle;
import openDemo.entity.sync.OpUserInfoModel;
import openDemo.service.OrgService;
import openDemo.service.PositionService;
import openDemo.service.UserService;

public class OpSyncService {
	// 用户接口请求参数名
	private static final String REQUESTID = "RequestId";
	private static final String SERVICENAME = "ServiceName";
	private static final String SERVICEOPERATION = "ServiceOperation";
	private static final String SERVICEVERSION = "ServiceVersion";
	private static final String MODE = "Mode";
	private static final String ESBREQHEAD = "EsbReqHead";
	private static final String ESBREQDATA = "EsbReqData";
	// 用户接口请求参数值
	private static final String REQUEST_URL = "http://esbuat.opple.com:50831/esb_emp/json";
	private static final String USERNAME = "yxtuser";
	private static final String PASSWORD = "yxtpwd";
	private static final String SERVICE_NAME = "YXT_ESB_EmpOrgQuery";
	private static final String SERVICE_VERSION = "1.0";
	private static final String SERVICEOPERATION_EMP = "QueryEmpInfo";
	private static final String SERVICEOPERATION_ORG = "QueryOrgInfo";
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// json请求及转换时字符集类型
	private static final String CHARSET_UTF8 = "UTF-8";

	// 客户提供接口返回的json数据中组织数据和员工数据的key
	private static final String ORG_RES_DATA_KEY = "SapMiddleOrg";
	private static final String EMP_RES_DATA_KEY = "SapMiddleEmp";

	private static String MAPKEY_USER_SYNC_ADD = "userSyncAdd";
	private static String MAPKEY_USER_SYNC_UPDATE = "userSyncUpdate";
	// private static String MAPKEY_USER_SYNC_DELETE = "userSyncDelete";
	private static String MAPKEY_USER_SYNC_ENABLE = "userSyncEnable";
	private static String MAPKEY_USER_SYNC_DISABLE = "userSyncDisable";
	private static String MAPKEY_ORG_SYNC_ADD = "orgSyncAdd";
	private static String MAPKEY_ORG_SYNC_UPDATE = "orgSyncUpdate";
	private static String MAPKEY_ORG_SYNC_DELETE = "orgSyncDelete";
	private static String MAPKEY_POS_SYNC_ADD = "posSyncAdd";
	// private static String MAPKEY_POS_SYNC_UPDATE = "posSyncUpdate";

	private static String SYNC_CODE_SUCCESS = "0";
	// 岗位类别的默认值
	private static String POSITION_CLASS_DEFAULT = "未分类";
	private static String POSITION_CLASS_SEPARATOR = ";";

	private static SimpleDateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat JAVA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private PositionService positionService = new PositionService();
	private OrgService orgService = new OrgService();
	private UserService userService = new UserService();
	private PositionDao positionDao = new PositionDao();
	private OuInfoDao ouInfoDao = new OuInfoDao();
	private UserInfoDao userInfoDao = new UserInfoDao();
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
		int posCount = positionDao.getAllCount();
		if (posCount > 0) {
			// 岗位增量同步
			logger.info("[岗位增量]同步开始...");
			opPosSync(SERVICEOPERATION_EMP, MODE_UPDATE);
			logger.info("[岗位增量]同步结束");
		} else {
			// 岗位全量同步
			logger.info("[岗位全量]同步开始...");
			opPosSync(SERVICEOPERATION_EMP, MODE_FULL);
			logger.info("[岗位全量]同步结束");
		}

		int orgCount = ouInfoDao.getAllCount();
		if (orgCount > 0) {
			// 组织增量同步
			logger.info("[组织增量]同步开始...");
			opOrgSync(SERVICEOPERATION_ORG, MODE_UPDATE, false);
			logger.info("[组织增量]同步结束");
		} else {
			// 组织全量同步
			logger.info("[组织全量]同步开始...");
			opOrgSync(SERVICEOPERATION_ORG, MODE_FULL, false);
			logger.info("[组织全量]同步结束");
		}

		int userCount = userInfoDao.getAllCount();
		if (userCount > 0) {
			// 用户增量同步
			logger.info("[用户增量]同步开始...");
			opUserSync(SERVICEOPERATION_EMP, MODE_UPDATE, true);
			logger.info("[用户增量]同步结束");
		} else {
			// 用户全量同步
			logger.info("[用户全量]同步开始...");
			opUserSync(SERVICEOPERATION_EMP, MODE_FULL, true);
			logger.info("[用户全量]同步结束");
		}
	}

	/**
	 * 岗位同步
	 * 
	 * @param serviceOperation
	 * @param mode
	 * @throws ReflectiveOperationException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void opPosSync(String serviceOperation, String mode)
			throws IOException, ReflectiveOperationException, SQLException {
		List<OpUserInfoModel> userModelList = getUserModelList(serviceOperation, mode);// TODO mode=1
		List<PositionEntity> newList = getPosListFromUsers(userModelList);

		logger.info("岗位同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			syncAddPosOneByOne(newList);
			logger.info("岗位同步新增Size: " + newList.size());
		}
		// 增量模式
		else {
			// 获取数据库全量list
			List<PositionEntity> allList = positionDao.getAll();
			Map<String, List<PositionEntity>> map = comparePosList(allList, newList);

			syncAddPosOneByOne(map.get(MAPKEY_POS_SYNC_ADD));
		}
	}

	/**
	 * 根据用户集合生成岗位对象集合
	 * 
	 * @param userModelList
	 * @return
	 */
	private List<PositionEntity> getPosListFromUsers(List<OpUserInfoModel> userModelList) {
		// 使用Set保证无重复
		Set<String> posNames = new HashSet<>();
		for (OpUserInfoModel modle : userModelList) {
			posNames.add(modle.getPostionName());
		}

		List<PositionEntity> list = new ArrayList<>(posNames.size());
		PositionEntity temp = null;
		for (String posName : posNames) {
			temp = new PositionEntity();
			temp.setpNo(UUID.randomUUID().toString());
			temp.setpNames(POSITION_CLASS_DEFAULT + POSITION_CLASS_SEPARATOR + posName);
			list.add(temp);
		}

		return list;
	}

	/**
	 * 数据库岗位表数据集合与最新获取岗位数据集合进行比较
	 * 
	 * @param fullList
	 *            数据库岗位表数据集合
	 * @param newList
	 *            最新获取岗位数据集合
	 * @return
	 */
	private Map<String, List<PositionEntity>> comparePosList(List<PositionEntity> fullList,
			List<PositionEntity> newList) {
		Map<String, List<PositionEntity>> map = new HashMap<>();
		List<PositionEntity> posToSyncAdd = new ArrayList<>();

		// 待新增岗位
		for (PositionEntity newPos : newList) {
			String newPosName = newPos.getpNames();

			if (newPosName != null) {
				boolean isPosNameExist = false;

				for (PositionEntity fullPos : fullList) {
					String fullPosName = fullPos.getpNames();
					if (fullPosName != null && newPosName.equals(fullPosName)) {
						isPosNameExist = true;
						break;
					}
				}

				// 岗位名不存在
				if (!isPosNameExist) {
					posToSyncAdd.add(newPos);
				}
			}
		}

		map.put(MAPKEY_POS_SYNC_ADD, posToSyncAdd);
		logger.info("岗位同步新增Size: " + posToSyncAdd.size());

		return map;
	}

	/**
	 * 逐个岗位同步新增
	 * 
	 * @param posToSync
	 * @throws SQLException
	 * @throws IOException 
	 */
	private void syncAddPosOneByOne(List<PositionEntity> posToSync) throws SQLException, IOException {
		List<PositionEntity> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (PositionEntity pos : posToSync) {
			tempList.add(pos);

			resultEntity = positionService.syncPos(tempList);

			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				positionDao.insert(pos);
			} else {
				printLog("岗位同步新增失败", pos.getpNames(), resultEntity);
			}

			tempList.clear();
		}
	}

	/**
	 * 向客户提供的接口发送POST请求并获取json数据
	 * 
	 * @param serviceOperation
	 *            可在QueryEmpInfo（员工数据）和QueryOrgInfo（组织架构）中二选一
	 * @param mode
	 *            可在1（全量）和2（增量）中二选一。EMP拥有1和2两种模式。Org只有1，全量模式。
	 * @return 响应的json字符串
	 * @throws IOException
	 */
	public String getJsonPost(String serviceOperation, String mode) throws IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(REQUEST_URL);
		HttpResponse httpResponse = null;
		String responseStr = null;
		try {
			// 请求header中增加Auth部分
			httpPost.addHeader("Authorization", getBasicAuthHeader(USERNAME, PASSWORD));

			// 构建消息实体 发送Json格式的数据
			StringEntity entity = new StringEntity(buildReqJson(serviceOperation, mode), ContentType.APPLICATION_JSON);
			entity.setContentEncoding(CHARSET_UTF8);
			httpPost.setEntity(entity);

			// 发送post请求
			httpResponse = httpClient.execute(httpPost);// TODO ClientProtocolException

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				responseStr = EntityUtils.toString(httpResponse.getEntity(), CHARSET_UTF8);
			}

		} finally {
			if (httpClient != null) {
				HttpClientUtils.closeQuietly(httpClient);
			}

			if (httpResponse != null) {
				HttpClientUtils.closeQuietly(httpResponse);
			}
		}
		// TODO to delete
		logger.info("=====" + responseStr);
		return responseStr;
	}

	/**
	 * 请求header中增加Auth部分 Auth类型：Basic
	 * 
	 * @param username
	 * @param password
	 * @return Auth请求头内容
	 * @throws UnsupportedEncodingException
	 */
	private String getBasicAuthHeader(String username, String password) throws UnsupportedEncodingException {
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(CHARSET_UTF8));
		String authHeader = "Basic " + new String(encodedAuth, CHARSET_UTF8);

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
	 * @throws JsonProcessingException
	 */
	private String buildReqJson(String serviceOperation, String mode) throws JsonProcessingException {
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

		String str = mapper.writeValueAsString(map);

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
		String jsonString = getJsonPost(serviceOperation, MODE_FULL);// Org只有全量模式

		// 将json字符串转为组织单位json对象数据模型
		OpReqJsonModle<OpOuInfoModel> modle = mapper.readValue(jsonString,
				new TypeReference<OpReqJsonModle<OpOuInfoModel>>() {
				});

		List<OuInfoEntity> newList = copyCreateEntityList(modle.getEsbResData().get(ORG_RES_DATA_KEY),
				OuInfoEntity.class);

		// replaceIllegalChar(newList);

		logger.info("组织同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			removeExpiredOrgs(newList);
			syncAddOrgOneByOne(newList, isBaseInfo);
			logger.info("组织同步新增Size: " + newList.size());
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
				syncDeleteOrgOneByOne(orgsToSyncDelete);
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
			OuInfoEntity org = iterator.next();
			if (isOrgExpired(org)) {
				iterator.remove();
				logger.warn("删除了过期组织：" + org.getOuName());
			}
		}
	}

	/**
	 * 逐个组织同步删除
	 * 
	 * @param orgsToSyncDelete
	 * @throws SQLException
	 * @throws IOException 
	 */
	private void syncDeleteOrgOneByOne(List<OuInfoEntity> orgsToSyncDelete) throws SQLException, IOException {
		List<String> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (OuInfoEntity org : orgsToSyncDelete) {
			String orgId = org.getID();
			tempList.add(orgId);

			resultEntity = orgService.deleteous(tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				ouInfoDao.deleteById(orgId);
			} else {
				printLog("组织同步删除失败", org.getOuName(), resultEntity);
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
	 * @throws IOException 
	 */
	private void syncUpdateOrgOneByOne(List<OuInfoEntity> orgsToSyncUpdate, boolean isBaseInfo) throws SQLException, IOException {
		List<OuInfoEntity> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (OuInfoEntity org : orgsToSyncUpdate) {
			tempList.add(org);

			resultEntity = orgService.ous(isBaseInfo, tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				ouInfoDao.update(org);
			} else {
				printLog("组织同步更新失败", org.getOuName(), resultEntity);
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
	 * @throws IOException 
	 */
	private void syncAddOrgOneByOne(List<OuInfoEntity> orgsToSyncAdd, boolean isBaseInfo) throws SQLException, IOException {
		List<OuInfoEntity> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (OuInfoEntity org : orgsToSyncAdd) {
			tempList.add(org);

			resultEntity = orgService.ous(isBaseInfo, tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				ouInfoDao.insert(org);
			} else {
				printLog("组织同步新增失败", org.getOuName(), resultEntity);
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
		List<OpUserInfoModel> modelList = getUserModelList(serviceOperation, mode);
		List<UserInfoEntity> newList = copyCreateEntityList(modelList, UserInfoEntity.class);

		copySetUserId(newList);
		changeDateFormatAndSex(modelList, newList);

		// 关联岗位到用户
		setPositionNoToUser(newList);

		logger.info("用户同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			syncAddUserOneByOne(newList, islink);
			logger.info("用户同步新增Size: " + newList.size());

			List<UserInfoEntity> expiredUsers = getExpiredUsers(newList);
			if (expiredUsers.size() > 0) {
				syncDisableOneByOne(expiredUsers);
				logger.info("用户同步禁用Size: " + expiredUsers.size());
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
				syncAddUserOneByOne(usersToSyncAdd, islink);
			}

			List<UserInfoEntity> usersToSyncUpdate = map.get(MAPKEY_USER_SYNC_UPDATE);
			if (usersToSyncUpdate.size() > 0) {
				syncUpdateUserOneByOne(usersToSyncUpdate, islink);
			}

			List<UserInfoEntity> usersToDisable = map.get(MAPKEY_USER_SYNC_DISABLE);
			if (usersToDisable.size() > 0) {
				syncDisableOneByOne(usersToDisable);
			}

			List<UserInfoEntity> usersToEnable = map.get(MAPKEY_USER_SYNC_ENABLE);
			if (usersToEnable.size() > 0) {
				syncEnableOneByOne(usersToEnable);
			}
		}

	}

	/**
	 * 关联岗位到用户
	 * 
	 * @param newList
	 * @throws SQLException
	 */
	private void setPositionNoToUser(List<UserInfoEntity> newList) throws SQLException {
		List<PositionEntity> posList = positionDao.getAll();

		for (UserInfoEntity user : newList) {
			String pNameInUser = user.getPostionName();

			if (pNameInUser != null) {
				for (PositionEntity pos : posList) {
					// 根据岗位名进行查找
					if (pNameInUser.equals(getPositionName(pos.getpNames()))) {
						user.setPostionNo(pos.getpNo());
						break;
					}
				}
			}
		}
	}

	/**
	 * 从pNames中得到岗位名(pNames格式: 一级类别;二级类别;岗位名)
	 * 
	 * @param getpNames
	 * @return
	 */
	private String getPositionName(String pNames) {
		if (pNames == null) {
			return null;
		}

		String[] arr = pNames.split(POSITION_CLASS_SEPARATOR);
		int len = arr.length;
		if (len == 0) {
			return null;
		}

		// 最后是岗位名
		return arr[len - 1];
	}

	/**
	 * 向客户接口发送请求并返回员工json数据模型集合
	 * 
	 * @param serviceOperation
	 * @param mode
	 * @return
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	private List<OpUserInfoModel> getUserModelList(String serviceOperation, String mode)
			throws IOException, ReflectiveOperationException {
		String jsonString = getJsonPost(serviceOperation, mode);

		// 将json字符串转为用户json对象数据模型
		OpReqJsonModle<OpUserInfoModel> modle = mapper.readValue(jsonString,
				new TypeReference<OpReqJsonModle<OpUserInfoModel>>() {
				});

		return modle.getEsbResData().get(EMP_RES_DATA_KEY);
	}

	/**
	 * 返回过期员工
	 * 
	 * @param list
	 * @return
	 */
	private List<UserInfoEntity> getExpiredUsers(List<UserInfoEntity> list) {
		List<UserInfoEntity> expiredUsers = new ArrayList<>();
		for (UserInfoEntity user : list) {
			if (user.getExpireDate() != null) {
				expiredUsers.add(user);
			}
		}
		return expiredUsers;
	}

	/**
	 * 将json模型对象的日期进行格式化(yyyy-MM-dd)后赋值给对应的java同步对象 + 性别值转换
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
	 * 将userName字段值赋值给ID字段 用于解决返回json字符串中组织没有ID的问题
	 * 
	 * @param newList
	 */
	private void copySetUserId(List<UserInfoEntity> newList) {
		for (Iterator<UserInfoEntity> iterator = newList.iterator(); iterator.hasNext();) {
			UserInfoEntity userInfoEntity = iterator.next();
			// ID <= userName
			userInfoEntity.setID(userInfoEntity.getUserName());
		}
	}

	/**
	 * 逐个用户同步新增
	 * 
	 * @param usersToSyncAdd
	 * @param islink
	 * @throws SQLException
	 * @throws IOException 
	 */
	private void syncAddUserOneByOne(List<UserInfoEntity> usersToSyncAdd, boolean islink) throws SQLException, IOException {
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
	 * @throws IOException 
	 */
	private void syncUpdateUserOneByOne(List<UserInfoEntity> usersToSyncUpdate, boolean islink) throws SQLException, IOException {
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
	 * 逐个用户同步启用
	 * 
	 * @param usersToEnable
	 * @throws SQLException
	 * @throws IOException 
	 */
	private void syncEnableOneByOne(List<UserInfoEntity> usersToEnable) throws SQLException, IOException {
		List<String> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;

		for (UserInfoEntity user : usersToEnable) {
			tempList.add(user.getUserName());

			resultEntity = userService.enabledusersSync(tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				userInfoDao.update(user);
			} else {
				printLog("用户同步启用失败", user.getID(), resultEntity);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个用户同步禁用
	 * 
	 * @param usersToDisable
	 * @throws SQLException
	 * @throws IOException 
	 */
	private void syncDisableOneByOne(List<UserInfoEntity> usersToDisable) throws SQLException, IOException {
		List<String> tempList = new ArrayList<>();
		ResultEntity resultEntity = null;
		for (UserInfoEntity user : usersToDisable) {
			tempList.add(user.getUserName());

			resultEntity = userService.disabledusersSync(tempList);
			if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
				userInfoDao.update(user);
			} else {
				printLog("用户同步禁用失败", user.getID(), resultEntity);
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
							// TODO
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
			if (!fullList.contains(org)) {
				// 非过期组织
				if (!isOrgExpired(org)) {
					orgsToSyncAdd.add(org);
				} else {
					logger.warn("包含过期组织：" + org.getOuName());
				}
			}
		}

		map.put(MAPKEY_ORG_SYNC_ADD, orgsToSyncAdd);
		map.put(MAPKEY_ORG_SYNC_UPDATE, orgsToSyncUpdate);
		map.put(MAPKEY_ORG_SYNC_DELETE, orgsToSyncDelete);

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
	 * @return 包含 同步新增、更新、启用、禁用等用户集合的Map对象
	 */
	private Map<String, List<UserInfoEntity>> compareUserList(List<UserInfoEntity> fullList,
			List<UserInfoEntity> newList) {
		Map<String, List<UserInfoEntity>> map = new HashMap<>();

		List<UserInfoEntity> usersToSyncAdd = new ArrayList<>();
		List<UserInfoEntity> usersToSyncUpdate = new ArrayList<>();
		List<UserInfoEntity> usersToEnable = new ArrayList<>();
		List<UserInfoEntity> usersToDisable = new ArrayList<>();

		// 待更新用户
		for (UserInfoEntity fullUser : fullList) {
			for (UserInfoEntity newUser : newList) {
				// 已经存在的用户比较
				if (fullUser.equals(newUser)) {
					if (fullUser.getExpireDate() == null) {
						if (newUser.getExpireDate() != null) {
							// 用户过期禁用
							usersToDisable.add(newUser);
						} else {
							// 存在用户更新
							usersToSyncUpdate.add(newUser);
						}
					} else {
						if (newUser.getExpireDate() == null) {
							// 用户重新启用
							usersToEnable.add(newUser);
						} else {
							// 存在用户更新
							usersToSyncUpdate.add(newUser);
						}
					}
				}
			}
		}

		// 待新增用户
		for (UserInfoEntity user : newList) {
			if (!fullList.contains(user)) {
				usersToSyncAdd.add(user);
			}
		}

		map.put(MAPKEY_USER_SYNC_ADD, usersToSyncAdd);
		map.put(MAPKEY_USER_SYNC_UPDATE, usersToSyncUpdate);
		map.put(MAPKEY_USER_SYNC_ENABLE, usersToEnable);
		map.put(MAPKEY_USER_SYNC_DISABLE, usersToDisable);

		logger.info("用户同步新增Size: " + usersToSyncAdd.size());
		logger.info("用户同步更新Size: " + usersToSyncUpdate.size());
		logger.info("用户同步启用Size: " + usersToEnable.size());
		logger.info("用户同步禁用Size: " + usersToDisable.size());

		return map;
	}

	/**
	 * 同步返回错误信息日志记录
	 * 
	 * @param type
	 * @param resultEntity
	 */
	private void printLog(String type, String id, ResultEntity resultEntity) {
		// TODO
		logger.warn(type + "ID:" + id + " ErrMsg:" + resultEntity.getCode() + "-" + resultEntity.getMessage());
	}

}
