/**
 * EL_INT_JOBCD_SYNC_RES.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.entity.sync.elion;

public class EL_INT_JOBCD_SYNC_RES  implements java.io.Serializable {
    private java.lang.String operation_Name;

    private EL_INT_JOBCD_SYNC_RESLine[] line;

    public EL_INT_JOBCD_SYNC_RES() {
    }

    public EL_INT_JOBCD_SYNC_RES(
           java.lang.String operation_Name,
           EL_INT_JOBCD_SYNC_RESLine[] line) {
           this.operation_Name = operation_Name;
           this.line = line;
    }


    /**
     * Gets the operation_Name value for this EL_INT_JOBCD_SYNC_RES.
     * 
     * @return operation_Name
     */
    public java.lang.String getOperation_Name() {
        return operation_Name;
    }


    /**
     * Sets the operation_Name value for this EL_INT_JOBCD_SYNC_RES.
     * 
     * @param operation_Name
     */
    public void setOperation_Name(java.lang.String operation_Name) {
        this.operation_Name = operation_Name;
    }


    /**
     * Gets the line value for this EL_INT_JOBCD_SYNC_RES.
     * 
     * @return line
     */
    public EL_INT_JOBCD_SYNC_RESLine[] getLine() {
        return line;
    }


    /**
     * Sets the line value for this EL_INT_JOBCD_SYNC_RES.
     * 
     * @param line
     */
    public void setLine(EL_INT_JOBCD_SYNC_RESLine[] line) {
        this.line = line;
    }

    public EL_INT_JOBCD_SYNC_RESLine getLine(int i) {
        return this.line[i];
    }

    public void setLine(int i, EL_INT_JOBCD_SYNC_RESLine _value) {
        this.line[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EL_INT_JOBCD_SYNC_RES)) return false;
        EL_INT_JOBCD_SYNC_RES other = (EL_INT_JOBCD_SYNC_RES) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.operation_Name==null && other.getOperation_Name()==null) || 
             (this.operation_Name!=null &&
              this.operation_Name.equals(other.getOperation_Name()))) &&
            ((this.line==null && other.getLine()==null) || 
             (this.line!=null &&
              java.util.Arrays.equals(this.line, other.getLine())));
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
        if (getOperation_Name() != null) {
            _hashCode += getOperation_Name().hashCode();
        }
        if (getLine() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLine());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLine(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EL_INT_JOBCD_SYNC_RES.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_JOBCD_SYNC_RES.V1", ">EL_INT_JOBCD_SYNC_RES"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operation_Name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Operation_Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("line");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Line"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_JOBCD_SYNC_RES.V1", ">>EL_INT_JOBCD_SYNC_RES>Line"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
