/**
 * EL_INT_PER_SYNC_RESLine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.entity.sync.elion;

public class EL_INT_PER_SYNC_RESLine  implements java.io.Serializable {
    private java.lang.String employeeID;

    private java.lang.String employeeRecord;

    private java.lang.String employeeName;

    private java.lang.String HRStatus;

    private java.lang.String cellPhone;

    private java.lang.String companyEmail;

    private java.lang.String gender;

    private java.lang.String ethnicGroup;

    private java.lang.String birthDate;

    private java.lang.String residenceAddress;

    private java.lang.String employeeClass;

    private java.lang.String employeeType;

    private java.lang.String jobLevel;

    private java.lang.String managerLevel;

    private java.lang.String hireDate;

    private java.lang.String jobFunction;

    private java.lang.String jobCode;

    private java.lang.String jobCodeDescr;

    private java.lang.String departmentID;

    private java.lang.String deptDescr;

    private java.lang.String deptFullName;

    private java.lang.String termDate;

    private java.lang.String deptManagerID;

    private java.lang.String supervisorID;

    private java.lang.String supervisorEmail;

    private java.lang.String location;

    private java.lang.String locationDescr;

    private java.lang.String emergencyRelation;

    private java.lang.String emergencyContact;

    private java.lang.String emergencyPhone;

    private java.lang.String userID;

    private java.lang.String sequenceNbr;

    private java.lang.String defaultPassword;

    private java.lang.String departmentLv2ID;

    private java.lang.String departmentLv2Descr;

    public EL_INT_PER_SYNC_RESLine() {
    }

    public EL_INT_PER_SYNC_RESLine(
           java.lang.String employeeID,
           java.lang.String employeeRecord,
           java.lang.String employeeName,
           java.lang.String HRStatus,
           java.lang.String cellPhone,
           java.lang.String companyEmail,
           java.lang.String gender,
           java.lang.String ethnicGroup,
           java.lang.String birthDate,
           java.lang.String residenceAddress,
           java.lang.String employeeClass,
           java.lang.String employeeType,
           java.lang.String jobLevel,
           java.lang.String managerLevel,
           java.lang.String hireDate,
           java.lang.String jobFunction,
           java.lang.String jobCode,
           java.lang.String jobCodeDescr,
           java.lang.String departmentID,
           java.lang.String deptDescr,
           java.lang.String deptFullName,
           java.lang.String termDate,
           java.lang.String deptManagerID,
           java.lang.String supervisorID,
           java.lang.String supervisorEmail,
           java.lang.String location,
           java.lang.String locationDescr,
           java.lang.String emergencyRelation,
           java.lang.String emergencyContact,
           java.lang.String emergencyPhone,
           java.lang.String userID,
           java.lang.String sequenceNbr,
           java.lang.String defaultPassword,
           java.lang.String departmentLv2ID,
           java.lang.String departmentLv2Descr) {
           this.employeeID = employeeID;
           this.employeeRecord = employeeRecord;
           this.employeeName = employeeName;
           this.HRStatus = HRStatus;
           this.cellPhone = cellPhone;
           this.companyEmail = companyEmail;
           this.gender = gender;
           this.ethnicGroup = ethnicGroup;
           this.birthDate = birthDate;
           this.residenceAddress = residenceAddress;
           this.employeeClass = employeeClass;
           this.employeeType = employeeType;
           this.jobLevel = jobLevel;
           this.managerLevel = managerLevel;
           this.hireDate = hireDate;
           this.jobFunction = jobFunction;
           this.jobCode = jobCode;
           this.jobCodeDescr = jobCodeDescr;
           this.departmentID = departmentID;
           this.deptDescr = deptDescr;
           this.deptFullName = deptFullName;
           this.termDate = termDate;
           this.deptManagerID = deptManagerID;
           this.supervisorID = supervisorID;
           this.supervisorEmail = supervisorEmail;
           this.location = location;
           this.locationDescr = locationDescr;
           this.emergencyRelation = emergencyRelation;
           this.emergencyContact = emergencyContact;
           this.emergencyPhone = emergencyPhone;
           this.userID = userID;
           this.sequenceNbr = sequenceNbr;
           this.defaultPassword = defaultPassword;
           this.departmentLv2ID = departmentLv2ID;
           this.departmentLv2Descr = departmentLv2Descr;
    }

    // =====BeanUtils复制属性用=====
    public String getID() {
		return employeeID;
	}

	public String getUserName() {
		return userID;
	}

	public String getCnName() {
		return employeeName;
	}

	public String getSex() {
		return gender;
	}

	public String getMobile() {
		return null;// 手机号不同步
	}

	public String getMail() {
		return companyEmail;
	}

	public String getOrgOuCode() {
		return departmentID;
	}

	public String getPostionNo() {
		return jobCode;
	}
	
	public String getEntryTime() {
		return hireDate;
	}

	public String getBirthday() {
		return birthDate;
	}

	public String getExpireDate() {
		return termDate;
	}
	
	public String getStatus() {
		return HRStatus;
	}

	public String getDeleteStatus() {
		return employeeRecord;
	}
	// =====BeanUtils复制属性用=====

    /**
     * Gets the employeeID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return employeeID
     */
    public java.lang.String getEmployeeID() {
        return employeeID;
    }


    /**
     * Sets the employeeID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param employeeID
     */
    public void setEmployeeID(java.lang.String employeeID) {
        this.employeeID = employeeID;
    }


    /**
     * Gets the employeeRecord value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return employeeRecord
     */
    public java.lang.String getEmployeeRecord() {
        return employeeRecord;
    }


    /**
     * Sets the employeeRecord value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param employeeRecord
     */
    public void setEmployeeRecord(java.lang.String employeeRecord) {
        this.employeeRecord = employeeRecord;
    }


    /**
     * Gets the employeeName value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return employeeName
     */
    public java.lang.String getEmployeeName() {
        return employeeName;
    }


    /**
     * Sets the employeeName value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param employeeName
     */
    public void setEmployeeName(java.lang.String employeeName) {
        this.employeeName = employeeName;
    }


    /**
     * Gets the HRStatus value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return HRStatus
     */
    public java.lang.String getHRStatus() {
        return HRStatus;
    }


    /**
     * Sets the HRStatus value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param HRStatus
     */
    public void setHRStatus(java.lang.String HRStatus) {
        this.HRStatus = HRStatus;
    }


    /**
     * Gets the cellPhone value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return cellPhone
     */
    public java.lang.String getCellPhone() {
        return cellPhone;
    }


    /**
     * Sets the cellPhone value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param cellPhone
     */
    public void setCellPhone(java.lang.String cellPhone) {
        this.cellPhone = cellPhone;
    }


    /**
     * Gets the companyEmail value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return companyEmail
     */
    public java.lang.String getCompanyEmail() {
        return companyEmail;
    }


    /**
     * Sets the companyEmail value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param companyEmail
     */
    public void setCompanyEmail(java.lang.String companyEmail) {
        this.companyEmail = companyEmail;
    }


    /**
     * Gets the gender value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return gender
     */
    public java.lang.String getGender() {
        return gender;
    }


    /**
     * Sets the gender value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param gender
     */
    public void setGender(java.lang.String gender) {
        this.gender = gender;
    }


    /**
     * Gets the ethnicGroup value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return ethnicGroup
     */
    public java.lang.String getEthnicGroup() {
        return ethnicGroup;
    }


    /**
     * Sets the ethnicGroup value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param ethnicGroup
     */
    public void setEthnicGroup(java.lang.String ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }


    /**
     * Gets the birthDate value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return birthDate
     */
    public java.lang.String getBirthDate() {
        return birthDate;
    }


    /**
     * Sets the birthDate value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param birthDate
     */
    public void setBirthDate(java.lang.String birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * Gets the residenceAddress value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return residenceAddress
     */
    public java.lang.String getResidenceAddress() {
        return residenceAddress;
    }


    /**
     * Sets the residenceAddress value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param residenceAddress
     */
    public void setResidenceAddress(java.lang.String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }


    /**
     * Gets the employeeClass value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return employeeClass
     */
    public java.lang.String getEmployeeClass() {
        return employeeClass;
    }


    /**
     * Sets the employeeClass value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param employeeClass
     */
    public void setEmployeeClass(java.lang.String employeeClass) {
        this.employeeClass = employeeClass;
    }


    /**
     * Gets the employeeType value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return employeeType
     */
    public java.lang.String getEmployeeType() {
        return employeeType;
    }


    /**
     * Sets the employeeType value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param employeeType
     */
    public void setEmployeeType(java.lang.String employeeType) {
        this.employeeType = employeeType;
    }


    /**
     * Gets the jobLevel value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return jobLevel
     */
    public java.lang.String getJobLevel() {
        return jobLevel;
    }


    /**
     * Sets the jobLevel value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param jobLevel
     */
    public void setJobLevel(java.lang.String jobLevel) {
        this.jobLevel = jobLevel;
    }


    /**
     * Gets the managerLevel value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return managerLevel
     */
    public java.lang.String getManagerLevel() {
        return managerLevel;
    }


    /**
     * Sets the managerLevel value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param managerLevel
     */
    public void setManagerLevel(java.lang.String managerLevel) {
        this.managerLevel = managerLevel;
    }


    /**
     * Gets the hireDate value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return hireDate
     */
    public java.lang.String getHireDate() {
        return hireDate;
    }


    /**
     * Sets the hireDate value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param hireDate
     */
    public void setHireDate(java.lang.String hireDate) {
        this.hireDate = hireDate;
    }


    /**
     * Gets the jobFunction value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return jobFunction
     */
    public java.lang.String getJobFunction() {
        return jobFunction;
    }


    /**
     * Sets the jobFunction value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param jobFunction
     */
    public void setJobFunction(java.lang.String jobFunction) {
        this.jobFunction = jobFunction;
    }


    /**
     * Gets the jobCode value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return jobCode
     */
    public java.lang.String getJobCode() {
        return jobCode;
    }


    /**
     * Sets the jobCode value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param jobCode
     */
    public void setJobCode(java.lang.String jobCode) {
        this.jobCode = jobCode;
    }


    /**
     * Gets the jobCodeDescr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return jobCodeDescr
     */
    public java.lang.String getJobCodeDescr() {
        return jobCodeDescr;
    }


    /**
     * Sets the jobCodeDescr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param jobCodeDescr
     */
    public void setJobCodeDescr(java.lang.String jobCodeDescr) {
        this.jobCodeDescr = jobCodeDescr;
    }


    /**
     * Gets the departmentID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return departmentID
     */
    public java.lang.String getDepartmentID() {
        return departmentID;
    }


    /**
     * Sets the departmentID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param departmentID
     */
    public void setDepartmentID(java.lang.String departmentID) {
        this.departmentID = departmentID;
    }


    /**
     * Gets the deptDescr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return deptDescr
     */
    public java.lang.String getDeptDescr() {
        return deptDescr;
    }


    /**
     * Sets the deptDescr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param deptDescr
     */
    public void setDeptDescr(java.lang.String deptDescr) {
        this.deptDescr = deptDescr;
    }


    /**
     * Gets the deptFullName value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return deptFullName
     */
    public java.lang.String getDeptFullName() {
        return deptFullName;
    }


    /**
     * Sets the deptFullName value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param deptFullName
     */
    public void setDeptFullName(java.lang.String deptFullName) {
        this.deptFullName = deptFullName;
    }


    /**
     * Gets the termDate value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return termDate
     */
    public java.lang.String getTermDate() {
        return termDate;
    }


    /**
     * Sets the termDate value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param termDate
     */
    public void setTermDate(java.lang.String termDate) {
        this.termDate = termDate;
    }


    /**
     * Gets the deptManagerID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return deptManagerID
     */
    public java.lang.String getDeptManagerID() {
        return deptManagerID;
    }


    /**
     * Sets the deptManagerID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param deptManagerID
     */
    public void setDeptManagerID(java.lang.String deptManagerID) {
        this.deptManagerID = deptManagerID;
    }


    /**
     * Gets the supervisorID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return supervisorID
     */
    public java.lang.String getSupervisorID() {
        return supervisorID;
    }


    /**
     * Sets the supervisorID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param supervisorID
     */
    public void setSupervisorID(java.lang.String supervisorID) {
        this.supervisorID = supervisorID;
    }


    /**
     * Gets the supervisorEmail value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return supervisorEmail
     */
    public java.lang.String getSupervisorEmail() {
        return supervisorEmail;
    }


    /**
     * Sets the supervisorEmail value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param supervisorEmail
     */
    public void setSupervisorEmail(java.lang.String supervisorEmail) {
        this.supervisorEmail = supervisorEmail;
    }


    /**
     * Gets the location value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return location
     */
    public java.lang.String getLocation() {
        return location;
    }


    /**
     * Sets the location value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param location
     */
    public void setLocation(java.lang.String location) {
        this.location = location;
    }


    /**
     * Gets the locationDescr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return locationDescr
     */
    public java.lang.String getLocationDescr() {
        return locationDescr;
    }


    /**
     * Sets the locationDescr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param locationDescr
     */
    public void setLocationDescr(java.lang.String locationDescr) {
        this.locationDescr = locationDescr;
    }


    /**
     * Gets the emergencyRelation value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return emergencyRelation
     */
    public java.lang.String getEmergencyRelation() {
        return emergencyRelation;
    }


    /**
     * Sets the emergencyRelation value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param emergencyRelation
     */
    public void setEmergencyRelation(java.lang.String emergencyRelation) {
        this.emergencyRelation = emergencyRelation;
    }


    /**
     * Gets the emergencyContact value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return emergencyContact
     */
    public java.lang.String getEmergencyContact() {
        return emergencyContact;
    }


    /**
     * Sets the emergencyContact value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param emergencyContact
     */
    public void setEmergencyContact(java.lang.String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }


    /**
     * Gets the emergencyPhone value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return emergencyPhone
     */
    public java.lang.String getEmergencyPhone() {
        return emergencyPhone;
    }


    /**
     * Sets the emergencyPhone value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param emergencyPhone
     */
    public void setEmergencyPhone(java.lang.String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }


    /**
     * Gets the userID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return userID
     */
    public java.lang.String getUserID() {
        return userID;
    }


    /**
     * Sets the userID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param userID
     */
    public void setUserID(java.lang.String userID) {
        this.userID = userID;
    }


    /**
     * Gets the sequenceNbr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return sequenceNbr
     */
    public java.lang.String getSequenceNbr() {
        return sequenceNbr;
    }


    /**
     * Sets the sequenceNbr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param sequenceNbr
     */
    public void setSequenceNbr(java.lang.String sequenceNbr) {
        this.sequenceNbr = sequenceNbr;
    }


    /**
     * Gets the defaultPassword value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return defaultPassword
     */
    public java.lang.String getDefaultPassword() {
        return defaultPassword;
    }


    /**
     * Sets the defaultPassword value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param defaultPassword
     */
    public void setDefaultPassword(java.lang.String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }


    /**
     * Gets the departmentLv2ID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return departmentLv2ID
     */
    public java.lang.String getDepartmentLv2ID() {
        return departmentLv2ID;
    }


    /**
     * Sets the departmentLv2ID value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param departmentLv2ID
     */
    public void setDepartmentLv2ID(java.lang.String departmentLv2ID) {
        this.departmentLv2ID = departmentLv2ID;
    }


    /**
     * Gets the departmentLv2Descr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @return departmentLv2Descr
     */
    public java.lang.String getDepartmentLv2Descr() {
        return departmentLv2Descr;
    }


    /**
     * Sets the departmentLv2Descr value for this EL_INT_PER_SYNC_RESLine.
     * 
     * @param departmentLv2Descr
     */
    public void setDepartmentLv2Descr(java.lang.String departmentLv2Descr) {
        this.departmentLv2Descr = departmentLv2Descr;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EL_INT_PER_SYNC_RESLine)) return false;
        EL_INT_PER_SYNC_RESLine other = (EL_INT_PER_SYNC_RESLine) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.employeeID==null && other.getEmployeeID()==null) || 
             (this.employeeID!=null &&
              this.employeeID.equals(other.getEmployeeID()))) &&
            ((this.employeeRecord==null && other.getEmployeeRecord()==null) || 
             (this.employeeRecord!=null &&
              this.employeeRecord.equals(other.getEmployeeRecord()))) &&
            ((this.employeeName==null && other.getEmployeeName()==null) || 
             (this.employeeName!=null &&
              this.employeeName.equals(other.getEmployeeName()))) &&
            ((this.HRStatus==null && other.getHRStatus()==null) || 
             (this.HRStatus!=null &&
              this.HRStatus.equals(other.getHRStatus()))) &&
            ((this.cellPhone==null && other.getCellPhone()==null) || 
             (this.cellPhone!=null &&
              this.cellPhone.equals(other.getCellPhone()))) &&
            ((this.companyEmail==null && other.getCompanyEmail()==null) || 
             (this.companyEmail!=null &&
              this.companyEmail.equals(other.getCompanyEmail()))) &&
            ((this.gender==null && other.getGender()==null) || 
             (this.gender!=null &&
              this.gender.equals(other.getGender()))) &&
            ((this.ethnicGroup==null && other.getEthnicGroup()==null) || 
             (this.ethnicGroup!=null &&
              this.ethnicGroup.equals(other.getEthnicGroup()))) &&
            ((this.birthDate==null && other.getBirthDate()==null) || 
             (this.birthDate!=null &&
              this.birthDate.equals(other.getBirthDate()))) &&
            ((this.residenceAddress==null && other.getResidenceAddress()==null) || 
             (this.residenceAddress!=null &&
              this.residenceAddress.equals(other.getResidenceAddress()))) &&
            ((this.employeeClass==null && other.getEmployeeClass()==null) || 
             (this.employeeClass!=null &&
              this.employeeClass.equals(other.getEmployeeClass()))) &&
            ((this.employeeType==null && other.getEmployeeType()==null) || 
             (this.employeeType!=null &&
              this.employeeType.equals(other.getEmployeeType()))) &&
            ((this.jobLevel==null && other.getJobLevel()==null) || 
             (this.jobLevel!=null &&
              this.jobLevel.equals(other.getJobLevel()))) &&
            ((this.managerLevel==null && other.getManagerLevel()==null) || 
             (this.managerLevel!=null &&
              this.managerLevel.equals(other.getManagerLevel()))) &&
            ((this.hireDate==null && other.getHireDate()==null) || 
             (this.hireDate!=null &&
              this.hireDate.equals(other.getHireDate()))) &&
            ((this.jobFunction==null && other.getJobFunction()==null) || 
             (this.jobFunction!=null &&
              this.jobFunction.equals(other.getJobFunction()))) &&
            ((this.jobCode==null && other.getJobCode()==null) || 
             (this.jobCode!=null &&
              this.jobCode.equals(other.getJobCode()))) &&
            ((this.jobCodeDescr==null && other.getJobCodeDescr()==null) || 
             (this.jobCodeDescr!=null &&
              this.jobCodeDescr.equals(other.getJobCodeDescr()))) &&
            ((this.departmentID==null && other.getDepartmentID()==null) || 
             (this.departmentID!=null &&
              this.departmentID.equals(other.getDepartmentID()))) &&
            ((this.deptDescr==null && other.getDeptDescr()==null) || 
             (this.deptDescr!=null &&
              this.deptDescr.equals(other.getDeptDescr()))) &&
            ((this.deptFullName==null && other.getDeptFullName()==null) || 
             (this.deptFullName!=null &&
              this.deptFullName.equals(other.getDeptFullName()))) &&
            ((this.termDate==null && other.getTermDate()==null) || 
             (this.termDate!=null &&
              this.termDate.equals(other.getTermDate()))) &&
            ((this.deptManagerID==null && other.getDeptManagerID()==null) || 
             (this.deptManagerID!=null &&
              this.deptManagerID.equals(other.getDeptManagerID()))) &&
            ((this.supervisorID==null && other.getSupervisorID()==null) || 
             (this.supervisorID!=null &&
              this.supervisorID.equals(other.getSupervisorID()))) &&
            ((this.supervisorEmail==null && other.getSupervisorEmail()==null) || 
             (this.supervisorEmail!=null &&
              this.supervisorEmail.equals(other.getSupervisorEmail()))) &&
            ((this.location==null && other.getLocation()==null) || 
             (this.location!=null &&
              this.location.equals(other.getLocation()))) &&
            ((this.locationDescr==null && other.getLocationDescr()==null) || 
             (this.locationDescr!=null &&
              this.locationDescr.equals(other.getLocationDescr()))) &&
            ((this.emergencyRelation==null && other.getEmergencyRelation()==null) || 
             (this.emergencyRelation!=null &&
              this.emergencyRelation.equals(other.getEmergencyRelation()))) &&
            ((this.emergencyContact==null && other.getEmergencyContact()==null) || 
             (this.emergencyContact!=null &&
              this.emergencyContact.equals(other.getEmergencyContact()))) &&
            ((this.emergencyPhone==null && other.getEmergencyPhone()==null) || 
             (this.emergencyPhone!=null &&
              this.emergencyPhone.equals(other.getEmergencyPhone()))) &&
            ((this.userID==null && other.getUserID()==null) || 
             (this.userID!=null &&
              this.userID.equals(other.getUserID()))) &&
            ((this.sequenceNbr==null && other.getSequenceNbr()==null) || 
             (this.sequenceNbr!=null &&
              this.sequenceNbr.equals(other.getSequenceNbr()))) &&
            ((this.defaultPassword==null && other.getDefaultPassword()==null) || 
             (this.defaultPassword!=null &&
              this.defaultPassword.equals(other.getDefaultPassword()))) &&
            ((this.departmentLv2ID==null && other.getDepartmentLv2ID()==null) || 
             (this.departmentLv2ID!=null &&
              this.departmentLv2ID.equals(other.getDepartmentLv2ID()))) &&
            ((this.departmentLv2Descr==null && other.getDepartmentLv2Descr()==null) || 
             (this.departmentLv2Descr!=null &&
              this.departmentLv2Descr.equals(other.getDepartmentLv2Descr())));
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
        if (getEmployeeID() != null) {
            _hashCode += getEmployeeID().hashCode();
        }
        if (getEmployeeRecord() != null) {
            _hashCode += getEmployeeRecord().hashCode();
        }
        if (getEmployeeName() != null) {
            _hashCode += getEmployeeName().hashCode();
        }
        if (getHRStatus() != null) {
            _hashCode += getHRStatus().hashCode();
        }
        if (getCellPhone() != null) {
            _hashCode += getCellPhone().hashCode();
        }
        if (getCompanyEmail() != null) {
            _hashCode += getCompanyEmail().hashCode();
        }
        if (getGender() != null) {
            _hashCode += getGender().hashCode();
        }
        if (getEthnicGroup() != null) {
            _hashCode += getEthnicGroup().hashCode();
        }
        if (getBirthDate() != null) {
            _hashCode += getBirthDate().hashCode();
        }
        if (getResidenceAddress() != null) {
            _hashCode += getResidenceAddress().hashCode();
        }
        if (getEmployeeClass() != null) {
            _hashCode += getEmployeeClass().hashCode();
        }
        if (getEmployeeType() != null) {
            _hashCode += getEmployeeType().hashCode();
        }
        if (getJobLevel() != null) {
            _hashCode += getJobLevel().hashCode();
        }
        if (getManagerLevel() != null) {
            _hashCode += getManagerLevel().hashCode();
        }
        if (getHireDate() != null) {
            _hashCode += getHireDate().hashCode();
        }
        if (getJobFunction() != null) {
            _hashCode += getJobFunction().hashCode();
        }
        if (getJobCode() != null) {
            _hashCode += getJobCode().hashCode();
        }
        if (getJobCodeDescr() != null) {
            _hashCode += getJobCodeDescr().hashCode();
        }
        if (getDepartmentID() != null) {
            _hashCode += getDepartmentID().hashCode();
        }
        if (getDeptDescr() != null) {
            _hashCode += getDeptDescr().hashCode();
        }
        if (getDeptFullName() != null) {
            _hashCode += getDeptFullName().hashCode();
        }
        if (getTermDate() != null) {
            _hashCode += getTermDate().hashCode();
        }
        if (getDeptManagerID() != null) {
            _hashCode += getDeptManagerID().hashCode();
        }
        if (getSupervisorID() != null) {
            _hashCode += getSupervisorID().hashCode();
        }
        if (getSupervisorEmail() != null) {
            _hashCode += getSupervisorEmail().hashCode();
        }
        if (getLocation() != null) {
            _hashCode += getLocation().hashCode();
        }
        if (getLocationDescr() != null) {
            _hashCode += getLocationDescr().hashCode();
        }
        if (getEmergencyRelation() != null) {
            _hashCode += getEmergencyRelation().hashCode();
        }
        if (getEmergencyContact() != null) {
            _hashCode += getEmergencyContact().hashCode();
        }
        if (getEmergencyPhone() != null) {
            _hashCode += getEmergencyPhone().hashCode();
        }
        if (getUserID() != null) {
            _hashCode += getUserID().hashCode();
        }
        if (getSequenceNbr() != null) {
            _hashCode += getSequenceNbr().hashCode();
        }
        if (getDefaultPassword() != null) {
            _hashCode += getDefaultPassword().hashCode();
        }
        if (getDepartmentLv2ID() != null) {
            _hashCode += getDepartmentLv2ID().hashCode();
        }
        if (getDepartmentLv2Descr() != null) {
            _hashCode += getDepartmentLv2Descr().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EL_INT_PER_SYNC_RESLine.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_PER_SYNC_RES.V1", ">>EL_INT_PER_SYNC_RES>Line"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EmployeeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EmployeeRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EmployeeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HRStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HRStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cellPhone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CellPhone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("companyEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CompanyEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gender");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Gender"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ethnicGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EthnicGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("birthDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BirthDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("residenceAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ResidenceAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeClass");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EmployeeClass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EmployeeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JobLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("managerLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ManagerLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hireDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HireDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobFunction");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JobFunction"));
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
        elemField.setFieldName("jobCodeDescr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JobCodeDescr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("departmentID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DepartmentID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deptDescr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DeptDescr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deptFullName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DeptFullName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("termDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TermDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deptManagerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DeptManagerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supervisorID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SupervisorID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supervisorEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SupervisorEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("location");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Location"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationDescr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LocationDescr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emergencyRelation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EmergencyRelation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emergencyContact");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EmergencyContact"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emergencyPhone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EmergencyPhone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "UserID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequenceNbr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SequenceNbr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultPassword");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DefaultPassword"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("departmentLv2ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DepartmentLv2ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("departmentLv2Descr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DepartmentLv2Descr"));
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
