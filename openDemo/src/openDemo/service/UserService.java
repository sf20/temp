package openDemo.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import openDemo.common.Config;
import openDemo.common.HttpResultUtil;
import openDemo.common.JsonUtil;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoEntity;


/**
 * 用户同步
 * @author yaoj
 *
 */
public class UserService {
	
	/**
	 * 
	 * @param islink 是否同步用户基本信息
	 * @param users 用户信息以JSON格式
	 * @return
	 */
	public ResultEntity userSync(boolean islink, List<UserInfoEntity> users){
		JsonConfig jsonConfig = JsonUtil.jsonConfig(UserInfoEntity.class);
		JSONArray array = JSONArray.fromObject(users, jsonConfig);
		Map<String, Object> params = HttpResultUtil.getParamsMap();
		params.put("islink", islink);
		params.put("users", array.toString());
		String url = Config.baseUrl + "el/sync/users";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
	/**
	 * 同步禁用用户
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @return
	 */
	public ResultEntity disabledusersSync(List<String> userNames){
		String url = Config.baseUrl + "el/sync/disabledusers";
		ResultEntity result = userOp(userNames, url);
		return result;
	}
	
	/**
	 * 同步启用用户
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @return
	 */
	public ResultEntity enabledusersSync(List<String> userNames){
		String url = Config.baseUrl + "el/sync/enabledusers";
		ResultEntity result = userOp(userNames, url);
		return result;
	}
	
	/**
	 * 同步删除用户
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @return
	 */
	public ResultEntity deletedusersSync(List<String> userNames){
		String url = Config.baseUrl + "el/sync/deletedusers";
		ResultEntity result = userOp(userNames, url);
		return result;
	}
	
	/**
	 * 同步禁用用户,同步删除用户,同步启用用户
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @param url
	 * @return
	 */
	private ResultEntity userOp(List<String> userNames, String url){
		Map<String, Object> params = HttpResultUtil.getParamsMap();
		JSONArray userNameArray = JSONArray.fromObject(userNames);
		params.put("userNames", userNameArray.toString());
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
}
