package com.ktc.tvlauncher.application;



import java.io.File;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.ktc.tvlauncher.R;
import com.ktc.tvlauncher.utils.Constant;
import com.ktc.tvlauncher.utils.Logger;
import com.ktc.tvlauncher.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import java.util.ArrayList;
import android.os.SystemProperties;
import com.ktcp.mta.sdk.KtcpMtaSdk;

/**
 * @Description 客户端主程序类 ，对应用程序进行初始化，完了话。
 * @author joychang
 * 
 */
public final class MyApplication extends Application {
	
	private final static String TAG = "MyApplication";
	private String onPlay;
	private static File cacheDir;
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;//策略监控 true开启 false关闭
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter technologyfilter = new IntentFilter();
		technologyfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "UniversalImageLoader/Cache");
		
		
		//创建当前程序sd卡资源存放目录
		makedir();
		init();		
		initImageLoader(getApplicationContext());
		
		// Tencent Video MTA init
				Context context = this.getApplicationContext();
				String pr = "LAUNCHER";
				ArrayList<String> pkgList = new ArrayList<String>();
				pkgList.add("com.ktc.tvlauncher");
				String deviceBD = SystemProperties.get("ro.product.model");
				String deviceMD = SystemProperties.get("ktc.customer.tvid");
				String pt = "KTC";
				String channelId = "10032";
				String grantTag = "CNTV";
				String mtaKey = "AYA771SZKF5P";
				//Log.d("Jason","MyApplication:onCreaate:deviceBD = " + deviceBD);
				KtcpMtaSdk.init(context, pr, pkgList, deviceBD, deviceMD, pt, channelId, grantTag, mtaKey);
				
	}
	
	private void makedir(){
		File file = new File(Constant.PUBLIC_DIR);
		if(!file.exists()){
			//每次启动检测该目录是否存在，不存在则创建这个目录
			boolean flag = file.mkdir();
		}else {
			//如果存在，则删除该目录下的所有apk文件
			Utils.deleteAppApks(Constant.PUBLIC_DIR);
		}
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	
	public void setOnPlay(String onPlay){
		this.onPlay  = onPlay;
	}
	
	public String getOnPlay(){
		
		return onPlay;
	}
	
	public String getTechnology(){
		WifiManager mm = null;
		return technology;
	}
	/**
	 * 初始化工作
	 */
	@SuppressWarnings("unused")
	protected void init() {
 
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().penaltyDeath().build());
		}

	}
	
	
	/*public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)//线程优先级
		.threadPoolSize(3)//线程3个
		.denyCacheImageMultipleSizesInMemory()//当你显示一个图像在一个小的ImageView后来你试图显示这个图像（从相同的URI）在一个大的，大尺寸的ImageView解码图像将被缓存在内存中为先前解码图像的小尺寸。
		.memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))//设置内存缓存大小
		.discCache(new UnlimitedDiscCache(cacheDir))//硬盘缓存目录
		.discCacheFileNameGenerator(new Md5FileNameGenerator())//生产缓存的名称
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}*/
	
public static void initImageLoader(Context context) {
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		 opts.inSampleSize = 2;
		 
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
	    .cacheInMemory(true)
	    .cacheOnDisc(true)
	    .decodingOptions(opts)
	    .build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.defaultDisplayImageOptions(defaultOptions)
		    .memoryCacheExtraOptions(480, 800) // default=device screen dimensions
		    .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
		    .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
		    .threadPoolSize(5) // default
		    .threadPriority(Thread.NORM_PRIORITY - 2) 
		    .tasksProcessingOrder(QueueProcessingType.FIFO) 
		    .discCacheFileNameGenerator(new Md5FileNameGenerator())
		    .denyCacheImageMultipleSizesInMemory()
		    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		    .memoryCacheSize(2 * 1024 * 1024)
		    .discCacheSize(50 * 1024 * 1024)
		    .discCacheFileCount(100)
		    .build();
		ImageLoader.getInstance().init(config);
	}
/*	*//**
	 * 电池信息监测广广播
	 *//*
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				int status = intent.getIntExtra("status", 0);
				int health = intent.getIntExtra("health", 0);
				boolean present = intent.getBooleanExtra("present", false);
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 0);
				int icon_small = intent.getIntExtra("icon-small", 0);
				int plugged = intent.getIntExtra("plugged", 0);
				int voltage = intent.getIntExtra("voltage", 0);
				int temperature = intent.getIntExtra("temperature", 0);
				technology = intent.getStringExtra("technology");
				
				String statusString = "";

				switch (status) {
				case BatteryManager.BATTERY_STATUS_UNKNOWN:
					statusString = "unknown";
					break;
				case BatteryManager.BATTERY_STATUS_CHARGING:
					statusString = "charging";
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING:
					statusString = "discharging";
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
					statusString = "not charging";
					break;
				case BatteryManager.BATTERY_STATUS_FULL:
					statusString = "full";
					break;
				}

				String healthString = "";

				switch (health) {
				case BatteryManager.BATTERY_HEALTH_UNKNOWN:
					healthString = "unknown";
					break;
				case BatteryManager.BATTERY_HEALTH_GOOD:
					healthString = "good";
					break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT:
					healthString = "overheat";
					break;
				case BatteryManager.BATTERY_HEALTH_DEAD:
					healthString = "dead";
					break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
					healthString = "voltage";
					break;
				case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
					healthString = "unspecified failure";
					break;
				}

				String acString = "";
				switch (plugged) {
				case BatteryManager.BATTERY_PLUGGED_AC:
					acString = "plugged ac";
					break;
				case BatteryManager.BATTERY_PLUGGED_USB:
					acString = "plugged usb";
					break;
				}
			}
		}
	};*/
	
/*	*//**
	 * 注册网络变动的广播接收
	 *//*
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
				if (networkInfo == null || !networkInfo.isConnected()) {
					Utils.showToast(context,
							getString(R.string.tvback_str_data_loading_error),
							R.drawable.toast_err);
				}
			}
		}
	};*/
	/**
	 * 结束操作
	 */
    protected void _finish() {

    }
    /**
     * 系统内存过低，进行内存回收策略
     */
    @Override
    public void onLowMemory() {
    	// TODO Auto-generated method stub
    	super.onLowMemory();
    }
    protected static String technology = "";
}
