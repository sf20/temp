/**
 * EL_INT_JOBCD_SYNC_RESLine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.entity.sync.elion;

public class EL_INT_JOBCD_SYNC_RESLine  implements java.io.Serializable {
    private java.lang.String setID;

    private java.lang.String jobCode;

    private java.lang.String effectiveDate;

    private java.lang.String effectiveStatus;

    private java.lang.String description;

    private java.lang.String jobFunctionCode;

    private java.lang.String jobFunctionDescription;

    private java.lang.String typeOfBusinessCode;

    private java.lang.String typeOfBusinessDescription;

    private java.lang.String longDescription;

    public EL_INT_JOBCD_SYNC_RESLine() {
    }

    public EL_INT_JOBCD_SYNC_RESLine(
           java.lang.String setID,
           java.lang.String jobCode,
           java.lang.String effectiveDate,
           java.lang.String effectiveStatus,
           java.lang.String description,
           java.lang.String jobFunctionCode,
           java.lang.String jobFunctionDescription,
           java.lang.String typeOfBusinessCode,
           java.lang.String typeOfBusinessDescription,
           java.lang.String longDescription) {
           this.setID = setID;
           this.jobCode = jobCode;
           this.effectiveDate = effectiveDate;
           this.effectiveStatus = effectiveStatus;
           this.description = description;
           this.jobFunctionCode = jobFunctionCode;
           this.jobFunctionDescription = jobFunctionDescription;
           this.typeOfBusinessCode = typeOfBusinessCode;
           this.typeOfBusinessDescription = typeOfBusinessDescription;
           this.longDescription = longDescription;
    }
    
    // =====BeanUtils复制属性用=====
	public String getpNo() {
		return jobCode;
	}

	public String getpNames() {
		return description;
	}
	
	public String getpNameClass() {
		return typeOfBusinessDescription;
	}
	
	public String getStatus() {
		return effectiveStatus;
	}
	// =====BeanUtils复制属性用=====

    /**
     * Gets the setID value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return setID
     */
    public java.lang.String getSetID() {
        return setID;
    }


    /**
     * Sets the setID value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param setID
     */
    public void setSetID(java.lang.String setID) {
        this.setID = setID;
    }


    /**
     * Gets the jobCode value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return jobCode
     */
    public java.lang.String getJobCode() {
        return jobCode;
    }


    /**
     * Sets the jobCode value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param jobCode
     */
    public void setJobCode(java.lang.String jobCode) {
        this.jobCode = jobCode;
    }


    /**
     * Gets the effectiveDate value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return effectiveDate
     */
    public java.lang.String getEffectiveDate() {
        return effectiveDate;
    }


    /**
     * Sets the effectiveDate value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param effectiveDate
     */
    public void setEffectiveDate(java.lang.String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


    /**
     * Gets the effectiveStatus value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return effectiveStatus
     */
    public java.lang.String getEffectiveStatus() {
        return effectiveStatus;
    }


    /**
     * Sets the effectiveStatus value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param effectiveStatus
     */
    public void setEffectiveStatus(java.lang.String effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }


    /**
     * Gets the description value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the jobFunctionCode value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return jobFunctionCode
     */
    public java.lang.String getJobFunctionCode() {
        return jobFunctionCode;
    }


    /**
     * Sets the jobFunctionCode value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param jobFunctionCode
     */
    public void setJobFunctionCode(java.lang.String jobFunctionCode) {
        this.jobFunctionCode = jobFunctionCode;
    }


    /**
     * Gets the jobFunctionDescription value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return jobFunctionDescription
     */
    public java.lang.String getJobFunctionDescription() {
        return jobFunctionDescription;
    }


    /**
     * Sets the jobFunctionDescription value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param jobFunctionDescription
     */
    public void setJobFunctionDescription(java.lang.String jobFunctionDescription) {
        this.jobFunctionDescription = jobFunctionDescription;
    }


    /**
     * Gets the typeOfBusinessCode value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return typeOfBusinessCode
     */
    public java.lang.String getTypeOfBusinessCode() {
        return typeOfBusinessCode;
    }


    /**
     * Sets the typeOfBusinessCode value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param typeOfBusinessCode
     */
    public void setTypeOfBusinessCode(java.lang.String typeOfBusinessCode) {
        this.typeOfBusinessCode = typeOfBusinessCode;
    }


    /**
     * Gets the typeOfBusinessDescription value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return typeOfBusinessDescription
     */
    public java.lang.String getTypeOfBusinessDescription() {
        return typeOfBusinessDescription;
    }


    /**
     * Sets the typeOfBusinessDescription value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param typeOfBusinessDescription
     */
    public void setTypeOfBusinessDescription(java.lang.String typeOfBusinessDescription) {
        this.typeOfBusinessDescription = typeOfBusinessDescription;
    }


    /**
     * Gets the longDescription value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @return longDescription
     */
    public java.lang.String getLongDescription() {
        return longDescription;
    }


    /**
     * Sets the longDescription value for this EL_INT_JOBCD_SYNC_RESLine.
     * 
     * @param longDescription
     */
    public void setLongDescription(java.lang.String longDescription) {
        this.longDescription = longDescription;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EL_INT_JOBCD_SYNC_RESLine)) return false;
        EL_INT_JOBCD_SYNC_RESLine other = (EL_INT_JOBCD_SYNC_RESLine) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.setID==null && other.getSetID()==null) || 
             (this.setID!=null &&
              this.setID.equals(other.getSetID()))) &&
            ((this.jobCode==null && other.getJobCode()==null) || 
             (this.jobCode!=null &&
              this.jobCode.equals(other.getJobCode()))) &&
            ((this.effectiveDate==null && other.getEffectiveDate()==null) || 
             (this.effectiveDate!=null &&
              this.effectiveDate.equals(other.getEffectiveDate()))) &&
            ((this.effectiveStatus==null && other.getEffectiveStatus()==null) || 
             (this.effectiveStatus!=null &&
              this.effectiveStatus.equals(other.getEffectiveStatus()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.jobFunctionCode==null && other.getJobFunctionCode()==null) || 
             (this.jobFunctionCode!=null &&
              this.jobFunctionCode.equals(other.getJobFunctionCode()))) &&
            ((this.jobFunctionDescription==null && other.getJobFunctionDescription()==null) || 
             (this.jobFunctionDescription!=null &&
              this.jobFunctionDescription.equals(other.getJobFunctionDescription()))) &&
            ((this.typeOfBusinessCode==null && other.getTypeOfBusinessCode()==null) || 
             (this.typeOfBusinessCode!=null &&
              this.typeOfBusinessCode.equals(other.getTypeOfBusinessCode()))) &&
            ((this.typeOfBusinessDescription==null && other.getTypeOfBusinessDescription()==null) || 
             (this.typeOfBusinessDescription!=null &&
              this.typeOfBusinessDescription.equals(other.getTypeOfBusinessDescription()))) &&
            ((this.longDescription==null && other.getLongDescription()==null) || 
             (this.longDescription!=null &&
              this.longDescription.equals(other.getLongDescription())));
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
        if (getSetID() != null) {
            _hashCode += getSetID().hashCode();
        }
        if (getJobCode() != null) {
            _hashCode += getJobCode().hashCode();
        }
        if (getEffectiveDate() != null) {
            _hashCode += getEffectiveDate().hashCode();
        }
        if (getEffectiveStatus() != null) {
            _hashCode += getEffectiveStatus().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getJobFunctionCode() != null) {
            _hashCode += getJobFunctionCode().hashCode();
        }
        if (getJobFunctionDescription() != null) {
            _hashCode += getJobFunctionDescription().hashCode();
        }
        if (getTypeOfBusinessCode() != null) {
            _hashCode += getTypeOfBusinessCode().hashCode();
        }
        if (getTypeOfBusinessDescription() != null) {
            _hashCode += getTypeOfBusinessDescription().hashCode();
        }
        if (getLongDescription() != null) {
            _hashCode += getLongDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EL_INT_JOBCD_SYNC_RESLine.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_JOBCD_SYNC_RES.V1", ">>EL_INT_JOBCD_SYNC_RES>Line"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("setID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SetID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JobCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("effectiveDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EffectiveDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("effectiveStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EffectiveStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobFunctionCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JobFunctionCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobFunctionDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JobFunctionDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeOfBusinessCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TypeOfBusinessCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeOfBusinessDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TypeOfBusinessDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LongDescription"));
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
