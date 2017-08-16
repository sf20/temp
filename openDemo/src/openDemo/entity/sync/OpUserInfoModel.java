package openDemo.entity.sync;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 欧普公司用户json数据模型
 * 
 * @author yanl
 *
 */
public class OpUserInfoModel {

	/**
	 * 用户ID(同步必传)
	 */
	@JsonProperty("OpUserId")
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	// 客户提供json数据中没有匹配字段
	@JsonIgnore
	private String userName;

	/**
	 * 中文姓名(同步必传)
	 */
	@JsonProperty("UserName")
	private String cnName;

	/**
	 * 密码备注：如果用MD5或者CMD5加密则必须使用标准MD5 32位小写加密的字符串（如果不传使用平台配置的默认密码）
	 */
	@JsonIgnore
	private String password;

	/**
	 * 性别
	 */
	@JsonProperty("Gender")
	private String sex;

	/**
	 * 移动电话
	 */
	// @JsonProperty("Mobile")
	@JsonIgnore
	private String mobile;

	/**
	 * 电子邮件
	 */
	@JsonProperty("Mail")
	private String mail;

	/**
	 * 部门编号
	 */
	@JsonProperty("OrgCode")
	private String orgOuCode;

	/**
	 * 密码加密方式： YXT(云学堂加密默认)、MD5 (密码MD5加密)、CMD5(用户名+密码MD5加密)
	 */
	@JsonIgnore
	private String encryptionType;

	/**
	 * 岗位编号
	 */
	// TODO @JsonProperty("PositionCode")
	@JsonIgnore
	private String postionNo;

	/*
	 * 岗位名
	 */
	@JsonProperty("PositionName")
	private String postionName;

	/**
	 * 入职日期
	 */
	@JsonProperty("EnterDate")
	private Date entryTime;

	/**
	 * 出生日期
	 */
	@JsonIgnore
	private Date birthday;

	/**
	 * 过期日期
	 */
	@JsonProperty("LeaveDate")
	private Date expireDate;

	/**
	 * 扩展字段 1~10
	 */
	@JsonIgnore
	private String spare1;
	/*
	 * 工作地点
	 */
	// @JsonProperty("Gbort")
	@JsonProperty("ZzgZddWb")
	private String spare2;
	/*
	 * 职级
	 */
	@JsonProperty("PositionLevel")
	private String spare3;
	@JsonIgnore
	private String spare4;
	@JsonIgnore
	private String spare5;
	@JsonIgnore
	private String spare6;
	@JsonIgnore
	private String spare7;
	@JsonIgnore
	private String spare8;
	@JsonIgnore
	private String spare9;
	@JsonIgnore
	private String spare10;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getOrgOuCode() {
		return orgOuCode;
	}

	public void setOrgOuCode(String orgOuCode) {
		this.orgOuCode = orgOuCode;
	}

	public String getEncryptionType() {
		return encryptionType;
	}

	public void setEncryptionType(String encryptionType) {
		this.encryptionType = encryptionType;
	}

	public String getPostionNo() {
		return postionNo;
	}

	public void setPostionNo(String postionNo) {
		this.postionNo = postionNo;
	}

	public String getPostionName() {
		return postionName;
	}

	public void setPostionName(String postionName) {
		this.postionName = postionName;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getSpare1() {
		return spare1;
	}

	public void setSpare1(String spare1) {
		this.spare1 = spare1;
	}

	public String getSpare2() {
		return spare2;
	}

	public void setSpare2(String spare2) {
		this.spare2 = spare2;
	}

	public String getSpare3() {
		return spare3;
	}

	public void setSpare3(String spare3) {
		this.spare3 = spare3;
	}

	public String getSpare4() {
		return spare4;
	}

	public void setSpare4(String spare4) {
		this.spare4 = spare4;
	}

	public String getSpare5() {
		return spare5;
	}

	public void setSpare5(String spare5) {
		this.spare5 = spare5;
	}

	public String getSpare6() {
		return spare6;
	}

	public void setSpare6(String spare6) {
		this.spare6 = spare6;
	}

	public String getSpare7() {
		return spare7;
	}

	public void setSpare7(String spare7) {
		this.spare7 = spare7;
	}

	public String getSpare8() {
		return spare8;
	}

	public void setSpare8(String spare8) {
		this.spare8 = spare8;
	}

	public String getSpare9() {
		return spare9;
	}

	public void setSpare9(String spare9) {
		this.spare9 = spare9;
	}

	public String getSpare10() {
		return spare10;
	}

	public void setSpare10(String spare10) {
		this.spare10 = spare10;
	}

}
