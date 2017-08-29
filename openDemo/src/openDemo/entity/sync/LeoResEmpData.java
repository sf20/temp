package openDemo.entity.sync;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 人员数据
 * 
 * @author yanl
 *
 */
public class LeoResEmpData {
	private String total;
	@JsonProperty("employees")
	private List<LeoUserInfoModel> dataList;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<LeoUserInfoModel> getDataList() {
		return dataList;
	}

	public void setDataList(List<LeoUserInfoModel> dataList) {
		this.dataList = dataList;
	}

}
