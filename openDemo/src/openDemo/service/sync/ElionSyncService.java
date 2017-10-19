package openDemo.service.sync;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.DOMException;

import openDemo.config.ElionConfig;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.elion.EL_INT_COMMON_SYNC_REQ_TypeShape;
import openDemo.entity.sync.elion.EL_INT_DEPT_FULLSYNC_RES;
import openDemo.entity.sync.elion.EL_INT_DEPT_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_DEPT_SYNC_RESLine;
import openDemo.entity.sync.elion.EL_INT_JOBCD_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_JOBCD_SYNC_RESLine;
import openDemo.entity.sync.elion.EL_INT_PER_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_PER_SYNC_RESLine;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncPositionService;
import openDemo.service.SyncUserService;

public class ElionSyncService extends AbstractSyncService implements ElionConfig {
	// 用户接口请求参数值
	// 请求webservice的TargetEndpointAddress参数
	private static String ENDPOINT_ADDRESS = "http://ehr.elion.com.cn/PSIGW/PeopleSoftServiceListeningConnector/PSFT_HR";
	// 全量同步共通参数
	private static String FULLSYNC_REQ_ELEMENT_NAME = "EL_INT_COMMON_FULLSYNC_REQ";
	private static String FULLSYNC_REQ_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INTERFACE.EL_INT_COMMON_FULLSYNC_REQ.V1";
	// 增量同步共通参数
	private static String SYNC_REQ_ELEMENT_NAME = "EL_INT_COMMON_SYNC_REQ";
	private static String SYNC_REQ_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INTERFACE.EL_INT_COMMON_SYNC_REQ.V1";
	// 岗位全量同步参数
	private static String JOB_FULLSYNC_OPERATION_NAME = "EL_INT_JOBCD_FULLSYNC_OP";
	private static String JOB_FULLSYNC_SOAP_ACTION = "EL_INT_JOBCD_FULLSYNC_OP.v1";
	private static String JOB_FULLSYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_JOBCD_FULLSYNC_RES.V1";
	// 岗位增量同步参数
	private static String JOB_SYNC_OPERATION_NAME = "EL_INT_JOBCD_SYNC_OP";
	private static String JOB_SYNC_SOAP_ACTION = "EL_INT_JOBCD_SYNC_OP.v1";
	private static String JOB_SYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_JOBCD_SYNC_RES.V1";
	// 部门全量同步参数
	private static String DEPT_FULLSYNC_OPERATION_NAME = "EL_INT_DEPT_FULLSYNC_OP";
	private static String DEPT_FULLSYNC_SOAP_ACTION = "EL_INT_DEPT_FULLSYNC_OP.v1";
	private static String DEPT_FULLSYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_DEPT_FULLSYNC_RES.V1";
	// 部门增量同步参数
	private static String DEPT_SYNC_OPERATION_NAME = "EL_INT_DEPT_SYNC_OP";
	private static String DEPT_SYNC_SOAP_ACTION = "EL_INT_DEPT_SYNC_OP.v1";
	private static String DEPT_SYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_DEPT_SYNC_RES.V1";
	// 人员全量同步参数
	private static String EMP_FULLSYNC_OPERATION_NAME = "EL_INT_PER_FULLSYNC_OP";
	private static String EMP_FULLSYNC_SOAP_ACTION = "EL_INT_PER_FULLSYNC_OP.v1";
	private static String EMP_FULLSYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_PER_FULLSYNC_RES.V1";
	// 人员增量同步参数
	private static String EMP_SYNC_OPERATION_NAME = "EL_INT_PER_SYNC_OP";
	private static String EMP_SYNC_SOAP_ACTION = "EL_INT_PER_SYNC_OP.v1";
	private static String EMP_SYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_PER_SYNC_RES.V1";
	// 请求数据参数
	private static final String DATA_SOURCE_ESB = "99";
	private static final String DATA_FROM_INDEX = "1";
	private static final String DATA_TO_INDEX = "5000";
	private static final int PAGE_SIZE = 5000;
	// 生效状态
	private static final String EFFECTIVE_STATUS = "A";
	// 员工主岗标志
	private static final String EMPLOYEE_RECORD = "0";
	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// 自定义map的key
	private static final String MAPKEY_USER_SYNC_ADD = "userSyncAdd";
	private static final String MAPKEY_USER_SYNC_UPDATE = "userSyncUpdate";
	private static final String MAPKEY_USER_SYNC_DELETE = "userSyncDelete";
	private static final String MAPKEY_ORG_SYNC_ADD = "orgSyncAdd";
	private static final String MAPKEY_ORG_SYNC_UPDATE = "orgSyncUpdate";
	private static final String MAPKEY_ORG_SYNC_DELETE = "orgSyncDelete";
	private static final String MAPKEY_POS_SYNC_ADD = "posSyncAdd";
	private static final String MAPKEY_POS_SYNC_UPDATE = "posSyncUpdate";
	// 请求同步接口成功返回码
	private static final String SYNC_CODE_SUCCESS = "0";
	// 岗位类别的默认值
	private static final String POSITION_CLASS_DEFAULT = "未分类";
	private static final String POSITION_CLASS_SEPARATOR = ";";
	// 日期格式化用
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat CUSTOMER_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	// 记录日志
	private static final Logger logger = LogManager.getLogger(ElionSyncService.class);

	// 请求同步接口的service
	private SyncPositionService positionService = new SyncPositionService();
	private SyncOrgService orgService = new SyncOrgService();
	private SyncUserService userService = new SyncUserService();
	// 用于存放请求获取到的数据的集合
	private List<PositionModel> positionList = new LinkedList<PositionModel>();
	private List<OuInfoModel> ouInfoList = new LinkedList<OuInfoModel>();
	private List<UserInfoModel> userInfoList = new LinkedList<UserInfoModel>();

	public ElionSyncService() {
	}

	/**
	 * 对外提供的同步方法
	 * 
	 * @throws Exception
	 */
	@Override
	public void sync() throws Exception {
		int posCount = positionList.size();
		if (posCount > 0) {
			// 岗位增量同步
			opPosSync(MODE_UPDATE);
		} else {
			// 岗位全量同步
			opPosSync(MODE_FULL);
		}

		int orgCount = ouInfoList.size();
		if (orgCount > 0) {
			// 组织增量同步
			opOrgSync(MODE_UPDATE, false);
		} else {
			// 组织全量同步
			opOrgSync(MODE_FULL, false);
		}

		int userCount = userInfoList.size();
		if (userCount > 0) {
			// 用户增量同步
			opUserSync(MODE_UPDATE, true);
		} else {
			// 用户全量同步
			opUserSync(MODE_FULL, true);
		}
	}

	/**
	 * 岗位同步
	 * 
	 * @param mode
	 * @throws Exception
	 */
	public void opPosSync(String mode) throws Exception {
		List<EL_INT_JOBCD_SYNC_RESLine> modelList = getDataModelList(mode, EL_INT_JOBCD_SYNC_RESLine.class);
		List<PositionModel> newList = copyCreateEntityList(modelList, PositionModel.class);

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
		// 状态为非生效的场合 岗位过期
		if (!EFFECTIVE_STATUS.equals(status)) {
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
			String pNameClass = pos.getpNameClass();
			if (StringUtils.isBlank(pNameClass)) {
				pos.setpNames(prefix + pos.getpNames());
			} else {
				// 替换岗位类别名中的特殊字符
				pNameClass = pNameClass.replaceAll("&amp;", "&");
				pos.setpNames(pNameClass + POSITION_CLASS_SEPARATOR + pos.getpNames());
			}
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
	 *            全量岗位数据集合
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
							if (!newPosName.equals(fullPos.getpNames())) {
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
	 * 组织同步
	 * 
	 * @param mode
	 * @param isBaseInfo
	 * @throws Exception
	 */
	public void opOrgSync(String mode, boolean isBaseInfo) throws Exception {
		List<EL_INT_DEPT_SYNC_RESLine> modelList = getDataModelList(mode, EL_INT_DEPT_SYNC_RESLine.class);
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
		// 仅全量模式下执行
		if (MODE_FULL.equals(mode)) {
			for (Iterator<OuInfoModel> iterator = list.iterator(); iterator.hasNext();) {
				OuInfoModel org = iterator.next();
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
			// 客户数据中根组织的上级部门id为""
			if ("".equals(org.getParentID())) {
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
	 * @throws Exception
	 */
	public void opUserSync(String mode, boolean islink) throws Exception {
		List<EL_INT_PER_SYNC_RESLine> modelList = getDataModelList(mode, EL_INT_PER_SYNC_RESLine.class);
		List<UserInfoModel> newList = copyCreateEntityList(modelList, UserInfoModel.class);

		changeDateFormatAndSex(modelList, newList);
		removeExpiredUsers(newList, mode);

		logger.info("用户同步Total Size: " + newList.size());
		// 全量模式
		if (MODE_FULL.equals(mode)) {
			logger.info("用户同步新增Size: " + newList.size());
			syncAddUserOneByOne(newList, islink);
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

			List<UserInfoModel> usersToDelete = map.get(MAPKEY_USER_SYNC_DELETE);
			if (usersToDelete.size() > 0) {
				syncDeleteOneByOne(usersToDelete);
			}
		}

	}

	/**
	 * 删除过期员工
	 * 
	 * @param list
	 * @param mode
	 */
	private void removeExpiredUsers(List<UserInfoModel> list, String mode) {
		// 仅全量模式下执行
		if (MODE_FULL.equals(mode)) {
			for (Iterator<UserInfoModel> iterator = list.iterator(); iterator.hasNext();) {
				UserInfoModel user = iterator.next();
				if (isUserExpired(user)) {
					iterator.remove();
					logger.info("删除了过期员工：" + user.getID());
				}
			}

		}
	}

	/**
	 * 将模型对象的日期进行格式化(yyyy-MM-dd)后赋值给对应的java同步对象 + 性别值转换
	 * 
	 * @param fromList
	 *            模型对象集合
	 * @param toList
	 *            java同步对象集合
	 */
	private void changeDateFormatAndSex(List<EL_INT_PER_SYNC_RESLine> fromList, List<UserInfoModel> toList) {
		int listSize = toList.size();
		UserInfoModel toModel = null;
		EL_INT_PER_SYNC_RESLine fromModel = null;

		for (int i = 0; i < listSize; i++) {
			toModel = toList.get(i);
			fromModel = fromList.get(i);

			String entryTime = fromModel.getEntryTime();
			if (StringUtils.isNotEmpty(entryTime)) {
				try {
					toModel.setEntryTime(DATE_FORMAT.format(CUSTOMER_DATE_FORMAT.parse(entryTime)));
				} catch (ParseException e) {
					logger.warn("日期格式有误 " + fromModel.getID() + "：" + entryTime);
				}
			}

			String birthday = fromModel.getBirthday();
			if (StringUtils.isNotEmpty(birthday)) {
				try {
					toModel.setBirthday(DATE_FORMAT.format(CUSTOMER_DATE_FORMAT.parse(birthday)));
				} catch (ParseException e) {
					logger.warn("日期格式有误 " + fromModel.getID() + "：" + birthday);
				}
			}

			// 性别字符串转换 M：男 F：女
			String sex = fromModel.getSex();
			if ("M".equals(sex)) {
				toModel.setSex("男");
			} else if ("F".equals(sex)) {
				toModel.setSex("女");
			}
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
	 * 逐个用户同步删除
	 * 
	 * @param usersToDelete
	 */
	private void syncDeleteOneByOne(List<UserInfoModel> usersToDelete) {
		List<String> tempList = new ArrayList<String>();
		ResultEntity resultEntity = null;
		for (UserInfoModel user : usersToDelete) {
			tempList.add(user.getUserName());

			try {
				resultEntity = userService.deletedusersSync(tempList, apikey, secretkey, baseUrl);
				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					userInfoList.remove(user);
					userInfoList.add(user);
				} else {
					printLog("用户同步删除失败 ", user.getID(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("用户同步删除失败 " + user.getID(), e);
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
		String status = org.getStatus();
		// 状态为非生效的场合 组织过期
		if (!EFFECTIVE_STATUS.equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用户全量数据集合与最新获取用户数据集合进行比较
	 * 
	 * @param fullList
	 *            全量用户数据集合
	 * @param newList
	 *            最新获取用户数据集合
	 * @return 包含 同步新增、更新、删除等用户集合的Map对象
	 */
	private Map<String, List<UserInfoModel>> compareUserList(List<UserInfoModel> fullList,
			List<UserInfoModel> newList) {
		Map<String, List<UserInfoModel>> map = new HashMap<String, List<UserInfoModel>>();

		List<UserInfoModel> usersToSyncAdd = new ArrayList<UserInfoModel>();
		List<UserInfoModel> usersToSyncUpdate = new ArrayList<UserInfoModel>();
		List<UserInfoModel> usersToSyncDelete = new ArrayList<UserInfoModel>();

		for (UserInfoModel newUser : newList) {
			// 待新增用户
			if (!fullList.contains(newUser)) {
				if (!isUserExpired(newUser)) {
					usersToSyncAdd.add(newUser);
				} else {
					logger.info("包含过期员工：" + newUser.getID());
				}
			}
			// 已经存在的用户比较
			else {
				if (isUserExpired(newUser)) {
					// 用户过期删除
					usersToSyncDelete.add(newUser);
				} else {
					// 存在用户更新
					usersToSyncUpdate.add(newUser);
				}
			}
		}

		map.put(MAPKEY_USER_SYNC_ADD, usersToSyncAdd);
		map.put(MAPKEY_USER_SYNC_UPDATE, usersToSyncUpdate);
		map.put(MAPKEY_USER_SYNC_DELETE, usersToSyncDelete);

		logger.info("用户同步新增Size: " + usersToSyncAdd.size());
		logger.info("用户同步更新Size: " + usersToSyncUpdate.size());
		logger.info("用户同步删除Size: " + usersToSyncDelete.size());

		return map;
	}

	/**
	 * 判断用户是否过期
	 * 
	 * @param user
	 * @return
	 */
	private boolean isUserExpired(UserInfoModel user) {
		String userName = user.getUserName();
		String status = user.getStatus();
		// 该字段在请求到客户接口数据时已关联EmployeeRecord字段
		String deleteStatus = user.getDeleteStatus();
		String expireDate = user.getExpireDate();
		// UserName为空或者用户状态为非生效或者非主岗或者已经离职的场合下过期
		if (StringUtils.isBlank(userName) || !EFFECTIVE_STATUS.equals(status) || !EMPLOYEE_RECORD.equals(deleteStatus)
				|| StringUtils.isNotEmpty(expireDate)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 向客户接口发送请求并返回数据模型集合
	 * 
	 * @param <T>
	 * 
	 * @param mode
	 * @param classType
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 * @throws SOAPException
	 * @throws DOMException
	 */
	private <T> List<T> getDataModelList(String mode, Class<T> classType)
			throws IOException, ServiceException, DOMException, SOAPException {
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(ENDPOINT_ADDRESS);

		// 设置请求参数
		EL_INT_COMMON_SYNC_REQ_TypeShape req = new EL_INT_COMMON_SYNC_REQ_TypeShape();
		req.setReqSystemID(DATA_SOURCE_ESB);
		if (MODE_FULL.equals(mode)) {
			req.setParam1(DATA_FROM_INDEX);
			req.setParam2(DATA_TO_INDEX);
		} else {
			Date today = new Date();
			req.setBeginDate(CUSTOMER_DATE_FORMAT.format(getYesterdayDate(today)));
			req.setEndDate(CUSTOMER_DATE_FORMAT.format(today));
		}

		Object[] lines = null;
		// 请求岗位数据
		if (classType.isAssignableFrom(EL_INT_JOBCD_SYNC_RESLine.class)) {
			if (MODE_FULL.equals(mode)) {
				setPropsBeforeCall(mode, call, JOB_FULLSYNC_SOAP_ACTION, JOB_FULLSYNC_OPERATION_NAME,
						JOB_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_JOBCD_SYNC_RES.class);
			} else {
				setPropsBeforeCall(mode, call, JOB_SYNC_SOAP_ACTION, JOB_SYNC_OPERATION_NAME,
						JOB_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_JOBCD_SYNC_RES.class);
			}
			EL_INT_JOBCD_SYNC_RES res = (EL_INT_JOBCD_SYNC_RES) call.invoke(new java.lang.Object[] { req });
			lines = res.getLine();
		}
		// 请求部门数据
		else if (classType.isAssignableFrom(EL_INT_DEPT_SYNC_RESLine.class)) {
			if (MODE_FULL.equals(mode)) {
				setPropsBeforeCall(mode, call, DEPT_FULLSYNC_SOAP_ACTION, DEPT_FULLSYNC_OPERATION_NAME,
						DEPT_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_DEPT_FULLSYNC_RES.class);

				EL_INT_DEPT_FULLSYNC_RES res = (EL_INT_DEPT_FULLSYNC_RES) call.invoke(new java.lang.Object[] { req });
				lines = res.getLine();
			} else {
				setPropsBeforeCall(mode, call, DEPT_SYNC_SOAP_ACTION, DEPT_SYNC_OPERATION_NAME,
						DEPT_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_DEPT_SYNC_RES.class);

				EL_INT_DEPT_SYNC_RES res = (EL_INT_DEPT_SYNC_RES) call.invoke(new java.lang.Object[] { req });
				lines = res.getLine();
			}
		}
		// 请求人员数据
		else if (classType.isAssignableFrom(EL_INT_PER_SYNC_RESLine.class)) {
			if (MODE_FULL.equals(mode)) {
				setPropsBeforeCall(mode, call, EMP_FULLSYNC_SOAP_ACTION, EMP_FULLSYNC_OPERATION_NAME,
						EMP_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_PER_SYNC_RES.class);
			} else {
				setPropsBeforeCall(mode, call, EMP_SYNC_SOAP_ACTION, EMP_SYNC_OPERATION_NAME,
						EMP_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_PER_SYNC_RES.class);
			}

			EL_INT_PER_SYNC_RES res = (EL_INT_PER_SYNC_RES) call.invoke(new java.lang.Object[] { req });
			lines = res.getLine();

			// 人员数据较多进行多次获取
			int i = 1;
			Object[] tempLines = lines;
			while (tempLines != null) {
				req.setParam1(String.valueOf(PAGE_SIZE * i + 1));
				req.setParam2(String.valueOf(PAGE_SIZE * (i + 1)));

				res = (EL_INT_PER_SYNC_RES) call.invoke(new java.lang.Object[] { req });
				tempLines = res.getLine();
				// 数组合并
				lines = ArrayUtils.addAll(lines, tempLines);
				i++;
			}
		}

		List<T> tempList = new ArrayList<T>();
		if (lines != null && lines.length > 0) {
			tempList = (List<T>) Arrays.asList(lines);
		} else {
			logger.info("获取客户接口[" + classType.getSimpleName() + "]数据为空！");
		}
		return tempList;
	}

	private Date getYesterdayDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}

	/**
	 * 请求客户接口前设置请求属性
	 * 
	 * @param mode
	 *            全量/增量类型
	 * @param call
	 *            axis中call对象
	 * @param soapAction
	 *            请求属性SOAPAction属性值
	 * @param operationName
	 *            请求webservice的操作名
	 * @param resElementNamaspace
	 *            返回xml中对象命名空间属性值
	 * @param reqClassType
	 *            请求xml对应java类
	 * @param resClassType
	 *            返回xml对应java类
	 * @throws SOAPException
	 * @throws DOMException
	 */
	private <E, T> void setPropsBeforeCall(String mode, Call call, String soapAction, String operationName,
			String resElementNamaspace, Class<E> reqClassType, Class<T> resClassType)
			throws DOMException, SOAPException {
		// 设置共通参数
		String reqElementNamaspace = null;
		String reqElement = null;
		if (MODE_FULL.equals(mode)) {
			reqElement = FULLSYNC_REQ_ELEMENT_NAME;
			reqElementNamaspace = FULLSYNC_REQ_ELEMENT_NAMASPACE;
		} else {
			reqElement = SYNC_REQ_ELEMENT_NAME;
			reqElementNamaspace = SYNC_REQ_ELEMENT_NAMASPACE;
		}
		// 设置OperationDesc
		OperationDesc oper = new OperationDesc();
		oper.setName(operationName);
		ParameterDesc param = new ParameterDesc(new QName(reqElementNamaspace, reqElement), ParameterDesc.IN,
				new QName(reqElementNamaspace, reqClassType.getSimpleName()), reqClassType, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName(resElementNamaspace, resClassType.getSimpleName()));
		oper.setReturnClass(resClassType);
		oper.setReturnQName(new QName(resElementNamaspace, resClassType.getSimpleName()));
		oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		// 设置call参数值
		call.setOperation(oper);
		call.setUseSOAPAction(true);
		call.setSOAPActionURI(soapAction);
		call.setEncodingStyle(null);
		call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		call.setOperationName(new QName("", operationName));

		// 请求信息带用户名密码
		addSecurityAuth(call);
	}

	/**
	 * 请求xml的header标签中增加安全认证信息
	 * 
	 * @param call
	 * @throws DOMException
	 * @throws SOAPException
	 */
	private void addSecurityAuth(Call call) throws DOMException, SOAPException {
		String AUTH_PREFIX = "wsse";
		String AUTH_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		SOAPHeaderElement soapHeaderElement = null;

		SOAPFactory soapFactory = SOAPFactory.newInstance();
		SOAPElement wsSecHeaderElm = soapFactory.createElement("Security", AUTH_PREFIX, AUTH_NS);
		SOAPElement userNameTokenElm = soapFactory.createElement("UsernameToken", AUTH_PREFIX, AUTH_NS);
		SOAPElement userNameElm = soapFactory.createElement("Username", AUTH_PREFIX, AUTH_NS);
		SOAPElement passwdElm = soapFactory.createElement("Password", AUTH_PREFIX, AUTH_NS);
		passwdElm.setAttribute("Type",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");

		userNameElm.addTextNode("EL_INTERFACE");
		passwdElm.addTextNode("interface");

		userNameTokenElm.addChildElement(userNameElm);
		userNameTokenElm.addChildElement(passwdElm);
		wsSecHeaderElm.addChildElement(userNameTokenElm);
		soapHeaderElement = new SOAPHeaderElement(wsSecHeaderElm);
		soapHeaderElement.setMustUnderstand(true);
		soapHeaderElement.setActor(null);

		call.addHeader(soapHeaderElement);
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
