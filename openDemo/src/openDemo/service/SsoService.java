package openDemo.service;

import java.util.Map;

import openDemo.common.Config;
import openDemo.common.HttpResultUtil;
import openDemo.entity.ResultEntity;

/**
 * 单点登录
 * @author yaoj
 *
 */
public class SsoService {
	
	/**
	 * 单点登录
	 * @param uname 登录企业大学时输入的用户名
	 * @return
	 */
	public ResultEntity sso(String uname){
		Map<String, Object> params = HttpResultUtil.getParamsMap();
		params.put("uname", uname);
		String url = Config.baseUrl + "el/sso";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}

}
