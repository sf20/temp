package openDemo.entity.sync;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 职位数据
 * 
 * @author yanl
 *
 */
public class LeoResPosData {
	private String total;
	@JsonProperty("jobPositions")
	private List<LeoPositionModel> dataList;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<LeoPositionModel> getDataList() {
		return dataList;
	}

	public void setDataList(List<LeoPositionModel> dataList) {
		this.dataList = dataList;
	}

}
