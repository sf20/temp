package openDemo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import openDemo.common.HttpResultUtil;
import openDemo.common.JsonUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.ResultEntity;

/**
 * 组织单位同步
 * @author yaoj
 *
 */
public class SyncOrgService {
	
	/**
	 * 同步组织单位
	 * @param isBaseInfo 只同步组织单位基本信息。
	 * @param ouInfos 组织单位列表
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity ous(boolean isBaseInfo,List<OuInfoModel> ouInfos, String apikey, String secretkey, String baseUrl) throws IOException{
		JsonConfig jsonConfig = JsonUtil.jsonConfig(OuInfoModel.class);
		JSONArray array = JSONArray.fromObject(ouInfos, jsonConfig);
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		params.put("isBaseInfo", isBaseInfo);
		params.put("ouInfo", array.toString());
		String url = baseUrl + "el/sync/ous";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
	/**
	 * 同步删除组织单位
	 * @param ouCodeOrThirdSystemIDs
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity deleteous(List<String> ouCodeOrThirdSystemIDs, String apikey, String secretkey, String baseUrl) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		JSONArray ouCodeOrThirdSystemIDArray = JSONArray.fromObject(ouCodeOrThirdSystemIDs);
		params.put("OuCodeOrThirdSystemID", ouCodeOrThirdSystemIDArray.toString());
		String url = baseUrl + "el/sync/deleteous";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
	/**
	 * 同步用户移除组织单位
	 * @param userNames 用户名列表JSON格式例如：["sum11", "sum10"];
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity removeusersfromou(List<String> userNames, String apikey, String secretkey, String baseUrl) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		JSONArray userNamesArray = JSONArray.fromObject(userNames);
		params.put("userNames", userNamesArray.toString());
		String url = baseUrl + "el/sync/removeusersfromou";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
	/**
	 * 同步用户更改组织单位
	 * @param userNames 用户名列表JSON格式例如：["sum11", "sum10"];
	 * @param newOuID 第三方ID或部门编号
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity batchchangeorgou(List<String> userNames, String newOuID, String apikey, String secretkey, String baseUrl) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		JSONArray userNamesArray = JSONArray.fromObject(userNames);
		params.put("userNames", userNamesArray.toString());
		params.put("newOuID", newOuID);
		String url = baseUrl + "el/sync/batchchangeorgou";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}

	/**
	 * 根据部门名称查询部门编号
	 * 
	 * @param ouname
	 *            第三方ID或部门编号
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity getOucodeByName(String ouname, String apikey, String secretkey, String baseUrl) throws IOException {
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		params.put("ouname", ouname);
		String url = baseUrl + "el/sync/getoucodebyouname";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
}
