package com.ktc.tvlauncher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.NetworkInfo;
import android.net.ethernet.EthernetManager;
import android.net.ethernet.EthernetStateTracker;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewPropertyAnimator;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktc.tvlauncher.adapter.FragAdapter;
import com.ktc.tvlauncher.application.MyApplication;
import com.ktc.tvlauncher.fragment.AppFragment;
import com.ktc.tvlauncher.fragment.ArtsFragment;
import com.ktc.tvlauncher.fragment.MovieFragment;
import com.ktc.tvlauncher.fragment.TeleplayFragment;
import com.ktc.tvlauncher.tencent.bean.TencentAppsReInfo;
import com.ktc.tvlauncher.tencent.bean.TencentArtsReInfo;
import com.ktc.tvlauncher.tencent.bean.TencentTeleplaysReInfo;
import com.ktc.tvlauncher.tencent.bean.TencentMoviesReInfo;
import com.ktc.tvlauncher.ui.DepthPageTransformer;
import com.ktc.tvlauncher.ui.FixedSpeedScroller;
import com.ktc.tvlauncher.update.UpdateApk;
import com.ktc.tvlauncher.update.UpdateAppData;
import com.ktc.tvlauncher.update.UpdateArtsData;
import com.ktc.tvlauncher.update.UpdateMovieData;
import com.ktc.tvlauncher.update.UpdateTeleplayData;
import com.ktc.tvlauncher.update.Version;
import com.ktc.tvlauncher.utils.BlurUtils;
import com.ktc.tvlauncher.utils.Constant;
import com.ktc.tvlauncher.utils.LruCacheUtils;
import com.ktc.tvlauncher.utils.NetTool;
import com.ktc.tvlauncher.utils.StringTool;
import com.ktc.tvlauncher.utils.Utils;
import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.MIntent;
import com.ktcp.mta.sdk.KtcpMtaSdk;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
public class HomeActivity extends BaseActivity implements View.OnClickListener{

	private final String TAG = "HomeActivity";

	public FrameLayout fl_main;
	private RelativeLayout rl_bg;
	private TextView tv_time, time_colon;
	//private ImageView iv_net_state,
	private ImageView iv_titile;
	private RadioButton rb_app_store, rb_teleplay, rb_movie, rb_arts;
	private boolean isRunning = false;
	private ViewPager vpager;
	private RadioGroup title_group, rg_video_type_bottom;
	private FragAdapter adapter;
	private float fromXDelta;
	private float toX;
	private AnimationSet mAnimationSet;
	private TranslateAnimation mTranslateAnimation;
	private List<Fragment> fragments;
	public List<PackageInfo> packLst;
	protected static Boolean ISTV;
	private static int titile_position = 0;
	protected String technology = "";
	private float countSize; // 软件更新总大小
	private float currentSize; // 软件更新当前下载进度
	// private boolean isFromPageChange;

	private TextView tv_main_date, tv_update_msg;

	private MovieFragment rf;
	private TeleplayFragment mf;
	private AppFragment af;
	// private TVFragment tf;
	private ArtsFragment artsFrag;

	// private UserFragment uf;

	private Boolean isHasFouse;

	private LinearLayout ll_rb;

	private LruCacheUtils mCacheUtils;

	public static String homeFrom;
	public static String homeParams;

	private boolean activityIsRun = false;

	public boolean net_state = Constant.CONNECT_FAILED;


	private Handler HandlerUpdata;
	private HandlerThread handlerThread;

	public boolean isMovieLoadSucess = false;
	public boolean isTeleplaysLoadSucess = false;
	public boolean isAppsLoadSucess = false;
	public boolean isArtsLoadSucess = false;

	private int allWasuDataUpate = 0;// 标记四个界面的数据是否全部更新完成，如果该值为4，则表示加盟全部更新完成

	private boolean is_timeout = true;// 默认超时
	private boolean timeout_flag = false;// 超时标志：false：4个节目请求都没有超时；true：至少一个界面超时

	private boolean isPageLoadSucess = false;// true：表示四个页面数据全部加载成功

	public static final int GET_INFO_SUCCESS = 10;
	public static final int DETECTION_NET = 18;

	private CheckNewVersionTask mCheckNewVersionTask;
	private Dialog alertDialog;
	private Boolean isPass = false;

	private ProgressDialog dialog;

	boolean isFirstBoot = true;

	private ImageView usb_image;

	// ////////////////notification////////////////////////
	private final int USB_STATE_ON = 100001; // usb storage on

	private final int USB_STATE_OFF = 100002; // usb storage off

	private final int UPDATE_USB_ICON = 100005; // update usb icon

	private final int USB_STATE_CHANGE = 100006; // usb change state

	public boolean mUsbFlag = false;

	public int usbDeviceCount = 0;


    public final static String PPPOE_STATE_ACTION = "android.net.pppoe.PPPOE_STATE_ACTION";

    public final static String PPPOE_STATE_STATUE = "PppoeStatus";

    public static final String PPPOE_STATE_CONNECT = "connect";

    public static final String PPPOE_STATE_DISCONNECT = "disconnect";

    public static final String PPPOE_STATE_AUTHFAILED = "authfailed";

    public static final String PPPOE_STATE_FAILED = "failed";
    private ImageView netStatus;
    
    
    private boolean mWireFlag = false;
    private boolean mWireLessFlag = false;
    boolean mWifiEnabled = false;
    boolean mWifiConnected = false;
    int mWifiRssi = 0;
    int mWifiLevel = 0;
    private int[] WifiIconArray;
    public final static int WIFI_LEVEL_COUNT = 4;
    private WifiManager mWifiManager;
    private NetTool mNetTool;
    private String mWifiSsid;
    
    //private boolean PowerOn = true;
    private int ThirdPartyDtvValue = 0;// colin@20130418 add for ddi
    // Hisa 2015.11.10 当弹出更新apk对话框时,时间闪动异常 start
    private boolean homeAcivityIsForeground = true;
    // Hisa 2015.11.10 当弹出更新apk对话框时,时间闪动异常 end
    
    private static int currentPageNumber = 0;
    private FrameLayout mcontent;
    private float bgImageAlph;
    private boolean isBootUpdate = true;//定义是否为开机更新数据：true:开机更新数据，false：为网路变化更新数据

    private static boolean isBoot = false;//true:表示开机启动;false:表示从其他信源启动
    //ktc 2015.11.05 nathan.liao add for is power on start
    private final String IS_POWER_ON_PROPERTY = "mstar.launcher.1stinit";
    private boolean isPowerOn = false;
	//ktc 2015.11.05 nathan.liao add for is power on end
    
    private TvCommonManager commonService;
    
    private TvChannelManager tvChannelManager;
    private Boolean bSync = true;
	private Button tv;
	//tv show
    private SurfaceView surfaceView = null;
    private android.view.SurfaceHolder.Callback callback;
    private boolean hasTVClick = false;
    private Boolean bSystemShutdown = false;
    private static final int SCALE_NONE = 0;

    private static final int SCALE_FULL = 1;

    private static final int SCALE_SMALL = 2;

    private int fullScale = SCALE_NONE;
    private boolean createsurface = false;
    private InputSourceThread inputSourceThread = new InputSourceThread();
    private static final String STR_STATUS_NONE = "0";

    private static final String STR_STATUS_SUSPENDING = "1";
    private int toChangeInputSource = TvCommonManager.INPUT_SOURCE_NONE;
    private Boolean bExitThread = false;
    private int panel_height;
    
    private int panel_width;
    
    private final String FILE_FLY_LAUNCH = "com.jrm.filefly.action";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mcontent=(FrameLayout) findViewById(R.id.activity_main);
		mcontent.setVisibility(View.INVISIBLE);
		bgImageAlph = mcontent.getAlpha();
		mcontent.setAlpha(0);
		updateWallpaperVisibility(false);
		MyApplication mApp = (MyApplication) getApplication();
		technology = mApp.getTechnology();
		mCacheUtils = LruCacheUtils.getInstance();
		isPowerOn = isPowerOn();
		homeFrom = from;
		homeParams = params;
		SystemProperties.set("persist.sys.StandbyStatus", "false");
		titile_position = 0;
		if ("".equals(technology)) {
			if (screenSize > 9) {
				ISTV = true;
				devicetype = "TV";
			} else {
				ISTV = false;
				devicetype = "MOBILE";
			}
		} else {
			if (null != technology && !"null".equals(technology)) {
				if (screenSize > 9) {
					ISTV = true;
					devicetype = "TV";
				} else {
					ISTV = false;
					devicetype = "MOBILE";
				}
			} else {
				ISTV = true;
				devicetype = "TV";
			}
		}
		//ktc 2015.11.05 nathan.liao add for is power on start
		if(isPowerOn)
		{
			isBoot  = true;
			initDialog();			
		}
		initImageData();
		initHandler();
		//ktc 2015.11.05 nathan.liao add for is power on end
		initView();// 初始化
		//initwhiteBorder();
		//registNetReceiver();
		
		//new Thread(inputSourceThread).start();
        commonService = TvCommonManager.getInstance();
        tvChannelManager = TvChannelManager.getInstance();

        try {
			panel_height = TvManager.getInstance().getPictureManager().getPanelWidthHeight().height;
			  panel_width = TvManager.getInstance().getPictureManager().getPanelWidthHeight().width;
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        new Thread(inputSourceThread).start();
        
		Intent intent = new Intent("com.wasuali.action.register");
		// 必须设置，否则新安装的应用无法收到广播
		intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		sendBroadcast(intent);
		ImageLoader.getInstance().clearMemoryCache(); 
		ImageLoader.getInstance().clearDiscCache();
		//打开MTA Log调试模式,true:打开调试模式;false:关闭调试模式
		//KtcpMtaSdk.openMTALog(getApplicationContext(), true);
		 //Tencent Video MTA数据上报:app_action_start
	    String data = "{" + 
	    			"\"app_package\":\"com.ktc.tvlauncher\"," + 
	    			"\"event_id\":\"app_action_start\"," + 
	    			"\"event_type\":3," +
	    			"\"data_type\":1," + 
	    			"\"pr\":\"LAUNCHER\"," +
	    			"\"data\":{}}";
	    Log.d("ktc_tencent","启动Launcher App:app_action_start:data = " + data.toString());
	    KtcpMtaSdk.mtaReport(HomeActivity.this.getApplicationContext(),data);

		if (ExistUsbDeviceMounted())
		{
			usbDeviceCount = getUsbDeviceCount();
		}
		else {
			usbDeviceCount = 0;
		}
	}
 
	
    private void updateWallpaperVisibility(boolean visible) {
        int wpflags = visible ? WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER : 0;
        int curflags = getWindow().getAttributes().flags
                & WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER;
        if (wpflags != curflags) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            WallpaperManager.getInstance(this).suggestDesiredDimensions(metrics.widthPixels,
                    metrics.heightPixels);

            getWindow().setFlags(wpflags, WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
        }
    }

	private void initDialog() {
		// 设置一个progressdialog的弹窗
		dialog = ProgressDialog.show(this, "加载中...", "您的网络较慢，正在为您努力加载中...");
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (isPageLoadSucess == true)
						// homeHandler.sendEmptyMessageDelayed(0, 0);
						break;
				}
			}
		});
		thread.start();

	}
    public void BackHomeSource() {
        synchronized (bSync) {
            toChangeInputSource = TvCommonManager.INPUT_SOURCE_ATV;
        }
    }
	// 数据初始化
	private void initImageData() {
		SharedPreferences sp = context.getSharedPreferences(Constant.TV_CONFIG,
				Context.MODE_PRIVATE);
		isFirstBoot = sp.getBoolean("isFirstBoot", true);
		if (isFirstBoot) {
			// initDialog();
			new LoadAppsImage().execute();
			new LoadMoviesImage().execute();
			new LoadTeleplaysImage().execute();
			new LoadArtsImage().execute();
		} else {
			//Hisa 2015.11.10 ktc有时切回主页时加载时间过长 start
			if (isPowerOn){
				homeHandler.sendEmptyMessageDelayed(Constant.IMAGE_LOADED, 0);
			}else{
				homeHandler.sendEmptyMessageDelayed(Constant.JUMP_MAIN_UI, 0);
			}
			//Hisa 2015.11.10 ktc有时切回主页时加载时间过长 end
		}
		SharedPreferences.Editor editor = context.getSharedPreferences(
				Constant.TV_CONFIG, Context.MODE_PRIVATE).edit();
		editor.putBoolean("isFirstBoot", false);
		editor.commit();

	}

	class LoadAppsImage extends AsyncTask<Void, Void, TencentAppsReInfo> {
		@Override
		protected TencentAppsReInfo doInBackground(Void... arg0) {
			//Log.d("Jason","LoadAppsImaage");
			copyFilesFassets(HomeActivity.this, "appsimage","data/data/com.ktc.tvlauncher/files/appsImages/");

			AssetManager am = null;
			am = context.getAssets();
			InputStream is = null;
			TencentAppsReInfo tencentAppInfo = null;
			try {
				// String url = "appsimage/" + Constant.appsJsonFile;
				is = am.open(Constant.appsJsonFile);
				byte[] buff = new byte[1024];
				int len = -1;
				StringBuffer text = new StringBuffer();
				while (-1 != (len = is.read(buff))) {
					String res = new String(buff, 0, len, "utf-8");
					text.append(res);
				}
				tencentAppInfo = StringTool.getTencentAppInfo(text.toString());
				is.close();
			} catch (IOException e1) {

			}
			return tencentAppInfo;

		}

		@Override
		protected void onPostExecute(TencentAppsReInfo tencentAppInfo) {
			// TODO Auto-generated method stub
			super.onPostExecute(tencentAppInfo);
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
				SharedPreferences.Editor editor = context.getSharedPreferences(
						Constant.SAVE_TENCENT_APPSINFO, context.MODE_PRIVATE).edit();
				for (int i = 0, j = 0; i < pic_710_350.length; i++) {
					if (pic_710_350[i] != null) {
						
						editor.putString("app_image_"+j, i + ".jpg");
		                editor.putString("app_uri_"+j, uri[i]);
		                editor.putString("app_name_"+j, titile[i]);
		                editor.putString("app_year_" + j, year[i]);
		                editor.putString("app_item_id_" + j, item_id[i]);
		                editor.putString("app_director_"+j, director[i]);
		                editor.putString("app_description_"+j, description[i]);

						j++;
					}
				}

				editor.commit();
				editor.commit();

				isAppsLoadSucess = true;
				homeHandler.sendEmptyMessageDelayed(Constant.IMAGE_DATA_SUCESS,
						0);

			}
			/*
			 * Message msg=WasuHandler.obtainMessage(Constants.UPDATAWASU_MS);
			 * WasuHandler.sendMessage(msg);
			 */
		}

	}

	class LoadMoviesImage extends AsyncTask<Void, Void, TencentMoviesReInfo> {
		@Override
		protected TencentMoviesReInfo doInBackground(Void... arg0) {
			copyFilesFassets(HomeActivity.this, "moviesimage",
					"data/data/com.ktc.tvlauncher/files/moviesImages/");
			AssetManager am = null;
			am = context.getAssets();
			InputStream is = null;
			TencentMoviesReInfo tencentMoviesInfo = null;
			try {
				String url = "moviesImages/" + Constant.moviesJsonFile;
				is = am.open(Constant.moviesJsonFile);
				byte[] buff = new byte[1024];
				int len = -1;
				StringBuffer text = new StringBuffer();
				while (-1 != (len = is.read(buff))) {
					String res = new String(buff, 0, len, "utf-8");
					text.append(res);
				}
				tencentMoviesInfo = StringTool.getTencentMovieInfo(text.toString());
				is.close();
			} catch (IOException e1) {

			}
			return tencentMoviesInfo;

		}

		@Override
		protected void onPostExecute(TencentMoviesReInfo tencentMoviesInfo) {
			// TODO Auto-generated method stub
			super.onPostExecute(tencentMoviesInfo);
			if (tencentMoviesInfo != null) {
				String[] titile=tencentMoviesInfo.getTitle();
	            String[] uri=tencentMoviesInfo.getUri();
	            String[] description=tencentMoviesInfo.getDescription();
	            String[] pic_470_630=tencentMoviesInfo.getPic_470_630();
	            String[] year=tencentMoviesInfo.getPresent_year();
	            String[] item_id=tencentMoviesInfo.getItem_id();
	            String[] director=tencentMoviesInfo.getDirectors();
	            String[] viewPoint=tencentMoviesInfo.getS_title();
	            String[] actor=tencentMoviesInfo.getActor();
	            String[] s_title=tencentMoviesInfo.getS_title();

				SharedPreferences.Editor editor = context.getSharedPreferences(
						Constant.SAVE_TENCENT_MOVIESINFO, context.MODE_PRIVATE).edit();
				for (int i = 0, j = 0; i < pic_470_630.length; i++) {
					if (pic_470_630[i] != null) {

						editor.putString("movie_image_"+j, i + ".jpg");
		                editor.putString("movie_uri_"+j, uri[i]);
		                editor.putString("movie_name_"+j, s_title[i]);
		                editor.putString("movie_year_" + j, year[i]);
		                editor.putString("movie_item_id_" + j, item_id[i]);
		                editor.putString("movie_director_"+j, director[i]);
		                editor.putString("movie_description_"+j, description[i]);
		                editor.putString("movie_actor_"+j, actor[i]);
		                editor.putString("movie_director_"+j, director[i]);
		                editor.putString("movie_title_"+j, titile[i]);
		                editor.putString("movie_s_title_"+j, s_title[i]);

						j++;
					}
				}
/*				editor.putBoolean("isFirstBoot", false);*/
				editor.commit();
				editor.commit();
				isMovieLoadSucess = true;
				homeHandler.sendEmptyMessageDelayed(Constant.IMAGE_DATA_SUCESS,
						0);
			}
		}
	}

	class LoadTeleplaysImage extends AsyncTask<Void, Void, TencentTeleplaysReInfo> {
		@Override
		protected TencentTeleplaysReInfo doInBackground(Void... arg0) {
			copyFilesFassets(HomeActivity.this, "teleplaysimage",
					"data/data/com.ktc.tvlauncher/files/teleplaysImages/");

			AssetManager am = null;
			am = context.getAssets();
			InputStream is = null;
			TencentTeleplaysReInfo tencentTeleplayInfo = null;
			try {
				String url = "teleplaysimage/" + Constant.teleplaysJsonFile;
				is = am.open(Constant.teleplaysJsonFile);
				byte[] buff = new byte[1024];
				int len = -1;
				StringBuffer text = new StringBuffer();
				while (-1 != (len = is.read(buff))) {
					String res = new String(buff, 0, len, "utf-8");
					text.append(res);
				}
				tencentTeleplayInfo = StringTool.getTencentTeleplayInfo(text.toString());
				is.close();
			} catch (IOException e1) {

			}
			return tencentTeleplayInfo;

		}

		@Override
		protected void onPostExecute(TencentTeleplaysReInfo tencentTeleplaysInfo) {
			// TODO Auto-generated method stub
			super.onPostExecute(tencentTeleplaysInfo);
			if (tencentTeleplaysInfo != null) {
				String[] titile=tencentTeleplaysInfo.getTitle();
	            String[] uri=tencentTeleplaysInfo.getUri();
	            String[] description=tencentTeleplaysInfo.getDescription();
	            String[] pic_470_630=tencentTeleplaysInfo.getPic_470_630();
	            String[] year=tencentTeleplaysInfo.getPresent_year();
	            String[] item_id=tencentTeleplaysInfo.getItem_id();
	            String[] director=tencentTeleplaysInfo.getDirectors();
	            String[] viewPoint=tencentTeleplaysInfo.getS_title();
	            String[] actor=tencentTeleplaysInfo.getActor();
	            String[] s_title=tencentTeleplaysInfo.getS_title();

				SharedPreferences.Editor editor = context.getSharedPreferences(
						Constant.SAVE_TENCENT_TELEPLAYSINFO, context.MODE_PRIVATE)
						.edit();
				for (int i = 0, j = 0; i < pic_470_630.length; i++) {
					if (pic_470_630[i] != null) {


						editor.putString("teleplay_image_"+j, i + ".jpg");
		                editor.putString("teleplay_uri_"+j, uri[i]);
		                editor.putString("teleplay_name_"+j, s_title[i]);
		                editor.putString("teleplay_year_" + j, year[i]);
		                editor.putString("teleplay_item_id_" + j, item_id[i]);
		                editor.putString("teleplay_director_"+j, director[i]);
		                editor.putString("teleplay_description_"+j, description[i]);
		                editor.putString("teleplay_actor_"+j, actor[i]);
		                editor.putString("teleplay_director_"+j, director[i]);
		                editor.putString("teleplay_title_"+j, titile[i]);
		                editor.putString("teleplay_s_title_"+j, s_title[i]);
						j++;
					}
				}
	/*			editor.putBoolean("isFirstBoot", false);*/
				editor.commit();
				editor.commit();

				isTeleplaysLoadSucess = true;
				homeHandler.sendEmptyMessageDelayed(Constant.IMAGE_DATA_SUCESS,
						0);
			}
			/*
			 * Message msg=WasuHandler.obtainMessage(Constants.UPDATAWASU_MS);
			 * WasuHandler.sendMessage(msg);
			 */
		}

	}

	class LoadArtsImage extends AsyncTask<Void, Void, TencentArtsReInfo> {
		@Override
		protected TencentArtsReInfo doInBackground(Void... arg0) {
			copyFilesFassets(HomeActivity.this, "artsimage",
					"data/data/com.ktc.tvlauncher/files/artsImages/");
			AssetManager am = null;
			am = context.getAssets();
			InputStream is = null;
			TencentArtsReInfo tencentArtInfo = null;
			try {
				String url = "artsimage/" + Constant.artsJsonFile;
				is = am.open(Constant.artsJsonFile);
				byte[] buff = new byte[1024];
				int len = -1;
				StringBuffer text = new StringBuffer();
				while (-1 != (len = is.read(buff))) {
					String res = new String(buff, 0, len, "utf-8");
					text.append(res);
				}
				tencentArtInfo = StringTool.getTencentArtInfo(text.toString());
				is.close();
			} catch (IOException e1) {

			}
			return tencentArtInfo;

		}

		@Override
		protected void onPostExecute(TencentArtsReInfo tencentArtInfo) {
			// TODO Auto-generated method stub
			super.onPostExecute(tencentArtInfo);
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
				SharedPreferences.Editor editor = context.getSharedPreferences(
						Constant.SAVE_TENCENT_ARTSINFO, context.MODE_PRIVATE).edit();
				for (int i = 0, j = 0; i < pic_354_354.length; i++) {
					if (pic_354_354[i] != null) {

						editor.putString("art_image_"+j, i + ".jpg");
		                editor.putString("art_uri_"+j, uri[i]);
		                editor.putString("art_name_"+j, titile[i]);
		                editor.putString("art_year_" + j, year[i]);
		                editor.putString("art_item_id_" + j, item_id[i]);
		                editor.putString("art_director_"+j, director[i]);
		                editor.putString("art_description_"+j, description[i]);
						j++;
					}
				}
/*				editor.putBoolean("isFirstBoot", false);*/
				editor.commit();
				editor.commit();

				isArtsLoadSucess = true;
				homeHandler.sendEmptyMessageDelayed(Constant.IMAGE_DATA_SUCESS,
						0);
			}
			/*
			 * Message msg=WasuHandler.obtainMessage(Constants.UPDATAWASU_MS);
			 * WasuHandler.sendMessage(msg);
			 */
		}

	}

	public String getPicName(String pic) {
		String[] picStr = pic.split("/");
		return picStr[picStr.length - 1];
	}

	/**
	 * 从assets目录中复制整个文件夹内容
	 * 
	 * @param context
	 *            Context 使用CopyFiles类的Activity
	 * @param oldPath
	 *            String 原文件路径 如：/aa
	 * @param newPath
	 *            String 复制后路径 如：xx:/bb/cc
	 */
	public void copyFilesFassets(Context context, String oldPath, String newPath) {
		try {

			String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名

			if (fileNames.length > 0) {// 如果是目录
				File file = new File(newPath);
				file.mkdirs();// 如果文件夹不存在，则递归
				for (String fileName : fileNames) {
					copyFilesFassets(context, oldPath + "/" + fileName, newPath
							+ "/" + fileName);
				}
			} else {// 如果是文件

				InputStream is = context.getAssets().open(oldPath);
				FileOutputStream fos = new FileOutputStream(new File(newPath));
				byte[] buffer = new byte[1024];
				int byteCount = 0;
				while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
																// buffer字节
					fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
				}
				fos.flush();// 刷新缓冲区
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 如果捕捉到错误则通知UI线程
			// MainActivity.handler.sendEmptyMessage(COPY_FALSE);
		}
	}

	private void initHandler() {
		handlerThread = new HandlerThread("Launcher");
		handlerThread.start();
		HandlerUpdata = new Handler(handlerThread.getLooper());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("titile_position", titile_position);
		outState.putFloat("fromXDelta", fromXDelta);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		titile_position = savedInstanceState.getInt("titile_position");
		toX = savedInstanceState.getFloat("fromXDelta");
		if (titile_position != 0) {
			mAnimationSet = new AnimationSet(true);
			mTranslateAnimation = new TranslateAnimation(fromXDelta, toX, 0f,
					0f);
			initAnimation(mAnimationSet, mTranslateAnimation);
			iv_titile.startAnimation(mAnimationSet);// titile蓝色横条图片的动画切换
			fromXDelta = toX;
		}
		// initTitle(titile_position);
		// if(ISTV){
		int wWidth = savedInstanceState.getInt("wWidth");
		int wHeight = savedInstanceState.getInt("wHeight");
		float wX = savedInstanceState.getFloat("wX");
		float wY = savedInstanceState.getFloat("wY");
		FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(
				wWidth, wHeight);
		layoutparams.leftMargin = (int) wX;
		layoutparams.topMargin = (int) wY;
	}

	@Override
	protected void onStart() {
		super.onStart();
		// initTitle(titile_position);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		if (titile_position != 0 )
		{
			titile_position = 0;
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isRunning = false;
		activityIsRun=false;
        //使能待机
        try {
			TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOnStatus");
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//使能home键
		handlertv.postDelayed(enable_homekey, 800);
		if (surfaceView != null) {
            if (bSystemShutdown == false ) {
                if (STR_STATUS_NONE.equals(SystemProperties.get("mstar.str.suspending", "0"))) {
                    //synchronized (bSync) {
                    //    fullScale = SCALE_FULL;
                    //}
                }
                
                if (createsurface) {
                    if (surfaceView.getVisibility() == View.VISIBLE) {
                        surfaceView.setBackgroundColor(Color.BLACK);
                        surfaceView.setVisibility(View.INVISIBLE);
                    }
                    
                    if (bSystemShutdown == false) {
                        synchronized (bSync) {
                            fullScale = SCALE_FULL;
                        }
                    } else {
                        bSystemShutdown = false;
                    }
                }
                
            } else {
                bSystemShutdown = false;
            }
        } else {
            Log.d(TAG, "---- removeCallbacks(handlerRuntv) ----");
            handlertv.removeCallbacks(handlerRuntv);
        } 
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	       if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	    	   if (homeAcivityIsForeground == false){
		    	   homeAcivityIsForeground = true;
		    	   homeHandler.removeMessages(WindowMessageID.REFLESH_TIME);
		    	   homeHandler.sendEmptyMessage(WindowMessageID.REFLESH_TIME);
	    	   }
	            return true;
	        }
			if (event.getKeyCode() == MKeyEvent.KEYCODE_ASPECT_RATIO)
	        {
	            return true;
	        }
	        if((event.getKeyCode() == KeyEvent.KEYCODE_TV_INPUT)){
	        	if(event.getAction() == KeyEvent.ACTION_DOWN)
	        	{
	            event.setSource(KeyEvent.ACTION_MULTIPLE);
	            //changeInputSource("com.mstar.tv.tvplayer.ui");

	            {
	                short sourcestatus[] = null;

	                try // michael_ktc
	                {
	                    sourcestatus = TvManager.getInstance()
	                            .setTvosCommonCommand("SetAutoSleepOnStatus");
	                } catch (TvCommonException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	            ComponentName componentName = new ComponentName(
	                    "com.mstar.tv.tvplayer.ui", "com.mstar.tv.tvplayer.ui.RootActivity");
	            Intent intent = new Intent(Intent.ACTION_MAIN);
	            intent.addCategory(Intent.CATEGORY_LAUNCHER);
	            intent.setComponent(componentName);
					//ktc nathan.liao add for issue ERP Q160513003 start
		            intent.putExtra("isPowerOn", false);
					//ktc nathan.liao add for issue ERP Q160513003 end
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
	                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	            HomeActivity.this.startActivity(intent);
	            overridePendingTransition(android.R.anim.fade_in,
	    				android.R.anim.fade_out);
	        	}
	            return true;
	        }
		return super.dispatchKeyEvent(event);
	}

	   /**
     * 网络状态发生改变的广播
     */
    private BroadcastReceiver mNetReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION))
            {
                final int event = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE,EthernetManager.ETHERNET_STATE_UNKNOWN);
             
                switch (event) {
                case EthernetStateTracker.EVENT_HW_CONNECTED:
                case EthernetStateTracker.EVENT_INTERFACE_CONFIGURATION_SUCCEEDED://ethernet link
                	if(activityIsRun){
	                    netStatus.setImageResource(R.drawable.com_status_link);
	                    mWireFlag = true;
	                    //weatherHodler.setWeather();
	                   // update();
	                    updateWasuData(true);
                	}
                    break;
                case EthernetStateTracker.EVENT_HW_DISCONNECTED:
                case EthernetStateTracker.EVENT_INTERFACE_CONFIGURATION_FAILED: //ethernet unlink
                	if(activityIsRun){
	                	netStatus.setImageResource(R.drawable.com_status_unlink);
	                    mWireFlag = false;
                	}
                    break;
                default:
                	if(activityIsRun){
                		netStatus.setImageResource(R.drawable.com_status_unlink);
                	}
                    break;
                }
            }else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)
                    ||action.equals(WifiManager.RSSI_CHANGED_ACTION)
                    ||action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
            {
            	if(activityIsRun){
	                updateWifiStatr(intent);
	                //weatherHodler.setWeather();
	                //update();
	                updateWasuData(true);
            	}
            }
            else if(action.equals(PPPOE_STATE_STATUE))
            {
                String pppoeState = intent.getStringExtra(PPPOE_STATE_STATUE);
                if(pppoeState.equals(PPPOE_STATE_CONNECT))//pppoe link 
                {
                	if(activityIsRun){
                		netStatus.setImageResource(R.drawable.pppoe_conected);
                	}
//                    updataWasuAndWeather();
                }
                else if(pppoeState.equals(PPPOE_STATE_DISCONNECT))//pppoe unlink
                {
                	if(activityIsRun){
                		netStatus.setImageResource(R.drawable.com_status_unlink);
                	}
                }
                else if(pppoeState.equals(PPPOE_STATE_AUTHFAILED))//pppoe authfailed
                {
//                    netStatus.setImageResource(R.drawable.com_status_unlink);
                }
                else if(pppoeState.equals(PPPOE_STATE_FAILED))//pppoe state failed
                {
                	if(activityIsRun){
                		netStatus.setImageResource(R.drawable.com_status_unlink);
                	}
                }
            }
                
        }
        
    };

    private void updateWifiStatr(Intent intent)
    {
        final String action = intent.getAction();
        if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            mWifiEnabled = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 
                    WifiManager.WIFI_STATE_UNKNOWN)== WifiManager.WIFI_STATE_ENABLED;
        }else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
            final NetworkInfo networkInfo = (NetworkInfo)
                    intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            boolean wasConnected = mWifiConnected;
            mWifiConnected = networkInfo != null && networkInfo.isConnected();
            if(mWifiConnected && !wasConnected)
            {
                WifiInfo info = (WifiInfo)intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                if(info == null)
                {
                    info = mWifiManager.getConnectionInfo();
                }
                if(info != null){
                    mWifiSsid = huntForSsid(info);
                }else{
                    mWifiSsid = null;
                }
            }else if(!mWifiConnected)
            {
                mWifiSsid = null;
            }
            mWifiLevel = 0;
            mWifiRssi = -200;
        } else if(action.equals(WifiManager.RSSI_CHANGED_ACTION)){
            if(mWifiConnected){
                mWifiRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -200);
                mWifiLevel = WifiManager.calculateSignalLevel(mWifiRssi, 4);
            }
        }
        if (mWifiEnabled == true){
	        if(mWifiConnected)
	        {
	            netStatus.setImageResource(WifiIconArray[mWifiLevel]);
	        }else
	        {
	            netStatus.setImageResource(R.drawable.com_status_unlink);
	        }
        }
        
    }
    
    private String huntForSsid(WifiInfo info)
    {
        String ssid = info.getSSID();
        if(ssid != null)
        {
            return ssid;
        }
        List<WifiConfiguration> networks = mWifiManager.getConfiguredNetworks();
        for(WifiConfiguration net : networks)
        {
            if(net.networkId == info.getNetworkId()){
                return net.SSID;
            }
        }
        return null;
    }
	@Override
	protected void onResume() {
		
		super.onResume();
		SystemProperties.set("mstar.str.storage", "0");
		try {
/*			TvManager.getInstance().setTvosCommonCommand(
					"SetAutoSleepOffStatus");*/
/*			Settings.System.putInt(getContentResolver(),
					"home_hot_key_disable", 1);*/
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// Hisa 2016.03.04 add Freeze function start
		TvPictureManager.getInstance().unFreezeImage();
		Intent intentCancel = new Intent();//取消静像菜单
		intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);		
		sendBroadcast(intentCancel);  
		// Hisa 2016.03.04 add Freeze function end
		rb_app_store.setText(this.getString(R.string.app_store));
		rb_teleplay.setText(this.getString(R.string.tvplay));
		rb_movie.setText(this.getString(R.string.movie));
		rb_arts.setText(this.getString(R.string.tv_show));
	      if (isPowerOn == true && ThirdPartyDtvValue == 0) {
	            Log.i(TAG, "<<<<<<----------PowerOn == true ---------->>>>>");
	            isPowerOn = false;
	            ComponentName componentName = new ComponentName("com.mstar.tv.tvplayer.ui",
	                    "com.mstar.tv.tvplayer.ui.RootActivity");
	            Intent intent = new Intent(Intent.ACTION_MAIN);
	            intent.addCategory(Intent.CATEGORY_LAUNCHER);
	            intent.setComponent(componentName);
	            intent.putExtra("isPowerOn", true);
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
	                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	            startActivity(intent);
	            handlertv.postDelayed(enable_homekey, 800);
	            //使能待机
	            try {
					TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOnStatus");
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        }
	        else {
	        
	        	activityIsRun=true;
	    /*         Log.i(TAG, "<<<<<<----------mstar.str.suspending == STR_STATUS_NONE---------->>>>>");
	            TvPictureManager tvPictureManager = TvPictureManager.getInstance();
	            tvPictureManager.unFreezeImage();
	            movieWidget.startRun();
	            appWidget.InitAppWidget();
	            weatherHodler.setWeather();*/
	            updateNetUI();
           
	           if (isBoot){
	        	   updateWasuData(false);
	        	   isBoot = false;
	           }else{
	        	   updateWasuData(true);
	           }
	           
			   // Hisa 2015.11.05 进入app后时间标志闪动异常 start
	           homeHandler.sendEmptyMessageDelayed(
						WindowMessageID.REFLESH_TIME, 1000);
			   // Hisa 2015.11.05 进入app后时间标志闪动异常 end
	           /*if (isBoot == 0)
	            	isBoot++;*/
	            
	           // update();
	            /* Intent it = new Intent("com.biaoqi.stb.launcherk.onresume");
	            sendBroadcast(it);
	           */
	           handlertv.removeCallbacks(enable_homekey);
	           Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 1);
	           boolean isRecoveryFactory = context.getSharedPreferences(Constant.TV_CONFIG,
	   				Context.MODE_PRIVATE).getBoolean("RecoveryFactory", true);
	           int startSurfaceTime =4000 ;
	           int setPipScaleTime = 5500;
	           if (isRecoveryFactory)
	           {
				 startSurfaceTime = 4000;
				 setPipScaleTime = 5500;
				 context.getSharedPreferences(Constant.TV_CONFIG,
			   				Context.MODE_PRIVATE).edit().putBoolean("RecoveryFactory", false).commit();
	           }
	           else
	           {
	        	  startSurfaceTime = 300;
				  setPipScaleTime = 2500;
	           }
	           if (titile_position == 0){ 
		           if (surfaceView == null) {
		                //Log.i(TAG, "<<<<<<---------- surfaceView == null---------->>>>>");
		                //mcontent.setVisibility(View.VISIBLE);
		                handlertv.postDelayed(handlerRuntv, startSurfaceTime);
		                BackHomeSource();
		                new Handler().postDelayed(new Runnable()
						{
							
							@Override
							public void run()
							{
								if (surfaceView!=null && surfaceView.getVisibility() == View.INVISIBLE)
								{
									surfaceView.setVisibility(View.VISIBLE);
									surfaceView.setBackgroundColor(Color.TRANSPARENT);
								}
								if (mcontent.getVisibility() == View.INVISIBLE)
								{
									 mcontent.setVisibility(View.VISIBLE);
									 mcontent.setAlpha(bgImageAlph);
								}
								setPipscale();
								
								
							}
						}, setPipScaleTime);
		            } else {
		                
		                //Log.i(TAG, "<<<<<<---------- surfaceView != null---------->>>>>");
		                BackHomeSource();
		                Message tmpMsg = new Message();
		                tmpMsg.what = SCALE_SMALL;
		                scaleWinHandler.sendMessageDelayed(tmpMsg, 500);
		                
		            }
	           }else{
	        	   SystemProperties.set("mstar.str.storage", "1");
	       			sendBroadcast(new Intent("com.jrm.filefly.action"));
	           }
	        	//设置不待机
				try {
					TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOffStatus");
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	//禁止home键
	        	Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 1);
	        }
	        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	
		queryInstalledApp();
		isRunning = true;
		String data = "{" + 
    			"\"app_package\":\"com.ktc.tvlauncher\"," + 
    			"\"event_id\":\"launcher_enter_page\"," + 
    			"\"event_type\":3," +
    			"\"data_type\":1," + 
    			"\"pr\":\"LAUNCHER\"," +
    			"\"data\":{}}";
		Log.d("ktc_tencent","启动Launcher:launcher_enter_page:data = " + data.toString());
	    KtcpMtaSdk.mtaReport(HomeActivity.this.getApplicationContext(),data);

		if (usbDeviceCount >= 1)
		{
	    	usb_image.setVisibility(View.VISIBLE);
		}
	    else
	    {
	    	usb_image.setVisibility(View.GONE);
	    }
		
	}
	private int getUsbDeviceCount()
	{
		StorageManager sm = (StorageManager) this.getSystemService(Context.STORAGE_SERVICE);
		String[] paths = (String[]) sm.getVolumePaths();
		return paths.length;
	}

	private boolean ExistUsbDeviceMounted() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
		return false;
	}
	Handler handlertv=new Handler();
    Runnable pip_thread = new Runnable() {
        @Override
        public void run() {
            try {
                if (surfaceView.getVisibility() != View.VISIBLE) {
                    surfaceView.setVisibility(View.VISIBLE);
                    surfaceView.setBackgroundColor(Color.TRANSPARENT);
                }
                Log.i("Hisa", "..PipThread..");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    
    Runnable handlerRuntv = new Runnable() {

        @Override
        public void run() {
            try {
                // setPipscale();
                Log.i("Hisa", "openSurfaceView");
                RelativeLayout surfaceViewLayout = (RelativeLayout)findViewById(R.id.tv_surfaceview_layout);
                surfaceView = new SurfaceView(getApplicationContext());
                surfaceView.setVisibility(View.INVISIBLE);
                surfaceView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                surfaceViewLayout.addView(surfaceView);
                openSurfaceView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handlertv.removeCallbacks(handlerRuntv);
        }
    };
    // /////////////tvsurface///////////////////
    private void openSurfaceView() {
        final SurfaceHolder mSurfaceHolder = surfaceView.getHolder();
        // surfaceParams = new LayoutParams();
        // surfaceParams.x = dimens2px(R.integer.surfaceParams_x);
        // surfaceParams.y = dimens2px(R.integer.surfaceParams_y);
        // surfaceParams.width = dimens2px(R.integer.surfaceParams_width);
        // surfaceParams.height = dimens2px(R.integer.surfaceParams_height);
        // surfaceParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;//
        // WindowManager.LayoutParams.TYPE_APPLICATION
        // // ;//WindowManager.LayoutParams.TYPE_WALLPAPER;
        // surfaceParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        // Log.v(TAG, "openSurfaceView===" + surfaceParams);
        // surfaceParams.gravity = Gravity.TOP | Gravity.LEFT;
        callback = new android.view.SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.v(TAG, "===surfaceDestroyed===");
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (holder == null || holder.getSurface() == null
                            || holder.getSurface().isValid() == false)
                        return;
                    Log.v(TAG, "===surfaceCreated===");
                    if (TvManager.getInstance() != null) {
                        TvManager.getInstance().getPlayerManager().setDisplay(mSurfaceHolder);
                    }
                    // Log.v(TAG, "commonService.getCurrentInputSource()======"
                    // + commonService.getCurrentInputSource());
                    // commonService.setInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
                    // TvManager.getChannelManager().selectProgram(TvManager.getChannelManager().getCurrChannelNumber(),
                    // (short) 0, 0x00);
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.v(TAG, "===surfaceChanged===");
                createsurface = true;
            }
        };
        mSurfaceHolder.addCallback((android.view.SurfaceHolder.Callback) callback);
        // wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        // wm = (WindowManager) getApplicationContext().getSystemService(
        // WINDOW_SERVICE);
        // wm.removeView(surfaceView);
        // wm.addView(surfaceView, surfaceParams);
    }



 
	private Handler scaleWinHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCALE_FULL:
                    synchronized (bSync) {
                        fullScale = SCALE_FULL;
                    }
                    break;
                case SCALE_SMALL:
                    synchronized (bSync) {
                        fullScale = SCALE_SMALL;
                    }

                    handlertv.postDelayed(pip_thread, 100); // delay to show tv
                                                            // window, wait
                                                            // launcher UI.

                    break;
            }
        }
    };
    public void changeInputSource(String packName) {
       // Log.i(TAG, "changeInputSource------------" + packName);
        if (packName != null) {
            if (packName.contentEquals("com.mstar.tv.tvplayer.ui")
                    || packName.contentEquals("mstar.factorymenu.ui")
                    || packName.contentEquals("com.tvos.pip")
                    || packName.contentEquals("com.mstar.tvsetting.hotkey")
                    || packName.contentEquals("com.babao.tvju")
                    || packName.contentEquals("com.babao.socialtv")
                    || packName.contentEquals("com.mstar.appdemo")) {
                Log.i("Hisa", "------------TV AP");
                 synchronized (bSync) {
                        fullScale = SCALE_FULL;
                    }
            } else {
                synchronized (bSync) {
                    if (STR_STATUS_SUSPENDING.equals(SystemProperties.get("mstar.str.suspending",
                            "0"))) {
                        SystemProperties.set("mstar.str.storage", "1");
                    }
                    toChangeInputSource = TvCommonManager.INPUT_SOURCE_STORAGE;
                }
            }
        }
    }
    /**
     * Implementation of the method from SysShutDownReceiver.Callbacks.
     */
    public void setSysShutdown() {
        Log.v(TAG, "------------setSysShutdown=");
        bSystemShutdown = true;
    }
	   //zjd,20140814
    Runnable changeInputSource_thread = new Runnable() {
        @Override
        public void run() {
        	PackageManager packageManager =getPackageManager();
        	Intent intent=null;
        	changeInputSource("com.mstar.tv.tvplayer.ui");
        	{
                short sourcestatus[] = null;

                try // michael_ktc
                {
                    sourcestatus = TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOnStatus");
                } catch (TvCommonException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            intent=packageManager.getLaunchIntentForPackage("com.mstar.tv.tvplayer.ui");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            surfaceView.setVisibility(View.INVISIBLE);
        }
    };

    // delay enableHomekey
    Runnable enable_homekey = new Runnable() {

        @Override
        public void run() {
            Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 0);
        }
    };
    
    public void updateNetUI(){
    	String netState=mNetTool.getNetType();
    	if(netState!=null){
    		if(netState.equals("Wifi")){
    			WifiManager wifiManager=mNetTool.getWifiManager();
    			WifiInfo info = wifiManager.getConnectionInfo();
    			int mWifiLevel = WifiManager.calculateSignalLevel(info.getRssi(),4);
    			netStatus.setImageResource(WifiIconArray[mWifiLevel]);
    		}else if(netState.equals("Ethernet")){
    			netStatus.setImageResource(R.drawable.com_status_link);
    		}else{
    			netStatus.setImageResource(R.drawable.com_status_unlink);
    		}
    	}else{
    		netStatus.setImageResource(R.drawable.com_status_unlink);
    	}
    }
	@Override
	protected void onDestroy() {
		super.onDestroy();
        unregisterReceiver(mNetReceiver);
		// 解除广播
		unregisterReceiver(mReceiver);
		unregisterReceiver(mUsbReceiver);
		unregisterReceiver(mWallReceiver);
		unregisterReceiver(FileFlyReceiver);
        bExitThread = true;
	}


	// 初始化
	protected void initView() {
		loadViewLayout();
		findViewById();
		initReciever();
	}

	private void initReciever() {
	
		registNetReceiver();
		registerPackageReceiver();
		registerWallpaperReceiver();
		registUsbReceiver();
		 registCityChangeReceiver();
		 registerStorageReceiver();
	}
	
    private void registerStorageReceiver() {
    	IntentFilter storageFilter = new IntentFilter();
		/*usbFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		usbFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		usbFilter.addDataScheme("file");*/
    	storageFilter.addAction("com.jrm.filefly.action");
		registerReceiver(FileFlyReceiver, storageFilter);
	}


	private void registNetReceiver(){
        //ethernet status changed
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);  
        //wifi status changed
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);       
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        //pppoe status changed
        intentFilter.addAction(PPPOE_STATE_ACTION);
        registerReceiver(mNetReceiver, intentFilter);
    }
    private void registCityChangeReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.WEATHER_CITY_CHANGE_BROADCAST);  
    }
	private void registUsbReceiver() {
		IntentFilter usbFilter = new IntentFilter();
		usbFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		usbFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		usbFilter.addDataScheme("file");
		registerReceiver(mUsbReceiver, usbFilter);
	}

	private void usbInAndOut() {
		Animation mAnimation;
		mAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.usb_in_out);
		mAnimation.setRepeatCount(5);
		usb_image.startAnimation(mAnimation);
	}

	void updateUsbMassStorageNotification(boolean available) {
		if (available) {
			usb_image.setVisibility(View.VISIBLE);
		} else {
			usb_image.setVisibility(View.GONE);
		}
	}

	public Handler mUsbHanler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case USB_STATE_ON:
				mUsbHanler.sendEmptyMessage(USB_STATE_CHANGE);
				break;
			case USB_STATE_OFF:
				if (usbDeviceCount < 1) {
					mUsbFlag = false;
					mUsbHanler.sendEmptyMessage(UPDATE_USB_ICON);
				} else {
					mUsbFlag = true;
					mUsbHanler.sendEmptyMessage(USB_STATE_CHANGE);
				}
				break;
			case USB_STATE_CHANGE:
				usbInAndOut();
				mUsbHanler.sendEmptyMessage(UPDATE_USB_ICON);
				break;
			case UPDATE_USB_ICON:
				updateUsbMassStorageNotification(mUsbFlag);
				break;
			default:
				break;
			}
		}
	};

	private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
				mUsbFlag = true;
				++usbDeviceCount;
				if (("0".equals(SystemProperties.get("mstar.audio.init", "0")))
						&& (SystemProperties.getBoolean("mstar.str.enable",
								false))) {
					mUsbHanler.sendEmptyMessageAtTime(USB_STATE_ON, 5000);
				} else {
					mUsbHanler.sendEmptyMessage(USB_STATE_ON);
				}
			} else if (action.equals(Intent.ACTION_MEDIA_EJECT))// remove
			{
				mUsbFlag = false;
				--usbDeviceCount;
				if (("0".equals(SystemProperties.get("mstar.audio.init", "0")))
						&& (SystemProperties.getBoolean("mstar.str.enable",
								false))) {
					// mUsbHanler.sendEmptyMessageAtTime(USB_STATE_ON, 5000);
				} else {
					mUsbHanler.sendEmptyMessage(USB_STATE_OFF);
				}
			}
		}
	};

	private void initFragment() {

		fragments = new ArrayList<Fragment>();
		Bundle args = new Bundle();

		af = new AppFragment();
		args.putInt("num", 0);
		af.setArguments(args);
		fragments.add(af);

		rf = new MovieFragment();
		args.putInt("num", 1);
		rf.setArguments(args);
		fragments.add(rf);
		/*
		 * rf = new MovieFragment(); args.putInt("num", 2);
		 * rf.setArguments(args); fragments.add(rf);
		 */
		mf = new TeleplayFragment();
		args.putInt("num", 2);
		mf.setArguments(args);
		fragments.add(mf);

		/*
		 * tf = new TVFragment(); args.putInt("num", 3); tf.setArguments(args);
		 * fragments.add(tf);
		 */
		artsFrag = new ArtsFragment();
		args.putInt("num", 3);
		artsFrag.setArguments(args);
		fragments.add(artsFrag);

		adapter = new FragAdapter(getSupportFragmentManager(), fragments);
		// adapter = new FragAdapter(getSupportFragmentManager(), fragments);
		vpager.setAdapter(adapter);
		vpager.setCurrentItem(0);
		vpager.setPageTransformer(true, new DepthPageTransformer());
		// vpager.setPageTransformer(true, new MyPageTransformer());
		if (ISTV) {
			try {
				Field field = ViewPager.class.getDeclaredField("mScroller");
				field.setAccessible(true);
				FixedSpeedScroller scroller = new FixedSpeedScroller(
						vpager.getContext(), new AccelerateInterpolator());
				field.set(vpager, scroller);
				scroller.setmDuration(700);
				// scroller.setmDuration(300);
			} catch (Exception e) {

			}
		}
		
		
	}

	private void initTitle(int position) {
		if (position != 0) {
			toX = title_group.getChildAt(position).getX();
			mAnimationSet = new AnimationSet(true);
			mTranslateAnimation = new TranslateAnimation(fromXDelta, toX, 0f,
					0f);
			initAnimation(mAnimationSet, mTranslateAnimation);
			iv_titile.startAnimation(mAnimationSet);// titile蓝色横条图片的动画切换
			fromXDelta = toX;
		}
	}

	private void registerWallpaperReceiver() {
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction("com.shenma.changewallpaper");
		registerReceiver(mWallReceiver, mFilter);
	}

	public void update() {
/*    	if(mNetTool.isNetworkConnected(getApplicationContext())){
    		
	        long curTime = System.currentTimeMillis();
	        long validTime=getSharedPreferences(Constants.SAVE_MOVIEINFO, MODE_PRIVATE).getLong("movie_validTime", 0);
	        if(curTime>validTime){
	            //Log.i(Constants.TAG, "-----------UpdataMovieData----------");
	            movieWidget.setPauseRun(true);
	            HandlerUpdata.post(new UpdataMovieData(getApplicationContext(),handlerWasu));
	        }
	        if(!isPass){
	        updateApk();
	        }
    	}else{
    		//Log.e(Constants.TAG,"Launcher movie update,Network not available");
    	}*/
	}

	Handler handlerWasu = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == Constant.UPDATAWASU_MS) {

				/*
				 * movieWidget.setPauseRun(false); if(activityIsRun){
				 * movieWidget.startRun(); }
				 */
			}
		}
	};

	protected void loadViewLayout() {

	}

	protected void findViewById() {
		rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
		Bitmap bmp = mCacheUtils.getBitmapFromMemCache(String
				.valueOf(R.drawable.bg));
		if (bmp == null) {
			bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.main_bg);
			mCacheUtils.addBitmapToMemoryCache(
					String.valueOf(R.drawable.main_bg), bmp);
		}
		rl_bg.setBackgroundDrawable(new BitmapDrawable(getResources(), bmp));
		fl_main = (FrameLayout) findViewById(R.id.fl_main);
		tv_time = (TextView) findViewById(R.id.tv_main_time);
		time_colon = (TextView) findViewById(R.id.time_colon);
		//iv_net_state = (ImageView) findViewById(R.id.iv_net_state);
        netStatus=(ImageView) findViewById(R.id.topbar_net_status);
		iv_titile = (ImageView) findViewById(R.id.iv_titile);
		vpager = (ViewPager) findViewById(R.id.pager);
		vpager.setOffscreenPageLimit(4);
		title_group = (RadioGroup) findViewById(R.id.title_group);
		rb_app_store = (RadioButton) findViewById(R.id.rb_app_store);
		rb_teleplay = (RadioButton) findViewById(R.id.rb_teleplay);
		rb_movie = (RadioButton) findViewById(R.id.rb_movie);

		rb_arts = (RadioButton) findViewById(R.id.rb_arts);

		tv_main_date = (TextView) findViewById(R.id.tv_main_date);
		ll_rb = (LinearLayout) findViewById(R.id.ll_rb);
		tv_update_msg = (TextView) findViewById(R.id.tv_update_msg);

		usb_image = (ImageView) findViewById(R.id.topbar_usb_status);
		WifiIconArray = new int[]{R.drawable.wifi_signal_0,R.drawable.wifi_signal_1,R.drawable.wifi_signal_2,R.drawable.wifi_signal_3};
        mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        mNetTool=new NetTool(getApplicationContext());
        
        


	}



	@Override
	protected void setListener() {
		// 更换背景
		String fileName = sp.getString("wallpaperFileName", null);
		if (fileName != null && !"".equals(fileName)) {
			changeBackImage(fileName);
		}

		fromXDelta = title_group.getChildAt(0).getX();

		for (int i = 0; i < 4; i++){
			RadioButton radio = (RadioButton)title_group.getChildAt(i);
			radio.setTextColor(Color.WHITE);
		}
		/*
		 * 开机进入第一个界面时，app的RadioButton字体设为黄色
		 */
		RadioButton radio = (RadioButton)title_group.getChildAt(0);
		radio.setTextColor(Color.YELLOW);
		
		//Hisa 2015.12.21 tcl在非app页面按信源键再按home键切回app页面tv不显示 start
		titile_position = 0;
		//Hisa 2015.12.21 tcl在非app页面按信源键再按home键切回app页面tv不显示 end
		fromXDelta = toX;
		mAnimationSet = new AnimationSet(true);
		mTranslateAnimation = new TranslateAnimation(fromXDelta, 0f,
				0f, 0f);
		initAnimation(mAnimationSet, mTranslateAnimation);
		iv_titile.startAnimation(mAnimationSet);// titile蓝色横条图片的动画切换
		

		int j = title_group.getChildCount();
		for (int i = 0; i < j; i++) {
			final int index = i;
			View v = title_group.getChildAt(i);
			v.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						// if(ISTV){
						// Hisa 2015.11.05区分主页与下部导航栏的焦点识别 start
						iv_titile.setImageResource(R.drawable.titile_top_yellow);
						iv_titile.setVisibility(View.VISIBLE);
						// Hisa 2015.11.05区分主页与下部导航栏的焦点识别 end

						// }
						((RadioButton) title_group.getChildAt(index))
								.setSelected(true);
						vpager.setCurrentItem(index, true);
					} else {
						((RadioButton) title_group.getChildAt(index))
								.setSelected(false);
						// Hisa 2015.11.05区分主页与下部导航栏的焦点识别 start
						iv_titile.setImageResource(R.drawable.titile_top);
						// Hisa 2015.11.05区分主页与下部导航栏的焦点识别 end
					}
				}
			});
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// if(ISTV){
					// }
					vpager.setCurrentItem(index, true);
				}
			});
		}

		// 导航按下监听
		title_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

			}
		});

		title_group.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

			}
		});

		vpager.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

			}
		});
		/**
		 * ViewPager的PageChangeListener(页面改变的监听器)
		 */
		vpager.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * 滑动viewPage页面获取焦点时更新导航标记
			 */
			@Override
			public void onPageSelected(int position) {
				int i = title_group.getChildCount();

				if (!ISTV) {
					if (position < i) {
						((RadioButton) title_group.getChildAt(position))
								.setChecked(true);
					}
				}
				switch (position) {
				case 0:

					if (!title_group.getChildAt(position).isSelected()) {
						if (null != af.app_typeLogs) {
							af.app_typeLogs[0].requestFocus();
						}
					}
					break;
				case 1:

					if (!title_group.getChildAt(position).isSelected()) {
						if (null != rf.re_typeLogs) {
							rf.re_typeLogs[0].requestFocus();
						}
					}

					break;
				case 2:

					if (!title_group.getChildAt(position).isSelected()) {
						if (null != mf.re_typeLogs) {
							mf.re_typeLogs[0].requestFocus();
						}
					}
				
					break;

				case 3:

					if (!title_group.getChildAt(position).isSelected()) {
						if (null != artsFrag.arts_typeLogs) {
							artsFrag.arts_typeLogs[0].requestFocus();
						}
					}
					break;
				case 4:

					break;
				}
				currentPageNumber = position;
				
				/* 在某一个界面时，响应的RadioButton高亮：黄色
				 * 
				 */
				for (i = 0; i < 4; i++){
					RadioButton radio = (RadioButton)title_group.getChildAt(i);
					radio.setTextColor(Color.WHITE);
				}
				RadioButton radio = (RadioButton)title_group.getChildAt(position);
				radio.setTextColor(Color.YELLOW);
				
				toX = title_group.getChildAt(position).getX();
				mAnimationSet = new AnimationSet(true);
				mTranslateAnimation = new TranslateAnimation(fromXDelta, toX,
						0f, 0f);
				initAnimation(mAnimationSet, mTranslateAnimation);
				iv_titile.startAnimation(mAnimationSet);// titile蓝色横条图片的动画切换
				fromXDelta = toX;

				titile_position = position;
				
				if (titile_position == 0){
					SystemProperties.set("mstar.str.storage", "0");
					if (surfaceView == null) {
		                //Log.i(TAG, "<<<<<<---------- surfaceView == null---------->>>>>");
		                //mcontent.setVisibility(View.VISIBLE);
		                handlertv.postDelayed(handlerRuntv, 300);
		                BackHomeSource();
		            } else {
		                
		                //Log.i(TAG, "<<<<<<---------- surfaceView != null---------->>>>>");
		                BackHomeSource();
		                Message tmpMsg = new Message();
		                tmpMsg.what = SCALE_SMALL;
		                scaleWinHandler.sendMessageDelayed(tmpMsg, 500);
		                
		            }
					
				}
				else{
					SystemProperties.set("mstar.str.storage", "1");
					sendBroadcast(new Intent("com.jrm.filefly.action"));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageScrollStateChanged(int position) {

			}
		});

	}

	/**
	 * titile动画
	 * 
	 * @param _AnimationSet
	 * @param _TranslateAnimation
	 */
	private void initAnimation(AnimationSet _AnimationSet,
			TranslateAnimation _TranslateAnimation) {
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(250L);
	}

	private Handler homeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 调用窗口消息处理函数
			onMessage(msg);
		}
	};

	public void updateWasuData(boolean isBootUpdate) {

		long curTime = System.currentTimeMillis();
		long validTime = getSharedPreferences(Constant.TV_CONFIG, MODE_PRIVATE)
				.getLong("wasu_validTime", 0);
		//if (curTime > validTime) {

			HandlerUpdata.post(new UpdateMovieData(getApplicationContext(),
					homeHandler, isBootUpdate));
			
			HandlerUpdata.post(new UpdateTeleplayData(getApplicationContext(),
					homeHandler, isBootUpdate));
			HandlerUpdata.post(new UpdateArtsData(getApplicationContext(),
					homeHandler, isBootUpdate));
			initFreshTiem();
		//} else {
			//Log.d("Hisa","JUMP_MAIN_UI15");
			//homeHandler.sendEmptyMessageDelayed(Constant.JUMP_MAIN_UI, 0);
		//}

	}

	protected void isNetWorkAvaiable() {
		/*if (Utils.hasNetwork(context)) {
			// 网络可用：成功：超时
			homeHandler.sendEmptyMessageDelayed(Constant.NETWORK_CONNECT, 0);
		} else {
			// 网络不可用：失败
			//showNetDialog(context);
			homeHandler.sendEmptyMessageDelayed(Constant.NETWORK_NOCONNECT, 0);
		}*/
		homeHandler.sendEmptyMessageDelayed(Constant.NETWORK_NOCONNECT, 0);
		homeHandler.sendEmptyMessageDelayed(Constant.NETWORK_CONNECT, 3000);
	}

	/**
	 * @brief 窗口消息处理函数。
	 * @author joychang
	 * @param[in] msg 窗口消息。
	 */
	private void onMessage(final Message msg) {
		if (msg != null) {
			switch (msg.what) {
			case WindowMessageID.ERROR:
				Toast.makeText(getApplicationContext(), "服务器内部异常", 1).show();
				break;
			case WindowMessageID.DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "下载失败", 1).show();
				break;
			case WindowMessageID.GET_INFO_SUCCESS:

				break;

			case WindowMessageID.REFLESH_TIME:
			// Hisa 2015.11.05 进入app后时间标志闪动异常 start
				if ((activityIsRun == false) || (homeAcivityIsForeground == false)){
					homeHandler.removeMessages(WindowMessageID.REFLESH_TIME);
					break;
				}
			// Hisa 2015.11.05 进入app后时间标志闪动异常 start
				tv_time.setText(Utils.getStringTime(" "));
				tv_main_date.setText(Utils.getStringData(context));
				if (time_colon.getVisibility() == View.VISIBLE) {
					time_colon.setVisibility(View.GONE);
				} else {
					time_colon.setVisibility(View.VISIBLE);
				}
				homeHandler.sendEmptyMessageDelayed(
						WindowMessageID.REFLESH_TIME, 1000);
				break;
			case 1001:// 软件更新 总大小
				countSize = (Float) msg.obj;
				break;
			case 1002:// 软件更新 当前下载大小
				currentSize = (Float) msg.obj;
				tv_update_msg.setText("正在下载更新 "
						+ (int) (currentSize / countSize * 100) + "%");
				if (currentSize >= countSize) {
					tv_update_msg.setVisibility(View.GONE);
				}
				break;

			// switch (msg.what) {
			case GET_INFO_SUCCESS:
				loadMainUI();
				break;
			case DETECTION_NET:
				if (Utils.hasNetwork(context)) {
					homeHandler.sendEmptyMessage(GET_INFO_SUCCESS);
				} else {
					homeHandler.sendEmptyMessageDelayed(DETECTION_NET, 3000);
				}
				break;
			case Constant.IMAGE_DATA_SUCESS:
				// 只有assets的数据全部加载到files文件夹下才跳转到主界面
				if ((isAppsLoadSucess == true) && (isMovieLoadSucess == true)
						&& (isTeleplaysLoadSucess == true)
						&& (isArtsLoadSucess == true)) {
					// loadMainUI();
					// 监测网络状态
					isNetWorkAvaiable();
				}
				break;
			case Constant.IMAGE_LOADED:
				isNetWorkAvaiable();
				break;
			case Constant.NETWORK_CONNECT:
				// 网络连接：判断是网络可用(也即是网络良好)，网络状态不佳：NETWORK_SUCESS，NETWORK_TIMEOUT
				if (Utils.hasNetwork(context))
				updateWasuData(false);
				break;
			case Constant.NETWORK_NOCONNECT:
				// 网络断开，跳转到主界面，不更新wasu数据
				homeHandler.sendEmptyMessageDelayed(Constant.JUMP_MAIN_UI, 0);
				break;

			case Constant.NETWORK_SUCESS:
				if (Constant.UPDATE_APPS_SUCESS == msg.arg1) {
					allWasuDataUpate += 1;
				}
				if (Constant.UPDATE_MOVIES_SUCESS == msg.arg1) {
					allWasuDataUpate += 1;
				}
				if (Constant.UPDATE_TELEPLAYS_SUCESS == msg.arg1) {
					allWasuDataUpate += 1;
				}
				if (Constant.UPDATE_ARTS_SUCESS == msg.arg1) {
					allWasuDataUpate += 1;
				}

				if (allWasuDataUpate == 4) {// 表示四个界面的wasu数据全部更新完成
					homeHandler.sendEmptyMessageDelayed(Constant.JUMP_MAIN_UI,
							0);
				}

				break;
			case Constant.NETWORK_TIMEOUT:
				if ((is_timeout == true) && (timeout_flag == false)) {// 超时
					// 网络超时，跳转到主界面，不更新wasu数据
					timeout_flag = true;
					homeHandler.sendEmptyMessageDelayed(Constant.JUMP_MAIN_UI,
							0);
				}
				break;
			case Constant.JUMP_MAIN_UI:
				loadMainUI();
				break;
			// }

			}
		}
	}
	class InputSourceThread implements Runnable {
        @Override
        public void run() {
            Log.i("Hisa", "InputSourceThread start");
            int tmpInputSource;
            int tmpScale;
            while (!bExitThread) {
            	//Log.i("Hisa", "InputSourceThread running!");
                synchronized (bSync) {
                    tmpInputSource = toChangeInputSource;
                    toChangeInputSource = TvCommonManager.INPUT_SOURCE_NONE;
                    tmpScale = fullScale;
                    fullScale = SCALE_NONE;
                }
                if ((tmpInputSource != TvCommonManager.INPUT_SOURCE_NONE)) {
                    int currentSource;
                    if (tmpInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                        currentSource = commonService.getCurrentTvInputSource();
                        Log.v("Hisa", "tmpInputSource = INPUT_SOURCE_ATV;currentSource = " + currentSource);
                        if (currentSource==TvCommonManager.INPUT_SOURCE_STORAGE) {
                        	int curSource = TvCommonManager.getInstance().getPowerOnSource().ordinal(); 

                            Log.v("Hisa", "currentSource=====INPUT_SOURCE_STORAGE");
                            if ((curSource >= 0)
                                    && (curSource <= EnumInputSource.E_INPUT_SOURCE_NONE.ordinal())) {
                                Log.v("Hisa", "PowerOnSource=====curSource = " + curSource);
                                commonService.setInputSource(curSource);
                                if (curSource == TvCommonManager.INPUT_SOURCE_ATV) {
                                    int channel = tvChannelManager.getCurrentChannelNumber();
                                    if ((channel < 0) || (channel > 255)) {
                                        channel = 0;
                                    }
                                    tvChannelManager.setAtvChannel(channel);
                                } else if (curSource ==TvCommonManager.INPUT_SOURCE_DTV) {
                                	
                                	tvChannelManager.selectProgram(
                                			tvChannelManager.getCurrentChannelNumber(),
                                            TvChannelManager.SERVICE_TYPE_DTV);
                                	
                                }
                            }
                        }
                    } else {
                    	 Log.v("Hisa", "tmpInputSource != INPUT_SOURCE_ATV;tmpInputSource = " + tmpInputSource);
                    	TvCommonManager.getInstance().setInputSource(tmpInputSource);
                    }
                }
//                Log.i(TAG,"tmpScale:"+ tmpScale);
                if (tmpScale == SCALE_SMALL) {
                    setPipscale();
                } else if (tmpScale == SCALE_FULL) {
                    setFullscale();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i("Hisa", "InputSourceThread is end!");
        }
    }
    private void setFullscale() {
        try {
            VideoWindowType videoWindowType = new VideoWindowType();
            videoWindowType.height = 0xffff;
            videoWindowType.width = 0xffff;
            videoWindowType.x = 0xffff;
            videoWindowType.y = 0xffff;
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getPictureManager()
                        .selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
                TvManager.getInstance().getPictureManager().setDisplayWindow(videoWindowType);
                TvManager.getInstance().getPictureManager().scaleWindow();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "setFullscale");
    }
	private void setPipscale() {
	        try {
	            if (STR_STATUS_SUSPENDING.equals(SystemProperties.get("mstar.lvds-off", "1"))) {
	                Log.i(TAG, "power off!!!");
	            } else {
	                VideoWindowType videoWindowType = new VideoWindowType();
	                if(panel_height==1080&&panel_width==1920){//HD
	                    videoWindowType.x = 250;//
	                    videoWindowType.y = 210;//
	                    videoWindowType.width =809;
	                    videoWindowType.height =438;
	                }else if(panel_height==2160&&panel_width==3840){//4K
	                	
	/*               	videoWindowType.x = 103*2;
	                    videoWindowType.y = (136+7)*2;
	                    videoWindowType.width =(770+39)*2;
	                    videoWindowType.height =(420+18)*2;*/
	                   	videoWindowType.x = 518;
	                    videoWindowType.y = 412;
	                    videoWindowType.width =1594;
	                    videoWindowType.height =880;
	                }
	                else if(panel_height==768&&panel_width==1366){//SD
	                    videoWindowType.x = 182;//184
	                    videoWindowType.y = 146;//148
	                    videoWindowType.width =570;//570
	                    videoWindowType.height =316;//336
	                }
	                Log.i(TAG, "TvManager.getInstance():"+(TvManager.getInstance()!=null)+
	                        "  TvManager.getInstance().getPictureManager() != null:"+(TvManager.getInstance().getPictureManager() != null));

	                if (TvManager.getInstance() != null) {
	                    if (TvManager.getInstance().getPictureManager() != null) {
	                        TvManager.getInstance().getPictureManager()
	                                .selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
	                    }
	                }
	                if (TvManager.getInstance() != null) {
	                    if (TvManager.getInstance().getPictureManager() != null) {
	                        Log.i(TAG, " ??????????????? x : " + videoWindowType.x + "   y : "
	                                + videoWindowType.y + "       w : " + videoWindowType.width
	                                + "       h : " + videoWindowType.height);
	                        TvManager.getInstance().getPictureManager()
	                                .setDisplayWindow(videoWindowType);
	                    }
	                }
	                if (TvManager.getInstance() != null) {
	                    if (TvManager.getInstance().getPictureManager() != null) {
	                        TvManager.getInstance().getPictureManager().scaleWindow();
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        Log.i(TAG, "***************>>>>>>>>>>>>>setPipscale");
	    }
	private void initFreshTiem() {
		// 设置刷新周期为1天

		SharedPreferences.Editor editor = context.getSharedPreferences(
				Constant.TV_CONFIG, MODE_PRIVATE).edit();
		long validTime = System.currentTimeMillis();
		validTime = validTime + 1 * 24 * 60 * 60 * 1000;
		//validTime = validTime + 1;
		editor.putLong("wasu_validTime", validTime);
		editor.commit();
	}

	private void loadMainUI() {

		initFragment();
		setListener();

		rb_app_store.setChecked(true);

		homeHandler.sendEmptyMessageDelayed(WindowMessageID.REFLESH_TIME, 1000);// 刷新时间
		isPageLoadSucess = true;// 表示四个页面数据全部加载成功
		// if (isFirstBoot == true)
		if(dialog !=null )
		dialog.dismiss();

		updateApk();
		
		// Hisa 2015.12.21 tcl华数数据刷新时tv窗口无显示 start
		titile_position = 0;
		if (activityIsRun == true){
			SystemProperties.set("mstar.str.storage", "0");
			if (surfaceView == null) {
	            //Log.i(TAG, "<<<<<<---------- surfaceView == null---------->>>>>");
	            //mcontent.setVisibility(View.VISIBLE);
	            handlertv.postDelayed(handlerRuntv, 300);
	            BackHomeSource();
	        } else {
	            
	            //Log.i(TAG, "<<<<<<---------- surfaceView != null---------->>>>>");
	            BackHomeSource();
	            Message tmpMsg = new Message();
	            tmpMsg.what = SCALE_SMALL;
	            scaleWinHandler.sendMessageDelayed(tmpMsg, 500);
	            
	        }
		}
		// Hisa 2015.12.21 tcl华数数据刷新时tv窗口无显示 end
	}



	private void registerPackageReceiver() {
		// 注册广播
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		mFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		mFilter.addDataScheme("package");
		registerReceiver(mReceiver, mFilter);
	}



	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			queryInstalledApp();
		}
	};
	private BroadcastReceiver mWallReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String fileName = intent.getStringExtra("wallpaperFileName");
			if (fileName == null)
				return;
			sp.edit().putString("wallpaperFileName", fileName).commit();
			changeBackImage(fileName);
			Utils.showToast(context, R.string.updata_bg, R.drawable.toast_smile);
		}
	};

	public void queryInstalledApp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				packLst = getPackageManager().getInstalledPackages(0);
			}
		}).start();
	}

	private void changeBackImage(String fileName) {
		if (fileName == null)
			return;
		if (context.getFilesDir().exists()) {
			Bitmap bmp = null;
			if ("开".equals(sp.getString("open_blur", "关"))) {
				// 开启高斯模糊背景效果
				bmp = mCacheUtils.getBitmapFromMemCache(context.getFilesDir()
						.getAbsolutePath() + "/" + fileName + "_blur");
				if (bmp == null) {
					try {
						Bitmap tempBmp = mCacheUtils
								.getBitmapFromMemCache(context.getFilesDir()
										.getAbsolutePath() + "/" + fileName);
						if (tempBmp == null) {
							tempBmp = BitmapFactory.decodeFile(context
									.getFilesDir().getAbsolutePath()
									+ "/"
									+ fileName);
							mCacheUtils.addBitmapToMemoryCache(context
									.getFilesDir().getAbsolutePath()
									+ "/"
									+ fileName, tempBmp);
						}
						bmp = BlurUtils.doBlur(tempBmp, 7, false);
						mCacheUtils.addBitmapToMemoryCache(context
								.getFilesDir().getAbsolutePath()
								+ "/"
								+ fileName + "_blur", bmp);
					} catch (OutOfMemoryError oom) {
						mCacheUtils.clearAllImageCache();
						Bitmap tempBmp = mCacheUtils
								.getBitmapFromMemCache(context.getFilesDir()
										.getAbsolutePath() + "/" + fileName);
						if (tempBmp == null) {
							tempBmp = BitmapFactory.decodeFile(context
									.getFilesDir().getAbsolutePath()
									+ "/"
									+ fileName);
							mCacheUtils.addBitmapToMemoryCache(context
									.getFilesDir().getAbsolutePath()
									+ "/"
									+ fileName, tempBmp);
						}
						bmp = BlurUtils.doBlur(tempBmp, 7, false);
						mCacheUtils.addBitmapToMemoryCache(context
								.getFilesDir().getAbsolutePath()
								+ "/"
								+ fileName + "_blur", bmp);
					}
				}
				rl_bg.setBackgroundDrawable(new BitmapDrawable(getResources(),
						bmp));

			} else {
				bmp = mCacheUtils.getBitmapFromMemCache(context.getFilesDir()
						.getAbsolutePath() + "/" + fileName);
				if (bmp == null) {
					try {
						bmp = BitmapFactory.decodeFile(context.getFilesDir()
								.getAbsolutePath() + "/" + fileName);
						mCacheUtils.addBitmapToMemoryCache(context
								.getFilesDir().getAbsolutePath()
								+ "/"
								+ fileName, bmp);
					} catch (OutOfMemoryError oom) {
						mCacheUtils.clearAllImageCache();
						bmp = BitmapFactory.decodeFile(context.getFilesDir()
								.getAbsolutePath() + "/" + fileName);
						mCacheUtils.addBitmapToMemoryCache(context
								.getFilesDir().getAbsolutePath()
								+ "/"
								+ fileName, bmp);
					}
				}
				rl_bg.setBackgroundDrawable(new BitmapDrawable(getResources(),
						bmp));
			}
		}
	}
	public static boolean getIsTV() {
		return ISTV == null ? true : ISTV;
	}

	public void updateApk() {
		if (mCheckNewVersionTask != null
				&& mCheckNewVersionTask.getStatus() != AsyncTask.Status.FINISHED) {
			mCheckNewVersionTask.cancel(true);
		}

		mCheckNewVersionTask = new CheckNewVersionTask();
		mCheckNewVersionTask.execute();
	}

	/**
	 * 检查新版本异步任务
	 * 
	 * @author yejb
	 * 
	 */
	private class CheckNewVersionTask extends AsyncTask<Void, Void, Version> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Version doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return new UpdateApk(getApplicationContext()).hasNewVersion();
		}

		@Override
		protected void onPostExecute(Version result) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result != null) {
					showNewVersionDialog(result);

				}
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 显示是否下载新版本对话框
	 * 
	 * @param version
	 */

	private void showNewVersionDialog(final Version version) {
		if (alertDialog == null) {
			alertDialog = new AlertDialog.Builder(this)
					.setTitle(R.string.new_version_text)
					.setMessage(version.getIntroduction())
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									UpdateApk update = new UpdateApk(
											HomeActivity.this);
									update.setVersion(version);
									update.checkUpdate();
								}
							})
					.setOnKeyListener(new OnKeyListener() {
						
						@Override
						public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
							// TODO Auto-generated method stub
							if (arg1 == KeyEvent.KEYCODE_BACK) {
								homeHandler.removeMessages(WindowMessageID.REFLESH_TIME);
								homeAcivityIsForeground = true;
								homeHandler.sendEmptyMessage(WindowMessageID.REFLESH_TIME);
							}
							return false;
						}
					})
					.setNeutralButton(R.string.skip_this_version,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									isPass = true;
									homeHandler.removeMessages(WindowMessageID.REFLESH_TIME);
									homeAcivityIsForeground = true;
									homeHandler.sendEmptyMessage(WindowMessageID.REFLESH_TIME);
								}
							}).create();
			
		}
		if (!alertDialog.isShowing()) {
			alertDialog.show();
		
		}
		// Hisa 2015.11.10 当弹出更新apk对话框时,时间闪动异常 start
		homeAcivityIsForeground = false;
		// Hisa 2015.11.10 当弹出更新apk对话框时,时间闪动异常 end
	}
	



	/**
	 * @class WindowMessageID
	 * @brief 内部消息ID定义类。
	 * @author joychang
	 */
	private class WindowMessageID {

		/**
		 * @brief 刷新数据。
		 */
		public static final int REFLESH_TIME = 0x00000005;

		/**
		 * @brief 请求出错。
		 */
		public final static int ERROR = 0x00000004;
		// 版本更新的消息
		private final static int DOWNLOAD_ERROR = 0x000000010;
		private final static int DOWNLOAD_SUCCESS = 0x000000012;
		private final static int GET_INFO_SUCCESS = 0x000000013;
	}
//ktc 2015.11.05 nathan.liao add for is power on start	
    public boolean isPowerOn() {
        Log.d(TAG, "Is Fist Power On: " + (SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)));

        if (!SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)) {
            SystemProperties.set(IS_POWER_ON_PROPERTY, "true");
            return true;
        } else {
            return false;
        }
    }
//ktc 2015.11.05 nathan.liao add for is power on end
    //private BroadcastReceiver FileFlyReceiver = new BroadcastReceiver {
    private BroadcastReceiver FileFlyReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Log.i("Jason", ">>>>>>>>>>>>>>>>>>>>>>>>>>  this is box receive com.jrm.filefly.action");
            String action = arg1.getAction();
            if (FILE_FLY_LAUNCH.equals(action)) {
                synchronized (bSync) {
                    if (STR_STATUS_SUSPENDING.equals(SystemProperties.get("mstar.str.suspending",
                            "0"))) {
                        SystemProperties.set("mstar.str.storage", "1");
                    }
                    //Hisa 2015.12.21 tcl在非app页面按信源键再按home键切回app页面tv不显示 start
                    //if ((titile_position == 0) && (activityIsRun == true)){
                    	//
                    //}else{
                    	toChangeInputSource = TvCommonManager.INPUT_SOURCE_STORAGE;
                   // }
					//Hisa 2015.12.21 tcl在非app页面按信源键再按home键切回app页面tv不显示 end
                }
            }
        }
    };

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		handlertv.postDelayed(changeInputSource_thread, 100); //zjd,20140814. delay to change,wait pip_thread finish
	}
}
