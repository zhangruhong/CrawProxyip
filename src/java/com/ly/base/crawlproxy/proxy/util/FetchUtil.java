package com.ly.base.crawlproxy.proxy.util;

/**
 * Creation Date:2009-12-24
 * 
 * Copyright 2008-2010 © 同程网 Inc. All Rights Reserved
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import sun.net.www.protocol.http.HttpURLConnection;

import com.tongcheng.lib.database.sqlserver.DBController;
import com.tongcheng.lib.database.sqlserver.DbUtil;
import com.usousou.crawl.hps.bean.HttpProxy;
import com.usousou.crawl.util.UrlUtil;

/**
 *获取网页内容工具类<br>
 */
public class FetchUtil {
	private static final String HTTP_AGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; iCafeMedia; GTB6; .NET CLR 1.1.4322; .NET CLR 2.0.50727; CIBA)";

	/**
	 * 获得HttpClient
	 * 
	 * @param httpProxy
	 *            代理
	 * @param ctimeout
	 *            连接超时
	 * @param stimeout
	 *            读写超时
	 * @param conectCharSet
	 *            编码方式
	 * @return
	 */
	private static HttpClient getHttpClient(HttpProxy httpProxy, int ctimeout, int stimeout, String conectCharSet) {
		HttpClient client = new HttpClient();
		HttpConnectionManagerParams params = client.getHttpConnectionManager().getParams();
		params.setParameter("http.useragent", HTTP_AGENT);
		params.setConnectionTimeout(ctimeout);
		params.setSoTimeout(stimeout);
		params.setDefaultMaxConnectionsPerHost(200);
		params.setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, conectCharSet);
		HostConfiguration hostConf = client.getHostConfiguration();
		if (null != httpProxy) {// 设置代理
			hostConf.setProxy(httpProxy.getIp(), httpProxy.getPort());
			if (null != httpProxy.getProxyUserName()) {// 代理认证
				client.getState().setProxyCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM), new UsernamePasswordCredentials(httpProxy.getProxyUserName(), httpProxy.getProxyPwd()));
			}
		}
		return client;
	}

	/**
	 * @param httpProxy
	 *            代理
	 * @param pageUrl
	 *            当前访问页面地址
	 * @param ctimeout
	 *            连接超时
	 * @param stimeout
	 *            读写超时
	 * @param location
	 *            跳转次数
	 * @param charset
	 *            页面正文编码方式
	 * @param startMark
	 *            读取开始标志，可以为空
	 * @param endMark
	 *            读取结束标志，可以为空
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private static String getPageContent(HttpProxy httpProxy, String pageUrl, int ctimeout, int stimeout, int location, String charset, String startMark, String endMark) throws HttpException, IOException {
		StringBuffer sb = new StringBuffer();
		HttpClient client = getHttpClient(httpProxy, ctimeout, stimeout, charset);
		GetMethod getMethod = new GetMethod(pageUrl);
		getMethod.setRequestHeader("Connection", "Close");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, true));
		if (null != charset) {
			getMethod.getParams().setContentCharset(charset);
		}
		try {
			long connectStart = System.currentTimeMillis();
			int statusCode = client.executeMethod(getMethod);
			long connectEnd = System.currentTimeMillis();
			if (null != httpProxy) {
				httpProxy.setResponseTime(connectEnd - connectStart);
			}
			if (statusCode == HttpStatus.SC_OK) {
				InputStream iStream = getMethod.getResponseBodyAsStream();
				if (null == iStream)
					return null;
				BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream, charset));
				String str = null;
				boolean start = false;
				while (null != (str = bReader.readLine())) {
					if (null == startMark || str.indexOf(startMark) >= 0) {
						start = true;
					}
					if (start)
						sb.append(str).append(System.getProperty("line.separator"));
					if (start && null != startMark && str.indexOf(endMark) >= 0) {
						break;
					}
				}
				bReader.close();
				bReader = null;
				iStream.close();
				iStream = null;
				getMethod.abort();
				getMethod.releaseConnection();
				getMethod.releaseConnection();
			} else if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				if (location > 0) {
					Header locationHeader = getMethod.getResponseHeader("location");
					if (null != locationHeader) {
						String locationedUrl = UrlUtil.getFullUrl(pageUrl, locationHeader.getValue());
						if (null != locationedUrl) {
							return getPageContent(httpProxy, pageUrl, ctimeout, stimeout, location - 1, charset, startMark, endMark);
						}
					}
				}
			} else {
				return null;
			}
		} catch (java.lang.NullPointerException e) {
			System.out.println("java.lang.IllegalArgumentException: port out of range:80831");
		} catch (java.lang.IllegalStateException e) {
			System.out.println("java.lang.IllegalStateException");
		} finally {
			if (!getMethod.isAborted()) {
				getMethod.releaseConnection();
				((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
			}
		}
		return sb.toString();
	}

	/**
	 * @param httpProxy
	 *            代理
	 * @param pageUrl
	 *            当前访问页面地址
	 * @param ctimeout
	 *            连接超时
	 * @param stimeout
	 *            读写超时
	 * @param location
	 *            跳转次数
	 * @param retry
	 *            出错重试次数
	 * @param charset
	 *            页面正文编码方式
	 * @param startMark
	 *            读取开始标志，可以为空
	 * @param endMark
	 *            读取结束标志，可以为空
	 * @return
	 */
	public static String getContent(HttpProxy httpProxy, String pageUrl, int ctimeout, int stimeout, int location, int retry, String charset, String startMark, String endMark) {
		String content = null;
		while (null == content && retry-- > 0) {
			try {
				content = getPageContent(httpProxy, pageUrl, ctimeout, stimeout, location, charset, startMark, endMark);
			} catch (HttpException e) {
			} catch (IOException e) {
			}
		}
		return content;
	}

	/**
	 * 
	 * @param pageUrl
	 *            当前访问页面地址
	 * @param ctimeout
	 *            连接超时
	 * @param stimeout
	 *            读写超时
	 * @param location
	 *            跳转次数
	 * @param charset
	 *            页面正文编码方式
	 * @param retry
	 *            出错重试次数
	 * @param startMark
	 *            读取开始标志，可以为空
	 * @param endMark
	 *            读取结束标志，可以为空
	 * @return
	 */
	public static String getContent(String pageUrl, int ctimeout, int stimeout, int location, String charset, int retry, String startMark, String endMark) {
		return getContent(null, pageUrl, ctimeout, stimeout, location, retry, charset, startMark, endMark);
	}

	/**
	 * 获取网页数据
	 * 
	 * @param pageUrl
	 * @param checkStr
	 *            该字符串用来检查网页是否打开
	 * @param retryTimes
	 *            出错时最大重试次数
	 * @return
	 * @throws IOException
	 */
	public static String getContent(String pageUrl, String checkStr, int retryTimes) throws IOException {
		for (int i = 0; i < Math.min(3, retryTimes); i++) {
			HttpURLConnection httpurlconnection = null;
			try {
				URL url = new URL(pageUrl);

				httpurlconnection = (HttpURLConnection) url.openConnection();
				httpurlconnection.setDoInput(true);
				httpurlconnection.setDoOutput(true);

				httpurlconnection.setReadTimeout(50000);
				httpurlconnection.setConnectTimeout(50000);

				int code = httpurlconnection.getResponseCode();
				if (code == 200) {
					DataInputStream in = new DataInputStream(httpurlconnection.getInputStream());
					ByteArrayOutputStream data = new ByteArrayOutputStream();
					int num = 0;
					byte[] buffer = new byte[1024 * 10];
					while ((num = in.read(buffer)) > 0) {
						data.write(buffer, 0, num);
					}
					in.close();
					String str = new String(data.toByteArray(), "UTF-8");
					if (null == checkStr || str.contains(checkStr)) {
						return str;
					}
				}
			} finally {
				if (httpurlconnection != null) {
					httpurlconnection.disconnect();
				}
			}
		}
		return null;
	}

	public static String upAddr = "";

	public static String getData(String url) {

		String testProxy = "http://www.usousou.com/test/ip_a.jsp";

		DBController db = DbUtil.getDbLink("TCUssCommon", "61.155.159.230:51158", "TCUssCommon", "ER$%TFE");

		String sql = "select * FROM [TCUssCommon].[dbo].[ussProxies] with(nolock)";
		try {
			db.executeQuery(sql);
			ResultSet rs = db.getRs();
			while (rs.next()) {

				HttpProxy httpProxy = new HttpProxy();
				httpProxy.setIp(rs.getString("ip"));
				httpProxy.setPort(rs.getInt("port"));

				String content = FetchUtil.getContent(httpProxy, "http://www.usousou.com/test/ip_a.jsp", 80000, 80000, 2, 2, "GBK", "<title>", "</center>");
				System.out.println(content);
				System.out.println(httpProxy.getResponseTime());
				if (null == content || content.contains("58.210.35.50")) {
					System.out.println("该地址不符合！");
				} else {
					System.out.println("该地址符合要求！");
					if (!upAddr.equals(rs.getString("ip"))) {
						upAddr = rs.getString("ip");
						return FetchUtil.getContent(httpProxy, url, 10000, 10000, 1, 2, "GBK", "", "");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		String testProxy = "http://www.usousou.com/test/ip_a.jsp";

		DBController db = DbUtil.getDbLink("TCUssCommon", "61.155.159.230:51158", "TCUssCommon", "ER$%TFE");

		String sql = "select * FROM [TCUssCommon].[dbo].[ussProxies] with(nolock)";
		try {
			db.executeQuery(sql);
			ResultSet rs = db.getRs();
			while (rs.next()) {

				HttpProxy httpProxy = new HttpProxy();
				httpProxy.setIp(rs.getString("ip"));
				httpProxy.setPort(rs.getInt("port"));

				String content = FetchUtil.getContent(httpProxy, "http://www.usousou.com/test/ip_a.jsp", 80000, 80000, 2, 2, "GBK", "<html>", "</html>");
				System.out.println(content);
				System.out.println(httpProxy.getResponseTime());
				if (null != content && content.contains("58.210.35.50")) {
					System.out.println("该地址不符合！");
				} else {
					System.out.println("该地址符合要求！");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
