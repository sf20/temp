package openDemo.entity.sync;

import java.util.List;

/**
 * 返回json数据中data对应的模型对象
 * 
 * @author yanl
 *
 */
public class LeoResData<T> {
	private int total;
	private List<T> dataList;
	// 以下未用到
	private List<T> jobPositions;
	private List<T> origizations;
	private List<T> employees;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public List<T> getJobPositions() {
		return jobPositions;
	}

	public void setJobPositions(List<T> jobPositions) {
		this.jobPositions = jobPositions;
	}

	public List<T> getOrigizations() {
		return origizations;
	}

	public void setOrigizations(List<T> origizations) {
		this.origizations = origizations;
	}

	public List<T> getEmployees() {
		return employees;
	}

	public void setEmployees(List<T> employees) {
		this.employees = employees;
	}

}
