package openDemo.entity;

/**
 * 岗位对象
 * 
 * @author yanl
 *
 */
public class OpPositionEntity {
	/**
	 * 岗位编号
	 */
	private String pNo;

	/**
	 * 一级类别；二级类别；岗位 (最后是岗位)
	 */
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
		OpPositionEntity other = (OpPositionEntity) obj;
		if (pNo == null) {
			if (other.pNo != null)
				return false;
		} else if (!pNo.equals(other.pNo))
			return false;
		return true;
	}

}
