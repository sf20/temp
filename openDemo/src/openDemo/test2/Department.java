package openDemo.test2;

public class Department {
	private long orgAccountId;// 967547186016371000, 【必填】
	private long id;// 7313901301646381000,
	private String name;// "炼狱殿", 【必填】
	private boolean enabled;// true, 【必填】
	private long sortId;// 2, 【必填】
	private boolean isGroup;// false, 【必填】
	private long superior;// 967547186016371000, 【必填】
	private String path;// "000000050002", 【必填】

	public long getOrgAccountId() {
		return orgAccountId;
	}

	public void setOrgAccountId(long orgAccountId) {
		this.orgAccountId = orgAccountId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getSortId() {
		return sortId;
	}

	public void setSortId(long sortId) {
		this.sortId = sortId;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setIsGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public long getSuperior() {
		return superior;
	}

	public void setSuperior(long superior) {
		this.superior = superior;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
