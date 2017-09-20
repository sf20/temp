package com.wsdl;

public class EL_INT_JOBCD_FULLSYNC_SVC_PortTypeProxy implements com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_PortType {
  private String _endpoint = null;
  private com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_PortType eL_INT_JOBCD_FULLSYNC_SVC_PortType = null;
  
  public EL_INT_JOBCD_FULLSYNC_SVC_PortTypeProxy() {
    _initEL_INT_JOBCD_FULLSYNC_SVC_PortTypeProxy();
  }
  
  public EL_INT_JOBCD_FULLSYNC_SVC_PortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initEL_INT_JOBCD_FULLSYNC_SVC_PortTypeProxy();
  }
  
  private void _initEL_INT_JOBCD_FULLSYNC_SVC_PortTypeProxy() {
    try {
      eL_INT_JOBCD_FULLSYNC_SVC_PortType = (new com.wsdl.EL_INT_JOBCD_FULLSYNC_SVCLocator()).getEL_INT_JOBCD_FULLSYNC_SVC_Port();
      if (eL_INT_JOBCD_FULLSYNC_SVC_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)eL_INT_JOBCD_FULLSYNC_SVC_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)eL_INT_JOBCD_FULLSYNC_SVC_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (eL_INT_JOBCD_FULLSYNC_SVC_PortType != null)
      ((javax.xml.rpc.Stub)eL_INT_JOBCD_FULLSYNC_SVC_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_PortType getEL_INT_JOBCD_FULLSYNC_SVC_PortType() {
    if (eL_INT_JOBCD_FULLSYNC_SVC_PortType == null)
      _initEL_INT_JOBCD_FULLSYNC_SVC_PortTypeProxy();
    return eL_INT_JOBCD_FULLSYNC_SVC_PortType;
  }
  
  public com.wsdl.EL_INT_JOBCD_FULLSYNC_RES EL_INT_JOBCD_FULLSYNC_OP(com.wsdl.EL_INT_COMMON_FULLSYNC_REQ_TypeShape parameter) throws java.rmi.RemoteException{
    if (eL_INT_JOBCD_FULLSYNC_SVC_PortType == null)
      _initEL_INT_JOBCD_FULLSYNC_SVC_PortTypeProxy();
    return eL_INT_JOBCD_FULLSYNC_SVC_PortType.EL_INT_JOBCD_FULLSYNC_OP(parameter);
  }
  
  
}