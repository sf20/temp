package openDemo.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;
import openDemo.entity.ResultEntity;

public class HttpResultUtil {
	/**
	 * 获取企业大学接口数据
	 * @param params
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	public static String getResult(Map<String, Object> params, String url) throws IOException{
		String result = null;
		try {
			result = HttpRequestUtil.sendPost(url, params);
		} catch (IOException e) {
			try {
				// 发生异常重新请求一次
				result = HttpRequestUtil.sendPost(url, params);
			} catch (IOException e2) {
				throw new IOException("获取企业大学接口数据失败!", e2);
			}
		}
		return result;
	}
	
	/**
	 * 统一将返回值封装为实体
	 * @param jsonResult 返回的json格式的字符串
	 * @return
	 */
	public static ResultEntity getResult(String jsonResult){
		JSONObject jsonObject = JSONObject.fromObject(jsonResult);
		ResultEntity entity = (ResultEntity) JSONObject.toBean(jsonObject, ResultEntity.class);
		return entity;
	}
	
	/**
	 * 初始化公共参数
	 * @return
	 */
	public static Map<String, Object> getParamsMap(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("apikey", Config.apikey);
		params.put("salt", new Random().nextInt(10000));
		String secretkey = Config.secretkey;
		params.put("signature", SHA256Util.SHA256Encrypt(secretkey+params.get("salt")));
		return params;
	}
	
	/**
	 * 初始化公共参数
	 * @param apikey
	 * @param secretkey
	 * @return
	 */
	public static Map<String, Object> getParamsMap(String apikey, String secretkey){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("apikey", apikey);
		params.put("salt", new Random().nextInt(10000));
		params.put("signature", SHA256Util.SHA256Encrypt(secretkey+params.get("salt")));
		return params;
	}

}
