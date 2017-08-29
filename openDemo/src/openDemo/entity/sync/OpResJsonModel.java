package openDemo.entity.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 欧普公司接口返回的json数据模型
 * 
 * @author yanl
 *
 * @param <T>
 *            EsbResData类型：员工类型或组织类型
 */
public class OpResJsonModel<T> {
	@JsonProperty("EsbResHead")
	Map<String, String> EsbResHead = new HashMap<>();

	@JsonProperty("EsbResData")
	Map<String, List<T>> EsbResData = new HashMap<>();

	public Map<String, String> getEsbResHead() {
		return EsbResHead;
	}

	public void setEsbResHead(Map<String, String> esbResHead) {
		EsbResHead = esbResHead;
	}

	public Map<String, List<T>> getEsbResData() {
		return EsbResData;
	}

	public void setEsbResData(Map<String, List<T>> esbResData) {
		EsbResData = esbResData;
	}

}
