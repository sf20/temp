package openDemo.entity;

import java.util.List;

/**
 * 组
 * @author yaoj
 *
 */
public class GroupInfoEntity {

	/**
	 * 组ID    
	 */
	private String ID;

	/**
	 * 组名称  
	 */
	private String groupName;
		
	/**
	 * 描述 
	 */
	private String description;

	/**
	 * 组下的用户名集合
	 */
	private List<String> users;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
		
}
