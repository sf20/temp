package com.wsdl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class AxisTest {
	public static void main(String[] args) throws RemoteException, ServiceException, MalformedURLException {
		EL_INT_JOBCD_FULLSYNC_SVCLocator locator = new EL_INT_JOBCD_FULLSYNC_SVCLocator();
		String endPoint = "http://119.61.11.215:8080/PSIGW/PeopleSoftServiceListeningConnector/PSFT_HR/EL_INT_JOBCD_FULLSYNC_SVC.1.wsdl";
		EL_INT_JOBCD_FULLSYNC_SVC_BindingStub stub = new EL_INT_JOBCD_FULLSYNC_SVC_BindingStub(new URL(endPoint),
				locator);
		stub.setMaintainSession(true);

		EL_INT_COMMON_FULLSYNC_REQ_TypeShape req = new EL_INT_COMMON_FULLSYNC_REQ_TypeShape();
		req.setReqSystemID("99");
		EL_INT_JOBCD_FULLSYNC_RES res = stub.EL_INT_JOBCD_FULLSYNC_OP(req);

		System.out.println(res.getOperation_Name());

	}
}
