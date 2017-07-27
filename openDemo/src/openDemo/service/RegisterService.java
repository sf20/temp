package openDemo.service;

import java.io.IOException;
import java.util.Map;

import openDemo.common.Config;
import openDemo.common.HttpResultUtil;
import openDemo.entity.ResultEntity;

/**
 * 企业大学注册
 * @author yaoj
 *
 */
public class RegisterService {

	/**
	 * 企业大学注册
	 * @param orgName 机构名称
	 * @param domainName 机构域名 (ex:coke,不需要.com等)
	 * @param mobile 手机号码
	 * @return
	 * @throws IOException 
	 */
	public ResultEntity syncregisterenterprise(String orgName, String domainName, String mobile) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap();
		params.put("orgName", orgName);
		params.put("domainName", domainName);
		params.put("mobile", mobile);
		String url = Config.baseUrl + "el/sync/syncregisterenterprise";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}

}
