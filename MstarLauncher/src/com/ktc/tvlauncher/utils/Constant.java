package com.ktc.tvlauncher.utils;

import java.io.File;

import android.os.Environment;

/**
 * @Description 配置信息
 * @author joychang
 *
 */
public class Constant {
	
	public static String UPDATE_URL = "http://www.smtvzm.com/index.php/version/chkupdate.json?";
	public static String STARTINFO_URL = "http://www.smtvzm.com/index.php/user/addstartinfo.json?";
	//public static String RECOMMEND_URL = "http://www.lepengbang.com/index.php/tjinfo/gettjinfo.json";
	public static String RECOMMEND_URL = "http://www.smtvzm.com/index.php/tjinfo/gettjinfo.json?type=推荐";
	public static String RECOMMEND_URL1 = "http://bs3-api.sdk.wasu.tv/XmlData/Recommend?tag=movie";
	
	public static String WASU_BASE_URL = "http://bs3-api.sdk.wasu.tv";
	public static String WASU_MOVIE_URL = "http://bs3-api.sdk.wasu.tv/XmlData/Recommend?tag=movie";
	public static String WASU_TELEPLAY_URL = "http://bs3-api.sdk.wasu.tv/XmlData/Recommend?tag=series";
	public static String WASU_RECOMMEND_MOVIE = "";
	public static String WASU_RECOMMEND_ARTS_URL = "http://bs3-api.sdk.wasu.tv/XmlData/MoreHot?tag=variety";
	public static String WASU_MOVIE_CAROUSEL = "http://bs3-api.sdk.wasu.tv/XmlData/Videos";
	
	public static String HEARD_URL = "http://www.smtvzm.com/";
	public static String RECAPPS = "http://www.smtvzm.com/index.php/tjinfo/gettjapp.json?";
	public static String MOVIE_URL = "http://www.smtvzm.com/index.php/tjinfo/getclassify.json?";
	public static String UPLOAD_URL = "http://www.smtvzm.com/index.php/user/downinfo.json?";
	public static String USERLOGIN = "http://www.smtvzm.com/index.php/user/login.json";
	public static String USERREG = "http://www.smtvzm.com/index.php/user/reg.json";
	
	//当前程序公用的文件路径
	public static String PUBLIC_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"ShenMa"+File.separator;
	
	//当前程序的SharedPreferences统一名称
	public final static String SHENMASP = "shenma_sp";
	public final static int TYPE_ZJ = 0;
	public final static int TYPE_SC = 1;
	public final static int TYPE_LS = 2;
	
	//回看
	public final static String WEPOWER_URL = "http://huibo.lsott.com";
	
	//直播
	public static final String ENTER_URL = "http://wlhd.lsott.com/api/index.php?mac=";
	public static final String ENTER_URL_MAC = "http://wlhd.lsott.com/api/index.php?mac=555666";
	
	//影视
	public final static String TVPLAY = "http://aps.lsott.com/app/?nozzle=list&class=tvplay";
	public final static String COMIC = "http://aps.lsott.com/app/?nozzle=list&class=comic";
	public final static String TVSHOW = "http://aps.lsott.com/app/?nozzle=list&class=tvshow";
	public final static String MOVIE = "http://aps.lsott.com/app/app.php?nozzle=list&class=movie";
	public final static String TEACH = "http://aps.lsott.com/app/app.php?nozzle=list&class=teach";
	public final static String DOCUMENTARY = "http://aps.lsott.com/app/app.php?nozzle=list&class=documentary";
	public final static String VODFILTER = "http://aps.lsott.com/app/app.php";
	public final static String VODFILTER_H123 = "http://aps.lsott.com/app/";
	
	//专题
	public final static String TOPIC_URL = "http://www.smtvzm.com/index.php/tjinfo/getyszt.json";
	public final static String TOPIC_HEAD_URL = "http://aps.lsott.com/app/app.php?nozzle=list&class=ablum&type=";
	//搜索
	public final static String VOD_TYPE_ALL = "http://www.smtvzm.com/index.php/seachinfo/seachsp.json?zm=";
	public final static String VOD_TYPE = "http://aps.lsott.com/app/app.php?nozzle=character&zm=";
	public final static String VOD_TYPE_HAO123 = "http://aps.lsott.com/app/?nozzle=character&zm=";
	
	public final static String URL_HEAD = "http://aps.lsott.com/app/parse.php?";
	public final static String POST_PHP = "http://aps.lsott.com/app/post.php?";
	
	public final static String AK = "HuRTrmYh7fieGyeoAumGj28F";//ak
	public final static String SK = "KNNU53DTetf7RNMlI9TNSbI2K1trS6fL";//前16位
	
	public final static String TVLIVE = "TVLIVE"; 
	public final static String TVLIVE_DIY = "TVLIVE_DIY"; 
	
	//电视频道
	public final static String TVSTATIONS = "http://smtvzm.com/index.php/channel/getsyschannel.json";
	public final static String WALLPAPER = "http://www.smtvzm.com/index.php/skin/getskininfo.json";
	

	
	
	
	public static final String TV_CONFIG = "638launcher_config";
	
	public static final boolean CONNECT_FAILED = false;
	public static final boolean CONNECT_SUCESS = true;
	
    public static final int UPDATAWASU_MS=0x001;
    
    public static final int SETSOURCE_MS=0x002;
	
    public static final String APP_BASE_URL = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KTC_LAUNCHER&channel_selector=chosen&content_selector=all&pictures=pic_354x354%2bpic_710x350";
    public static final String MOVIE_BASE_URL = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KTC_LAUNCHER&channel_selector=movie&content_selector=all&pictures=pic_354x354%2bpic_470x630";
    public static final String TELEPLAY_BASE_URL = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KTC_LAUNCHER&channel_selector=tv&content_selector=all&pictures=pic_354x354%2bpic_470x630";
    public static final String ART_BASE_URL = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KTC_LAUNCHER&channel_selector=variety&content_selector=all&pictures=pic_354x354%2bpic_281x281";
    public static final String DEFAULT_TENCENT_QUA = "QV%3D1%26PR%3DLAUNCHER%26PT%3DKTC%26CHID%3D10032%26RL%3D1920*1080%26VN%3D1.0%26VN_CODE%3D1%26SV%3D4.4.4%26DV%3Dktc_tencent%26VN_BUILD%3D0%26MD%3DTV638%26BD%3DTV638";
    public static final String DEFAULT_TENCENT_GUID = "05EA8F4D00E081E142ED006B56138F77";
    
    public static final String SAVE_TENCENT_APPSINFO = "tencent_apps_info";
	public static final String SAVE_TENCENT_ARTSINFO = "tencent_arts_info";
	public static final String SAVE_TENCENT_TELEPLAYSINFO = "tencent_teleplays_info";
	public static final String SAVE_TENCENT_MOVIESINFO = "tencent_movies_info";
    
	public static final String[] appsImagesNames={
		"0.jpg","1.jpg",
        "2.jpg","3.jpg"};
	public static final String[] moviesImagesNames={
		"0.jpg","1.jpg",
        "2.jpg","3.jpg",
        "4.jpg","5.jpg"};
	public static final String[] teleplaysImagesNames={
		"0.jpg","1.jpg",
        "2.jpg","3.jpg",
        "4.jpg","5.jpg"};
	public static final String[] artsImagesNames={
		"0.jpg","1.jpg",
		"2.jpg","3.jpg",
		"4.jpg","5.jpg",
		"6.jpg","7.jpg",
        "8.jpg","9.jpg"};
;
	
	public static final String appsJsonFile="TencentApps.txt";
	public static final String moviesJsonFile="TencentMovies.txt";
	public static final String teleplaysJsonFile="TencentTeleplays.txt";
	public static final String artsJsonFile="TencentArts.txt";
	
	public static final String NEW_MOVIE_URL = "tenvideo2://?action=3&channel_code=movie&channel_name=电影";
	public static final String NEW_TELEPLAY_URL = "tenvideo2://?action=3&channel_code=tv&channel_name=电视剧";

	
	

	public static final int IMAGE_DATA_SUCESS = 20;
	public static final int NETWORK_CONNECT = 30;
	public static final int NETWORK_SUCESS = 40;
	public static final int NETWORK_NOCONNECT = 50;
	public static final int NETWORK_TIMEOUT = 60;
	public static final int JUMP_MAIN_UI = 70;
	public static final int IMAGE_LOADED = 80;
	public static final int NETWORK_NO_FRESH = 40;
	
	public static final int UPDATE_APPS_SUCESS = 1;	
	public static final int UPDATE_MOVIES_SUCESS = 1;
	public static final int UPDATE_TELEPLAYS_SUCESS = 1;
	public static final int UPDATE_ARTS_SUCESS = 1;
	
	
	
	public static final String[] miniAppPackageName={
     "com.jrm.localmm",    //2
     "com.android.email",//3
     "com.android.settings",//4
     "com.mstar.tv.tvplayer.ui",//5
     "com.android.calendar",//6
     "com.ktcp.tvvideo",
     "",//7
     "" //8
     };
	
    public static final String WEATHER_CITY_CHANGE_BROADCAST="com.ktc.launcher.ui.WeatherHodler";
}
