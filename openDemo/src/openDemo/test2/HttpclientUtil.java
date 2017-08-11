package openDemo.test2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * HttpclientUtil
 */
public class HttpclientUtil {
	private static final Logger LOGGER = LogManager.getLogger(HttpclientUtil.class);
	private static final String WXCERTPATH = "/weixinpay/cert/";

	/**
	 * Hide Utility Class Constructor
	 */
	private HttpclientUtil() {
		// do nothing
	}

	/**
	 * post
	 *
	 * @param entity
	 *            HttpEntity
	 * @param url
	 *            String
	 * @return String
	 */
	public static String get(String url) {
		String responseBody = null;
		HttpGet get = new HttpGet(url);
		// post.setEntity(entity);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			responseBody = httpclient.execute(get, getResponseHandler());
		} catch (IOException e) {
			LOGGER.error("httpclient.execute() error:", e);
		}
		return responseBody;
	}

	/**
	 * post
	 *
	 * @param entity
	 *            HttpEntity
	 * @param url
	 *            String
	 * @return String
	 */
	public static String post(HttpEntity entity, String url) {
		String responseBody = null;
		if (entity != null) {
			HttpPost post = new HttpPost(url);
			post.setEntity(entity);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			try {
				responseBody = httpclient.execute(post, getResponseHandler());
			} catch (IOException e) {
				LOGGER.error("httpclient.execute() error:", e);
			}
		}
		return responseBody;
	}

	/**
	 * post
	 *
	 * @param headers
	 *            Header[]
	 * @param entity
	 *            HttpEntity
	 * @param url
	 *            String
	 * @return String
	 */
	public static String post(Header[] headers, HttpEntity entity, String url) {
		String responseBody = null;
		if (entity != null) {
			HttpPost post = new HttpPost(url);
			post.setEntity(entity);
			post.setHeaders(headers);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			try {
				responseBody = httpclient.execute(post, getResponseHandler());
			} catch (IOException e) {
				LOGGER.error("httpclient.execute() error:", e);
			}
		}
		return responseBody;
	}

	/**
	 * sslpost
	 *
	 * @param entity
	 *            HttpEntity
	 * @param url
	 *            String
	 * @return String
	 */
	public static String sslpost(HttpEntity entity, String url) {
		String responseBody = null;
		if (entity != null) {
			HttpPost post = new HttpPost(url);
			post.setEntity(entity);
			HttpClient httpclient = getSslClient();
			try {
				responseBody = httpclient.execute(post, getResponseHandler());
			} catch (Exception e) {
				LOGGER.error("httpclient.execute() error:", e);
			}
		}
		return responseBody;
	}

	/**
	 * sslpost
	 *
	 * @param headers
	 *            Map
	 * @param entity
	 *            HttpEntity
	 * @param url
	 *            String
	 * @return String
	 */
	public static String sslpost(Map<String, String> headers, HttpEntity entity, String url) {
		String responseBody = null;
		if (StringUtils.isNotBlank(url)) {
			HttpPost post = new HttpPost(url);
			if (headers != null && !headers.isEmpty()) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			if (entity != null) {
				post.setEntity(entity);
			}
			HttpClient httpclient = getSslClient();
			try {
				responseBody = httpclient.execute(post, getResponseHandler());
			} catch (Exception e) {
				LOGGER.error("httpclient.execute() error:", e);
			}
		}
		return responseBody;
	}

	/**
	 * dosslpost
	 *
	 * @param headers
	 *            Map
	 * @param url
	 *            String
	 * @return String
	 */
	public static String dosslpost(Map<String, String> headers, String url) {
		String responseBody = null;
		if (StringUtils.isNotBlank(url)) {
			HttpPost post = new HttpPost(url);
			if (!headers.isEmpty()) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpClient httpclient = getSslClient();
			try {
				responseBody = httpclient.execute(post, getResponseHandler());
			} catch (Exception e) {
				LOGGER.error("httpclient.execute() error:", e);
			}
		}
		return responseBody;
	}

	/**
	 * sslget
	 *
	 * @param url
	 *            String
	 * @return String
	 */
	public static String sslget(String url) {
		String responseBody = null;
		HttpGet get = new HttpGet(url);
		HttpClient httpclient = getSslClient();
		try {
			responseBody = httpclient.execute(get, getResponseHandler());
		} catch (IOException e) {
			LOGGER.error("httpclient.execute() error:", e);
		}
		return responseBody;
	}

	/**
	 * sslgetResponse
	 *
	 * @param url
	 *            String
	 * @return HttpResponse
	 */
	public static HttpResponse sslgetResponse(String url) {
		HttpGet get = new HttpGet(url);
		HttpClient httpclient = getSslClient();
		HttpResponse response = null;
		try {
			response = httpclient.execute(get);
		} catch (IOException e) {
			LOGGER.error("httpclient.execute() error:", e);
		}
		return response;
	}

	private static HttpClient getSslClient() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(ctx,
					new NoopHostnameVerifier());
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslConnectionSocketFactory).build();
			PoolingHttpClientConnectionManager mgr = new PoolingHttpClientConnectionManager(registry);
			return HttpClientBuilder.create().setConnectionManager(mgr).build();
		} catch (Exception e) {
			LOGGER.error("getSslClient() error:", e);
			return HttpClients.createDefault();
		}
	}

	/**
	 * weixinPaySslPost
	 *
	 * @param mchId
	 *            String
	 * @param entity
	 *            HttpEntity
	 * @param url
	 *            String
	 * @return String
	 */
	public static String weixinPaySslPost(String url, String mchId, String xml) {
		String responseBody = null;
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(xml, "UTF-8"));
		try {
			HttpClient httpclient = getWxpaySslClient(mchId);
			responseBody = httpclient.execute(post, getResponseHandler());
		} catch (Exception e) {
			LOGGER.error("weixinPaySslPost() error:", e);
		}
		return responseBody;
	}

	private static HttpClient getWxpaySslClient(String mchId) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = FileUtils.openInputStream(new File(WXCERTPATH + mchId + ".p12"));
		try {
			keyStore.load(instream, mchId.toCharArray());
		} finally {
			IOUtils.closeQuietly(instream);
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				new DefaultHostnameVerifier());
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

	/**
	 * getHttpEntity
	 *
	 * @param params
	 *            List
	 * @return HttpEntity
	 */
	public static HttpEntity getHttpEntity(List<NameValuePair> params) {
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("post.setEntity() error:", e);
		}
		return entity;
	}

	/**
	 * getHttpEntity
	 *
	 * @param bean
	 *            Object
	 * @return HttpEntity
	 */
	public static HttpEntity getHttpEntity(Object bean) {
		return null;// new StringEntity(BeanHelper.bean2Json(bean), "UTF-8");
	}

	private static ResponseHandler<String> getResponseHandler() {
		return new ResponseHandler<String>() {
			@Override
			public String handleResponse(final HttpResponse response) throws IOException {
				int status = response.getStatusLine().getStatusCode();
				String result = null;
				if (status >= 200 && status <= 500 && response.getEntity() != null) {
					result = EntityUtils.toString(response.getEntity(), "UTF-8");
				}
				return result;
			}
		};
	}

	/**
	 * sendPostForCreateTask
	 *
	 * @param url
	 *            String
	 * @param userId
	 *            String
	 * @param orgId
	 *            String
	 * @param fullName
	 *            String
	 */
	public static void sendPostForCreateTask(int sourceId, String url, String userId, String orgId, String fullName) {
		HttpPost post = new HttpPost(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("OrgID", orgId);
		jsonParam.put("MasterID", sourceId + "");
		if (511 == sourceId) {
			jsonParam.put("MasterName", "IM Login");
		} else if (506 == sourceId) {
			jsonParam.put("MasterName", "H5 Login");
		}
		jsonParam.put("CreateUserID", userId);
		jsonParam.put("CreateUserName", fullName);
		jsonParam.put("CreateDate", new Date());// CommonUtil.now()
		jsonParam.put("apikey", "ELearning");
		jsonParam.put("salt", "939500325685895");
		jsonParam.put("signature", "1e55406904a1c7de72833775659d9e017273691a9bcb04d2bf7bcf794823b737");
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		try {
			httpclient.execute(post);
		} catch (Exception e) {
			LOGGER.error("httpclient.execute() error:", e);
		}
	}

	public static String sendPostForCreateOrg(String apiKey, String salt, String signature, String url, String orgName,
			String domainName, String mobile, String industryId, String industryName, String inviteID) {
		HttpPost post = new HttpPost(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("orgName", orgName);
		jsonParam.put("domainName", domainName);
		jsonParam.put("mobile", mobile);
		jsonParam.put("industryID", industryId);
		jsonParam.put("industryName", industryName);
		if (StringUtils.isNotBlank(inviteID)) {
			jsonParam.put("inviteID", inviteID);
		}
		jsonParam.put("apikey", apiKey);
		jsonParam.put("salt", salt);
		jsonParam.put("signature", signature);
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		String reponseStr = "";
		try {
			CloseableHttpResponse response = httpclient.execute(post);
			reponseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (IOException e) {
			LOGGER.error("sendPostForCreateOrg error:", e);
		} catch (Exception e) {
			LOGGER.error("sendPostForCreateOrg error:", e);
		}
		return reponseStr;
	}

	public static String sendPostForVoiceCaptcha(String url, String mobile, String captcha) {
		HttpPost post = new HttpPost(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("numbers", mobile);
		jsonParam.put("content", captcha);
		jsonParam.put("apikey", "Lecai");
		jsonParam.put("salt", "20160120");
		jsonParam.put("signature", "cee64f63e2fdf6abe6aeca55b3d91e0f6e207b555b3046d990bb342759e13065");
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		String reponseStr = "";
		try {
			CloseableHttpResponse response = httpclient.execute(post);
			reponseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			LOGGER.error("sendPostForCreateOrg error:", e);
		}
		return reponseStr;
	}

}
