package openDemo.entity.sync;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 组织单位对象
 * 
 * @author yanl
 *
 */
public class OuInfoEntity {

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
	// TODO @JsonProperty("ParentCode")
	@JsonIgnore
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

}
