package openDemo.test;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.xingdou.XingdouOuInfoModel;
import openDemo.entity.sync.xingdou.XingdouResDeptData;
import openDemo.entity.sync.xingdou.XingdouResEmpData;
import openDemo.entity.sync.xingdou.XingdouUserInfoModel;
import openDemo.service.sync.xingdou.WebServiceLocator;
import openDemo.service.sync.xingdou.WebServiceSoap;
import openDemo.service.sync.xingdou.XingdouSyncService;

public class XingdouSyncServiceTest {
	private static final String IS_SESSION_VALID = "1";
	private static final String LOGIN_PARAM_S1 = "sharegoo_yun";
	private static final String LOGIN_PARAM_S2 = "yun.koi";

	public static void main(String[] args) throws Exception {
		// getDataTest();
		syncTest();
	}

	static void syncTest() {
		XingdouSyncService service = new XingdouSyncService();
		try {
			service.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void getDataTest() throws Exception {
		WebServiceLocator locator = new WebServiceLocator();
		WebServiceSoap service = locator.getWebServiceSoap();

		String sessionID = service.login(LOGIN_PARAM_S1, LOGIN_PARAM_S2);

		// 获取数据
		getDataModelList(null, PositionModel.class, service, sessionID);
		getDataModelList(null, OuInfoModel.class, service, sessionID);
		getDataModelList(null, UserInfoModel.class, service, sessionID);

		service.logOut(getSessionId(service, sessionID));
	}

	private static String getSessionId(WebServiceSoap service, String sessionId) throws RemoteException {
		if (IS_SESSION_VALID.equals(service.checkLicense(sessionId))) {
			return sessionId;
		} else {
			return service.login(LOGIN_PARAM_S1, LOGIN_PARAM_S2);
		}
	}

	private static <T> List<T> getDataModelList(String mode, Class<T> classType, WebServiceSoap service,
			String sessionID) throws RemoteException, JAXBException {
		sessionID = getSessionId(service, sessionID);

		int count = 0;
		JAXBContext context = null;
		String segment = null;
		if (classType.isAssignableFrom(PositionModel.class)) {
			count = service.orgBeginGetPosition2(sessionID, "0", null);
			segment = service.getSegment(sessionID, 1, count);
			System.out.println(segment);
		} else if (classType.isAssignableFrom(OuInfoModel.class)) {
			count = service.orgBeginGetDept2(sessionID, "0", null);
			context = JAXBContext.newInstance(new XingdouResDeptData().getClass());
			segment = service.getSegment(sessionID, 1, count);

			XingdouResDeptData depts = (XingdouResDeptData) context.createUnmarshaller()
					.unmarshal(new StringReader(segment));
			System.out.println(depts.getList().size());
			List<XingdouOuInfoModel> list = depts.getList();
			for (XingdouOuInfoModel dept : list) {
				System.out.println(
						dept.getID() + "=" + dept.getParentID() + "=" + dept.getOuName() + "=" + dept.getStatus());
			}
		} else if (classType.isAssignableFrom(UserInfoModel.class)) {
			count = service.empBeginGetEmployee2(sessionID, "0", null);
			System.out.println("==" + count);
			context = JAXBContext.newInstance(new XingdouResEmpData().getClass());
			segment = service.getSegment(sessionID, 1, count);

			XingdouResEmpData emps = (XingdouResEmpData) context.createUnmarshaller()
					.unmarshal(new StringReader(segment));
			System.out.println(emps.getList().size());
			List<XingdouUserInfoModel> list = emps.getList();
			for (XingdouUserInfoModel emp : list) {
				System.out.println(emp.getID() + "=" + emp.getCnName() + "=" + emp.getSex() + "=" + emp.getOrgOuCode()
						+ "=" + emp.getStatus());
			}
		}

		service.clearCache(sessionID);
		return null;
	}
}
