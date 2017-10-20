package openDemo.service.sync.xingdou;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.config.XingdouConfig;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.xingdou.XingdouOuInfoModel;
import openDemo.entity.sync.xingdou.XingdouResDeptData;
import openDemo.entity.sync.xingdou.XingdouResEmpData;
import openDemo.entity.sync.xingdou.XingdouUserInfoModel;
import openDemo.service.sync.AbstractSyncService2;

public class XingdouSyncService extends AbstractSyncService2 implements XingdouConfig {
	// 全量增量区分
	private static final String MODE_FULL = "0";
	private static final String MODE_UPDATE = "1";
	// 登录参数
	private static final String LOGIN_PARAM_S1 = "sharegoo_yun";
	private static final String LOGIN_PARAM_S2 = "yun.koi";
	// 生效状态
	private static final String EFFECTIVE_STATUS = "0";
	// 用户接口要求时间格式
	private static final SimpleDateFormat CUSTOM_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	// 记录日志
	private Logger logger = LogManager.getLogger(XingdouSyncService.class);

	private WebServiceLocator locator = new WebServiceLocator();
	private List<UserInfoModel> sharedModelList;

	public XingdouSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		super.setIsPosIdProvided(false);
		super.setLogger(logger);
	}

	private <T> List getDataModelList(String mode, Class<T> listClassType)
			throws ServiceException, RemoteException, JAXBException {
		// 调用WebService对象
		WebServiceSoap service = locator.getWebServiceSoap();
		// 调用login方法取得凭证
		String sessionID = service.login(LOGIN_PARAM_S1, LOGIN_PARAM_S2);

		// 读取数据总数
		int count = 0;
		List retList = new ArrayList();
		if (listClassType.isAssignableFrom(XingdouOuInfoModel.class)) {
			if (MODE_FULL.equals(mode)) {
				count = service.orgBeginGetDept2(sessionID, MODE_FULL, null);
			} else {
				count = service.orgBeginGetDept2(sessionID, MODE_UPDATE,
						CUSTOM_DATE_FORMAT.format(getYesterdayDate(new Date())));
			}

			if (count > 0) {
				// 返回xml绑定java对象
				JAXBContext context = JAXBContext.newInstance(XingdouResDeptData.class);
				// 读取数据
				String segment = service.getSegment(sessionID, 1, count);

				// xml解析为java对象
				XingdouResDeptData resData = (XingdouResDeptData) context.createUnmarshaller()
						.unmarshal(new StringReader(segment));

				retList = resData.getList();
			}
		} else if (listClassType.isAssignableFrom(XingdouUserInfoModel.class)) {
			if (MODE_FULL.equals(mode)) {
				count = service.empBeginGetEmployee2(sessionID, MODE_FULL, null);
			} else {
				count = service.empBeginGetEmployee2(sessionID, MODE_UPDATE,
						CUSTOM_DATE_FORMAT.format(getYesterdayDate(new Date())));
			}

			if (count > 0) {
				// 返回xml绑定java对象
				JAXBContext context = JAXBContext.newInstance(XingdouResEmpData.class);
				// 读取数据
				String segment = service.getSegment(sessionID, 1, count);

				// xml解析为java对象
				XingdouResEmpData resData = (XingdouResEmpData) context.createUnmarshaller()
						.unmarshal(new StringReader(segment));

				retList = resData.getList();
			}
		}

		// 清除服务器上缓存
		service.clearCache(sessionID);
		// 注销凭证
		service.logOut(sessionID);

		return retList;
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		String status = org.getStatus();
		// 状态为非生效的场合 组织过期
		if (!EFFECTIVE_STATUS.equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		return false;
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		// 无需设置
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
			// 入职日期修改
			String entryTime = tempModel.getEntryTime();
			if (StringUtils.isNotEmpty(entryTime)) {
				tempModel.setEntryTime(entryTime.substring(0, 10));// yyyy-MM-dd共10位
			}

			// 出生日期修改
			String birthday = tempModel.getBirthday();
			if (StringUtils.isNotEmpty(birthday)) {
				tempModel.setBirthday(birthday.substring(0, 10));// yyyy-MM-dd共10位
			}

			// 性别字符串转换 1：男 2：女
			String sex = tempModel.getSex();
			if ("1".equals(sex)) {
				tempModel.setSex("男");
			} else if ("2".equals(sex)) {
				tempModel.setSex("女");
			}
		}
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<XingdouUserInfoModel> dataModelList = (List<XingdouUserInfoModel>) getDataModelList(mode,
				XingdouUserInfoModel.class);
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);
		// 请求接口数据复用
		sharedModelList = newList;

		return getPosListFromUsers(newList);
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<XingdouOuInfoModel> dataModelList = (List<XingdouOuInfoModel>) getDataModelList(mode,
				XingdouOuInfoModel.class);
		List<OuInfoModel> newList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		// 人员同步请求数据与岗位同步时请求数据一致
		return sharedModelList;
	}

}
