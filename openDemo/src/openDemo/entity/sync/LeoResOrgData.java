package openDemo.entity.sync;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 组织数据
 * 
 * @author yanl
 *
 */
public class LeoResOrgData {
	private String total;
	@JsonProperty("origizations")
	private List<LeoOuInfoModel> dataList;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<LeoOuInfoModel> getDataList() {
		return dataList;
	}

	public void setDataList(List<LeoOuInfoModel> dataList) {
		this.dataList = dataList;
	}

}
