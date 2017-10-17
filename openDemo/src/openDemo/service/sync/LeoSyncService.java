package openDemo.service.sync;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.config.LeoConfig;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.LeoOuInfoModel;
import openDemo.entity.sync.LeoPositionModel;
import openDemo.entity.sync.LeoResData;
import openDemo.entity.sync.LeoResJsonModel;
import openDemo.entity.sync.LeoUserInfoModel;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncPositionService;
import openDemo.service.SyncUserService;
import openDemo.utils.HttpClientUtil4Sync;

public class LeoSyncService extends AbstractSyncService implements LeoConfig {
	// 用户接口请求参数值
	private static final String REQUEST_EMP_URL = "https://open.leo.cn/v1/hr/employees/last-updated";
	private static final String REQUEST_ORG_URL = "https://open.leo.cn/v1/hr/origizations/last-updated";
	private static final String REQUEST_POS_URL = "https://open.leo.cn/v1/hr/job-positions/last-updated";
	private static final String REQUEST_PARAM_FROM = "from";
	private static final String REQUEST_PARAM_PAGE = "p";
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	private static final String FROM_DATE = "2017-08-01";// TODO
	private static final String ENABLE_STATUS = "1";
	private static final String DELETED_STATUS = "1";
	private static final String USER_DISABLE_STATUS = "8";
	private static final int DEFAULT_PAGE_SIZE = 50;
	private static final int RESPONSE_STATUS_OK = 200;
	// 自定义map的key
	private static final String MAPKEY_USER_SYNC_ADD = "userSyncAdd";
	private static final String MAPKEY_USER_SYNC_UPDATE = "userSyncUpdate";
	private static final String MAPKEY_USER_SYNC_ENABLE = "userSyncEnable";
	private static final String MAPKEY_USER_SYNC_DISABLE = "userSyncDisable";
	private static final String MAPKEY_ORG_SYNC_ADD = "orgSyncAdd";
	private static final String MAPKEY_ORG_SYNC_UPDATE = "orgSyncUpdate";
	private static final String MAPKEY_ORG_SYNC_DELETE = "orgSyncDelete";
	private static final String MAPKEY_POS_SYNC_ADD = "posSyncAdd";
	private static final String MAPKEY_POS_SYNC_UPDATE = "posSynccUpdate";
	// 请求同步接口成功返回码
	private static final String SYNC_CODE_SUCCESS = "0";
	// 岗位类别的默认值
	private static final String POSITION_CLASS_DEFAULT = "未分类";
	private static final String POSITION_CLASS_SEPARATOR = ";";
	// 日期格式化用
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	// 记录日志
	private static final Logger logger = LogManager.getLogger(LeoSyncService.class);

	// 请求同步接口的service
	private SyncPositionService positionService = new SyncPositionService();
	private SyncOrgService orgService = new SyncOrgService();
	private SyncUserService userService = new SyncUserService();
	// 用于存放请求获取到的数据的集合
	private List<PositionModel> positionList = new LinkedList<PositionModel>();
	private List<OuInfoModel> ouInfoList = new LinkedList<OuInfoModel>();
	private List<UserInfoModel> userInfoList = new LinkedList<UserInfoModel>();
	private ObjectMapper mapper;

	public LeoSyncService() {
		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// json字符串的日期格式
		mapper.setDateFormat(DATE_FORMAT);
	}

	/**
	 * 对外提供的同步方法
	 * 
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	@Override
	public void sync() throws IOException, ReflectiveOperationException {
		int posCount = positionList.size();
		if (posCount > 0) {
			// 岗位增量同步
			logger.info("[岗位增量]同步开始...");
			opPosSync(MODE_UPDATE);
			logger.info("[岗位增量]同步结束");
		} else {
			// 岗位全量同步
			logger.info("[岗位全量]同步开始...");
			opPosSync(MODE_FULL);
			logger.info("[岗位全量]同步结束");
		}

		int orgCount = ouInfoList.size();
		if (orgCount > 0) {
			// 组织增量同步
			logger.info("[组织增量]同步开始...");
			opOrgSync(MODE_UPDATE, false);
			logger.info("[组织增量]同步结束");
		} else {
			// 组织全量同步
			logger.info("[组织全量]同步开始...");
			opOrgSync(MODE_FULL, false);
			logger.info("[组织全量]同步结束");
		}

		int userCount = userInfoList.size();
		if (userCount > 0) {
			// 用户增量同步
			logger.info("[用户增量]同步开始...");
			opUserSync(MODE_UPDATE, true);
			logger.info("[用户增量]同步结束");
		} else {
			// 用户全量同步
			logger.info("[用户全量]同步开始...");
			opUserSync(MODE_FULL, true);
			logger.info("[用户全量]同步结束");
		}
	}

	/**
	 * 岗位同步
	 * 
	 * @param mode
	 * @throws ReflectiveOperationException
	 * @throws IOException
	 */
	public void opPosSync(String mode) throws IOException, ReflectiveOperationException {
		List<LeoPositionModel> userModelList = getDataModelList(mode, REQUEST_POS_URL, LeoPositionModel.class);
		List<PositionModel> newList = copyCreateEntityList(userModelList, PositionModel.class);

		removeExpiredPos(newList);
		setFullPosNames(newList);

		logger.info("岗位同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			logger.info("岗位同步新增Size: " + newList.size());
			syncAddPosOneByOne(newList);
		}
		// 增量模式
		else {
			Map<String, List<PositionModel>> map = comparePosList(positionList, newList);

			List<PositionModel> posToSyncAdd = map.get(MAPKEY_POS_SYNC_ADD);
			if (posToSyncAdd.size() > 0) {
				syncAddPosOneByOne(posToSyncAdd);
			}

			List<PositionModel> posToSyncUpdate = map.get(MAPKEY_POS_SYNC_UPDATE);
			if (posToSyncUpdate.size() > 0) {
				syncUpdatePosOneByOne(posToSyncUpdate);
			}
		}
	}

	/**
	 * 去除过期岗位
	 * 
	 * @param list
	 */
	private void removeExpiredPos(List<PositionModel> list) {
		for (Iterator<PositionModel> iterator = list.iterator(); iterator.hasNext();) {
			PositionModel pos = iterator.next();
			if (isPosExpired(pos)) {
				iterator.remove();
				logger.info("删除了过期岗位：" + pos.getpNames());
			}
		}
	}

	/**
	 * 判断岗位是否过期
	 * 
	 * @param pos
	 * @return
	 */
	private boolean isPosExpired(PositionModel pos) {
		String status = pos.getStatus();
		String deleteStatus = pos.getDeleteStatus();
		// 是否启用为0或者是否删除为1的场合 岗位过期
		if (!ENABLE_STATUS.equals(status) || DELETED_STATUS.equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置岗位名为带类别岗位名
	 * 
	 * @param newList
	 */
	private void setFullPosNames(List<PositionModel> newList) {
		String prefix = POSITION_CLASS_DEFAULT + POSITION_CLASS_SEPARATOR;
		for (PositionModel pos : newList) {
			pos.setpNames(prefix + pos.getpNames());
		}
	}

	/**
	 * 从pNames中得到岗位名(pNames格式: 一级类别;二级类别;岗位名)
	 * 
	 * @param pNames
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
	 * 岗位全量数据集合与最新获取岗位数据集合进行比较
	 * 
	 * @param fullList
	 *            数据库岗位表数据集合
	 * @param newList
	 *            最新获取岗位数据集合
	 * @return
	 */
	private Map<String, List<PositionModel>> comparePosList(List<PositionModel> fullList, List<PositionModel> newList) {
		Map<String, List<PositionModel>> map = new HashMap<String, List<PositionModel>>();
		List<PositionModel> posToSyncAdd = new ArrayList<PositionModel>();
		List<PositionModel> posToSyncUpdate = new ArrayList<PositionModel>();

		for (PositionModel newPos : newList) {
			// 岗位不存在新增
			if (!fullList.contains(newPos)) {
				posToSyncAdd.add(newPos);
			} else {
				String newPosNo = newPos.getpNo();
				if (newPosNo != null) {
					for (PositionModel fullPos : fullList) {
						if (newPosNo.equals(fullPos.getpNo())) {
							String newPosName = newPos.getpNames();
							// 岗位名发生更新
							if (newPosName != null && !newPosName.equals(fullPos.getpNames())) {
								posToSyncUpdate.add(newPos);
							}
							break;
						}
					}
				}
			}
		}

		map.put(MAPKEY_POS_SYNC_ADD, posToSyncAdd);
		map.put(MAPKEY_POS_SYNC_UPDATE, posToSyncUpdate);
		logger.info("岗位同步新增Size: " + posToSyncAdd.size());
		logger.info("岗位同步更新Size: " + posToSyncUpdate.size());

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
			tempList.add(pos);

			try {
				resultEntity = positionService.syncPos(tempList, apikey, secretkey, baseUrl);

				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					positionList.add(pos);
				} else {
					printLog("岗位同步新增失败 ", pos.getpNames(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("岗位同步新增失败 " + pos.getpNames(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个岗位同步更新
	 * 
	 * @param posToSync
	 */
	private void syncUpdatePosOneByOne(List<PositionModel> posToSync) {
		ResultEntity resultEntity = null;
		for (PositionModel pos : posToSync) {
			try {
				// 同步岗位名不需要带分级类别
				resultEntity = positionService.changePosName(pos.getpNo(), getPositionName(pos.getpNames()), apikey,
						secretkey, baseUrl);

				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					positionList.remove(pos);
					positionList.add(pos);
				} else {
					printLog("岗位同步更新失败 ", pos.getpNames(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("岗位同步更新失败 " + pos.getpNames(), e);
			}

		}
	}

	/**
	 * 组织同步 用时：80-90s
	 * 
	 * @param mode
	 * @param isBaseInfo
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	public void opOrgSync(String mode, boolean isBaseInfo) throws IOException, ReflectiveOperationException {
		List<LeoOuInfoModel> modelList = getDataModelList(mode, REQUEST_ORG_URL, LeoOuInfoModel.class);
		List<OuInfoModel> newList = copyCreateEntityList(modelList, OuInfoModel.class);

		removeExpiredOrgs(newList, mode);
		setRootOrgParentId(newList);

		logger.info("组织同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			logger.info("组织同步新增Size: " + newList.size());
			// 进行多次同步
			for (int i = 0; i < 5; i++) {
				syncAddOrgOneByOne(newList, isBaseInfo);
			}
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
	 * 去除过期组织
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

		}
	}

	/**
	 * 设置根组织的父节点id为null
	 * 
	 * @param newList
	 */
	private void setRootOrgParentId(List<OuInfoModel> newList) {
		for (OuInfoModel org : newList) {
			if ("-2".equals(org.getParentID())) {
				org.setParentID(null);
				break;
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
	 * @param mode
	 * @param islink
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	public void opUserSync(String mode, boolean islink) throws IOException, ReflectiveOperationException {
		List<LeoUserInfoModel> modelList = getDataModelList(mode, REQUEST_EMP_URL, LeoUserInfoModel.class);
		List<UserInfoModel> newList = copyCreateEntityList(modelList, UserInfoModel.class);

		copySetUserName(newList);
		changeDateFormatAndSex(modelList, newList);

		logger.info("用户同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
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
	 * 返回过期员工
	 * 
	 * @param list
	 * @return
	 */
	private List<UserInfoModel> getExpiredUsers(List<UserInfoModel> list) {
		List<UserInfoModel> expiredUsers = new ArrayList<UserInfoModel>();
		for (UserInfoModel user : list) {
			if (isUserExpired(user)) {
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
	private void changeDateFormatAndSex(List<LeoUserInfoModel> fromList, List<UserInfoModel> toList) {
		int listSize = toList.size();
		UserInfoModel toModel = null;
		LeoUserInfoModel fromModel = null;

		for (int i = 0; i < listSize; i++) {
			toModel = toList.get(i);
			fromModel = fromList.get(i);

			Date entryTime = fromModel.getEntryTime();
			if (entryTime != null) {
				toModel.setEntryTime(DATE_FORMAT.format(entryTime));
			}

			Date birthday = fromModel.getBirthday();
			if (birthday != null) {
				toModel.setBirthday(DATE_FORMAT.format(birthday));
			}

			// 性别字符串转换 0：男 1：女
			String sex = fromModel.getSex();
			if ("0".equals(sex)) {
				toModel.setSex("男");
			} else if ("1".equals(sex)) {
				toModel.setSex("女");
			}
		}
	}

	/**
	 * 将mail字段值赋值给userName字段
	 * 
	 * @param newList
	 */
	private void copySetUserName(List<UserInfoModel> newList) {
		for (Iterator<UserInfoModel> iterator = newList.iterator(); iterator.hasNext();) {
			UserInfoModel userInfoEntity = iterator.next();
			// userName <= mail
			userInfoEntity.setUserName(userInfoEntity.getMail());
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
	 *            数据库组织表数据集合
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
		String status = org.getStatus();
		String deleteStatus = org.getDeleteStatus();
		// 是否启用为0或者是否删除为1的场合 组织过期
		if (!ENABLE_STATUS.equals(status) || DELETED_STATUS.equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用户全量数据集合与最新获取用户数据集合进行比较
	 * 
	 * @param fullList
	 *            数据库用户表数据集合
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
					if (!isUserExpired(fullUser)) {
						if (isUserExpired(newUser)) {
							// 用户过期禁用
							usersToDisable.add(newUser);
						} else {
							// 存在用户更新
							usersToSyncUpdate.add(newUser);
						}
					} else {
						if (!isUserExpired(newUser)) {
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
	 * 判断用户是否过期
	 * 
	 * @param user
	 * @return
	 */
	private boolean isUserExpired(UserInfoModel user) {
		String status = user.getStatus();
		String deleteStatus = user.getDeleteStatus();
		// 用户状态为8:离职 或者是否删除为1的场合下过期
		if (USER_DISABLE_STATUS.equals(status) || DELETED_STATUS.equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 向客户接口发送请求并返回json数据模型集合
	 * 
	 * @param <T>
	 * 
	 * @param mode
	 * @param requestUrl
	 * @param classType
	 * @return
	 * @throws IOException
	 */
	private <T> List<T> getDataModelList(String mode, String requestUrl, Class<T> classType) throws IOException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(REQUEST_PARAM_FROM, getTimestamp(mode));
		// 用于认证的header信息
		List<Header> authHeader = getAuthHeader();

		List<T> tempList = new ArrayList<T>();
		// 首次请求
		Map<Integer, List<T>> dataMap = requestGetData(requestUrl, paramMap, authHeader, classType);
		tempList.addAll(dataMap.values().iterator().next());

		// 获取total值后请求全部数据
		int total = dataMap.keySet().iterator().next();
		for (int i = 0; i < calcRequestTimes(total, DEFAULT_PAGE_SIZE) - 1; i++) {
			// 请求页码从2开始
			paramMap.put(REQUEST_PARAM_PAGE, i + 2);
			dataMap = requestGetData(requestUrl, paramMap, authHeader, classType);
			tempList.addAll(dataMap.values().iterator().next());
		}

		return tempList;
	}

	/**
	 * 向客户接口请求数据并解析
	 * 
	 * @param requestUrl
	 * @param paramMap
	 * @param headers
	 * @param classType
	 * @return Map集合 key：返回数据total值 value：岗位或组织或人员数据集合
	 * @throws IOException
	 */
	private <T> Map<Integer, List<T>> requestGetData(String requestUrl, Map<String, Object> paramMap,
			List<Header> headers, Class<T> classType) throws IOException {
		String jsonString = HttpClientUtil4Sync.doGet(requestUrl, paramMap, headers);
		// logger.info(jsonString);

		// 将json字符串转为用户json对象数据模型
		LeoResJsonModel<T> resJsonModel = null;
		// 将json字符串中的jobPositions, origizations, employees统一替换成dataList
		String replacement = "dataList";
		// 类型判断传入不同类型参数
		if (classType.isAssignableFrom(LeoPositionModel.class)) {
			jsonString = jsonString.replaceFirst("jobPositions", replacement);
			resJsonModel = mapper.readValue(jsonString, new TypeReference<LeoResJsonModel<LeoPositionModel>>() {
			});
		} else if (classType.isAssignableFrom(LeoOuInfoModel.class)) {
			jsonString = jsonString.replaceFirst("origizations", replacement);
			resJsonModel = mapper.readValue(jsonString, new TypeReference<LeoResJsonModel<LeoOuInfoModel>>() {
			});
		} else if (classType.isAssignableFrom(LeoUserInfoModel.class)) {
			jsonString = jsonString.replaceFirst("employees", replacement);
			resJsonModel = mapper.readValue(jsonString, new TypeReference<LeoResJsonModel<LeoUserInfoModel>>() {
			});
		}

		Map<Integer, List<T>> dataMap = new HashMap<Integer, List<T>>();
		List<T> dataList = new ArrayList<T>();
		// 返回数据状态判断
		if (RESPONSE_STATUS_OK == resJsonModel.getCode()) {
			LeoResData<T> data = resJsonModel.getData();
			if (data != null) {
				dataList = data.getDataList();
				dataMap.put(data.getTotal(), dataList);
			} else {
				throw new IOException("获取客户接口[" + classType.getSimpleName() + "]数据data为null");
			}
		} else {
			throw new IOException("获取客户接口[" + classType.getSimpleName() + "]数据错误：" + resJsonModel.getMessage());
		}

		return dataMap;
	}

	/**
	 * 根据数据总量和每页数量计算应当请求的次数
	 * 
	 * @param totalCount
	 * @param pageSize
	 * @return
	 */
	private int calcRequestTimes(int totalCount, int pageSize) {
		int reqTimes = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			reqTimes = reqTimes + 1;
		}

		return reqTimes;
	}

	/**
	 * 获取全量或增量模式下请求的时间戳参数值
	 * 
	 * @param mode
	 * @return
	 */
	private int getTimestamp(String mode) {
		// 默认时间戳
		int timestamp = (int) (new Date().getTime() / 1000);
		if (MODE_FULL.equals(mode)) {
			try {
				timestamp = (int) (DATE_FORMAT.parse(FROM_DATE).getTime() / 1000);
			} catch (ParseException e) {
				logger.error("获取时间戳失败", e);
			}
		} else {
			// 当日零点时间
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			timestamp = (int) (c.getTimeInMillis() / 1000);
		}
		return timestamp;
	}

	/**
	 * token放在请求header的Authorization中
	 * 
	 * @return
	 * @throws IOException
	 */
	private List<Header> getAuthHeader() throws IOException {
		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Authorization", "Bearer " + getToken()));
		return headers;
	}

	/**
	 * 获取token
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getToken() throws IOException {
		String url = "https://open.leo.cn/v1/authentication/oauth2/get-token";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("access_key", "oleo_42db6ee396eb8765435e44446befad8e");
		paramMap.put("secret_key", "5f81f9a50e7c4043efece652b7a82be2d0d90839b9b550b66c1fb865480a6aad");

		// 从json字符串中解析token
		JsonNode jsonNode = mapper.readTree(HttpClientUtil4Sync.doPost(url, paramMap));
		String token = jsonNode.get("data").get("token").asText();

		return token;
	}

	/**
	 * 同步返回错误信息日志记录
	 * 
	 * @param type
	 * @param errKey
	 * @param resultEntity
	 */
	protected void printLog(String type, String errKey, ResultEntity resultEntity) {
		logger.error(type + "ID：" + errKey + " 错误信息：" + resultEntity.getCode() + "-" + resultEntity.getMessage());
	}
}
