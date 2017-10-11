package openDemo.service.sync;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.config.OppleConfig;
import openDemo.dao.PositionDao;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.OpOuInfoModel;
import openDemo.entity.sync.OpResJsonModel;
import openDemo.entity.sync.OpUserInfoModel;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncPositionService;
import openDemo.service.SyncUserService;

public class OppleSyncService extends AbstractSyncService implements OppleConfig {
	// 用户接口请求参数名
	private static final String REQUESTID = "RequestId";
	private static final String SERVICENAME = "ServiceName";
	private static final String SERVICEOPERATION = "ServiceOperation";
	private static final String SERVICEVERSION = "ServiceVersion";
	private static final String MODE = "Mode";
	private static final String ESBREQHEAD = "EsbReqHead";
	private static final String ESBREQDATA = "EsbReqData";
	// 用户接口请求参数值
	private static final String REQUEST_URL = "https://esb.opple.com:50830/esb_emp/json"; // "http://esb.opple.com:50831/esb_emp/json";
	private static final String USERNAME = "yxtuser";
	private static final String PASSWORD = "u#5QTwNDaq";
	private static final String SERVICE_NAME = "YXT_ESB_EmpOrgQuery";
	private static final String SERVICE_VERSION = "1.0";
	private static final String SERVICEOPERATION_EMP = "QueryEmpInfo";
	private static final String SERVICEOPERATION_ORG = "QueryOrgInfo";
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	private static final String MODE_3 = "3";
	private static final String MODE_4 = "4";
	// json请求及转换时字符集类型
	private static final String CHARSET_UTF8 = "UTF-8";
	// 客户提供接口返回的json数据中组织数据和员工数据的key
	private static final String ORG_RES_DATA_KEY = "SapMiddleOrg";
	private static final String EMP_RES_DATA_KEY = "SapMiddleEmp";
	// 自定义map的key
	private static final String MAPKEY_USER_SYNC_ADD = "userSyncAdd";
	private static final String MAPKEY_USER_SYNC_UPDATE = "userSyncUpdate";
	private static final String MAPKEY_USER_SYNC_ENABLE = "userSyncEnable";
	private static final String MAPKEY_USER_SYNC_DISABLE = "userSyncDisable";
	private static final String MAPKEY_ORG_SYNC_ADD = "orgSyncAdd";
	private static final String MAPKEY_ORG_SYNC_UPDATE = "orgSyncUpdate";
	private static final String MAPKEY_ORG_SYNC_DELETE = "orgSyncDelete";
	private static final String MAPKEY_POS_SYNC_ADD = "posSyncAdd";
	// 请求同步接口成功返回码
	private static final String SYNC_CODE_SUCCESS = "0";
	// 岗位类别的默认值
	private static final String POSITION_CLASS_DEFAULT = "未分类";
	private static final String POSITION_CLASS_SEPARATOR = ";";
	// 日期格式化用
	private static final SimpleDateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat JAVA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	// 记录日志
	private static final Logger logger = LogManager.getLogger(OppleSyncService.class);

	// 请求同步接口的service
	private SyncPositionService positionService = new SyncPositionService();
	private SyncOrgService orgService = new SyncOrgService();
	private SyncUserService userService = new SyncUserService();
	// 用于存放请求获取到的数据的集合
	private List<PositionModel> positionList = new LinkedList<PositionModel>();
	private List<OuInfoModel> ouInfoList = new LinkedList<OuInfoModel>();
	private List<UserInfoModel> userInfoList = new LinkedList<UserInfoModel>();
	private ObjectMapper mapper;

	public OppleSyncService() {
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
	@Override
	public void sync() throws IOException, ReflectiveOperationException, SQLException {
		int posCount = positionList.size();
		if (posCount > 0) {
			// 岗位增量同步
			logger.info("[岗位增量]同步开始...");
			opPosSync(SERVICEOPERATION_EMP, MODE_UPDATE, null);
			logger.info("[岗位增量]同步结束");
		} else {
			// 岗位全量同步
			logger.info("[岗位全量]同步开始...");
			opPosSync(SERVICEOPERATION_EMP, MODE_FULL, null);
			logger.info("[岗位全量]同步结束");
		}

		int orgCount = ouInfoList.size();
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

		int userCount = userInfoList.size();
		if (userCount > 0) {
			// 用户增量同步
			logger.info("[用户增量]同步开始...");
			opUserSync(SERVICEOPERATION_EMP, MODE_UPDATE, true, null);
			logger.info("[用户增量]同步结束");
		} else {
			// 用户全量同步
			logger.info("[用户全量]同步开始...");
			opUserSync(SERVICEOPERATION_EMP, MODE_UPDATE, true, null);
			logger.info("[用户全量]同步结束");
		}
	}

	/**
	 * 岗位同步
	 * 
	 * @param serviceOperation
	 * @param mode
	 * @param paramAdded
	 * @throws ReflectiveOperationException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void opPosSync(String serviceOperation, String mode, Map<String, String> paramAdded)
			throws IOException, ReflectiveOperationException, SQLException {
		List<OpUserInfoModel> userModelList = getUserModelList(serviceOperation, mode, paramAdded);
		List<PositionModel> newList = getPosListFromUsers(userModelList);

		logger.info("岗位同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			logger.info("岗位同步新增Size: " + newList.size());
			compareDataWithDB(newList);
			syncAddPosOneByOne(newList);
		}
		// 增量模式
		else {
			Map<String, List<PositionModel>> map = comparePosList(positionList, newList);

			syncAddPosOneByOne(map.get(MAPKEY_POS_SYNC_ADD));
		}
	}

	/**
	 * 将要同步的岗位数据和数据库中的岗位数据进行比较后替换岗位编号
	 * 
	 * @param newList
	 * @throws SQLException
	 */
	private void compareDataWithDB(List<PositionModel> newList) throws SQLException {
		List<PositionModel> positionListDB = new ArrayList<PositionModel>();
		// 获取数据库岗位数据
		PositionDao dao = new PositionDao();
		positionListDB = dao.getAllById(apikey);

		for (PositionModel newPos : newList) {
			String newPosNames = newPos.getpNames();

			if (newPosNames != null) {
				for (PositionModel fullPos : positionListDB) {
					// 岗位名存在时将岗位编号用数据库中岗位编号替换
					if (newPosNames.equals(fullPos.getpNames())) {
						newPos.setpNo(fullPos.getpNo());
						break;
					}
				}
			}

		}

		positionListDB = null;
	}

	/**
	 * 根据用户集合生成岗位对象集合
	 * 
	 * @param userModelList
	 * @return
	 */
	private List<PositionModel> getPosListFromUsers(List<OpUserInfoModel> userModelList) {
		// 使用Set保证无重复
		Set<String> posNames = new HashSet<String>();
		for (OpUserInfoModel modle : userModelList) {
			posNames.add(modle.getPostionName());
		}

		List<PositionModel> list = new ArrayList<PositionModel>(posNames.size());
		PositionModel temp = null;
		for (String posName : posNames) {
			temp = new PositionModel();
			temp.setpNo(UUID.randomUUID().toString());
			temp.setpNames(posName);
			list.add(temp);
		}

		return list;
	}

	/**
	 * 返回带类别岗位名
	 * 
	 * @param posName
	 * @return
	 */
	private String getFullPosNames(String posName) {
		return POSITION_CLASS_DEFAULT + POSITION_CLASS_SEPARATOR + posName;
	}

	/**
	 * 岗位全量数据集合与最新获取岗位数据集合进行比较
	 * 
	 * @param fullList
	 *            全量岗位数据集合
	 * @param newList
	 *            最新获取岗位数据集合
	 * @return
	 */
	private Map<String, List<PositionModel>> comparePosList(List<PositionModel> fullList, List<PositionModel> newList) {
		Map<String, List<PositionModel>> map = new HashMap<String, List<PositionModel>>();
		List<PositionModel> posToSyncAdd = new ArrayList<PositionModel>();

		// 待新增岗位
		for (PositionModel newPos : newList) {
			String newPosName = newPos.getpNames();

			if (newPosName != null) {
				boolean isPosNameExist = false;

				for (PositionModel fullPos : fullList) {
					// 岗位名比较
					if (newPosName.equals(fullPos.getpNames())) {
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
	 */
	private void syncAddPosOneByOne(List<PositionModel> posToSync) {
		List<PositionModel> tempList = new ArrayList<PositionModel>();
		ResultEntity resultEntity = null;
		for (PositionModel pos : posToSync) {
			String tempPosName = pos.getpNames();
			// 调用同步接口时需要带类别岗位名
			pos.setpNames(getFullPosNames(tempPosName));
			tempList.add(pos);

			try {
				resultEntity = positionService.syncPos(tempList, apikey, secretkey, baseUrl);

				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					// 全量集合中保存不带类别的岗位名
					pos.setpNames(tempPosName);
					positionList.add(pos);
				} else {
					printLog("岗位同步新增失败 ", tempPosName, resultEntity);
				}
			} catch (IOException e) {
				logger.error("岗位同步新增失败 " + tempPosName, e);
			}

			tempList.clear();
		}
	}

	/**
	 * 向客户提供的接口发送POST请求并获取json数据
	 * 
	 * @param requestJsonParam
	 *            请求参数
	 * @return 响应的json字符串
	 * @throws IOException
	 */
	public String getJsonPost(String requestJsonParam) throws IOException {
		HttpClient httpClient = createSSLHttpClient();// HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(REQUEST_URL);
		HttpResponse httpResponse = null;
		String responseStr = null;
		try {
			// 请求header中增加Auth部分
			httpPost.addHeader("Authorization", getBasicAuthHeader(USERNAME, PASSWORD));

			// 构建消息实体 发送Json格式的数据
			StringEntity entity = new StringEntity(requestJsonParam, ContentType.APPLICATION_JSON);
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
		logger.info("请求用户接口返回数据：" + responseStr);
		return responseStr;
	}

	/**
	 * 创建用于https请求的HttpClient
	 * 
	 * @return
	 */
	private CloseableHttpClient createSSLHttpClient() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			// 实现一个X509TrustManager接口
			X509TrustManager trustManager = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			sslContext.init(null, new TrustManager[] { trustManager }, null);

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			logger.error("创建SSLClient失败", e);
		}

		return HttpClientBuilder.create().build();
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
	 * @param paramAdded
	 *            模式增加了Mode3和Mode4, 该参数为相应增加的参数的键值。Mode1和Mode2不需要传该参数。
	 * @return
	 * @throws JsonProcessingException
	 */
	public String buildReqJson(String serviceOperation, String mode, Map<String, String> paramAdded)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> reqHeadMap = new HashMap<String, Object>();
		reqHeadMap.put(REQUESTID, UUID.randomUUID().toString());
		reqHeadMap.put(SERVICENAME, SERVICE_NAME);
		reqHeadMap.put(SERVICEOPERATION, serviceOperation);
		reqHeadMap.put(SERVICEVERSION, SERVICE_VERSION);
		map.put(ESBREQHEAD, reqHeadMap);

		Map<String, Object> reqDataMap = new HashMap<String, Object>();
		reqDataMap.put(MODE, mode);
		if (MODE_3.equals(mode) || MODE_4.equals(mode)) {
			if (paramAdded != null && paramAdded.size() > 0) {
				for (String key : paramAdded.keySet()) {
					reqDataMap.put(key, paramAdded.get(key));
				}
			}
		}
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
	 */
	public void opOrgSync(String serviceOperation, String mode, boolean isBaseInfo)
			throws IOException, ReflectiveOperationException {
		String jsonString = getJsonPost(buildReqJson(serviceOperation, MODE_FULL, null));// Org只有全量模式

		// 将json字符串转为组织单位json对象数据模型
		OpResJsonModel<OpOuInfoModel> modle = mapper.readValue(jsonString,
				new TypeReference<OpResJsonModel<OpOuInfoModel>>() {
				});

		List<OuInfoModel> newList = copyCreateEntityList(modle.getEsbResData().get(ORG_RES_DATA_KEY),
				OuInfoModel.class);

		removeExpiredOrgs(newList, mode);

		logger.info("组织同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			logger.info("组织同步新增Size: " + newList.size());
			// 进行多次同步
			// for (int i = 0; i < 5; i++) {
				syncAddOrgOneByOne(newList, isBaseInfo);
			// }
		}
		// 增量模式
		else {
			Map<String, List<OuInfoModel>> map = compareOrgList(ouInfoList, newList);
			List<OuInfoModel> orgsToSyncAdd = map.get(MAPKEY_ORG_SYNC_ADD);
			if (orgsToSyncAdd.size() > 0) {
				syncAddOrgOneByOne(orgsToSyncAdd, isBaseInfo);
			}

			List<OuInfoModel> orgsToSyncUpdate = map.get(MAPKEY_ORG_SYNC_UPDATE);
			if (orgsToSyncUpdate.size() > 0) {
				syncUpdateOrgOneByOne(orgsToSyncUpdate, isBaseInfo);
			}

			List<OuInfoModel> orgsToSyncDelete = map.get(MAPKEY_ORG_SYNC_DELETE);
			if (orgsToSyncDelete.size() > 0) {
				syncDeleteOrgOneByOne(orgsToSyncDelete);
			}
		}
	}

	/**
	 * 去除过期组织和除了编号为00000001之外的所有无parentcode的部门组织
	 * 
	 * @param list
	 * @param mode
	 */
	private void removeExpiredOrgs(List<OuInfoModel> list, String mode) {
		for (Iterator<OuInfoModel> iterator = list.iterator(); iterator.hasNext();) {
			OuInfoModel org = iterator.next();
			// 仅全量模式下执行
			if (MODE_FULL.equals(mode)) {
				if (isOrgExpired(org)) {
					iterator.remove();
					logger.info("删除了过期组织：" + org.getOuName());
				}
			}

			// 除了编号为 00000001 之外的所有无parentcode的部门都不同步
			if (org.getParentID() == null && Integer.parseInt(org.getID()) != 1) {
				iterator.remove();
			}
		}
	}

	/**
	 * 逐个组织同步删除
	 * 
	 * @param orgsToSyncDelete
	 */
	private void syncDeleteOrgOneByOne(List<OuInfoModel> orgsToSyncDelete) {
		List<String> tempList = new ArrayList<String>();
		ResultEntity resultEntity = null;
		for (OuInfoModel org : orgsToSyncDelete) {
			tempList.add(org.getID());

			try {
				resultEntity = orgService.deleteous(tempList, apikey, secretkey, baseUrl);

				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					ouInfoList.remove(org);
				} else {
					printLog("组织同步删除失败 ", org.getOuName(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("组织同步删除失败 " + org.getOuName(), e);
			}

			tempList.clear();
		}

	}

	/**
	 * 逐个组织同步更新
	 * 
	 * @param orgsToSyncUpdate
	 * @param isBaseInfo
	 */
	private void syncUpdateOrgOneByOne(List<OuInfoModel> orgsToSyncUpdate, boolean isBaseInfo) {
		List<OuInfoModel> tempList = new ArrayList<OuInfoModel>();
		ResultEntity resultEntity = null;
		for (OuInfoModel org : orgsToSyncUpdate) {
			tempList.add(org);

			try {
				resultEntity = orgService.ous(isBaseInfo, tempList, apikey, secretkey, baseUrl);
				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					ouInfoList.remove(org);
					ouInfoList.add(org);
				} else {
					printLog("组织同步更新失败 ", org.getOuName(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("组织同步更新失败 " + org.getOuName(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个组织同步新增
	 * 
	 * @param orgsToSyncAdd
	 * @param isBaseInfo
	 */
	private void syncAddOrgOneByOne(List<OuInfoModel> orgsToSyncAdd, boolean isBaseInfo) {
		List<OuInfoModel> tempList = new ArrayList<OuInfoModel>();
		ResultEntity resultEntity = null;
		for (OuInfoModel org : orgsToSyncAdd) {
			tempList.add(org);

			try {
				resultEntity = orgService.ous(isBaseInfo, tempList, apikey, secretkey, baseUrl);
				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					ouInfoList.add(org);
				} else {
					printLog("组织同步新增失败 ", org.getOuName(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("组织同步新增失败 " + org.getOuName(), e);
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

		if (fromList != null) {
			int listSize = fromList.size();
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
	 * @param paramAdded
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	public void opUserSync(String serviceOperation, String mode, boolean islink, Map<String, String> paramAdded)
			throws IOException, ReflectiveOperationException {
		List<OpUserInfoModel> modelList = getUserModelList(serviceOperation, mode, paramAdded);
		List<UserInfoModel> newList = copyCreateEntityList(modelList, UserInfoModel.class);

		copySetUserName(newList);
		changeDateFormatAndSex(modelList, newList);

		// 关联岗位到用户
		setPositionNoToUser(newList);

		logger.info("用户同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode) || MODE_3.equals(mode) || MODE_4.equals(mode)) {
			logger.info("用户同步新增Size: " + newList.size());
			syncAddUserOneByOne(newList, islink);

			List<UserInfoModel> expiredUsers = getExpiredUsers(newList);
			if (expiredUsers.size() > 0) {
				logger.info("用户同步禁用Size: " + expiredUsers.size());
				syncDisableOneByOne(expiredUsers);
			}
		}
		// 增量模式
		else {
			// 与增量list进行比较
			Map<String, List<UserInfoModel>> map = compareUserList(userInfoList, newList);

			List<UserInfoModel> usersToSyncAdd = map.get(MAPKEY_USER_SYNC_ADD);
			if (usersToSyncAdd.size() > 0) {
				syncAddUserOneByOne(usersToSyncAdd, islink);
			}

			List<UserInfoModel> usersToSyncUpdate = map.get(MAPKEY_USER_SYNC_UPDATE);
			if (usersToSyncUpdate.size() > 0) {
				syncUpdateUserOneByOne(usersToSyncUpdate, islink);
			}

			List<UserInfoModel> usersToDisable = map.get(MAPKEY_USER_SYNC_DISABLE);
			if (usersToDisable.size() > 0) {
				syncDisableOneByOne(usersToDisable);
			}

			List<UserInfoModel> usersToEnable = map.get(MAPKEY_USER_SYNC_ENABLE);
			if (usersToEnable.size() > 0) {
				syncEnableOneByOne(usersToEnable);
			}
		}

	}

	/**
	 * 关联岗位到用户
	 * 
	 * @param newList
	 */
	private void setPositionNoToUser(List<UserInfoModel> newList) {

		for (UserInfoModel user : newList) {
			String pNameInUser = user.getPostionName();

			if (pNameInUser != null) {
				for (PositionModel pos : positionList) {
					// 根据岗位名进行查找
					if (pNameInUser.equals(pos.getpNames())) {
						user.setPostionNo(pos.getpNo());
						break;
					}
				}
			} else {
				// 岗位名为null时岗位编号设置为null
				// user.setPostionNo(null);
			}
		}
	}

	/**
	 * 向客户接口发送请求并返回员工json数据模型集合
	 * 
	 * @param serviceOperation
	 * @param mode
	 * @param paramAdded
	 * @return
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	private List<OpUserInfoModel> getUserModelList(String serviceOperation, String mode, Map<String, String> paramAdded)
			throws IOException, ReflectiveOperationException {
		String jsonString = getJsonPost(buildReqJson(serviceOperation, mode, paramAdded));

		// 将json字符串转为用户json对象数据模型
		OpResJsonModel<OpUserInfoModel> modle = mapper.readValue(jsonString,
				new TypeReference<OpResJsonModel<OpUserInfoModel>>() {
				});

		return modle.getEsbResData().get(EMP_RES_DATA_KEY);
	}

	/**
	 * 返回过期员工
	 * 
	 * @param list
	 * @return
	 */
	private List<UserInfoModel> getExpiredUsers(List<UserInfoModel> list) {
		List<UserInfoModel> expiredUsers = new ArrayList<UserInfoModel>();
		for (UserInfoModel user : list) {
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
	private void changeDateFormatAndSex(List<OpUserInfoModel> fromList, List<UserInfoModel> toList) {
		int listSize = toList.size();
		UserInfoModel toModel = null;
		OpUserInfoModel fromModel = null;

		for (int i = 0; i < listSize; i++) {
			toModel = toList.get(i);
			fromModel = fromList.get(i);

			Date entryTime = fromModel.getEntryTime();
			if (entryTime != null) {
				toModel.setEntryTime(JAVA_DATE_FORMAT.format(entryTime));
			}

			Date expireDate = fromModel.getExpireDate();
			if (expireDate != null) {
				toModel.setExpireDate(JAVA_DATE_FORMAT.format(expireDate));
			}

			// 性别字符串转换 1：男 2：女
			String sex = fromModel.getSex();
			if ("1".equals(sex)) {
				toModel.setSex("男");
			} else if ("2".equals(sex)) {
				toModel.setSex("女");
			}
		}
	}

	/**
	 * 将ID字段值赋值给userName字段
	 * 
	 * @param newList
	 */
	private void copySetUserName(List<UserInfoModel> newList) {
		for (Iterator<UserInfoModel> iterator = newList.iterator(); iterator.hasNext();) {
			UserInfoModel userInfoEntity = iterator.next();
			// userName <= ID
			userInfoEntity.setUserName(userInfoEntity.getID());
		}
	}

	/**
	 * 逐个用户同步新增
	 * 
	 * @param usersToSyncAdd
	 * @param islink
	 */
	private void syncAddUserOneByOne(List<UserInfoModel> usersToSyncAdd, boolean islink) {
		List<UserInfoModel> tempList = new ArrayList<UserInfoModel>();
		ResultEntity resultEntity = null;
		for (UserInfoModel user : usersToSyncAdd) {
			tempList.add(user);

			try {
				resultEntity = userService.userSync(islink, tempList, apikey, secretkey, baseUrl);
				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					userInfoList.add(user);
				} else {
					// 忽略邮箱再同步一次
					user.setMail(null);
					tempList.set(0, user);
					resultEntity = userService.userSync(islink, tempList, apikey, secretkey, baseUrl);
					if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
						userInfoList.add(user);
						logger.warn("该用户邮箱异常未同步：" + user.getID());
					} else {
						printLog("用户同步新增失败 ", user.getID(), resultEntity);
					}
				}
			} catch (IOException e) {
				logger.error("用户同步新增失败 " + user.getID(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个用户同步更新
	 * 
	 * @param usersToSyncUpdate
	 * @param islink
	 */
	private void syncUpdateUserOneByOne(List<UserInfoModel> usersToSyncUpdate, boolean islink) {
		List<UserInfoModel> tempList = new ArrayList<UserInfoModel>();
		ResultEntity resultEntity = null;

		for (UserInfoModel user : usersToSyncUpdate) {
			tempList.add(user);

			try {
				resultEntity = userService.userSync(islink, tempList, apikey, secretkey, baseUrl);
				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					userInfoList.remove(user);
					userInfoList.add(user);
				} else {
					// 忽略邮箱再同步一次
					user.setMail(null);
					tempList.set(0, user);
					resultEntity = userService.userSync(islink, tempList, apikey, secretkey, baseUrl);
					if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
						userInfoList.remove(user);
						userInfoList.add(user);
						logger.warn("该用户邮箱异常未同步：" + user.getID());
					} else {
						printLog("用户同步更新失败 ", user.getID(), resultEntity);
					}
				}
			} catch (Exception e) {
				logger.error("用户同步更新失败 " + user.getID(), e);
			}

			tempList.clear();
		}

	}

	/**
	 * 逐个用户同步启用
	 * 
	 * @param usersToEnable
	 */
	private void syncEnableOneByOne(List<UserInfoModel> usersToEnable) {
		List<String> tempList = new ArrayList<String>();
		ResultEntity resultEntity = null;

		for (UserInfoModel user : usersToEnable) {
			tempList.add(user.getUserName());

			try {
				resultEntity = userService.enabledusersSync(tempList, apikey, secretkey, baseUrl);
				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					userInfoList.remove(user);
					userInfoList.add(user);
				} else {
					printLog("用户同步启用失败 ", user.getID(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("用户同步启用失败  " + user.getID(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个用户同步禁用
	 * 
	 * @param usersToDisable
	 */
	private void syncDisableOneByOne(List<UserInfoModel> usersToDisable) {
		List<String> tempList = new ArrayList<String>();
		ResultEntity resultEntity = null;
		for (UserInfoModel user : usersToDisable) {
			tempList.add(user.getUserName());

			try {
				resultEntity = userService.disabledusersSync(tempList, apikey, secretkey, baseUrl);
				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					userInfoList.remove(user);
					userInfoList.add(user);
				} else {
					printLog("用户同步禁用失败 ", user.getID(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("用户同步禁用失败 " + user.getID(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 组织全量数据集合与最新获取组织数据集合进行比较
	 * 
	 * @param fullList
	 *            全量组织数据集合
	 * @param newList
	 *            最新获取组织数据集合
	 * @return 包含 同步新增、更新、 删除等组织集合的Map对象
	 */
	private Map<String, List<OuInfoModel>> compareOrgList(List<OuInfoModel> fullList, List<OuInfoModel> newList) {
		Map<String, List<OuInfoModel>> map = new HashMap<String, List<OuInfoModel>>();

		List<OuInfoModel> orgsToSyncAdd = new ArrayList<OuInfoModel>();
		List<OuInfoModel> orgsToSyncUpdate = new ArrayList<OuInfoModel>();
		List<OuInfoModel> orgsToSyncDelete = new ArrayList<OuInfoModel>();

		for (OuInfoModel newOrg : newList) {
			// 待新增组织
			if (!fullList.contains(newOrg)) {
				// 非过期组织
				if (!isOrgExpired(newOrg)) {
					orgsToSyncAdd.add(newOrg);
				} else {
					logger.info("包含过期组织：" + newOrg.getOuName());
				}
			}
			// 已经存在的组织比较
			else {
				// 组织过期待删除
				if (isOrgExpired(newOrg)) {
					orgsToSyncDelete.add(newOrg);
				} else {
					// 组织更新
					orgsToSyncUpdate.add(newOrg);
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
	private boolean isOrgExpired(OuInfoModel org) {
		Date endDate = org.getEndDate();
		if (endDate == null) {
			return true;
		}

		return endDate.compareTo(new Date()) < 0;
	}

	/**
	 * 用户全量数据集合与最新获取用户数据集合进行比较
	 * 
	 * @param fullList
	 *            全量用户数据集合
	 * @param newList
	 *            最新获取用户数据集合
	 * @return 包含 同步新增、更新、启用、禁用等用户集合的Map对象
	 */
	private Map<String, List<UserInfoModel>> compareUserList(List<UserInfoModel> fullList,
			List<UserInfoModel> newList) {
		Map<String, List<UserInfoModel>> map = new HashMap<String, List<UserInfoModel>>();

		List<UserInfoModel> usersToSyncAdd = new ArrayList<UserInfoModel>();
		List<UserInfoModel> usersToSyncUpdate = new ArrayList<UserInfoModel>();
		List<UserInfoModel> usersToEnable = new ArrayList<UserInfoModel>();
		List<UserInfoModel> usersToDisable = new ArrayList<UserInfoModel>();

		// 待更新用户
		for (UserInfoModel newUser : newList) {
			for (UserInfoModel fullUser : fullList) {
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
					break;
				}
			}
		}

		// 待新增用户
		for (UserInfoModel user : newList) {
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
	 * @param errKey
	 * @param resultEntity
	 */
	private void printLog(String type, String errKey, ResultEntity resultEntity) {
		logger.error(type + "ID：" + errKey + " 错误信息：" + resultEntity.getCode() + "-" + resultEntity.getMessage());
	}
}
