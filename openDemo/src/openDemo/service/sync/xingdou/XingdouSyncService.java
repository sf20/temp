package openDemo.service.sync.xingdou;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.config.TestConfig;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.xingdou.XingdouOuInfoModel;
import openDemo.entity.sync.xingdou.XingdouResData;
import openDemo.entity.sync.xingdou.XingdouUserInfoModel;
import openDemo.service.sync.AbstractSyncService2;

public class XingdouSyncService extends AbstractSyncService2 implements TestConfig {
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
	private static final Logger LOGGER = LogManager.getLogger(XingdouSyncService.class);

	public XingdouSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		super.setLogger(LOGGER);
	}

	private <T> List<T> getDataModelList(String mode, Class<T> classType)
			throws ServiceException, RemoteException, JAXBException {
		// 调用WebService对象
		WebServiceLocator locator = new WebServiceLocator();
		WebServiceSoap service = locator.getWebServiceSoap();
		// 调用login方法取得凭证
		String sessionID = service.login(LOGIN_PARAM_S1, LOGIN_PARAM_S2);

		// 读取数据总数
		int count = 0;
		if (classType.isAssignableFrom(XingdouOuInfoModel.class)) {
			if (MODE_FULL.equals(mode)) {
				count = service.orgBeginGetDept2(sessionID, MODE_FULL, null);
			} else {
				count = service.orgBeginGetDept2(sessionID, MODE_UPDATE,
						CUSTOM_DATE_FORMAT.format(getYesterdayDate(new Date())));
			}
		} else if (classType.isAssignableFrom(XingdouUserInfoModel.class)) {
			if (MODE_FULL.equals(mode)) {
				count = service.empBeginGetEmployee2(sessionID, MODE_FULL, null);
			} else {
				count = service.empBeginGetEmployee2(sessionID, MODE_UPDATE,
						CUSTOM_DATE_FORMAT.format(getYesterdayDate(new Date())));
			}
		}

		List<T> list = new ArrayList<T>();
		if (count > 0) {
			// 返回xml绑定java对象
			JAXBContext context = JAXBContext.newInstance(new XingdouResData<T>().getClass());
			// 读取数据
			String segment = service.getSegment(sessionID, 1, count);

			// xml解析为java对象
			@SuppressWarnings("unchecked")
			XingdouResData<T> resData = (XingdouResData<T>) context.createUnmarshaller()
					.unmarshal(new StringReader(segment));

			list = resData.getList();
		}
		// 清除服务器上缓存
		service.clearCache(sessionID);

		return list;
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
	protected Map<String, List<PositionModel>> comparePosList(List<PositionModel> fullList,
			List<PositionModel> newList) {
		return comparePosList2(fullList, newList);
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<XingdouUserInfoModel> dataModelList = getDataModelList(mode, XingdouUserInfoModel.class);
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		return getPosListFromUsers(newList);
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<XingdouOuInfoModel> dataModelList = getDataModelList(mode, XingdouOuInfoModel.class);
		List<OuInfoModel> newList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<XingdouUserInfoModel> dataModelList = getDataModelList(mode, XingdouUserInfoModel.class);
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		return newList;
	}

}
