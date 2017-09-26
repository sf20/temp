package openDemo.entity;

import java.util.Date;
import java.util.List;

/**
 * 组织单位对象
 * 
 * @author yaoj
 *
 */
public class OuInfoModel {

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
	private boolean isSub;

	/**
	 * 排序索引?
	 */
	private int orderIndex;

	/**
	 * 过期日期
	 */
	private Date endDate;

	/**
	 * 组织状态
	 */
	private String status;

	/**
	 * 组织删除状态
	 */
	private String deleteStatus;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OuInfoModel other = (OuInfoModel) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

}
