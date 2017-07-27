package openDemo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import openDemo.common.Config;
import openDemo.common.HttpResultUtil;
import openDemo.common.JsonUtil;
import openDemo.entity.GroupInfoEntity;
import openDemo.entity.ResultEntity;

/**
 * 角色同步
 * @author yaoj
 *
 */
public class RoleService {
	
	/**
	 * 同步组
	 * @param isBaseInfo 只同步组基本信息
	 * @param groupInfos 组列表
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity roles(boolean isBaseInfo,List<GroupInfoEntity> groupInfos) throws IOException{
		JsonConfig jsonConfig = JsonUtil.jsonConfig(GroupInfoEntity.class);
		JSONArray array = JSONArray.fromObject(groupInfos, jsonConfig);
		Map<String, Object> params = HttpResultUtil.getParamsMap();
		params.put("isBaseInfo", isBaseInfo);
		params.put("groupInfo", array.toString());
		String url = Config.baseUrl + "el/sync/roles";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
	/**
	 * 同步删除组
	 * @param roleThirdSystemIDs 组编号列表JSON格式例如：["role_kaifa",  "role_ceshi"];
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity deletedroles(List<String> roleThirdSystemIDs) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap();
		JSONArray roleThirdSystemIDArray = JSONArray.fromObject(roleThirdSystemIDs);
		params.put("roleThirdSystemID", roleThirdSystemIDArray.toString());
		String url = Config.baseUrl + "el/sync/deletedroles";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
	/**
	 * 同步用户移除组
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity removeusersfromrole(List<String> userNames) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap();
		JSONArray userNamesArray = JSONArray.fromObject(userNames);
		params.put("userNames", userNamesArray.toString());
		String url = Config.baseUrl + "el/sync/removeusersfromrole";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
}
