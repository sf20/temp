/**
 * WebServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.xingdou;

public interface WebServiceSoap extends java.rmi.Remote {
    public int orgBeginGetDept(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public int orgBeginGetDept2(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public int orgBeginGetPosition(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public int orgBeginGetPosition2(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public java.lang.String orgGetDept(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public int empBeginGetEmployee(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public int empBeginGetEmployee2(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public int empBeginGetEmployee_XiaShun(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public int empBeginGetCard(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException;
    public boolean empUpdatePhoto(java.lang.String args) throws java.rmi.RemoteException;
    public java.lang.String attSendLeave(java.lang.String sessionID, java.lang.String strXml) throws java.rmi.RemoteException;
    public java.lang.String attSendLeave2(java.lang.String sessionID, java.lang.String strXml) throws java.rmi.RemoteException;
    public java.lang.String selectLeave(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String leaveApply(java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String approvalEnd(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String leaveApplyHand(java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String attSendOver2(java.lang.String sessionID, java.lang.String strXml) throws java.rmi.RemoteException;
    public java.lang.String attOaApiLeaveCalTime(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String attOaApiLeaveAdd(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String attOaApiLeaveUpdateState(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String attOaApiOverCalTime(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String attOaApiOverAdd(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String attOaApiOverUpdateState(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException;
    public java.lang.String attGetEmployeeInfo(java.lang.String args) throws java.rmi.RemoteException;
    public java.lang.String attGetEmployeeInfoByA0190(java.lang.String args) throws java.rmi.RemoteException;
    public java.lang.String attGetTemplates(java.lang.String args) throws java.rmi.RemoteException;
    public java.lang.String attSetTemplate(java.lang.String args) throws java.rmi.RemoteException;
    public java.lang.String attDeleteTemplate(java.lang.String args) throws java.rmi.RemoteException;
    public java.lang.String recGetPostList() throws java.rmi.RemoteException;
    public java.lang.String recGetPostDetail(int reqId) throws java.rmi.RemoteException;
    public java.lang.String recPutResume(java.lang.String sessionID, java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String recPutApplyInfo(java.lang.String sessionID, java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String login(java.lang.String s1, java.lang.String s2) throws java.rmi.RemoteException;
    public java.lang.String checkLicense(java.lang.String sessionId) throws java.rmi.RemoteException;
    public java.lang.String logOut(java.lang.String sessionID) throws java.rmi.RemoteException;
    public java.lang.String getSegment(java.lang.String sessionID, int from, int to) throws java.rmi.RemoteException;
    public java.lang.String clearCache(java.lang.String sessionID) throws java.rmi.RemoteException;
    public java.lang.String getSoaKeyString(java.lang.String s1, java.lang.String s2, java.lang.String s3) throws java.rmi.RemoteException;
    public java.lang.String logoutSoaKeyString(java.lang.String s1, java.lang.String s2, java.lang.String s3) throws java.rmi.RemoteException;
    public java.lang.String customerFunction(java.lang.String args) throws java.rmi.RemoteException;
    public java.lang.String getDataByWsProc(java.lang.String args) throws java.rmi.RemoteException;
    public java.lang.String putDataByWsProc(java.lang.String args) throws java.rmi.RemoteException;
}
