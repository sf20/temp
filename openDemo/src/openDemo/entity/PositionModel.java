package openDemo.entity;

/**
 * 岗位对象
 * 
 * @author yanl
 *
 */
public class PositionModel {
	/**
	 * 岗位编号
	 */
	private String pNo;

	/**
	 * 一级类别；二级类别；岗位 (最后是岗位)
	 */
	private String pNames;

	/**
	 * 岗位类别
	 */
	private String pNameClass;
	
	/**
	 * 岗位状态
	 */
	private String status;

	/**
	 * 岗位删除状态
	 */
	private String deleteStatus;

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

	public String getpNameClass() {
		return pNameClass;
	}

	public void setpNameClass(String pNameClass) {
		this.pNameClass = pNameClass;
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
		result = prime * result + ((pNo == null) ? 0 : pNo.hashCode());
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
		PositionModel other = (PositionModel) obj;
		if (pNo == null) {
			if (other.pNo != null)
				return false;
		} else if (!pNo.equals(other.pNo))
			return false;
		return true;
	}

}
