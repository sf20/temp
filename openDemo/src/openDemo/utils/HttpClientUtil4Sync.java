package openDemo.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientUtil4Sync {
	public static final Logger LOGGER = LogManager.getLogger(HttpClientUtil4Sync.class);
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String TLS = "TLS";
	public static final String TLS_1_2 = "TLSv1.2";
	public static final int MAX_TOTAL = 200;
	public static final int MAX_PER_ROUTE = 100;
	// 请求配置
	private static RequestConfig requestConfig;
	// 请求头配置
	private static List<Header> headers;
	// 默认响应处理器
	private static ResponseHandler<String> responseHandler;

	static {
		// 设置超时时间
		requestConfig = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).build();

		// 重试次数，默认是3次，没有开启。已有默认值无需设置！！！
		// httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());
		// 保持长连接配置，需要在头添加Keep-Alive
		// httpClientBuilder.setKeepAliveStrategy(new
		// DefaultConnectionKeepAliveStrategy());

		headers = new ArrayList<Header>();
		headers.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
		// headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
		headers.add(new BasicHeader("Accept-Language", "zh-CN"));
		headers.add(new BasicHeader("Connection", "Keep-Alive"));

		// 创建响应处理器
		responseHandler = new ResponseHandler<String>() {

			@Override
			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				StatusLine statusLine = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				if (statusLine.getStatusCode() >= 300) {
					throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
				}
				if (entity == null) {
					throw new ClientProtocolException("Response contains no content");
				}
				// ContentType contentType = ContentType.getOrDefault(entity);
				// Charset charset = contentType.getCharset();
				String retStr = EntityUtils.toString(entity, CHARSET_UTF8);
				EntityUtils.consume(entity);

				return retStr;
			}

		};
	}

	/**
	 * 使用简单的HttpClient发送Get请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String doGet(String url) throws IOException {
		return doGet(url, null, null);
	}

	public static String doGet(String url, Map<String, Object> params, List<Header> headers) throws IOException {
		CloseableHttpClient httpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(url + buildGetParams(params));
		// 增加请求头
		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				httpGet.addHeader(header);
			}
		}

		CloseableHttpResponse response = null;
		String retStr = null;
		try {
			response = httpClient.execute(httpGet);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					retStr = EntityUtils.toString(entity, CHARSET_UTF8);
					EntityUtils.consume(entity);
				}
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
		return retStr;
	}

	/**
	 * 将参数map转成相应的参数字符串
	 * 
	 * @param params
	 * @return
	 */
	private static String buildGetParams(Map<String, Object> params) {
		if (params == null) {
			return "";
		}
		StringBuilder paramStr = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			try {
				paramStr.append("&").append(entry.getKey()).append("=")
						.append(URLEncoder.encode(String.valueOf(entry.getValue()), CHARSET_UTF8));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("UnsupportedEncoding", e);
				return null;
			}
		}
		if (paramStr.length() > 0) {
			paramStr.replace(0, 1, "?");
		}
		return paramStr.toString();
	}

	private static String post(String url, HttpEntity httpEntity, List<Header> headers) throws IOException {
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		if (httpEntity != null) {
			httpPost.setEntity(httpEntity);
		}

		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				httpPost.addHeader(header);
			}
		}

		CloseableHttpResponse response = null;
		String retStr = null;
		try {
			response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					retStr = EntityUtils.toString(entity, CHARSET_UTF8);
					EntityUtils.consume(entity);
				}
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
		return retStr;
	}

	public static String doPost(String url) throws IOException {
		return post(url, null, null);
	}

	public static String doPost(String url, String jsonParams) throws IOException {
		return post(url, getHttpEntity(jsonParams), null);
	}

	public static String doPost(String url, Map<String, Object> params) throws IOException {
		return post(url, getHttpEntity(params), null);
	}

	public static String doPost(String url, String jsonParams, List<Header> headers) throws IOException {
		return post(url, getHttpEntity(jsonParams), headers);
	}

	public static String doPost(String url, Map<String, Object> params, List<Header> headers) throws IOException {
		return post(url, getHttpEntity(params), headers);
	}

	/**
	 * 使用配置了SSL的HttpClient发送Get请求
	 * 
	 * @param url
	 * @param protocol
	 * @return
	 * @throws IOException
	 */
	public static String doSSLGet(String url, String protocol) throws IOException {
		return doSSLGet(url, protocol, null);
	}

	public static String doSSLGet(String url, String protocol, Map<String, Object> params) throws IOException {
		CloseableHttpClient httpClient = getSSLHttpClient(protocol);
		HttpGet httpGet = new HttpGet(url + buildGetParams(params));
		CloseableHttpResponse response = null;
		String retStr = null;
		try {
			response = httpClient.execute(httpGet);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					retStr = EntityUtils.toString(entity, CHARSET_UTF8);
					EntityUtils.consume(entity);
				}
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
		return retStr;
	}

	private static String sslPost(String url, String protocol, HttpEntity httpEntity)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		CloseableHttpClient httpClient = getSSLHttpClient(protocol);
		HttpPost httpPost = new HttpPost(url);
		if (httpEntity != null) {
			httpPost.setEntity(httpEntity);
		}

		CloseableHttpResponse response = null;
		String retStr = null;
		try {
			response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					retStr = EntityUtils.toString(entity, CHARSET_UTF8);
					EntityUtils.consume(entity);
				}
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
		return retStr;
	}

	public static String doSSLPost(String url, String protocol)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return sslPost(url, protocol, null);
	}

	public static String doSSLPost(String url, String protocol, String jsonParams)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return sslPost(url, protocol, getHttpEntity(jsonParams));
	}

	public static String doSSLPost(String url, String protocol, Map<String, Object> params)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return sslPost(url, protocol, getHttpEntity(params));
	}

	/**
	 * 使用配置了池连接管理的HttpClient发送Get请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String doGetUsePool(String url) throws IOException {
		return doGetUsePool(url, null, null);
	}

	public static String doGetUsePool(String url, Map<String, Object> params, List<Header> headers) throws IOException {
		HttpGet httpGet = new HttpGet(url + buildGetParams(params));
		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				httpGet.addHeader(header);
			}
		}
		return getPoolingHttpClient().execute(httpGet, responseHandler);
	}

	private static String postUsePool(String url, HttpEntity httpEntity) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		if (httpEntity != null) {
			httpPost.setEntity(httpEntity);
		}
		return getPoolingHttpClient().execute(httpPost, responseHandler);
	}

	public static String doPostUsePool(String url) throws IOException {
		return postUsePool(url, null);
	}

	public static String doPostUsePool(String url, String jsonParams) throws IOException {
		return postUsePool(url, getHttpEntity(jsonParams));
	}

	public static String doPostUsePool(String url, Map<String, Object> params) throws IOException {
		return postUsePool(url, getHttpEntity(params));
	}

	private static String sslPostUsePool(String url, String protocol, HttpEntity httpEntity)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		HttpClient httpClient = getPoolingSSLHttpClient(protocol);
		HttpPost httpPost = new HttpPost(url);
		if (httpEntity != null) {
			httpPost.setEntity(httpEntity);
		}
		return httpClient.execute(httpPost, responseHandler);
	}

	public static String doSSLPostUsePool(String url, String protocol)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return sslPostUsePool(url, protocol, null);
	}

	public static String doSSLPostUsePool(String url, String jsonParams, String protocol)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return sslPostUsePool(url, protocol, getHttpEntity(jsonParams));
	}

	public static String doSSLPostUsePool(String url, Map<String, Object> params, String protocol)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return sslPostUsePool(url, protocol, getHttpEntity(params));
	}

	private static HttpEntity getHttpEntity(String jsonParams) {
		// 构建消息实体 发送Json格式的数据
		StringEntity entity = new StringEntity(jsonParams, ContentType.APPLICATION_JSON);
		entity.setContentEncoding(CHARSET_UTF8);

		return entity;
	}

	private static HttpEntity getHttpEntity(Map<String, Object> params) {
		HttpEntity entity = null;
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>(params.size());
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
		}

		try {
			entity = new UrlEncodedFormEntity(paramsList, CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("getHttpEntity() error:", e);
		}
		return entity;
	}

	/**
	 * 创建通常的HttpClient
	 * 
	 * @return
	 */
	private static CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setDefaultRequestConfig(requestConfig).setDefaultHeaders(headers).build();
	}

	/**
	 * 创建设置了池连接管理的HttpClient
	 * 
	 * @return
	 */
	private static CloseableHttpClient getPoolingHttpClient() {
		// 池连接管理器
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(MAX_TOTAL);// 最大总连接数
		connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);// 同路由的并发数

		return HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig)
				.setDefaultHeaders(headers).build();
	}

	/**
	 * 创建支持SSL的HttpClient
	 * 
	 * @param protocol
	 * @return
	 */
	private static CloseableHttpClient getSSLHttpClient(String protocol) {
		try {
			return HttpClients.custom().setSSLSocketFactory(getSSLConnSocketFactory(protocol))
					.setDefaultRequestConfig(requestConfig).setDefaultHeaders(headers).build();
		} catch (Exception e) {
			LOGGER.error("创建SSLClient失败", e);
		}
		return HttpClients.createDefault();
	}

	/**
	 * 创建设置了池连接管理且支持SSL的HttpClient
	 * 
	 * @param protocol
	 * @return
	 */
	private static CloseableHttpClient getPoolingSSLHttpClient(String protocol) {
		try {
			// 注册ConnectionSocketFactory实例
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", getSSLConnSocketFactory(protocol)).build();
			PoolingHttpClientConnectionManager connManager4SSL = new PoolingHttpClientConnectionManager(registry);
			connManager4SSL.setMaxTotal(MAX_TOTAL);
			connManager4SSL.setDefaultMaxPerRoute(MAX_PER_ROUTE);

			return HttpClients.custom().setConnectionManager(connManager4SSL).setDefaultRequestConfig(requestConfig)
					.setDefaultHeaders(headers).build();
		} catch (Exception e) {
			LOGGER.error("创建SSLClient失败", e);
		}
		return HttpClients.createDefault();
	}

	/**
	 * 创建指定协议的SSLConnectionSocketFactory
	 * 
	 * @param protocol
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static SSLConnectionSocketFactory getSSLConnSocketFactory(String protocol)
			throws NoSuchAlgorithmException, KeyManagementException {
		// SSL连接配置
		SSLContext sslContext = SSLContext.getInstance(protocol == null ? TLS : protocol);
		// 实现一个X509TrustManager接口
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		sslContext.init(null, new TrustManager[] { trustManager }, null);

		return new SSLConnectionSocketFactory(sslContext);
	}

	public static void main(String[] args) {
		poolTest();
		// sslTest();
	}

	public static void sslTest() {
		String url = "https://www.taobao.com";
		try {
			HttpClientUtil4Sync.doGetUsePool(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void poolTest() {
		String url = "http://tu.duowan.com/gallery/";
		try {
			// HttpClientUtil.doPost(null);
			Date start = new Date();
			for (int i = 125600; i < 125700; i++) {
				HttpClientUtil4Sync.doGet(url + i + ".html#p1");
			}
			Date end = new Date();
			System.out.println("无连接池总耗时：" + (end.getTime() - start.getTime()));

			Date start2 = new Date();
			for (int i = 125600; i < 125700; i++) {
				HttpClientUtil4Sync.doGetUsePool(url + i + ".html#p1");
			}
			Date end2 = new Date();
			System.out.println("使用连接池总耗时：" + (end2.getTime() - start2.getTime()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
