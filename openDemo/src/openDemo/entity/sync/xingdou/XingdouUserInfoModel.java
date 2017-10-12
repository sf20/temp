package openDemo.entity.sync.xingdou;

import javax.xml.bind.annotation.XmlElement;

public class XingdouUserInfoModel {
	private String ID;
	private String userName;
	private String cnName;
	private String sex;
	private String orgOuCode;
	private String postionName;
	private String status;
	private String entryTime;
	private String birthday;

	@XmlElement(name = "EmpID")
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	@XmlElement(name = "EmpNo")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@XmlElement(name = "EmpName")
	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	@XmlElement(name = "SEX")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@XmlElement(name = "DeptId")
	public String getOrgOuCode() {
		return orgOuCode;
	}

	public void setOrgOuCode(String orgOuCode) {
		this.orgOuCode = orgOuCode;
	}

	@XmlElement(name = "POSITIONID")
	public String getPostionName() {
		return postionName;
	}

	public void setPostionName(String postionName) {
		this.postionName = postionName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name = "Timetoenter")
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	@XmlElement(name = "Birth")
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

}