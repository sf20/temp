package openDemo.common;

import net.sf.json.JsonConfig;
import net.sf.json.processors.PropertyNameProcessor;
import net.sf.json.util.CycleDetectionStrategy;

public class JsonUtil {
	
	/**
	 * json每个字段首字母变大写
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> JsonConfig jsonConfig(Class<T> clazz){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		// json字段重命名 java--->json修改字段名字的时候使用
	    jsonConfig.registerJsonPropertyNameProcessor(clazz, new PropertyNameProcessor() {
			@Override
			public String processPropertyName(@SuppressWarnings("rawtypes") Class clazz, String name) {
				// 将javaben中的指定字段修改名字,
	            name = name.substring(0,1).toUpperCase()+name.substring(1);
	            return name;
			}
	    });
	    return jsonConfig;
	}

}
