package openDemo.entity.sync;

/**
 * 利欧公司接口返回的json数据模型
 * 
 * @author yanl
 *
 * @param <T>
 *            人员或组织或职位数据
 */
public class LeoResJsonModel<T> {
	private String code;
	private String message;
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
