package com.ktc.tvlauncher.utils;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.util.Log;

/**
 * 
 * 
 * @ClassName: HttpUtil
 * 
 * @Description: 工具类，用来进行网络访问
 * 
 * @author Nathan.Liao
 * 
 * @date 2013-3-19 上午10:14:23
 */
public class HttpUtil {
	private static HttpUtil httpUtilInstance = null;
	
	public HttpUtil(Context context) {
	}

	public static HttpUtil getInstance(Context context) {
		if (httpUtilInstance == null) {
			httpUtilInstance = new HttpUtil(context);
		}
		return httpUtilInstance;
	}
	public HttpUtil() {
	    
    }

	/**
	 * 
	 * @ param url_str @ return String
	 */
	public String getJsonContent(String url_str) {
		HttpGet httpRequest = new HttpGet(url_str);

		/**
		 * params用于设定网络访问的属性
		 */
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 3000);
		HttpClient httpclient = new DefaultHttpClient(params);
		try {
			HttpResponse response = httpclient.execute(httpRequest);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String content = EntityUtils.toString(response.getEntity(),"UTF-8");
				return content;
			}
			else
			{

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.v("TimeOut", "net fail connect!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.v("TimeOut", "net fail connect!");
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return null;
	}
}
