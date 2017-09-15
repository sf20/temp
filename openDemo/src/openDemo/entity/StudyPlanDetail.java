package openDemo.entity;

public class StudyPlanDetail {

	private String planID; // -计划ID
	private String userId; // – 用户UserName，如有第三方ID则为第三方ID
	private String userName;
	private String userNo;
	private String thirdSystemUserNo;
	private String planName;
	private String status; // 计划状态
	private String masterTitle; // -知识/考试名称
	private double actualStudyScore; // -实际获得学分
	private String laststStudyTime; // -最后学习时间
	private double studySchedule; // -进度
	private String actualFinishDate; // -实际完成时间
	private int examTotalScore; // -试卷总分
	private int examScore; // -得分
	private String isPassed; // -是否通过

	public String getPlanID() {
		return planID;
	}

	public void setPlanID(String planID) {
		this.planID = planID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getThirdSystemUserNo() {
		return thirdSystemUserNo;
	}

	public void setThirdSystemUserNo(String thirdSystemUserNo) {
		this.thirdSystemUserNo = thirdSystemUserNo;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMasterTitle() {
		return masterTitle;
	}

	public void setMasterTitle(String masterTitle) {
		this.masterTitle = masterTitle;
	}

	public double getActualStudyScore() {
		return actualStudyScore;
	}

	public void setActualStudyScore(double actualStudyScore) {
		this.actualStudyScore = actualStudyScore;
	}

	public String getLaststStudyTime() {
		return laststStudyTime;
	}

	public void setLaststStudyTime(String laststStudyTime) {
		this.laststStudyTime = laststStudyTime;
	}

	public double getStudySchedule() {
		return studySchedule;
	}

	public void setStudySchedule(double studySchedule) {
		this.studySchedule = studySchedule;
	}

	public String getActualFinishDate() {
		return actualFinishDate;
	}

	public void setActualFinishDate(String actualFinishDate) {
		this.actualFinishDate = actualFinishDate;
	}

	public int getExamTotalScore() {
		return examTotalScore;
	}

	public void setExamTotalScore(int examTotalScore) {
		this.examTotalScore = examTotalScore;
	}

	public int getExamScore() {
		return examScore;
	}

	public void setExamScore(int examScore) {
		this.examScore = examScore;
	}

	public String getIsPassed() {
		return isPassed;
	}

	public void setIsPassed(String isPassed) {
		this.isPassed = isPassed;
	}

}
