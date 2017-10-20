package openDemo.test;

import java.rmi.RemoteException;
import java.util.Date;

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
import org.w3c.dom.DOMException;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.sync.elion.EL_INT_COMMON_SYNC_REQ_TypeShape;
import openDemo.entity.sync.elion.EL_INT_DEPT_FULLSYNC_RES;
import openDemo.entity.sync.elion.EL_INT_DEPT_FULLSYNC_RESLine;
import openDemo.entity.sync.elion.EL_INT_DEPT_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_JOBCD_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_JOBCD_SYNC_RESLine;
import openDemo.entity.sync.elion.EL_INT_PER_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_PER_SYNC_RESLine;
import openDemo.service.sync.ElionSyncService;

public class ElionSyncServiceTest {
	// 请求webservice的TargetEndpointAddress参数
	static String ENDPOINT_ADDRESS = "http://119.61.11.215:8080/PSIGW/PeopleSoftServiceListeningConnector/PSFT_HR";
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

	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	static Service service = new Service();

	public static void main(String[] args) throws ServiceException, RemoteException, ReflectiveOperationException {
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(ENDPOINT_ADDRESS);
		// jobFullSyncTest(call);
		// jobSyncTest(call);
		// deptFullSyncTest(call);
		// deptSyncTest(call);
		// empFullSyncTest(call);
		// empSyncTest(call);

		elionSyncServiceTest();
	}

	static void elionSyncServiceTest() {
		Date startDate = new Date();
		System.out.println("同步中......");

		ElionSyncService elionSyncService = new ElionSyncService();
		try {
			elionSyncService.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("同步时间：" + calcMinutesBetween(startDate, new Date()));
	}

	private static long calcMinutesBetween(Date d1, Date d2) {
		return Math.abs((d2.getTime() - d1.getTime())) / 1000;
	}

	static void empSyncTest(Call call) throws RemoteException {
		setPropsBeforeCall(MODE_UPDATE, call, EMP_SYNC_SOAP_ACTION, EMP_SYNC_OPERATION_NAME,
				EMP_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class, EL_INT_PER_SYNC_RES.class);

		EL_INT_COMMON_SYNC_REQ_TypeShape req = new EL_INT_COMMON_SYNC_REQ_TypeShape();
		req.setReqSystemID("99");
		EL_INT_PER_SYNC_RES res = (EL_INT_PER_SYNC_RES) call.invoke(new java.lang.Object[] { req });

		System.out.println(res.getOperation_Name());
	}

	static void empFullSyncTest(Call call) throws RemoteException, ReflectiveOperationException, ServiceException {
		setPropsBeforeCall(MODE_FULL, call, EMP_FULLSYNC_SOAP_ACTION, EMP_FULLSYNC_OPERATION_NAME,
				EMP_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class, EL_INT_PER_SYNC_RES.class);

		EL_INT_COMMON_SYNC_REQ_TypeShape req = new EL_INT_COMMON_SYNC_REQ_TypeShape();
		req.setParam1("1");
		req.setParam2("5000");
		req.setReqSystemID("99");
		EL_INT_PER_SYNC_RES res = (EL_INT_PER_SYNC_RES) call.invoke(new java.lang.Object[] { req });

		EL_INT_PER_SYNC_RESLine[] lines = res.getLine();
		System.out.println(lines.length);

		// 人员数据较多进行多次获取
		int i = 1;
		Object[] tempLines = lines;
		while (tempLines != null) {
			// call.clearOperation();

			req.setParam1(String.valueOf(5000 * i + 1));
			req.setParam2(String.valueOf(5000 * (i + 1)));

			res = (EL_INT_PER_SYNC_RES) call.invoke(new java.lang.Object[] { req });
			tempLines = res.getLine();
			lines = (EL_INT_PER_SYNC_RESLine[]) ArrayUtils.addAll(lines, tempLines);
			i++;
		}
		System.out.println(lines.length);

		// EL_INT_PER_SYNC_RESLine emps = res.getLine(0);
		// UserInfoModel userInfo = new UserInfoModel();
		// BeanUtils.copyProperties(userInfo, emps);
		// System.out.println(
		// userInfo.getID() + "=" + userInfo.getUserName() + "=" +
		// userInfo.getCnName()
		// + "=" + userInfo.getSex()
		// + "=" + userInfo.getMail() + "=" + userInfo.getMobile() + "=" +
		// userInfo.getOrgOuCode() + "="
		// + userInfo.getPostionNo() + "=" + userInfo.getExpireDate() + "=" +
		// userInfo.getStatus());
	}

	static void deptSyncTest(Call call) throws RemoteException {
		setPropsBeforeCall(MODE_UPDATE, call, DEPT_SYNC_SOAP_ACTION, DEPT_SYNC_OPERATION_NAME,
				DEPT_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class, EL_INT_DEPT_SYNC_RES.class);

		EL_INT_COMMON_SYNC_REQ_TypeShape req = new EL_INT_COMMON_SYNC_REQ_TypeShape();
		req.setReqSystemID("99");
		EL_INT_DEPT_SYNC_RES res = (EL_INT_DEPT_SYNC_RES) call.invoke(new java.lang.Object[] { req });

		System.out.println(res.getOperation_Name());
	}

	static void deptFullSyncTest(Call call) throws RemoteException, ReflectiveOperationException {
		setPropsBeforeCall(MODE_FULL, call, DEPT_FULLSYNC_SOAP_ACTION, DEPT_FULLSYNC_OPERATION_NAME,
				DEPT_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
				EL_INT_DEPT_FULLSYNC_RES.class);

		EL_INT_COMMON_SYNC_REQ_TypeShape req = new EL_INT_COMMON_SYNC_REQ_TypeShape();
		req.setReqSystemID("99");
		EL_INT_DEPT_FULLSYNC_RES res = (EL_INT_DEPT_FULLSYNC_RES) call.invoke(new java.lang.Object[] { req });

		EL_INT_DEPT_FULLSYNC_RESLine dept = res.getLine(0);
		OuInfoModel ouInfo = new OuInfoModel();
		BeanUtils.copyProperties(ouInfo, dept);
		System.out.println(
				ouInfo.getID() + "=" + ouInfo.getOuName() + "=" + ouInfo.getParentID() + "=" + ouInfo.getStatus());
	}

	static void jobSyncTest(Call call) throws RemoteException {
		setPropsBeforeCall(MODE_UPDATE, call, JOB_SYNC_SOAP_ACTION, JOB_SYNC_OPERATION_NAME,
				JOB_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class, EL_INT_JOBCD_SYNC_RES.class);

		EL_INT_COMMON_SYNC_REQ_TypeShape req = new EL_INT_COMMON_SYNC_REQ_TypeShape();
		req.setReqSystemID("99");
		EL_INT_JOBCD_SYNC_RES res = (EL_INT_JOBCD_SYNC_RES) call.invoke(new java.lang.Object[] { req });

		System.out.println(res.getOperation_Name());
	}

	static void jobFullSyncTest(Call call) throws RemoteException, ReflectiveOperationException {
		setPropsBeforeCall(MODE_FULL, call, JOB_FULLSYNC_SOAP_ACTION, JOB_FULLSYNC_OPERATION_NAME,
				JOB_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
				EL_INT_JOBCD_SYNC_RES.class);

		EL_INT_COMMON_SYNC_REQ_TypeShape req = new EL_INT_COMMON_SYNC_REQ_TypeShape();
		req.setReqSystemID("99");
		EL_INT_JOBCD_SYNC_RES res = (EL_INT_JOBCD_SYNC_RES) call.invoke(new java.lang.Object[] { req });

		EL_INT_JOBCD_SYNC_RESLine job = res.getLine(0);
		PositionModel pos = new PositionModel();
		BeanUtils.copyProperties(pos, job);
		System.out.println(pos.getpNo() + "=" + pos.getpNames() + "=" + pos.getStatus());
	}

	static <E, T> void setPropsBeforeCall(String mode, Call call, String soapAction, String operationName,
			String resElementNamaspace, Class<E> reqClassType, Class<T> resClassType) {
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

		addSecurityAuth(call);
	}

	static void addSecurityAuth(Call call) {
		String AUTH_PREFIX = "wsse";
		String AUTH_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		SOAPHeaderElement soapHeaderElement = null;
		try {
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
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		call.addHeader(soapHeaderElement);
	}

}
