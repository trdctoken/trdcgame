package org.trdc.announcement.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;

/**
 * Author Ibrahim Jamali
 */
public class DefaultClient implements Client {

	private HttpClient client;
	private String cookieFolderName;

	public DefaultClient(String cookieFolderName) throws IOException {
		Path cookieFolder = Paths.get(cookieFolderName);
		if (!Files.exists(cookieFolder))
			Files.createDirectories(cookieFolder);
		this.cookieFolderName = cookieFolderName;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(40 * 1000)
				.setConnectionRequestTimeout(40 * 1000).setSocketTimeout(40 * 1000).setCookieSpec(CookieSpecs.STANDARD)
				.build();
		client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
				.setDefaultRequestConfig(requestConfig).build();
	}

	public DefaultClient() throws IOException {
		this("cookies");
	}

	@SuppressWarnings("unchecked")
	public Response doPost(String url, String referer, Object params, Map<String, String> headers, String cookieFile)
			throws Exception {
		HttpPost request = new HttpPost(url);
		Path cookie = Paths.get(this.cookieFolderName, cookieFile);
		if (headers != null)
			for (String key : headers.keySet()) {
				request.setHeader(key, headers.get(key));
			}
		if (params != null)
			if (params instanceof String)
				request.setEntity(new StringEntity(params.toString()));
			else
				request.setEntity(new UrlEncodedFormEntity((List<NameValuePair>) params, "UTF-8"));
		HttpClientContext context = HttpClientContext.create();
		if (cookieFile != null && !cookieFile.equals("") && Files.exists(cookie)) {
			context.setCookieStore(Util.getCookieStore(cookie));
		}
		HttpResponse response = client.execute(request, context);
		if (cookieFile != null && !cookieFile.equals("") && Files.exists(cookie)) {
			Util.setCookieStore(cookie, context.getCookieStore());
		}
		String content = "";
		if (response.getEntity() != null)
			content = EntityUtils.toString(response.getEntity());

		Map<String, String> asList = new TreeMap<String, String>();
		for (Header header : response.getAllHeaders()) {
			asList.put(header.getName(), header.getValue());
		}
		request.releaseConnection();
		return new Response(content, asList);
	}

	/**
	 * 
	 * @param url
	 * @param referer
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public Response doGet(String url, String referer, Map<String, String> headers, String cookieFile) throws Exception {
		HttpGet request = new HttpGet(url);
		Path cookie = Paths.get(this.cookieFolderName, cookieFile);
		if (headers != null)
			for (String key : headers.keySet()) {
				request.setHeader(key, headers.get(key));
			}
		HttpClientContext context = HttpClientContext.create();
		if (cookieFile != null && !cookieFile.equals("") && Files.exists(cookie)) {
			context.setCookieStore(Util.getCookieStore(cookie));
		}
		HttpResponse response = client.execute(request, context);
		if (cookieFile != null && !cookieFile.equals("")) {
			Util.setCookieStore(cookie, context.getCookieStore());
		}
		String content = EntityUtils.toString(response.getEntity());
		request.releaseConnection();
		Map<String, String> asList = new TreeMap<String, String>();
		for (Header header : response.getAllHeaders()) {
			asList.put(header.getName(), header.getValue());
		}
		Response res = new Response(content, asList);
		request.releaseConnection();
		return res;
	}

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}

	@Override
	public Response doPost(String url, String referer, Object data, Map<String, String> headers) throws Exception {
		return this.doPost(url, referer, data, headers, "");
	}

	@Override
	public Response doGet(String url, String referer, Map<String, String> headers) throws Exception {
		return this.doGet(url, referer, headers, "");
	}

	public String getCookieFolderName() {
		return cookieFolderName;
	}

}
