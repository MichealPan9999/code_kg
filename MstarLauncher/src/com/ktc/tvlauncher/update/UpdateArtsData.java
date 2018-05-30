
package com.ktc.tvlauncher.update;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ktc.tvlauncher.tencent.bean.TencentArtsReInfo;
import com.ktc.tvlauncher.utils.Constant;
import com.ktc.tvlauncher.utils.HttpUtil;
import com.ktc.tvlauncher.utils.Logger;
import com.ktc.tvlauncher.utils.StringTool;
import com.ktcp.mta.sdk.KtcpMtaSdk;
import java.net.URL;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import android.util.Log;
import com.ktc.tvlauncher.tencent.bean.TencentArtsReInfo;
public class UpdateArtsData implements Runnable {
	private Context context;
	private Handler handler;
	private boolean isBootUpdate;

	public UpdateArtsData(Context context, Handler handler, boolean isBootUpdate) {
		this.context = context;
		this.handler = handler;
		this.isBootUpdate = isBootUpdate;
	}

	@Override
	public void run() {

		SharedPreferences sp = context.getSharedPreferences(
				Constant.SAVE_TENCENT_ARTSINFO, context.MODE_PRIVATE);

		HttpUtil httpUtil = new HttpUtil();
		 String guid = KtcpMtaSdk.getBoxGuid(context);
	      String qua = KtcpMtaSdk.getBoxQua(context,"LAUNCHER");
	      String guidEncode = Constant.DEFAULT_TENCENT_GUID;
	      String quaEncode = Constant.DEFAULT_TENCENT_QUA;
	        try {
				if ((guid != null) && !("".equals(guid))){
					guidEncode = URLEncoder.encode(guid, "UTF-8");
					//Log.d("Jason","guidEncode = " + guidEncode);
				}
				if ((qua != null) && !("".equals(qua))){
					quaEncode = URLEncoder.encode(qua, "UTF-8");
					//Log.d("Jason","quaEncode = " + quaEncode);
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	       // String baseurl = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=PHILIPS_LAUNCHER&channel_selector=launcher&content_selector=all&pictures=pic_354x354%2bpic_496x722";
	        String arturl = Constant.ART_BASE_URL + "&Q-UA=" + quaEncode + "&guid=" + guidEncode;
		
	        Log.d("Jason","arturl = " + arturl);
		
		String content = httpUtil
				.getJsonContent(arturl);
		if (content != null) {
			TencentArtsReInfo tencentArtInfo = StringTool.getTencentArtInfo(content);
			if (tencentArtInfo != null) {
				String[] titile=tencentArtInfo.getTitle();
	            String[] uri=tencentArtInfo.getUri();
	            String[] description=tencentArtInfo.getDescription();
	            String[] pic_354_354=tencentArtInfo.getPic_354_354();
	            String[] year=tencentArtInfo.getPresent_year();
	            String[] item_id=tencentArtInfo.getItem_id();
	            String[] director=tencentArtInfo.getDirectors();
	            String[] viewPoint=tencentArtInfo.getS_title();
	            String[] actor=tencentArtInfo.getActor();
		
				boolean isSucees = true;
				for (int i = 0; i < 10; i++) {
					try {
						if (pic_354_354[i] != null) {
/*							FileOutputStream fos = context
									.openFileOutput(getPicName(picUrl[i]),
											context.MODE_PRIVATE);
							// FileOutputStream fos=new
							// FileOutputStream("data/data/com.ktc.launcher/files/"+getPicName(pic[i]));
*/		
							 FileOutputStream fos=new FileOutputStream("data/data/com.ktc.tvlauncher/files/artsImages/"+i+".jpg");
							isSucees = downImage(fos, pic_354_354[i]);
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
							.getSharedPreferences(Constant.SAVE_TENCENT_ARTSINFO,
									context.MODE_PRIVATE).edit();
					for (int i = 0, j = 0; i < pic_354_354.length; i++) {
						if (pic_354_354[i] != null) {
			                   
							editor.putString("art_image_"+j, i + ".jpg");
							editor.putString("art_uri_"+j, uri[i]);
							editor.putString("art_name_"+j, titile[i]);
							editor.putString("art_year_" + j, year[i]);
							editor.putString("art_item_id_" + j,item_id[i]);
							editor.putString("art_director_"+j, director[i]);
							editor.putString("art_description_"+j, description[i]);
		                    j++;
						}
					}
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
					editor.putBoolean("tencent_art_update", true);
					editor.commit();
				}
				else{
					//没有更新成功
					Log.d("Jason","更新art数据不成功validTime = ");
					SharedPreferences.Editor editor = context
							.getSharedPreferences(Constant.SAVE_TENCENT_ARTSINFO,
									context.MODE_PRIVATE).edit();
					editor.putBoolean("tencent_art_update", false);
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
			  msg.arg1 = Constant.UPDATE_ARTS_SUCESS;//表示
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
