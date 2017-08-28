package openDemo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import openDemo.common.HttpResultUtil;
import openDemo.common.JsonUtil;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;


/**
 * 用户同步
 * @author yaoj
 *
 */
public class SyncUserService {
	
	/**
	 * 
	 * @param islink 是否同步用户基本信息
	 * @param users 用户信息以JSON格式
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity userSync(boolean islink, List<UserInfoModel> users, String apikey, String secretkey, String baseUrl) throws IOException{
		JsonConfig jsonConfig = JsonUtil.jsonConfig(UserInfoModel.class);
		JSONArray array = JSONArray.fromObject(users, jsonConfig);
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		params.put("islink", islink);
		params.put("users", array.toString());
		String url = baseUrl + "el/sync/users";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
	/**
	 * 同步禁用用户
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity disabledusersSync(List<String> userNames, String apikey, String secretkey, String baseUrl) throws IOException{
		String url = baseUrl + "el/sync/disabledusers";
		ResultEntity result = userOp(userNames, url, apikey, secretkey, baseUrl);
		return result;
	}
	
	/**
	 * 同步启用用户
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity enabledusersSync(List<String> userNames, String apikey, String secretkey, String baseUrl) throws IOException{
		String url = baseUrl + "el/sync/enabledusers";
		ResultEntity result = userOp(userNames, url, apikey, secretkey, baseUrl);
		return result;
	}
	
	/**
	 * 同步删除用户
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity deletedusersSync(List<String> userNames, String apikey, String secretkey, String baseUrl) throws IOException{
		String url = baseUrl + "el/sync/deletedusers";
		ResultEntity result = userOp(userNames, url, apikey, secretkey, baseUrl);
		return result;
	}
	
	/**
	 * 同步禁用用户,同步删除用户,同步启用用户
	 * @param userNames 用户名列表JSON格式，例如：["sum11", "sum10"];
	 * @param url
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException 
	 */
	private ResultEntity userOp(List<String> userNames, String url, String apikey, String secretkey, String baseUrl) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		JSONArray userNameArray = JSONArray.fromObject(userNames);
		params.put("userNames", userNameArray.toString());
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
}
