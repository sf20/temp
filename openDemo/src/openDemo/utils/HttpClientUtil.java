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
import java.util.concurrent.TimeUnit;

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

public class HttpClientUtil {
	public static final Logger LOGGER = LogManager.getLogger(HttpClientUtil.class);
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String TLS_1_2 = "TLSv1.2";
	// 池连接管理器
	private static PoolingHttpClientConnectionManager connManager;
	// 请求配置
	private static RequestConfig requestConfig;
	// 请求头配置
	private static List<Header> headers;
	// 默认响应处理器
	private static ResponseHandler<String> responseHandler;
	//
	private static SSLConnectionSocketFactory sslConnSocketFactory;

	static {
		// 池连接管理器
		connManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
		// 最大总连接数
		connManager.setMaxTotal(100);
		// 同路由的并发数
		connManager.setDefaultMaxPerRoute(20);

		// 设置超时时间
		requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000)
				.setSocketTimeout(5000).build();

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

		System.out.println("static construct");
	}

	public static String doGet(String url) throws IOException {
		return doGet(url, null);
	}

	public static String doGet(String url, Map<String, String> params) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
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

	/**
	 * 将参数map转成相应的参数字符串
	 * 
	 * @param params
	 * @return
	 */
	public static String buildGetParams(Map<String, String> params) {
		if (params == null) {
			return "";
		}
		StringBuilder paramStr = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			try {
				paramStr.append("&").append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), CHARSET_UTF8));
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

	public static String doPost(String url, HttpEntity httpEntity) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		if (httpEntity.getContentLength() > 0) {
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

	public static String doPost(String url) throws IOException {
		return doPost(url, new StringEntity(""));// TODO
	}

	public static String doPost(String url, String jsonParams) throws IOException {
		return doPost(url, getHttpEntity(jsonParams));
	}

	public static String doPost(String url, Map<String, String> params) throws IOException {
		return doPost(url, getHttpEntity(params));
	}

	public static String doSSLPost(String url, HttpEntity httpEntity)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {

		CloseableHttpClient httpClient = getSslHttpClient(TLS_1_2);
		HttpPost httpPost = new HttpPost(url);
		if (httpEntity.getContentLength() > 0) {
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

	public static String doSSLPost(String url) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return doSSLPost(url, new StringEntity(""));// TODO
	}

	public static String doSSLPost(String url, String jsonParams)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return doSSLPost(url, getHttpEntity(jsonParams));
	}

	public static String doSSLPost(String url, Map<String, String> params)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return doSSLPost(url, getHttpEntity(params));
	}

	public static String doGetUsePool(String url) throws IOException {
		return getPoolingHttpClient().execute(new HttpGet(url), responseHandler);
	}

	public static String doGetUsePool(String url, Map<String, String> params) throws IOException {
		return getPoolingHttpClient().execute(new HttpGet(url + buildGetParams(params)), responseHandler);
	}

	public static String doPostUsePool(String url, HttpEntity httpEntity) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(httpEntity);
		return getPoolingHttpClient().execute(httpPost, responseHandler);
	}

	public static String doPostUsePool(String url) throws IOException {
		return doPostUsePool(url, new StringEntity("", CHARSET_UTF8));// TODO
	}

	public static String doPostUsePool(String url, String jsonParams) throws IOException {
		return doPostUsePool(url, getHttpEntity(jsonParams));
	}

	public static String doPostUsePool(String url, Map<String, String> params) throws IOException {
		return doPostUsePool(url, getHttpEntity(params));
	}

	public static String doSSLPostUsePool(String url, HttpEntity httpEntity)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		HttpClient httpClient = getPoolingSslHttpClient(TLS_1_2);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(httpEntity);
		return httpClient.execute(httpPost, responseHandler);
	}

	public static HttpEntity getHttpEntity(String jsonParams) {
		// 构建消息实体 发送Json格式的数据
		StringEntity entity = new StringEntity(jsonParams, ContentType.APPLICATION_JSON);
		entity.setContentEncoding(CHARSET_UTF8);

		return entity;
	}

	public static HttpEntity getHttpEntity(Map<String, String> params) {
		HttpEntity entity = null;
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>(params.size());
		for (Map.Entry<String, String> entry : params.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		try {
			entity = new UrlEncodedFormEntity(paramsList, CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("getHttpEntity() error:", e);
		}
		return entity;
	}

	public static CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setDefaultRequestConfig(requestConfig).setDefaultHeaders(headers).build();
	}

	public static CloseableHttpClient getPoolingHttpClient() {
		return HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig)
				.setDefaultHeaders(headers).build();
	}

	public static CloseableHttpClient getSslHttpClient(String protocol) {
		try {
			return HttpClients.custom().setDefaultRequestConfig(requestConfig).setDefaultHeaders(headers)
					.setSSLSocketFactory(getSSLConnSocketFactory(protocol)).build();
		} catch (Exception e) {
			LOGGER.error("创建SSLClient失败", e);
		}
		return HttpClients.createDefault();
	}

	public static CloseableHttpClient getPoolingSslHttpClient(String protocol) {
		try {
			return HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig)
					.setDefaultHeaders(headers).setSSLSocketFactory(getSSLConnSocketFactory(protocol)).build();
		} catch (Exception e) {
			LOGGER.error("创建SSLClient失败", e);
		}
		return HttpClients.createDefault();
	}

	public static SSLConnectionSocketFactory getSSLConnSocketFactory(String protocol)
			throws NoSuchAlgorithmException, KeyManagementException {
		if (sslConnSocketFactory != null) {
			return sslConnSocketFactory;
		}

		SSLContext sslContext = SSLContext.getInstance(protocol);
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
		sslConnSocketFactory = new SSLConnectionSocketFactory(sslContext);

		return sslConnSocketFactory;
	}

	public static void main(String[] args) {
		String url = "https://www.baidu.com/s?ie=UTF-8&wd=";
		try {
			// HttpClientUtil.doPost(null);
			Date start = new Date();
			for (int i = 0; i < 50; i++) {
				HttpClientUtil.doGet(url + i);
			}
			Date end = new Date();
			System.out.println("无连接池总耗时：" + (end.getTime() - start.getTime()));

			Date start2 = new Date();
			for (int i = 50; i < 100; i++) {
				HttpClientUtil.doGetUsePool(url + i);
			}
			Date end2 = new Date();
			System.out.println("使用连接池总耗时：" + (end2.getTime() - start2.getTime()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
