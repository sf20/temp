package openDemo.entity.sync;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 欧普公司组织单位json数据模型
 * 
 * @author yanl
 *
 */
public class OpOuInfoModel {

	/**
	 * 组织单位ID
	 */
	@JsonProperty("OrgCode")
	private String ID;

	/**
	 * 组织单位名称
	 */
	@JsonProperty("OrgName")
	private String ouName;

	/**
	 * 父节点ID
	 */
	@JsonProperty("ParentCode")
	private String parentID;

	/**
	 * 描述
	 */
	@JsonIgnore
	private String description;

	/**
	 * 组织单位下的用户名集合
	 */
	@JsonIgnore
	private List<String> users;

	/**
	 * 是否分支机构
	 */
	@JsonIgnore
	private boolean isSub;

	/**
	 * 排序索引?
	 */
	@JsonIgnore
	private int orderIndex;

	/*
	 * 组织废止日期
	 */
	@JsonProperty("Endda")
	private Date endDate;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getOuName() {
		return ouName;
	}

	public void setOuName(String ouName) {
		this.ouName = ouName;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public boolean getIsSub() {
		return isSub;
	}

	public void setIsSub(boolean isSub) {
		this.isSub = isSub;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
