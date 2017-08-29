package openDemo.entity.sync;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 利欧公司岗位json数据模型
 * 
 * @author yanl
 */
public class LeoPositionModel {
	/*
	 * 岗位编号
	 */
	@JsonProperty("id") // TODO code or oid ??
	private String pNo;

	/*
	 * 一级类别；二级类别；岗位 (最后是岗位)
	 */
	@JsonProperty("name")
	private String pNames;

	public String getpNo() {
		return pNo;
	}

	public void setpNo(String pNo) {
		this.pNo = pNo;
	}

	public String getpNames() {
		return pNames;
	}

	public void setpNames(String pNames) {
		this.pNames = pNames;
	}

}
