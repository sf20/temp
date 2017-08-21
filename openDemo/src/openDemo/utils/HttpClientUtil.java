package openDemo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientUtil {
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final Logger LOGGER = LogManager.getLogger(HttpClientUtil.class);
	// 默认响应处理器
	private static ResponseHandler<String> responseHandler;
	// 设置超时及cookie策略
	private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
			.setConnectionRequestTimeout(1000).setSocketTimeout(5000).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

	static {
		// 池连接管理器
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
		// 最大总连接数
		connManager.setMaxTotal(100);
		// 同路由的并发数
		connManager.setDefaultMaxPerRoute(10);

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setConnectionManager(connManager);
		// 重试次数，默认是3次，没有开启。已有默认值无需设置！！！
		// httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());
		// 保持长连接配置，需要在头添加Keep-Alive
		httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
		headers.add(new BasicHeader("Accept-Language", "zh-CN"));
		// headers.add(new BasicHeader("Connection", "Keep-Alive"));
		// 设置通用请求头
		httpClientBuilder.setDefaultHeaders(headers);

		// 创建响应处理器
		responseHandler = new ResponseHandler<String>() {

			@Override
			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};

	}

	public static String doGet(String url) {

		return null;
	}

	public static String doGet(String url, Map<String, Object> params) {

		return null;
	}

	public static String doPost(String url) {

		return null;
	}

	public static String doPost(String url, String json) {

		return null;
	}

	public static String doPost(String url, Map<String, Object> params) {

		return null;
	}

	public static String doSSLPost(String url, String json) {

		return null;
	}

	public static String doSSLPost(String url, Map<String, Object> params) {

		return null;
	}

	HttpClient getHttpClient() {
		return null;// httpClientBuilder.build();
	}
}
