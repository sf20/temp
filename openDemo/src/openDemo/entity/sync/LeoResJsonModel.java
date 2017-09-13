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
	private int code;
	private String message;
	private LeoResData<T> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LeoResData<T> getData() {
		return data;
	}

	public void setData(LeoResData<T> data) {
		this.data = data;
	}

}
