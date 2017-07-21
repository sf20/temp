package openDemo.entity;

import java.util.Date;
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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
		OuInfoEntity other = (OuInfoEntity) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

}
