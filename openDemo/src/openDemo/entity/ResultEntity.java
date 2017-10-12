package openDemo.entity;

/**
 * 调用接口统一返回格式
 * @author yaoj
 *
 */
public class ResultEntity {
	
	/**
	 *  返回码
	 *  0 请求成功
	 *  -1 服务繁忙
	 *  40101 授权码签名无效
	 *  40102 未提供授权码签名信息
	 *  50001 未授权该api
	 *  50002 api功能未授权
	 *  60100 服务内部错误!
	 *  60101 业务处理错误
	 */
	private String code;
	
	/**
	 * 返回提示
	 */
	private String message;
	
	/**
	 * 记录总条数
	 */
	private int totalcount;
	
	/**
	 * 成功执行后返回的数据(Json格式字符串)
	 */
	private String data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
