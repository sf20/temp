package openDemo.entity;


public class UserEntity {

	/**
	 * 用户ID (同步必传)
	 */
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	private String userName;

	/**
	 * 密码  
	 * 备注：如果用MD5或者CMD5加密则必须使用标准MD5 
	 * 32位小写加密的字符串（如果不传使用平台配置的默认密码）
	 */
	private String password;

	/**
	 * 姓名 (同步必传)
	 */
	private String cnName;

	/**
	 * 性别 
	 */
	private String sex;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 英文缩写
	 */
	private String EName;

	/**
	 * 办公电话 
	 */
	private String officePhone;

	/**
	 * 家庭电话
	 */
	private String homePhone;

	/**
	 * 移动电话 
	 */
	private String mobile;

	/**
	 * 电子邮件  
	 */
	private String mail;

	/**
	 * 个人主页  
	 */
	private String homePage;

	/**
	 * 省 
	 */
	private String province;

	/**
	 * 市 
	 */
	private String city;

	/**
	 * 街道  
	 */
	private String streetAddress;

	/**
	 * 传真
	 */
	private String fax;

	/**
	 * 职务名称
	 */
	private String jobName;

	/**
	 * 错误信息
	 */
	private String errorInfo;

	/**
	 * 是否是主管
	 */
	private boolean isManager;

	/**
	 * 部门编号
	 */
    private String orgOuCode;

	/**
	 * 密码加密方式： YXT(云学堂加密)、MD5 (密码MD5加密)、CMD5(用户名+密码MD5加密)
	 */
	private String encryptionType;

	/**
	 * 岗位编号
	 */
	private String postionNo;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEName() {
		return EName;
	}

	public void setEName(String eName) {
		EName = eName;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
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

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
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

	public boolean getIsManager() {
		return isManager;
	}

	public void setIsManager(boolean isManager) {
		this.isManager = isManager;
	}

}
