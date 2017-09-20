/**
 * EL_INT_COMMON_FULLSYNC_REQ_TypeShape.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.wsdl;

public class EL_INT_COMMON_FULLSYNC_REQ_TypeShape  implements java.io.Serializable {
    private java.lang.String param1;

    private java.lang.String param2;

    private java.lang.String reqSystemID;

    public EL_INT_COMMON_FULLSYNC_REQ_TypeShape() {
    }

    public EL_INT_COMMON_FULLSYNC_REQ_TypeShape(
           java.lang.String param1,
           java.lang.String param2,
           java.lang.String reqSystemID) {
           this.param1 = param1;
           this.param2 = param2;
           this.reqSystemID = reqSystemID;
    }


    /**
     * Gets the param1 value for this EL_INT_COMMON_FULLSYNC_REQ_TypeShape.
     * 
     * @return param1
     */
    public java.lang.String getParam1() {
        return param1;
    }


    /**
     * Sets the param1 value for this EL_INT_COMMON_FULLSYNC_REQ_TypeShape.
     * 
     * @param param1
     */
    public void setParam1(java.lang.String param1) {
        this.param1 = param1;
    }


    /**
     * Gets the param2 value for this EL_INT_COMMON_FULLSYNC_REQ_TypeShape.
     * 
     * @return param2
     */
    public java.lang.String getParam2() {
        return param2;
    }


    /**
     * Sets the param2 value for this EL_INT_COMMON_FULLSYNC_REQ_TypeShape.
     * 
     * @param param2
     */
    public void setParam2(java.lang.String param2) {
        this.param2 = param2;
    }


    /**
     * Gets the reqSystemID value for this EL_INT_COMMON_FULLSYNC_REQ_TypeShape.
     * 
     * @return reqSystemID
     */
    public java.lang.String getReqSystemID() {
        return reqSystemID;
    }


    /**
     * Sets the reqSystemID value for this EL_INT_COMMON_FULLSYNC_REQ_TypeShape.
     * 
     * @param reqSystemID
     */
    public void setReqSystemID(java.lang.String reqSystemID) {
        this.reqSystemID = reqSystemID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EL_INT_COMMON_FULLSYNC_REQ_TypeShape)) return false;
        EL_INT_COMMON_FULLSYNC_REQ_TypeShape other = (EL_INT_COMMON_FULLSYNC_REQ_TypeShape) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.param1==null && other.getParam1()==null) || 
             (this.param1!=null &&
              this.param1.equals(other.getParam1()))) &&
            ((this.param2==null && other.getParam2()==null) || 
             (this.param2!=null &&
              this.param2.equals(other.getParam2()))) &&
            ((this.reqSystemID==null && other.getReqSystemID()==null) || 
             (this.reqSystemID!=null &&
              this.reqSystemID.equals(other.getReqSystemID())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getParam1() != null) {
            _hashCode += getParam1().hashCode();
        }
        if (getParam2() != null) {
            _hashCode += getParam2().hashCode();
        }
        if (getReqSystemID() != null) {
            _hashCode += getReqSystemID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EL_INT_COMMON_FULLSYNC_REQ_TypeShape.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INTERFACE.EL_INT_COMMON_FULLSYNC_REQ.V1", "EL_INT_COMMON_FULLSYNC_REQ_TypeShape"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("param1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INTERFACE.EL_INT_COMMON_FULLSYNC_REQ.V1", "Param1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("param2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INTERFACE.EL_INT_COMMON_FULLSYNC_REQ.V1", "Param2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reqSystemID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INTERFACE.EL_INT_COMMON_FULLSYNC_REQ.V1", "ReqSystemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
