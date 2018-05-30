package com.ktc.tvlauncher.update;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ktc.tvlauncher.tencent.bean.TencentAppsReInfo;
import com.ktc.tvlauncher.utils.Constant;
import com.ktc.tvlauncher.utils.HttpUtil;
import com.ktc.tvlauncher.utils.Logger;
import com.ktc.tvlauncher.utils.StringTool;
import com.ktcp.mta.sdk.KtcpMtaSdk;
import java.net.URL;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import android.util.Log;
import com.ktc.tvlauncher.tencent.bean.TencentAppsReInfo;

public class UpdateAppData implements Runnable {
	private Context context;
	private Handler handler;
	private boolean isBootUpdate;
	private int validUrlNum = 0;
	public UpdateAppData(Context context, Handler handler, boolean isBootUpdate) {
		this.context = context;
		this.handler = handler;
		this.isBootUpdate = isBootUpdate;
	}

	@Override
	public void run() {

		SharedPreferences sp = context.getSharedPreferences(
				Constant.SAVE_TENCENT_APPSINFO, context.MODE_PRIVATE);

		HttpUtil httpUtil = new HttpUtil();
		
		 String guid = KtcpMtaSdk.getBoxGuid(context);
	      String qua = KtcpMtaSdk.getBoxQua(context,"LAUNCHER");
	      String guidEncode = Constant.DEFAULT_TENCENT_GUID;
	      String quaEncode = Constant.DEFAULT_TENCENT_QUA;
	        try {
				if ((guid != null) && !("".equals(guid))){
					guidEncode = URLEncoder.encode(guid, "UTF-8");
					Log.d("Jason","guidEncode = " + guidEncode);
				}
				if ((qua != null) && !("".equals(qua))){
					quaEncode = URLEncoder.encode(qua, "UTF-8");
					Log.d("Jason","quaEncode = " + quaEncode);
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	       // String baseurl = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=PHILIPS_LAUNCHER&channel_selector=launcher&content_selector=all&pictures=pic_354x354%2bpic_496x722";
	        String appurl = Constant.APP_BASE_URL + "&Q-UA=" + quaEncode + "&guid=" + guidEncode;
		Log.d("Jason","appurl = " + appurl);
		String content = httpUtil
				.getJsonContent(appurl);
		if (content != null) {
			TencentAppsReInfo tencentAppInfo = StringTool.getTencentAppInfo(content);
			if (tencentAppInfo != null) {
				String[] titile=tencentAppInfo.getTitle();
	            String[] uri=tencentAppInfo.getUri();
	            String[] description=tencentAppInfo.getDescription();
	            String[] pic_710_350=tencentAppInfo.getPic_710_350();
	            String[] year=tencentAppInfo.getPresent_year();
	            String[] item_id=tencentAppInfo.getItem_id();
	            String[] director=tencentAppInfo.getDirectors();
	            String[] viewPoint=tencentAppInfo.getS_title();
	            String[] actor=tencentAppInfo.getActor();

				boolean isSucees = true;
				for (int i = 0; i < 5; i++) {
					try {
						if (pic_710_350[i] != null) {
							String url = pic_710_350[i];
							 FileOutputStream fos=new FileOutputStream("data/data/com.ktc.tvlauncher/files/appsImages/"+ i + ".jpg");
							isSucees = downImage(fos, url);
							
						}
					} catch (FileNotFoundException e) {
						isSucees = false;
						// Log.e(Constants.TAG,
						// "downImage false!!!!!!!!!!!!!!!");
						break;
					}
				}
				if (isSucees) {
					// Log.i(Constants.TAG, "downImage Sucess");
					SharedPreferences.Editor editor = context
							.getSharedPreferences(Constant.SAVE_TENCENT_APPSINFO,
									context.MODE_PRIVATE).edit();
					for (int i = 0, j = 0; i < pic_710_350.length; i++) {
						
						if (pic_710_350[i] != null) {							
							validUrlNum++;
							editor.putString("app_image_"+j, i + ".jpg");
							editor.putString("app_uri_"+j, uri[i]);
							editor.putString("app_name_"+j, titile[i]);
							editor.putString("app_year_" + j, year[i]);
							editor.putString("app_item_id_" + j,item_id[i]);
							editor.putString("app_director_"+j, director[i]);
							editor.putString("app_description_"+j, description[i]);
							j++;
						}
					}
					editor.putInt("movie_valid_url_num", validUrlNum);
					SharedPreferences.Editor tvconfig = context
							.getSharedPreferences(Constant.TV_CONFIG,
									context.MODE_PRIVATE).edit();
					// 设置刷新周期为1天
					long validTime = System.currentTimeMillis();
					validTime = validTime + 1 * 24 * 60 * 60 * 1000;
					//validTime = validTime + 1;
					Log.d("Jason","更新App数据成功validTime = " + validTime);
					tvconfig.putLong("movie_validTime", validTime);
					tvconfig.commit();
					editor.putBoolean("tencent_app_update", true);
					editor.commit();
				}
				else{
					//没有更新成功
					Log.d("Jason","更新Apo数据不成功validTime = ");
					SharedPreferences.Editor editor = context
							.getSharedPreferences(Constant.SAVE_TENCENT_APPSINFO,
									context.MODE_PRIVATE).edit();
					editor.putBoolean("tencent_app_update", false);
					editor.commit();
				}
			}
		}

		if (content == null){
			if (isBootUpdate == false){
			  Message msg=handler.obtainMessage(Constant.NETWORK_TIMEOUT);
			  handler.sendMessage(msg);
			}
		}
		else{
			if (isBootUpdate == false){
			  Message msg=handler.obtainMessage(Constant.NETWORK_SUCESS);
			  handler.sendMessage(msg);	
			}
		}
	}

	public String getPicName(String pic) {
		String[] picStr = pic.split("/");
		return picStr[picStr.length - 1];
	}

	public boolean downImage(FileOutputStream fos, String imageUrl) {
		try {
			URL myFileUrl = new URL(imageUrl);
			InputStream in = myFileUrl.openStream();
			int len = -1;
			byte[] data = new byte[1024];
			while ((len = in.read(data)) != -1) {
				fos.write(data, 0, len);
				fos.flush();
			}
			in.close();
			fos.close();
			return true;
		} catch (IOException e) {
			// Log.i(Constants.TAG,
			// "--------------downImage false-------------");
			return false;
		}
	}
}
