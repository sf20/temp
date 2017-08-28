package openDemo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import openDemo.common.HttpResultUtil;
import openDemo.common.JsonUtil;
import openDemo.entity.PositionModel;
import openDemo.entity.ResultEntity;

/**
 * 岗位同步
 * 
 * @author yanl
 *
 */
public class SyncPositionService {

	/**
	 * 同步岗位
	 * 
	 * @param positionInfos
	 *            岗位列表
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity syncPos(List<PositionModel> positionInfos, String apikey, String secretkey, String baseUrl) throws IOException{
		JsonConfig jsonConfig = JsonUtil.jsonConfig(PositionModel.class);
		JSONArray array = JSONArray.fromObject(positionInfos, jsonConfig);
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		params.put("positionInfo", array.toString());
		String url = baseUrl + "el/sync/position";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}

	/**
	 * 同步岗位（返回对应岗位编号）
	 * 
	 * @param positionInfos
	 *            岗位列表
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity syncPosGetPNo(List<PositionModel> positionInfos, String apikey, String secretkey, String baseUrl) throws IOException{
		JsonConfig jsonConfig = JsonUtil.jsonConfig(PositionModel.class);
		JSONArray array = JSONArray.fromObject(positionInfos, jsonConfig);
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		params.put("positionInfo", array.toString());
		String url = baseUrl + "el/sync/syncpositionfornopno";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}

	/**
	 * 同步更改岗位名称
	 * 
	 * @param positionNo
	 *            岗位编号
	 * @param positionName
	 *            岗位名称(修改后)
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity changePosName(String positionNo, String positionName, String apikey, String secretkey, String baseUrl) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		params.put("positionNo", positionNo);
		params.put("positionName", positionName);
		String url = baseUrl + "el/sync/updatepositioninfo";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}

}
