package cn.cemobile.infointerface.util;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtil {

	private HttpUtil() {
	}

	/** 单例的HttpClient,典型的单例模式，构造方法private，调用getHttpClient返回的只有一个对象 */
	public static DefaultHttpClient httpClient;
	public static CookieStore cookieStore;// 定义一个Cookie来保存session

	// private static final String TAG = "HttpUtil";

	public static DefaultHttpClient getHttpClient() {

		// 这个经常来设置超时时间
		BasicHttpParams httpParams = new BasicHttpParams();
		// 从ConnectionManager管理的连接池中取出连接的超时时间
		ConnManagerParams.setTimeout(httpParams, 1000);
		// 连接超时(通过网络与服务器建立连接的超时时间)
		HttpConnectionParams.setConnectionTimeout(httpParams, CommonData.HTTP_TIMEOUT_CONNECT);
		// 请求超时(从服务器获取响应数据需要等待的时间)
		HttpConnectionParams.setSoTimeout(httpParams, CommonData.HTTP_TIMEOUT_SOCKET);

		// 维持一组协议
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		// 使用线程安全的连接管理来创建HttpClient
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

		httpClient = new DefaultHttpClient(cm, httpParams);
		return httpClient;

	}

	/**
	 * 通用的get请求方法，用HttpClient实现
	 * 
	 * @param url
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String httpGet(String url) throws IllegalStateException, IOException {
		HttpClient httpClient = getHttpClient();
		HttpGet httpRequestGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpRequestGet);
		HttpEntity entity = response.getEntity();
		// 从返回报文的实体中获取输入流
		// InputStream is = entity.getContent();
		String resultData = EntityUtils.toString(entity);
		return resultData;
		// return is;
	}

	/**
	 * 通用的get请求方法，用HttpClient实现,键值对以HashMap方式传入
	 * 
	 * @param url
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String httpGet(String url, HashMap<String, String> hm) throws IllegalStateException, IOException {
		Iterator<Entry<String, String>> itr = hm.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; itr.hasNext(); i++) {
			if (i == 0)
				sb.append("?");
			if (i > 0)
				sb.append("&");
			sb.append(itr.next().toString());
		}
		String currentTime = System.currentTimeMillis() + "";
		String watchword = Md5Crypt.md5Crypt((currentTime + CommonData.SECRET).getBytes());
		sb.append("&time=" + currentTime + "&watchword=" + watchword);
		return httpGet(url + sb.toString());
	}

	/**
	 * 通用的post方法
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String httpPost(String url, HashMap<String, String> map) throws ClientProtocolException, IOException {
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);

		httpPost.addHeader("Connection", "Close");
		httpPost.addHeader("Session", HeadUtil.getSession());// 传入sessionid。
		httpPost.addHeader("PhoneMode", android.os.Build.MODEL);// 传入当前手机品牌
		httpPost.addHeader("IMEI", HeadUtil.getDeviceId());// 传入当前手机串号
		httpPost.addHeader("ClientType", "1");// 1 Android 2 IOS
		httpPost.addHeader("Version", "1.0");// 传入客户端版本
		httpPost.addHeader("OS", android.os.Build.VERSION.RELEASE);// 设备系统类型
																	// android
																	// 4.2/ios
																	// 7.0
		httpPost.addHeader("MAC", HeadUtil.getMac());// 设备的MAC地址
		httpPost.addHeader("Resolution", HeadUtil.getResolution());// 设备的分辨率

		httpPost.addHeader("UserType", HeadUtil.getUserType());// 1 user 2 lawer
		httpPost.addHeader("UserID", HeadUtil.getUserId());// 1
															// userID，与lawerID互斥
		httpPost.addHeader("LawerID", HeadUtil.getLawyerId());// 1
																// userID，与lawerID互斥

		List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
		Iterator<Map.Entry<String, String>> i = map.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<String, String> o = i.next();
			nameValuePair.add(new BasicNameValuePair(o.getKey().toString(), o.getValue().toString()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "utf-8"));
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		if (response.getHeaders("Session") != null) {
			String session = response.getHeaders("Session")[0].getValue();
			HeadUtil.setSession(session);
		}
		String resultData = EntityUtils.toString(entity);
		return resultData;
	}

	/**
	 * 报文模式的http请求(post)
	 * 
	 */
	public static String httpPost_baowen(String url, String content) throws ClientProtocolException, IOException {
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new StringEntity(content, "utf-8"));
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		String resultData = EntityUtils.toString(entity);
		Log.i("Mark", "1");
		return resultData;
	}

	/**
	 * 用post上传文件的方法
	 */
	public static void uploadFile(String actionUrl, String filepath) {
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data");
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(filepath);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 ，开始请求 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			System.out.println(b.toString());
			/* 关闭DataOutputStream */
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
