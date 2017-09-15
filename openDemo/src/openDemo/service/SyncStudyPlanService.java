package openDemo.service;

import java.io.IOException;
import java.util.Map;

import openDemo.common.HttpResultUtil;
import openDemo.entity.ResultEntity;

/**
 * 学习计划同步
 * @author 2017-09-14 jixf
 *
 */
public class SyncStudyPlanService {
	
	/**
	 * 指定用户添加到指定的学习计划
	 * @param studyPlanID  计划ID,必输
	 * @param userNames 账号集合，如user1;user2;以;隔开，一次最多添加100个账号,必输
	 * @param apikey
	 * @param secretkey
	 * @param baseUrl
	 * @return
	 * @throws IOException
	 */
	public ResultEntity addPersonToPlan(String studyPlanID, String userNames, String apikey, String secretkey, String baseUrl) throws IOException{
		Map<String, Object> params = HttpResultUtil.getParamsMap(apikey, secretkey);
		params.put("ID", studyPlanID);
		params.put("UserNames", userNames);
		String url = baseUrl + "el/sty/addpersontoplan";
		String result = HttpResultUtil.getResult(params, url);
		return HttpResultUtil.getResult(result);
	}
	
}
