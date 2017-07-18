package openDemo.entity;

import java.util.List;

/**
 * 组织单位对象
 * 
 * @author yaoj
 *
 */
public class OuInfoEntity {

	/**
	 * 组织单位ID
	 */
	private String ID;

	/**
	 * 组织单位名称
	 */
	private String ouName;

	/**
	 * 父节点ID
	 */
	private String parentID;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 组织单位下的用户名集合
	 */
	private List<String> users;

	/**
	 * 是否分支机构
	 */
	private String isSub;

	/**
	 * 排序索引?
	 */
	private Integer orderIndex;

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

	public String getIsSub() {
		return isSub;
	}

	public void setIsSub(String isSub) {
		this.isSub = isSub;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

}
