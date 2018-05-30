package com.ktc.tvlauncher.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ktc.tvlauncher.R;
import com.ktc.tvlauncher.domain.ForecastWeatherInfo;

import com.ktc.tvlauncher.tencent.bean.TencentAppsReInfo;
import com.ktc.tvlauncher.tencent.bean.TencentArtsReInfo;
import com.ktc.tvlauncher.tencent.bean.TencentMoviesReInfo;
import com.ktc.tvlauncher.tencent.bean.TencentTeleplaysReInfo;

/**
 * 
 * 
 * @ClassName: JsonTool
 * 
 * @Description: 完成对JSON数据的解析
 * 
 * @author Nathan.Liao
 * 
 * @date 2013-3-18 下午4:39:14
 */
public class StringTool {
	public static final int DAY_COUNT = 3;

	public StringTool() {

		// TODO Auto-generated constructor stub
	}


	public static ForecastWeatherInfo getWeatherInfo(String jsonString) {
		ForecastWeatherInfo weatherInfo = new ForecastWeatherInfo();

		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONObject weatherInfoObject = jsonObject.getJSONObject("f");
			// JSONObject forcastAllInfo = weatherInfoObject.getJSONObject("f");
			JSONArray weatherInfoArray = weatherInfoObject.getJSONArray("f1");
			int length = weatherInfoArray.length() >= DAY_COUNT ? DAY_COUNT
					: weatherInfoArray.length();
			String[] day_temp = new String[length];
			String[] night_temp = new String[length];
			String[] day_weather_status = new String[length];
			String[] night_weather_status = new String[length];
			String[] day_wind_direct = new String[length];
			String[] night_wind_direct = new String[length];
			String[] day_wind_strong = new String[length];
			String[] night_wind_strong = new String[length];
			String[] switch_time = new String[length];
			for (int i = 0; i < length; i++) {
				JSONObject object = weatherInfoArray.getJSONObject(i);
				day_weather_status[i] = object.getString("fa");
				night_weather_status[i] = object.getString("fb");
				day_temp[i] = object.getString("fc");
				night_temp[i] = object.getString("fd");
				day_wind_direct[i] = object.getString("fe");
				night_wind_direct[i] = object.getString("ff");
				day_wind_strong[i] = object.getString("fg");
				night_wind_strong[i] = object.getString("fh");
				switch_time[i] = object.getString("fi");
			}
			weatherInfo.setDay_weather_status(day_weather_status);
			weatherInfo.setNight_weather_status(night_weather_status);
			weatherInfo.setDay_temp(day_temp);
			weatherInfo.setNight_temp(night_temp);
			weatherInfo.setDay_wind_direct(day_wind_direct);
			weatherInfo.setNight_wind_direct(night_wind_direct);
			weatherInfo.setDay_wind_strong(day_wind_strong);
			weatherInfo.setNight_wind_strong(night_wind_strong);
			weatherInfo.setSwitch_time(switch_time);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return weatherInfo;
	}


	public static String getWeatherStatusTransform(String weatherStatusSign,
			Context mContext) {
		String weatherStatus = "";
		String weatherStatusArray[];
		int index = 0;
		int sign = 0;
		weatherStatusArray = mContext.getResources().getStringArray(
				R.array.str_array_weather_status);
		if (weatherStatusSign != null && weatherStatusSign.length() > 0)
			sign = Integer.parseInt(weatherStatusSign);
		if (sign >= 0 && sign <= 31) {
			index = sign;
		} else if (sign == 53) {
			index = 32;
		} else {
			index = 33;
		}
		if (weatherStatusArray != null && weatherStatusArray.length > 0
				&& index < weatherStatusArray.length)
			weatherStatus = weatherStatusArray[index];
		return weatherStatus;
	}

	// Hisa 2015.09.09 philips视频icntv start
	public static TencentAppsReInfo getTencentAppInfo(String jsonString){
		
        JSONObject jsonObject=null;
        TencentAppsReInfo tencentAppInfo=null;
		try {
			
			String[] title=new String[32];
			String[] type=new String[32];
			String[] pic=new String[32];
			String[] pic_710_350=new String[32];
			String[] uri=new String[32];
			String[] actor=new String[32];
			String[] director=new String[32];
			String[] description=new String[32];
			String[] link=new String[32];
			int[] id=new int[32];
			String[] year=new String[32];
			String[] item_id = new String[32];
			String[] viewPoint=new String[32];
			
			int count=0;
			jsonObject = new JSONObject(jsonString);
			JSONObject dataObject = jsonObject.getJSONObject("data");
			JSONArray contentArray = dataObject.getJSONArray("channel_contents");
			for (int j = 0; j < contentArray.length(); j++){
				JSONObject objj = contentArray.getJSONObject(j);
				JSONArray moduleArray = objj.getJSONArray("modules");
				for (int k = 0; k < moduleArray.length(); k++){
					JSONObject objk = moduleArray.getJSONObject(k);
					JSONArray itemsArray = objk.getJSONArray("items");
					for (int l = 0; l < itemsArray.length(); l++){
						JSONObject objl = itemsArray.getJSONObject(l);
						JSONObject comm_item = objl.getJSONObject("comm_item");
						description[count] = comm_item.getString("description");
						title[count] = comm_item.getString("title");
						year[count] = comm_item.getString("present_year");	
						item_id[count] = comm_item.getString("item_id");
						JSONObject ext_infoObject = comm_item.getJSONObject("ext_info1");
						pic_710_350[count] = ext_infoObject.getString("pic_710x350");
						uri[count] = ext_infoObject.getString("uri");
						count++;
					}
				}
			}
	
			tencentAppInfo=new TencentAppsReInfo();
			tencentAppInfo.setTitle(title);
			tencentAppInfo.setActor(actor);
			tencentAppInfo.setDirectors(director);
			tencentAppInfo.setDescription(description);
			tencentAppInfo.setPic_710_350(pic_710_350);
			tencentAppInfo.setUri(uri);
			tencentAppInfo.setPresent_year(year);
			tencentAppInfo.setItem_id(item_id);
			tencentAppInfo.setS_title(viewPoint);
		} catch (JSONException e) {
			Log.e("StringTool","解析推荐电影信息异常！！！！！！！！！！！");
		}
        return tencentAppInfo;
	}
	
	public static TencentMoviesReInfo getTencentMovieInfo(String jsonString){
        JSONObject jsonObject=null;
        TencentMoviesReInfo tencentMovieInfo=null;
		try {
			
			String[] title=new String[32];
			String[] type=new String[32];
			String[] pic=new String[32];
			String[] pic_470_630=new String[32];
			String[] uri=new String[32];
			String[] actor=new String[32];
			String[] director=new String[32];
			String[] description=new String[32];
			String[] link=new String[32];
			int[] id=new int[32];
			String[] year=new String[32];
			String[] item_id = new String[32];
			String[] viewPoint=new String[32];
			String[] s_title = new String[32];
			int count=0;
			StringBuffer stringBuffer = new StringBuffer();
				
			jsonObject = new JSONObject(jsonString);
			JSONObject dataObject = jsonObject.getJSONObject("data");
			JSONArray contentArray = dataObject.getJSONArray("channel_contents");
			for (int j = 0; j < contentArray.length(); j++){
				JSONObject objj = contentArray.getJSONObject(j);
				JSONArray moduleArray = objj.getJSONArray("modules");
				for (int k = 0; k < moduleArray.length(); k++){
					JSONObject objk = moduleArray.getJSONObject(k);
					JSONArray itemsArray = objk.getJSONArray("items");
					
					for (int l = 0; l < itemsArray.length(); l++){
						JSONObject objl = itemsArray.getJSONObject(l);
						JSONObject comm_item = objl.getJSONObject("comm_item");
						
						 
						JSONArray actorsarry = comm_item.getJSONArray("actors");
						int actorlength =actorsarry.length();
						for (int m = 0; m < actorlength; m++){
							stringBuffer.append(actorsarry.getString(m) + ",");						
						}
						actor[count] = stringBuffer.toString();
						stringBuffer.delete(0, stringBuffer.length());
						//Log.d("Jason","actor = " + actor[count]);
						JSONArray directorsarry = comm_item.getJSONArray("directors");
						int directorsarrylength =directorsarry.length();
						for (int m = 0; m < directorsarrylength; m++){
							stringBuffer.append(directorsarry.getString(m) + ",");	
						}
						director[count] = stringBuffer.toString();
						stringBuffer.delete(0, stringBuffer.length());
						//Log.d("Jason","director = " + director[count]);
						//actor[count] = comm_item.getString("actors");
						//comm_item.getJSONArray("directors").length();
						description[count] = comm_item.getString("description");
						title[count] = comm_item.getString("title");
						s_title[count] = comm_item.getString("s_title");
						year[count] = comm_item.getString("present_year");	
						item_id[count] = comm_item.getString("item_id");
						JSONObject ext_infoObject = comm_item.getJSONObject("ext_info1");
						pic_470_630[count] = ext_infoObject.getString("pic_470x630");
						uri[count] = ext_infoObject.getString("uri");
						//Log.d("Jason","movie:uri = " + uri[count]);
						count++;
					}
				}
			}
	
			tencentMovieInfo=new TencentMoviesReInfo();
			tencentMovieInfo.setTitle(title);
			tencentMovieInfo.setActor(actor);
			tencentMovieInfo.setDirectors(director);
			tencentMovieInfo.setDescription(description);
			tencentMovieInfo.setPic_470_630(pic_470_630);
			tencentMovieInfo.setUri(uri);
			tencentMovieInfo.setPresent_year(year);
			tencentMovieInfo.setItem_id(item_id);
			tencentMovieInfo.setS_title(s_title);
			
		} catch (JSONException e) {
			Log.e("StringTool","解析推荐电影信息异常！！！！！！！！！！！");
		}
        return tencentMovieInfo;
	}
	public static TencentTeleplaysReInfo getTencentTeleplayInfo(String jsonString){
        JSONObject jsonObject=null;
        TencentTeleplaysReInfo tencentTeleplayInfo=null;
		try {
			
			String[] title=new String[32];
			String[] type=new String[32];
			String[] pic=new String[32];
			String[] pic_470_630=new String[32];
			String[] uri=new String[32];
			String[] actor=new String[32];
			String[] director=new String[32];
			String[] description=new String[32];
			String[] link=new String[32];
			int[] id=new int[32];
			String[] year=new String[32];
			String[] item_id = new String[32];
			String[] viewPoint=new String[32];
			String[] s_title = new String[32];
			StringBuffer stringBuffer = new StringBuffer();
			int count=0;
				
			jsonObject = new JSONObject(jsonString);
			JSONObject dataObject = jsonObject.getJSONObject("data");
			JSONArray contentArray = dataObject.getJSONArray("channel_contents");
			for (int j = 0; j < contentArray.length(); j++){
				JSONObject objj = contentArray.getJSONObject(j);
				JSONArray moduleArray = objj.getJSONArray("modules");
				for (int k = 0; k < moduleArray.length(); k++){
					JSONObject objk = moduleArray.getJSONObject(k);
					JSONArray itemsArray = objk.getJSONArray("items");
					
					for (int l = 0; l < itemsArray.length(); l++){
						JSONObject objl = itemsArray.getJSONObject(l);
						JSONObject comm_item = objl.getJSONObject("comm_item");
						
						JSONArray actorsarry = comm_item.getJSONArray("actors");
						int actorlength =actorsarry.length();
						for (int m = 0; m < actorlength; m++){
							stringBuffer.append(actorsarry.getString(m) + ",");	
						}
						actor[count] = stringBuffer.toString();
						stringBuffer.delete(0, stringBuffer.length());
						
						//Log.d("Jason","TencentTeleplay: actor = " + actor[count]);
						JSONArray directorsarry = comm_item.getJSONArray("directors");
						int directorsarrylength =directorsarry.length();
						for (int m = 0; m < directorsarrylength; m++){
							stringBuffer.append(directorsarry.getString(m) + ",");	
						}
						director[count] = stringBuffer.toString();
						stringBuffer.delete(0, stringBuffer.length());
						
						description[count] = comm_item.getString("description");
						title[count] = comm_item.getString("title");
						s_title[count] = comm_item.getString("s_title");
						year[count] = comm_item.getString("present_year");	
						item_id[count] = comm_item.getString("item_id");
						JSONObject ext_infoObject = comm_item.getJSONObject("ext_info1");
						pic_470_630[count] = ext_infoObject.getString("pic_470x630");
						uri[count] = ext_infoObject.getString("uri");
						//Log.d("Jason","teleplay:uri = " + uri[count]);
						count++;
					}
				}
			}
	
			tencentTeleplayInfo=new TencentTeleplaysReInfo();
			tencentTeleplayInfo.setTitle(title);
			tencentTeleplayInfo.setActor(actor);
			tencentTeleplayInfo.setDirectors(director);
			tencentTeleplayInfo.setDescription(description);
			tencentTeleplayInfo.setPic_470_630(pic_470_630);
			tencentTeleplayInfo.setUri(uri);
			tencentTeleplayInfo.setPresent_year(year);
			tencentTeleplayInfo.setItem_id(item_id);
			tencentTeleplayInfo.setS_title(s_title);
		} catch (JSONException e) {
			Log.e("StringTool","解析推荐电影信息异常！！！！！！！！！！！");
		}
        return tencentTeleplayInfo;
	}
	public static TencentArtsReInfo getTencentArtInfo(String jsonString){
        JSONObject jsonObject=null;
        TencentArtsReInfo tencentArtInfo=null;
		try {
			
			String[] title=new String[32];
			String[] type=new String[32];
			String[] pic=new String[32];
			String[] pic_354_354=new String[32];
			String[] uri=new String[32];
			String[] actor=new String[32];
			String[] director=new String[32];
			String[] description=new String[32];
			String[] link=new String[32];
			int[] id=new int[32];
			String[] year=new String[32];
			String[] item_id = new String[32];
			String[] viewPoint=new String[32];
			int count=0;
				
			jsonObject = new JSONObject(jsonString);
			JSONObject dataObject = jsonObject.getJSONObject("data");
			JSONArray contentArray = dataObject.getJSONArray("channel_contents");
			for (int j = 0; j < contentArray.length(); j++){
				JSONObject objj = contentArray.getJSONObject(j);
				JSONArray moduleArray = objj.getJSONArray("modules");
				for (int k = 0; k < moduleArray.length(); k++){
					JSONObject objk = moduleArray.getJSONObject(k);
					JSONArray itemsArray = objk.getJSONArray("items");
					
					for (int l = 0; l < itemsArray.length(); l++){
						JSONObject objl = itemsArray.getJSONObject(l);
						JSONObject comm_item = objl.getJSONObject("comm_item");
						description[count] = comm_item.getString("description");
						title[count] = comm_item.getString("title");
						year[count] = comm_item.getString("present_year");	
						item_id[count] = comm_item.getString("item_id");
						JSONObject ext_infoObject = comm_item.getJSONObject("ext_info1");
						pic_354_354[count] = ext_infoObject.getString("pic_354x354");
						uri[count] = ext_infoObject.getString("uri");
						//Log.d("Jason","art:uri = " + uri[count]);
						count++;
					}
				}
			}
	
			tencentArtInfo=new TencentArtsReInfo();
			tencentArtInfo.setTitle(title);
			tencentArtInfo.setActor(actor);
			tencentArtInfo.setDirectors(director);
			tencentArtInfo.setDescription(description);
			tencentArtInfo.setPic_354_354(pic_354_354);
			tencentArtInfo.setUri(uri);
			tencentArtInfo.setPresent_year(year);
			tencentArtInfo.setItem_id(item_id);
			tencentArtInfo.setS_title(viewPoint);
		} catch (JSONException e) {
			Log.e("StringTool","解析推荐电影信息异常！！！！！！！！！！！");
		}
        return tencentArtInfo;
	}
	// Hisa 2015.09.09 philips视频icntv end




}
