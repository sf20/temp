/**
 * EL_INT_JOBCD_FULLSYNC_SVCLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.wsdl;

public class EL_INT_JOBCD_FULLSYNC_SVCLocator extends org.apache.axis.client.Service implements com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC {

/**
 * 岗位数据（全量）
 */

    public EL_INT_JOBCD_FULLSYNC_SVCLocator() {
    }


    public EL_INT_JOBCD_FULLSYNC_SVCLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EL_INT_JOBCD_FULLSYNC_SVCLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EL_INT_JOBCD_FULLSYNC_SVC_Port
    private java.lang.String EL_INT_JOBCD_FULLSYNC_SVC_Port_address = "http://119.61.11.215:8080/PSIGW/PeopleSoftServiceListeningConnector/PSFT_HR";

    public java.lang.String getEL_INT_JOBCD_FULLSYNC_SVC_PortAddress() {
        return EL_INT_JOBCD_FULLSYNC_SVC_Port_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EL_INT_JOBCD_FULLSYNC_SVC_PortWSDDServiceName = "EL_INT_JOBCD_FULLSYNC_SVC_Port";

    public java.lang.String getEL_INT_JOBCD_FULLSYNC_SVC_PortWSDDServiceName() {
        return EL_INT_JOBCD_FULLSYNC_SVC_PortWSDDServiceName;
    }

    public void setEL_INT_JOBCD_FULLSYNC_SVC_PortWSDDServiceName(java.lang.String name) {
        EL_INT_JOBCD_FULLSYNC_SVC_PortWSDDServiceName = name;
    }

    public com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_PortType getEL_INT_JOBCD_FULLSYNC_SVC_Port() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EL_INT_JOBCD_FULLSYNC_SVC_Port_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEL_INT_JOBCD_FULLSYNC_SVC_Port(endpoint);
    }

    public com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_PortType getEL_INT_JOBCD_FULLSYNC_SVC_Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_BindingStub _stub = new com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_BindingStub(portAddress, this);
            _stub.setPortName(getEL_INT_JOBCD_FULLSYNC_SVC_PortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEL_INT_JOBCD_FULLSYNC_SVC_PortEndpointAddress(java.lang.String address) {
        EL_INT_JOBCD_FULLSYNC_SVC_Port_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_BindingStub _stub = new com.wsdl.EL_INT_JOBCD_FULLSYNC_SVC_BindingStub(new java.net.URL(EL_INT_JOBCD_FULLSYNC_SVC_Port_address), this);
                _stub.setPortName(getEL_INT_JOBCD_FULLSYNC_SVC_PortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("EL_INT_JOBCD_FULLSYNC_SVC_Port".equals(inputPortName)) {
            return getEL_INT_JOBCD_FULLSYNC_SVC_Port();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/services/EL_INT_JOBCD_FULLSYNC_SVC.1", "EL_INT_JOBCD_FULLSYNC_SVC");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/services/EL_INT_JOBCD_FULLSYNC_SVC.1", "EL_INT_JOBCD_FULLSYNC_SVC_Port"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EL_INT_JOBCD_FULLSYNC_SVC_Port".equals(portName)) {
            setEL_INT_JOBCD_FULLSYNC_SVC_PortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
