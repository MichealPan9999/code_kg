package com.ktc.launcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.EthernetManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.SQLException;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnHoverListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ktc.launcher.R;
import com.ktc.launcher.bean.AppItem;
import com.ktc.launcher.bean.InputSourceItem;
import com.ktc.launcher.boardproperty.BoardProperty;
import com.ktc.launcher.boardsouce.SourceContext;
import com.ktc.launcher.boardsouce.T5C2HasDvbsHasCiSourceState;
import com.ktc.launcher.boardsouce.T5C2HasDvbsNonCiSourceState;
import com.ktc.launcher.boardsouce.T5C2NonDvbsHasCiSourceState;
import com.ktc.launcher.boardsouce.T5C2NonDvbsNonCiSourceState;
import com.ktc.launcher.boardsouce.T8C2HasDvbsHasCiSourceState;
import com.ktc.launcher.boardsouce.T8C2HasDvbsNonCiSourceState;
import com.ktc.launcher.boardsouce.T8C2NonDvbsHasCiSourceState;
import com.ktc.launcher.boardsouce.T8C2NonDvbsNonCiSourceState;
import com.ktc.launcher.constants.Constants;
import com.ktc.launcher.mode.AppInfo;
import com.ktc.launcher.ui.IAppWidget;
import com.ktc.launcher.utils.NetTool;
import com.ktc.launcher.utils.ScrollTextView;
import com.ktc.launcher.utils.Utils;
import com.ktc.launcher.view.SourceInfoPopWindow;
import com.ktc.launcher.view.SourceInfoPopWindow.OnChoiceListener;
import com.mstar.android.MKeyEvent;
import cn.ktc.library.update.Update;
import cn.ktc.library.update.Version;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.pppoe.PppoeManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.Enum3dType;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoDisplayFormat;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import android.app.Dialog;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.mstar.android.MIntent;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class LauncherActivity extends Activity
		implements OnClickListener, OnFocusChangeListener, OnKeyListener, OnTouchListener, OnHoverListener {
	private static final String TAG = "LauncherActivity";
	private static final String STR_STATUS_NONE = "0";
	private static final String STR_STATUS_SUSPENDING = "1";

	// ////////////////////////////net////////////////////////
	private boolean mWireFlag = false;
	boolean mWifiEnabled = false;
	boolean mWifiConnected = false;
	int mWifiRssi = 0;
	int mWifiLevel = 0;
	private int[] WifiIconArray;
	public final static int WIFI_LEVEL_COUNT = 4;
	private WifiManager mWifiManager;
	private NetTool mNetTool;
	private String mWifiSsid;

	public final static String PPPOE_STATE_ACTION = "android.net.pppoe.PPPOE_STATE_ACTION";
	public final static String PPPOE_STATE_STATUE = "PppoeStatus";
	public static final String PPPOE_STATE_CONNECT = "connect";
	public static final String PPPOE_STATE_DISCONNECT = "disconnect";
	public static final String PPPOE_STATE_AUTHFAILED = "authfailed";
	public static final String PPPOE_STATE_FAILED = "failed";
	public final static short T_SystemSetting_IDX = 0x19;
	// ////////////////notification////////////////////////
	private final int USB_STATE_ON = 100001; // usb storage on
	private final int USB_STATE_OFF = 100002; // usb storage off
	private final int UPDATE_USB_ICON = 100005; // update usb icon
	private final int USB_STATE_CHANGE = 100006; // usb change state
	private ImageView usb_image;
	public boolean mUsbFlag = false;
	public int usbDeviceCount = 0;

	private IAppWidget appWidget;
	private ImageView netStatus;
	private EnumInputSource toChangeInputSource = EnumInputSource.E_INPUT_SOURCE_NONE;
	private Boolean bSync = true;

	private TvCommonManager commonService;
	private TvChannelManager tvChannelManager;
	private DisplayManager mDisplayManager;

	private final String FILE_FLY_LAUNCH = "com.jrm.filefly.action";

	private HandlerThread handlerThread;
	private Handler HandlerUpdata;
	private CheckNewVersionTask mCheckNewVersionTask;

	private int panel_height;
	private int panel_width;
	private Boolean isPass = false;

	private Dialog alertDialog;
	private Boolean NeedDelay = true;
	private boolean activityIsRun = false;

	private SharedPreferences mSpOobe;
	private SharedPreferences.Editor mSpOobeEditor;
	// lixq 20151119 start
	// 淇敼launcher鍒ゆ柇绗竴娆″紑鏈烘柟娉曘�傞伩鍏峚ndroid娓呯悊鎺塴auncher锛屽鑷寸殑浠庡簲鐢ㄩ��鍑哄悗杩涘叆鍒扮數瑙嗙晫闈�
	// private boolean PowerOn;
	private final String IS_POWER_ON_PROPERTY = "mstar.launcher.1stinit";
	// lixq 20151119 end

	// zhang
	private HorizontalScrollView hs_content;

	private ImageView iv_tv_source;
	private ImageView iv_setting;

	private RelativeLayout rl_view_first;
	private TextView tv_time;
	private TextView tv_date;
	private TextView tv_am_pm;

	private RelativeLayout rl_view_second;
	private ImageView iv_view_icon2;
	private ScrollTextView tv_view_txt2;

	private RelativeLayout rl_view_third;
	private ImageView iv_view_icon3;
	private ScrollTextView tv_view_txt3;

	private RelativeLayout[] app_layout = new RelativeLayout[9];
	
	private String [] left_view_packages=new String[2];
	private List<AppItem> appItems=new ArrayList<AppItem>();
	
	SharedPreferences share=null;
	private boolean hasDvb;
	private boolean hasT4C2Board;
	private TvCommonManager tvCommonmanager;
	private TvPipPopManager tvPipPopManager = null;
	private List<InputSourceItem> sourceItems;
	private int inputSource = TvCommonManager.INPUT_SOURCE_ATV;
	boolean isSourceNeedSwitch = false;
	final static int SETIS_END_COMPLETE = -101;
	final static int SETIS_START = -100;
	private static int systemAutoTime = 0;
	private BroadcastReceiver mTimeTickReciver;


	//add
	private ScrollTextView[] app_name = new ScrollTextView[9];

	private String tvName1, tvName2;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SETIS_START) {
			} else if (msg.what == SETIS_END_COMPLETE) {
				startActivity("com.mstar.tv.tvplayer.ui");
			}
		};
	};
	private static final int MESG_TIME = 0X111;
	private static final int MESG_DATE = 0X112;
	Handler timeHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESG_TIME:
				String time=(String) msg.obj;
				if(time.length()>5){
					String[] strings = time.split(":");
					tv_time.setText(strings[0]+":"+strings[1]);
					tv_am_pm.setText(strings[2]);
				}else{
					tv_time.setText(time);
					tv_am_pm.setText("");
				}
				break;
			case MESG_DATE:
				String date=(String) msg.obj;
				tv_date.setText(date);
				break;
			}
		};
	};
	SourceContext sourceContext = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
		super.onCreate(savedInstanceState);
		commonService = TvCommonManager.getInstance();
		tvCommonmanager = TvCommonManager.getInstance();
		tvPipPopManager = TvPipPopManager.getInstance();
		tvChannelManager = TvChannelManager.getInstance();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		setContentView(R.layout.activity_main);
		updateWallpaperVisibility(false);
		InitData();
		findView();
		InitHandler();
		registNetReceiver();
		registUsbReceiver();
		String subtitleEncoding = getEncodingFromPersistProp();
		updateDatabase_systemsetting("bStrStatus",1);
		SystemProperties.set("persist.sys.str.status", "true");
		SystemProperties.set("sys.ms.subtitle.language", subtitleEncoding);
		setEncodingFromPersistProp(subtitleEncoding);

		mSpOobe = getPreferences(Activity.MODE_PRIVATE);
		mSpOobeEditor = mSpOobe.edit();
		// 2015.3.5 add
		// PowerOn=true; //lixq 20151119
		// 2015.3.5 add
		initLeftApps();
		initSourceData();
		calendarLayoutInit();
	}

	// pzq get&set subtitle encoding start
	private String getEncodingFromPersistProp() {
		String curEncoding = SystemProperties.get("persist.encode.subtitle");
		if (curEncoding == null) {
			curEncoding = "UTF-8";
		}
		return curEncoding;
	}

	private void setEncodingFromPersistProp(String objString) {
		if (objString != null) {
			SystemProperties.set("persist.encode.subtitle", objString);
		}
	}

	// pzq get&set subtitle encoding end
	public void InitData() {
		try {
			panel_height = TvManager.getInstance().getPictureManager().getPanelWidthHeight().height;
			panel_width = TvManager.getInstance().getPictureManager().getPanelWidthHeight().width;
			Log.v("zhanghz",panel_height + "  " + panel_width);
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WifiIconArray = new int[] { R.drawable.wifi_signal_0, R.drawable.wifi_signal_1, R.drawable.wifi_signal_2,
				R.drawable.wifi_signal_3 };
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mNetTool = new NetTool(getApplicationContext());

		// zhanghz
		share = this.getSharedPreferences(Constants.SAVE_APPLIST, this.MODE_PRIVATE);
		boolean isFirstBoot = share.getBoolean("isFirstBoot", true);
		if(isFirstBoot)
			loadResourceFormXml();
		else
			loadFromPreference();
	}

	private void loadFromPreference() {
		left_view_packages[0]=share.getString("left_second", "");
		left_view_packages[1]=share.getString("left_third", "");
	}

	private void loadResourceFormXml() {
		appItems.clear();
		InputStream is=null;
		XmlPullParserFactory factory;
		AppItem appItem=null;
		try {
			is=this.getResources().getAssets().open("PreApp.xml");
			factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(is, "UTF-8");
            int evtType = xpp.getEventType();
            while(evtType!=XmlPullParser.END_DOCUMENT){
            	switch (evtType) {
				case XmlPullParser.START_TAG:
					String tag = xpp.getName();
					if (tag.equals("app")) {
						String position=xpp.getAttributeValue(0);
						appItem=new AppItem();
						appItem.setPosition(position);
					}else if(tag.equals("apppackage")){
						String apppackage=xpp.getAttributeValue(0);
						appItem.setPackageName(apppackage);
						appItems.add(appItem);
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
            	//鑾峰緱涓嬩竴涓妭鐐圭殑淇℃伅
                evtType = xpp.next();
            }
		} catch (Exception e) {
			Log.v("zhanghz", "xml error"+e.toString());
		}
		Editor editor = share.edit();
		for(int i=0;i<appItems.size();i++){
			appItem=appItems.get(i);
			if(appItem.getPosition().startsWith("left")){
				if(appItem.getPosition().equals("left_second")){
					left_view_packages[0]=appItem.getPackageName();
					editor.putString("left_second", appItem.getPackageName());
				}else if(appItem.getPosition().equals("left_third")){
					left_view_packages[1]=appItem.getPackageName();
					editor.putString("left_third", appItem.getPackageName());
				}
			}else if(appItem.getPosition().startsWith("right")){
				int index=Integer.parseInt(appItem.getPosition().charAt(appItem.getPosition().length()-1)+"");
				editor.putString("right_"+index, appItem.getPackageName());
			}
		}
		editor.putBoolean("isFirstBoot", false);
		editor.commit();
	}

	public void InitHandler() {
		handlerThread = new HandlerThread("Launcher");
		handlerThread.start();
		HandlerUpdata = new Handler(handlerThread.getLooper());
		appWidget.setChangeSourceHandler(changeSourceHandler);
	}

	public void findView() {
		appWidget = (IAppWidget) findViewById(R.id.iAppWidget);
		appWidget.setWindowsWidth(panel_width);
		appWidget.setWindowsHeight(panel_height);
		netStatus = (ImageView) findViewById(R.id.topbar_net_status);
		usb_image = (ImageView) findViewById(R.id.topbar_usb_status);

		// zhang
		hs_content = (HorizontalScrollView) findViewById(R.id.hs_content);

		iv_tv_source = (ImageView) findViewById(R.id.iv_tv_source);
		iv_tv_source.setOnClickListener(this);
		iv_tv_source.setOnFocusChangeListener(this);
		iv_tv_source.setOnHoverListener(this);
		
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		iv_setting.setOnClickListener(this);
		iv_setting.setOnFocusChangeListener(this);
		iv_setting.setOnHoverListener(this);

		rl_view_first = (RelativeLayout) findViewById(R.id.rl_view_first);
		rl_view_first.setOnFocusChangeListener(this);
		rl_view_first.setOnClickListener(this);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_am_pm = (TextView) findViewById(R.id.tv_am_pm);
		rl_view_first.setOnTouchListener(this);
		rl_view_first.setOnHoverListener(this);

		rl_view_second = (RelativeLayout) findViewById(R.id.rl_view_second);
		iv_view_icon2 = (ImageView) findViewById(R.id.iv_view_icon2);
		tv_view_txt2 = (ScrollTextView) findViewById(R.id.tv_view_txt2);
		tvName1 = tv_view_txt2.getText().toString();
		rl_view_second.setOnFocusChangeListener(new LauncherFocusListener());
		rl_view_second.setOnClickListener(this);
		rl_view_second.setOnTouchListener(this);
		rl_view_second.setOnHoverListener(this);

		rl_view_third = (RelativeLayout) findViewById(R.id.rl_view_third);
		iv_view_icon3 = (ImageView) findViewById(R.id.iv_view_icon3);
		tv_view_txt3 = (ScrollTextView) findViewById(R.id.tv_view_txt3);
		tvName2 = tv_view_txt3.getText().toString();
		rl_view_third.setOnFocusChangeListener(new LauncherFocusListener());
		rl_view_third.setOnClickListener(this);
		rl_view_third.setOnKeyListener(this);
		rl_view_third.setOnTouchListener(this);
		rl_view_third.setOnHoverListener(this);

		app_layout[0] = (RelativeLayout) findViewById(R.id.app_widget_1);
		app_layout[1] = (RelativeLayout) findViewById(R.id.app_widget_2);
		app_layout[2] = (RelativeLayout) findViewById(R.id.app_widget_3);
		app_layout[3] = (RelativeLayout) findViewById(R.id.app_widget_4);
		app_layout[4] = (RelativeLayout) findViewById(R.id.app_widget_5);
		app_layout[5] = (RelativeLayout) findViewById(R.id.app_widget_6);
		app_layout[6] = (RelativeLayout) findViewById(R.id.app_widget_7);
		app_layout[7] = (RelativeLayout) findViewById(R.id.app_widget_8);
		app_layout[8]= (RelativeLayout) findViewById(R.id.app_widget_appcenter);
		
		app_name[0] = (ScrollTextView) findViewById(R.id.app_widget_1_name);
		app_name[1] = (ScrollTextView) findViewById(R.id.app_widget_2_name);
		app_name[2] = (ScrollTextView) findViewById(R.id.app_widget_3_name);
		app_name[3] = (ScrollTextView) findViewById(R.id.app_widget_4_name);
		app_name[4] = (ScrollTextView) findViewById(R.id.app_widget_5_name);
		app_name[5] = (ScrollTextView) findViewById(R.id.app_widget_6_name);
		app_name[6] = (ScrollTextView) findViewById(R.id.app_widget_7_name);
		app_name[7] = (ScrollTextView) findViewById(R.id.app_widget_8_name);
		app_name[8] = (ScrollTextView) findViewById(R.id.app_widget_appcenter_name);
		for(int i=0;i<9;i++){
			app_layout[i].setOnFocusChangeListener(new LauncherFocusListener(i));
			app_layout[i].setOnTouchListener(this);
			app_layout[i].setOnHoverListener(this);
		}
		rl_view_first.requestFocus();
	}

	//start by fengjw at time : 171206
	class LauncherFocusListener implements OnFocusChangeListener{

		private int id;

		public LauncherFocusListener(int id){
			this.id = id;
		}

		public LauncherFocusListener(){

		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			if (v.getId() == R.id.rl_view_second){
				if (hasFocus){
					tv_view_txt2.setText(tvName1);
					tv_view_txt2.setCanFocused(true);
					v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
							R.anim.view_launcher_anim_big));
				}else {
					tv_view_txt2.setText(tvName1);
					tv_view_txt2.setCanFocused(false);
					v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
							R.anim.view_launcher_anim_small));
				}
			}

			if (v.getId() == R.id.rl_view_third){
				if (hasFocus){
					tv_view_txt3.setText(tvName2);
					tv_view_txt3.setCanFocused(true);
					v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
							R.anim.view_launcher_anim_big));
				}else {
					tv_view_txt3.setText(tvName2);
					tv_view_txt3.setCanFocused(false);
					v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
							R.anim.view_launcher_anim_small));
				}
			}

			if (v.getId() == R.id.app_widget_1){
				if (hasFocus){
					scrollToRight();
				}
			}
			if (v.getId() == R.id.app_widget_1 || v.getId() == R.id.app_widget_2
					|| v.getId() == R.id.app_widget_3 || v.getId() == R.id.app_widget_4
					|| v.getId() == R.id.app_widget_5 || v.getId() == R.id.app_widget_6
					|| v.getId() == R.id.app_widget_7 || v.getId() == R.id.app_widget_8
					|| v.getId() == R.id.app_widget_appcenter) {
				if (hasFocus) {
					app_name[id].setCanFocused(true);
					app_name[id].setSelected(true);
				} else {
					app_name[id].setCanFocused(false);
					app_name[id].setSelected(false);
				}
			}
		}
	}
	//end by fengjw at time : 171206

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		if (event.getKeyCode() == MKeyEvent.KEYCODE_ASPECT_RATIO) {
			return true;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_TV_INPUT)) {
			event.setSource(KeyEvent.ACTION_MULTIPLE);
			changeInputSource("com.mstar.tv.tvplayer.ui");
			{
				short sourcestatus[] = null;
				try {
					sourcestatus = TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOnStatus");
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ComponentName componentName = new ComponentName("com.mstar.tv.tvplayer.ui",
					"com.mstar.tv.tvplayer.ui.RootActivity");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(componentName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			LauncherActivity.this.startActivity(intent);
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "----------onStop----------");
		super.onStop();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "----------onPause----------");
		activityIsRun = false;
		//浣胯兘home閿�
        handlertv.postDelayed(enable_homekey, 800);
		super.onPause();
	}

	public void updateNetUI() {
		String netState = mNetTool.getNetType();
		if (netState != null) {
			if (netState.equals("Wifi")) {
				
				//lixq 20160517 start
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				mWifiConnected = networkInfo != null && networkInfo.isConnected();
				if (mWifiConnected) {
					WifiManager wifiManager = mNetTool.getWifiManager();
					WifiInfo info = wifiManager.getConnectionInfo();
					int mWifiLevel = WifiManager.calculateSignalLevel(info.getRssi(), 4);
					netStatus.setImageResource(WifiIconArray[mWifiLevel]);
				} else {
					netStatus.setImageResource(R.drawable.com_status_unlink);
				}
				//lixq 20160517 end
			} else if (netState.equals("Ethernet")) {
				netStatus.setImageResource(R.drawable.com_status_link);
			}else if(netState.equals("Pppoe")){
				netStatus.setImageResource(R.drawable.pppoe_conected);
			}else {
				netStatus.setImageResource(R.drawable.com_status_unlink);
			}
		} else {
			netStatus.setImageResource(R.drawable.com_status_unlink);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//add for displaying the time in 12-hour or 24-hour system, zjd20160812, start
		calendarLayoutInit();
		Log.i(Constants.TAG, "onResume");


		if (isPowerOn() == true) {
		SystemProperties.set("com.jrm.localmm","true");
			
                        String startsetup = SystemProperties.get("persist.sys.startsetup");
                        if(startsetup.equals("true")){
                                ComponentName componentName = new ComponentName("cn.ktc.android.oobe",
                                                "cn.ktc.android.oobe.StartupActivity");
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.setComponent(componentName);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                LauncherActivity.this.startActivity(intent);
                        }else{
 			ComponentName componentName = new ComponentName("com.mstar.tv.tvplayer.ui",
					"com.mstar.tv.tvplayer.ui.RootActivity");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(componentName);
			intent.putExtra("isPowerOn", true);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			LauncherActivity.this.startActivity(intent);
			handlertv.postDelayed(enable_homekey, 800);
			}
		}
		// 2015.3.5 zjf add end
		// mDisplayManager.disconnectWifiDisplay();
		SystemProperties.set("mstar.str.storage", "0");
		// commonService.setInputSource(EnumInputSource.E_INPUT_SOURCE_STORAGE);

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> runningServiceInfos = activityManager.getRunningServices(100);
		for (int i = 0; i < runningServiceInfos.size(); i++) {
			ActivityManager.RunningServiceInfo runningItem = runningServiceInfos.get(i);
			if ("com.android.music.MediaPlaybackService".equals(runningItem.service.getClassName())) {
				Intent intent = new Intent();
				intent.setAction("com.android.music.musicservicecommand.pause");
				intent.putExtra("command", "pause");
				sendBroadcast(intent);
				break;
			}
		}

		activityIsRun = true;
		Log.i(TAG, "<<<<<<----------mstar.str.suspending == STR_STATUS_NONE---------->>>>>");
		appWidget.InitAppWidget();
		updateNetUI();
		update();
		updataNetAndUsbIcon();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		NeedDelay = true;
		handlerDelay.sendEmptyMessageDelayed(0, 2000);
		//lixq 20160517 Add to check whether mount the USB start
		if (CheckUsbIsExist()) {
			Log.d(TAG, " usbDeviceCount = "+usbDeviceCount);
			mUsbFlag = true;
			mUsbHanler.sendEmptyMessage(UPDATE_USB_ICON);
		}
		/*//lixq 20160517 Add to check whether mount the USB end
		// Hisa 2016.03.04 add Freeze function start
        Intent intentCancel = new Intent();//鍙栨秷闈欏儚鑿滃崟
        intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
        //K_TvPictureManager.getInstance().K_unFreezeImage();
        sendBroadcast(intentCancel);
        // Hisa 2016.03.04 add Freeze function end*/
		Log.i(TAG, "<<<<<<----------end onResume---------->>>>>");
		//绂佹home閿�
        Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 1);
	}
	Handler handlertv=new Handler();
	// delay enableHomekey
    Runnable enable_homekey = new Runnable() {

        @Override
        public void run() {
            Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 0);
        }
    };
    
	@Override
	protected void onDestroy() {
		Log.i(TAG, "----------onDestroy----------");
		unregisterReceiver(mNetReceiver);
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}

	public void changeInputSource(String packName) {
		Log.i(TAG, "changeInputSource------------" + packName);
		if (packName != null) {
			if (packName.contentEquals("com.mstar.tv.tvplayer.ui") || packName.contentEquals("mstar.factorymenu.ui")
					|| packName.contentEquals("com.tvos.pip") || packName.contentEquals("com.mstar.tvsetting.hotkey")
					|| packName.contentEquals("com.babao.tvju") || packName.contentEquals("com.babao.socialtv")
					|| packName.contentEquals("com.mstar.appdemo")) {
				Log.i(TAG, "------------TV AP");
			} else {
				synchronized (bSync) {
					if (STR_STATUS_SUSPENDING.equals(SystemProperties.get("mstar.str.suspending", "0"))) {
						SystemProperties.set("mstar.str.storage", "1");
					}
					toChangeInputSource = EnumInputSource.E_INPUT_SOURCE_STORAGE;
				}
			}
		}
	}

	private void updateWallpaperVisibility(boolean visible) {
		int wpflags = visible ? WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER : 0;
		int curflags = getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER;
		if (wpflags != curflags) {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			WallpaperManager.getInstance(this).suggestDesiredDimensions(metrics.widthPixels, metrics.heightPixels);

			getWindow().setFlags(wpflags, WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
		}
	}

	public void BackHomeSource() {
		synchronized (bSync) {
			toChangeInputSource = EnumInputSource.E_INPUT_SOURCE_ATV;
		}
	}

	class FileFlyReceiver extends BroadcastReceiver {
		public FileFlyReceiver() {
			Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>  this is box");
		}

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>  this is box receive com.jrm.filefly.action");
			String action = arg1.getAction();
			if (FILE_FLY_LAUNCH.equals(action)) {
				synchronized (bSync) {
					if (STR_STATUS_SUSPENDING.equals(SystemProperties.get("mstar.str.suspending", "0"))) {
						SystemProperties.set("mstar.str.storage", "1");
					}
					toChangeInputSource = EnumInputSource.E_INPUT_SOURCE_STORAGE;
				}
			}
		}
	}

	private Handler changeSourceHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.SETSOURCE_MS:
				String packAgeName = (String) msg.getData().get("packAgeName");
				changeInputSource(packAgeName);
				break;
			}
		};
	};

	/**
	 * 娉ㄥ唽缃戠粶鐘舵�佹敼鍙樺箍鎾� @ param @ return void
	 */
	private void registNetReceiver() {
		// ethernet status changed
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE_IMMEDIATE");
		// wifi status changed
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		// pppoe status changed
		intentFilter.addAction(PPPOE_STATE_ACTION);
		registerReceiver(mNetReceiver, intentFilter);
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
		mAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.usb_in_out);
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
				if (!CheckUsbIsExist()) {
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		 initLeftApps();
	}

	private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
				mUsbFlag = true;
				++usbDeviceCount;
				if (("0".equals(SystemProperties.get("mstar.audio.init", "0")))
						&& (SystemProperties.getBoolean("mstar.str.enable", false))) {
					mUsbHanler.sendEmptyMessageAtTime(USB_STATE_OFF, 5000);
				} else {
					mUsbHanler.sendEmptyMessage(USB_STATE_OFF);
				}
			} else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {// remove
				mUsbFlag = false;
				--usbDeviceCount;
				if (("0".equals(SystemProperties.get("mstar.audio.init", "0")))
						&& (SystemProperties.getBoolean("mstar.str.enable", false))) {
					// mUsbHanler.sendEmptyMessageAtTime(USB_STATE_ON, 5000);
				} else {
					mUsbHanler.sendEmptyMessage(USB_STATE_OFF);
				}
			}
		}
	};

	public void update() {
		if (mNetTool.isNetworkConnected(getApplicationContext())) {
			if (!isPass) {
				updateApk();
			}
		} else {
			Log.e(Constants.TAG, "Launcher movie update,Network not available");
		}
	}

	/**
	 * 缃戠粶鐘舵�佸彂鐢熸敼鍙樼殑骞挎挱
	 */
	private BroadcastReceiver mNetReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(Constants.TAG, "------mNetReceiver");
			String action = intent.getAction();
			Log.i(Constants.TAG, "------action:   "+action);
			/*if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)) {
				final int event = intent.getIntExtra(
						EthernetManager.EXTRA_ETHERNET_STATE,
						EthernetManager.ETHERNET_STATE_UNKNOWN);
				Log.i(Constants.TAG,
						"------mNetReceiver  ETHERNET_STATE_CHANGED_ACTION:"
								+ event);
				switch (event) {
				case EthernetStateTracker.EVENT_HW_CONNECTED:
				case EthernetStateTracker.EVENT_INTERFACE_CONFIGURATION_SUCCEEDED:// ethernet
																					// link
					if (activityIsRun) {
						netStatus.setImageResource(R.drawable.com_status_link);
						mWireFlag = true;
						// weatherHodler.setWeather();
						update();
					}
					break;
				case EthernetStateTracker.EVENT_HW_DISCONNECTED:
				case EthernetStateTracker.EVENT_INTERFACE_CONFIGURATION_FAILED: // ethernet
																				// unlink
					if (activityIsRun) {
						netStatus
								.setImageResource(R.drawable.com_status_unlink);
						mWireFlag = false;
					}
					break;
				default:
					if (activityIsRun) {
						netStatus
								.setImageResource(R.drawable.com_status_unlink);
					}
					break;
				}
			}*/
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) { // for android MM
				final NetworkInfo networkInfo = (NetworkInfo) intent
						.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
				final boolean Connected = networkInfo != null && networkInfo.isConnected();
				if (Connected && (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)) { // ethernet
																									// connected
					//// mEthernetFlag = true;
					netStatus.setImageResource(R.drawable.com_status_link);
					if (activityIsRun) {
						updateNetUI();
						mWireFlag = true;
						update();
					}
				} else { // ethernet disconnected
					//// mEthernetFlag = false;
					netStatus.setImageResource(R.drawable.com_status_unlink);
					if (activityIsRun) {
						updateNetUI();
						mWireFlag = false;
					}
				}
				//refresh time when net status changed 
            	calendarLayoutInit();
        	} else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)
					|| action.equals(WifiManager.RSSI_CHANGED_ACTION)
					|| action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				if (activityIsRun) {
					updateWifiStatr(intent);
					// weatherHodler.setWeather();
					update();
				}
			} else if (action.equals(PPPOE_STATE_STATUE)) {
				String pppoeState = intent.getStringExtra(PPPOE_STATE_STATUE);
				if (pppoeState.equals(PppoeManager.PPPOE_STATE_CONNECT)) {// pppoe link
					if (activityIsRun) {
                		updateNetUI();
	                    mWireFlag = true;
	                    update();
					}
				} else if (pppoeState.equals(PPPOE_STATE_DISCONNECT)) {// pppoe
					if (activityIsRun) {
						netStatus.setImageResource(R.drawable.com_status_unlink);
					}
				} else if (pppoeState.equals(PPPOE_STATE_AUTHFAILED)) {// pppoe
																		// authfailed
					// netStatus.setImageResource(R.drawable.com_status_unlink);
				} else if (pppoeState.equals(PPPOE_STATE_FAILED)) {// pppoe
																	// state
																	// failed
					if (activityIsRun) {
						netStatus.setImageResource(R.drawable.com_status_unlink);
					}
				}
			}
		}
	};
	private BroadcastReceiver updataAppList = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			Log.i(Constants.TAG, "UpdataAppList BroadcastReceiver");
			String action = arg1.getAction();
			if (Constants.APPUPDATA_ACTION.equals(action)) {
				appWidget.InitAppWidget();
			}
		}
	};

	private void updateWifiStatr(Intent intent) {
		final String action = intent.getAction();
		if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			mWifiEnabled = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_UNKNOWN) == WifiManager.WIFI_STATE_ENABLED;
		} else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			final NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			boolean wasConnected = mWifiConnected;
			mWifiConnected = networkInfo != null && networkInfo.isConnected();
			if (mWifiConnected && !wasConnected) {
				WifiInfo info = (WifiInfo) intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
				if (info == null) {
					info = mWifiManager.getConnectionInfo();
				}
				if (info != null) {
					mWifiSsid = huntForSsid(info);
				} else {
					mWifiSsid = null;
				}
			} else if (!mWifiConnected) {
				mWifiSsid = null;
			}
			mWifiLevel = 0;
			mWifiRssi = -200;
		} else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
			if (mWifiConnected) {
				mWifiRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -200);
				mWifiLevel = WifiManager.calculateSignalLevel(mWifiRssi, 4);
			}
		}

		if (mWifiConnected) {
			netStatus.setImageResource(WifiIconArray[mWifiLevel]);
		} else {
			netStatus.setImageResource(R.drawable.com_status_unlink);
		}

	}

	private String huntForSsid(WifiInfo info) {
		String ssid = info.getSSID();
		if (ssid != null) {
			return ssid;
		}
		List<WifiConfiguration> networks = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration net : networks) {
			if (net.networkId == info.getNetworkId()) {
				return net.SSID;
			}
		}
		return null;
	}

	public int updataNetAndUsbIcon() {
		Log.i(Constants.TAG, "----updataNetAndUsbIcon");
		int netType = -1;

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_WIFI) {
			Log.i(Constants.TAG, "----TYPE_WIFI");
		} else if (nType == ConnectivityManager.TYPE_ETHERNET) {
			Log.i(Constants.TAG, "----TYPE_ETHERNET");
		}
		// else if(nType==ConnectivityManager.TYPE_PPPOE){
		// Log.i(Constants.TAG, "----TYPE_PPPOE");
		// }
		else {
			Log.i(Constants.TAG, "----else");
		}
		return 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	Runnable changeInputSource_thread = new Runnable() {
		@Override
		public void run() {
			PackageManager packageManager = getPackageManager();
			Intent intent = null;
			changeInputSource("com.mstar.tv.tvplayer.ui");
			{
				short sourcestatus[] = null;
				try {
					sourcestatus = TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOnStatus");
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			intent = packageManager.getLaunchIntentForPackage("com.mstar.tv.tvplayer.ui");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			startActivity(intent);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_view_second:
			startActivity(left_view_packages[0]);
			break;
		case R.id.rl_view_third:
			startActivity(left_view_packages[1]);
			break;
		case R.id.iv_setting:
			startActivity("com.android.tv.settings");
			break;
		case R.id.iv_tv_source:
			//淇℃簮鍒囨崲
			showSource();
			break;
		}
	}

	// zhanghz
	private int[] SourceListFlag = { 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	// 鏍囧織瀵瑰簲source鏄惁鏈変俊鍙�
	private boolean[] signalFlag = { false, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true };
	/**
	 * 淇″彿婧愭爣璇�
	 */
	public final static int VGA = 0;
	public final static int TV = 1;
	public final static int AV = 2;
	public final static int YPBPR = 3;
	public final static int HDMI = 4;
	// 鏍囧織瀵瑰簲淇℃簮鐨勬爣蹇�0锛歏GA 1锛歍V 2锛欰V 3锛歒PBPR 4锛欻DMI
	private int[] typeFlag = { VGA, TV, AV, AV, AV, AV, AV, AV, AV, AV, -1, -1, -1, -1, -1, -1, YPBPR, YPBPR, YPBPR, -1,
			-1, -1, -1, HDMI, HDMI, -1, -1, -1, TV, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

	private String[] tmpData = null;

	@SuppressLint("NewApi")
	private void showSource() {
		if(sourceItems==null)
			sourceItems=new ArrayList<InputSourceItem>();
		else
			sourceItems.clear();
		
		getInputSourcelist();

		getSourceStatus();

		if (hasDvb) {
			tmpData = getResources().getStringArray(R.array.str_arr_input_source_dtv_vals);
		} else {
			tmpData = getResources().getStringArray(R.array.str_arr_input_source_vals);
		}
		Log.i("test", "-----------------------------");
		for (String s : tmpData) {
			Log.i("test", s);
		}
		Log.i("test", "-----------------------------");

		for (int i = 0; i < tmpData.length; i++) {
			// if (i == 10 || i == 15 || i == 19 || i == 22 || i == 27 ||
			// SourceListFlag[i] == 0)
			if (SourceListFlag[i] == 0)// for i = 10 ,15, 19, 22,
										// 27SOURCE_XXXX_MAX aways equals zero
			{
				continue;
			}

			if ((tvPipPopManager.isPipModeEnabled() == true)
					&& (tvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_PIP)) {
				if (tvPipPopManager.checkPipSupportOnSubSrc(i) == false) {
					continue;
				}
			} else if ((tvPipPopManager.isPipModeEnabled() == true)
					&& (tvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP)) {
				Enum3dType formatType = Enum3dType.EN_3D_TYPE_NUM;
				try {
					formatType = TvManager.getInstance().getThreeDimensionManager().getCurrent3dFormat();
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (formatType == Enum3dType.EN_3D_DUALVIEW) {
					// System.out.println("\n continue()xx " + i);
				} else {
					// use pip's instead of pop's
					if (tvPipPopManager.checkPipSupportOnSubSrc(i) == false) {

						continue;
					}
				}
			} else if ((tvPipPopManager.isPipModeEnabled() == true)
					&& (tvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_TRAVELING)) {
				int curSubSource = TvCommonManager.INPUT_SOURCE_ATV;
				// try
				// {
				curSubSource = TvCommonManager.getInstance().getCurrentTvSubInputSource();
				// }
				// catch (TvCommonException e)
				// {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				if (curSubSource == i) {
					continue;
				} else if (tvPipPopManager.checkTravelingModeSupport(i, curSubSource) == false) {
					continue;
				}
			}

			Log.i("test", tmpData[i] + "=" + signalFlag[i]);
			Log.v("zhanghz", tmpData[i] + "--" + i + "---" + typeFlag[i] + "--" + signalFlag[i]);
			InputSourceItem sourceItem=new InputSourceItem(tmpData[i],  i, typeFlag[i], signalFlag[i]);
			sourceItems.add(sourceItem);
		}

		if (BoardProperty.isT8c1()){
			/*if (BoardProperty.isHasDvbsHasCi()){
				Log.d("Maxs700","--->T8C2:KLauncher:isHasDvbsHasCi");
				sourceContext = new SourceContext(new T8C2HasDvbsHasCiSourceState());
			}else if (BoardProperty.isHasDvbsNonCi()){
				Log.d("Maxs700","--->T8C2:KLauncher:isHasDvbsNonCi");
				sourceContext = new SourceContext(new T8C2HasDvbsNonCiSourceState());
			}else if (BoardProperty.isNonDvbsHasCi()){
				Log.d("Maxs700","--->T8C2:KLauncher:isNonDvbsHasCi");
				sourceContext = new SourceContext(new T8C2NonDvbsHasCiSourceState());
			}else if (BoardProperty.isNonDvbsNonCi()){
				Log.d("Maxs700","--->T8C2:KLauncher:isNonDvbsNonCi");
				sourceContext = new SourceContext(new T8C2NonDvbsNonCiSourceState());
			}else {
				sourceContext = new SourceContext(new T8C2HasDvbsNonCiSourceState());
				Log.d("Maxs700","--->T8C2KLauncher:-----");
			}*/
			if(BoardProperty.isHasYpbpr()){
				sourceContext = new SourceContext(new T8C2HasDvbsNonCiSourceState());
			}else{
				sourceContext = new SourceContext(new T8C2HasDvbsHasCiSourceState());
			}
		}else if (BoardProperty.isT5c1()){
			/*if (BoardProperty.isHasDvbsHasCi()){
				Log.d("Maxs700","--->T5C2:KLauncher:isHasDvbsHasCi");
				sourceContext = new SourceContext(new T5C2HasDvbsHasCiSourceState());
			}else if (BoardProperty.isHasDvbsNonCi()){
				Log.d("Maxs700","--->T5C2:KLauncher:isHasDvbsNonCi");
				sourceContext = new SourceContext(new T5C2HasDvbsNonCiSourceState());
			}else if (BoardProperty.isNonDvbsHasCi()){
				Log.d("Maxs700","--->T5C2:KLauncher:isNonDvbsHasCi");
				sourceContext = new SourceContext(new T5C2NonDvbsHasCiSourceState());
			}else if (BoardProperty.isNonDvbsNonCi()){
				Log.d("Maxs700","--->T5C2:KLauncher:isNonDvbsNonCi");
				sourceContext = new SourceContext(new T5C2NonDvbsNonCiSourceState());
			}else {
				sourceContext = new SourceContext(new T5C2HasDvbsNonCiSourceState());
				Log.d("Maxs700","--->T5C2-KLauncher:----");
			}*/
			if(BoardProperty.isHasYpbpr()){
				sourceContext = new SourceContext(new T5C2HasDvbsNonCiSourceState());
			}else{
				sourceContext = new SourceContext(new T5C2HasDvbsHasCiSourceState());
			}
		}
		if (sourceContext == null)
		{
			sourceContext = new SourceContext(new T8C2HasDvbsNonCiSourceState());
		}
		sourceItems.clear();
		sourceItems = sourceContext.getSupportSourceList(LauncherActivity.this);
		// 閲嶆柊鎺掑簭锛屽皢DTV鏀惧湪ATV鍚庨潰锛屽皢VGA淇″彿婧愭斁鍦ㄦ渶鍚�
		SourceInfoPopWindow popWindow = new SourceInfoPopWindow(LauncherActivity.this, sourceItems);
		popWindow.showAsDropDown(iv_tv_source, 10, 10, Gravity.RIGHT);
		popWindow.setChoiceListener(new OnChoiceListener() {
			
			@Override
			public void selectItem(int position) {
				Log.v("zhanghz", "lcauncehr" + position);
				/*if(position==sourceItems.size() - 1){
					position=0;
				}else if(position==0){
					position=sourceItems.size() - 1;
				}*/
				changeSource(position);
			}
		});
	}

	private void getInputSourcelist() {

		int[] sourceList;
		try {
			sourceList = TvManager.getInstance().getSourceList();
			// IntArrayList sourceList = tvCommonmanager.getSourceList();
			if (sourceList != null) {
				for (int i = 0; i < SourceListFlag.length - 15; i++) {
					if (i < sourceList.length) {
						SourceListFlag[i] = sourceList[i];
					}
				}
				// 鍒ゆ柇鏄惁甯TMB锛屽惁鍒欓殣钘廌TV淇℃簮閫夐」
				if (!hasDvb) {
					SourceListFlag[SourceListFlag.length - 1] = 0;
				}
				// 寮哄埗鎵撳紑AV2(1)
				SourceListFlag[3] = 0;
				// 寮哄埗闅愯棌YPBPR
				//SourceListFlag[16] = 0;
				// lixq 20160314 add for T4C2 board start
				if (hasT4C2Board) {
					SourceListFlag[25] = 0;
				}
				// lixq 20160314 add for T4C2 board end
			}
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getSourceStatus() {
		boolean[] sourceStatusList = tvCommonmanager.GetInputSourceStatus();
		if (sourceStatusList != null) {
			for (int i = 0; i < signalFlag.length; i++) {
				// if (i < sourceStatusList.size())
				{
					signalFlag[i] = sourceStatusList[i];
				}
			}
		}
	}

	public void updateApk() {
		if (mCheckNewVersionTask != null && mCheckNewVersionTask.getStatus() != AsyncTask.Status.FINISHED) {
			mCheckNewVersionTask.cancel(true);
		}
		//鍒ゆ柇鏄惁鏈夋柊鐗堟湰,鏈夋彁绀烘洿鏂帮紝鏃犺繑鍥瀗ull
		mCheckNewVersionTask = new CheckNewVersionTask();
		mCheckNewVersionTask.execute();
	}

	/**
	 * 妫�鏌ユ柊鐗堟湰寮傛浠诲姟
	 * 
	 * @author yejb
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
			return new Update(getApplicationContext()).hasNewVersion();
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
	 * 鏄剧ず鏄惁涓嬭浇鏂扮増鏈璇濇
	 * 
	 * @param version
	 */
	private void showNewVersionDialog(final Version version) {
		if (alertDialog == null) {
			alertDialog = new AlertDialog.Builder(this).setTitle(R.string.new_version_text)
					.setMessage(version.getIntroduction())
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Update update = new Update(LauncherActivity.this);
							update.setVersion(version);
							update.checkUpdate();
						}
					}).setNeutralButton(R.string.skip_this_version, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							isPass = true;
						}
					}).create();
		}
		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}

	Handler handlerChangePip = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String packName = b.getString("packName");
			changeInputSource(packName);
		}
	};

	Handler handlerDelay = new Handler() {
		public void handleMessage(Message msg) {
			NeedDelay = false;
		}
	};

	// 鐟欙絽鍠呴柅澶嬪閻焦绁﹂弮鍫曟？閺冭绱漈V閵嗕俯auncher閵嗕讣etting娑擃厽妞傞梻缈犵瑝娑擄拷閼锋挳妫舵０锟� start
	@SuppressLint("NewApi")
	private boolean getAutoState(String name) {
		try {
			return Settings.Global.getInt(getContentResolver(), name) > 0;
		} catch (SettingNotFoundException snfe) {
			return false;
		}
	}

	//lixq 20151119 start
	 public boolean isPowerOn() {
		 Log.d(TAG, "Is Fist Power On: " + (SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)));
		 if (!SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)) {
			 SystemProperties.set(IS_POWER_ON_PROPERTY, "true");
			 return true;
			 } else {
				 return false;
				 }
		 }
	//lixq 20151119 end
	 
	//lixq 20160517 Add to check whether mount the USB start
	 private boolean CheckUsbIsExist() {
			// TODO Auto-generated method stub
			boolean ret = false;
		StorageManager storageManager = (StorageManager) this.getSystemService(Context.STORAGE_SERVICE);
				 StorageVolume[] volumes = storageManager.getVolumeList();
				 if (volumes != null || volumes.length > 0) {
						String path = "";
						int mountedDeviceCount = 0;
						// List all your mount disk
						for (StorageVolume item : volumes) {
							path = item.getPath();
							String state = storageManager.getVolumeState(path);
							// Mount is not successful
							if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
								mountedDeviceCount++ ;
							} 
						}
						usbDeviceCount = mountedDeviceCount;
					}
			return usbDeviceCount>1;
		}
	boolean isRight=false;
	// lixq 20160517 Add to check whether mount the USB end
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus&&v.getId()!=R.id.iv_tv_source&&v.getId()!=R.id.iv_setting) {
			v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view_launcher_anim_big));
		} else if(!hasFocus&&v.getId()!=R.id.iv_tv_source&&v.getId()!=R.id.iv_setting) {
			v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view_launcher_anim_small));
		}
		if(hasFocus){
			if(v.getId()==R.id.rl_view_first||v.getId()==R.id.rl_view_second||v.getId()==R.id.rl_view_third){
				if(isRight)
					scrollToLeft();
			}else{
				if(!isRight)
					scrollToRight();
			}
		}
	}

	private void scrollToLeft() {
		hs_content.smoothScrollTo(0, 0);
		isRight=false;
	}

	private void scrollToRight() {
		hs_content.smoothScrollTo(hs_content.getWidth(), 0);
		isRight=true;
	}

	private void initSourceData() {
		String value = Utils.readPropValue("/system/build.prop", "ktc.dvb");
		hasDvb = value != null && value.equals("true");
		value = Utils.readPropValue("/system/build.prop", "ktc.board.t4c2");
		hasT4C2Board = value != null && value.equals("true");
	}

	private void initLeftApps() {
		PackageManager packageManager = this.getPackageManager();
		PackageInfo packageInfo;
		AppInfo [] appinfos =new AppInfo[2];
		Drawable ic_add = this.getResources().getDrawable(R.drawable.ic_add);
		for(int i=0;i<left_view_packages.length;i++){
			if(!left_view_packages[i].equals("")){
				try {
					packageInfo = packageManager.getPackageInfo(left_view_packages[i], PackageManager.GET_PERMISSIONS);
					int status=packageManager.getApplicationEnabledSetting(packageInfo.packageName);
					if (packageInfo != null &&status==PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
						AppInfo tmpInfo = new AppInfo();
						tmpInfo.appName = packageInfo.applicationInfo.loadLabel(this.getPackageManager()).toString();
						tmpInfo.packageName = packageInfo.packageName;
						tmpInfo.versionName = packageInfo.versionName;
						tmpInfo.versionCode = packageInfo.versionCode;
						tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(this.getPackageManager());
						appinfos[i] = tmpInfo;
					} else {
						AppInfo tmpInfo = new AppInfo();
						tmpInfo.appName = "";
						tmpInfo.packageName = "";
						tmpInfo.versionName = "";
						tmpInfo.versionCode = 0;
						tmpInfo.appIcon = ic_add;
						appinfos[i] = tmpInfo;
					}
				} catch (Exception e) {
					Log.v("zhanghz","read app error");
				}
			}
			
		}
		for(int i=0;i<appinfos.length;i++){
			if(appinfos[i]!=null){
				if(i==0){
					iv_view_icon2.setImageDrawable(appinfos[0].appIcon);
					tv_view_txt2.setText(appinfos[0].appName);
					tvName1 = appinfos[0].appName;
				}else if (i==1){
					iv_view_icon3.setImageDrawable(appinfos[1].appIcon);
					tv_view_txt3.setText(appinfos[1].appName);
					tvName2 = appinfos[1].appName;
				}
			}else{
				if(i==0){
					iv_view_icon2.setImageDrawable(new BitmapDrawable());
					tv_view_txt2.setText("");
				}else if (i==1){
					iv_view_icon3.setImageDrawable(new BitmapDrawable());
					tv_view_txt3.setText("");
				}
			}
		}
	}

	public void startActivity(String packAgeName) {
		if (!packAgeName.equals("")) {
			PackageManager packageManager = getApplicationContext().getPackageManager();
			Intent intent = new Intent();
			intent = packageManager.getLaunchIntentForPackage(packAgeName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			try {
				getApplicationContext().startActivity(intent);
			} catch (Exception e) {
				Log.e("IAppWidget", "startActivity " + packAgeName + "error");
			}
		}
	}

	@SuppressLint("NewApi")
	private void changeSource(int position) {
		inputSource = sourceItems.get(position).getPoistion();
		if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// to ensure input-source is switched after database has
					// been saved, we'll do it later
					TvIsdbChannelManager.getInstance().setAntennaType(
								TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
					tvCommonmanager.setInputSource(inputSource);
				}
			}).start();
		} else if (inputSource == TvCommonManager.INPUT_SOURCE_ATV) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// to ensure input-source is switched after database has
					// been saved, we'll do it later
					TvIsdbChannelManager.getInstance().setAntennaType(
								TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE);
					tvCommonmanager.setInputSource(inputSource);
				}
			}).start();

		} else {
			tvCommonmanager.setInputSource(inputSource);
		}
		handler.sendEmptyMessage(SETIS_END_COMPLETE);
	}
	// calendar
	private void calendarLayoutInit() {

		Calendar cal = Calendar.getInstance();
		// 瑙ｅ喅閫夋嫨鐮佹祦鏃堕棿鏃讹紝TV銆丩auncher銆丼etting涓椂闂翠笉涓�鑷撮棶棰� start
		if (getAutoState(Settings.Global.TV_AUTO_TIME)) {// from TV
			Time curTime = TvTimerManager.getInstance().getCurrentTvTime();
			cal.setTimeInMillis(curTime.toMillis(true));
		} else if (getAutoState(Settings.Global.AUTO_TIME)
				&& (new NetTool(getApplicationContext()).getNetType() == null)) {// from
																					// Net
																					// but
																					// no
																					// net
			Time curTime = TvTimerManager.getInstance().getCurrentTvTime();
			cal.setTimeInMillis(curTime.toMillis(true));
		}
		// 瑙ｅ喅閫夋嫨鐮佹祦鏃堕棿鏃讹紝TV銆丩auncher銆丼etting涓椂闂翠笉涓�鑷撮棶棰� end
		Log.v("zhang", cal.get(Calendar.HOUR_OF_DAY) + " : " + cal.get(Calendar.MINUTE) + ":"
				+ String.valueOf(cal.get(Calendar.SECOND)));

		// add for displaying the time in 12-hour or 24-hour system,
		// zjd20160812, start
		boolean isTimeFormat24h = DateFormat.is24HourFormat(this);
		String time = null;
		if (!isTimeFormat24h) {
			String hour = getHour_12(cal.get(Calendar.HOUR_OF_DAY));
			String minute = getTimeDay(cal.get(Calendar.MINUTE));
			if (cal.get(Calendar.HOUR_OF_DAY) < 12) {
				time = hour + ":" + minute+":AM";
			} else {
				time = hour + ":" + minute+":PM";
			}
		} else {
			time = getTimeDay(cal.get(Calendar.HOUR_OF_DAY)) + ":" + getTimeDay(cal.get(Calendar.MINUTE));
		}
		Message msg=timeHandler.obtainMessage();
		msg.what=MESG_TIME;
		msg.obj=time;
		timeHandler.sendMessage(msg);
		// add for displaying the time in 12-hour or 24-hour system,
		// zjd20160812, end

		String month = getMonthString(cal.get(Calendar.MONTH));
		String week = getWeekString(cal.get(Calendar.DAY_OF_WEEK));
		String day = getDay(cal.get(Calendar.DAY_OF_MONTH));
		int year=cal.get(Calendar.YEAR);
		String date=week+","+day+" "+month+","+year;
		msg=timeHandler.obtainMessage();
		msg.what=MESG_DATE;
		msg.obj=date;
		timeHandler.sendMessage(msg);
		// add for displaying the time in 12-hour or 24-hour system,
		// zjd20160812, start
		// add for displaying the time in 12-hour or 24-hour system,
		// zjd20160812, end
		mTimeTickReciver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
				if (action.equals(Intent.ACTION_TIME_TICK) || action.equals(Intent.ACTION_TIME_CHANGED)
						|| action.equals(Intent.ACTION_DATE_CHANGED) || action.equals(Intent.ACTION_TIMEZONE_CHANGED)
						|| action.equals(Intent.ACTION_LOCALE_CHANGED)) {
					Calendar cal = Calendar.getInstance();
					// 瑙ｅ喅閫夋嫨鐮佹祦鏃堕棿鏃讹紝TV銆丩auncher銆丼etting涓椂闂翠笉涓�鑷撮棶棰� start
					if (getAutoState(Settings.Global.TV_AUTO_TIME)) {
						Time curTime = TvTimerManager.getInstance().getCurrentTvTime();
						cal.setTimeInMillis(curTime.toMillis(true));
					} else if (getAutoState(Settings.Global.AUTO_TIME)
							&& (new NetTool(getApplicationContext()).getNetType() == null)) {// from
																								// Net
																								// but
																								// no
																								// net
						Time curTime = TvTimerManager.getInstance().getCurrentTvTime();
						cal.setTimeInMillis(curTime.toMillis(true));
					}
					// 瑙ｅ喅閫夋嫨鐮佹祦鏃堕棿鏃讹紝TV銆丩auncher銆丼etting涓椂闂翠笉涓�鑷撮棶棰� end
					// String date = getTimeDay(cal.get(Calendar.HOUR_OF_DAY))
					// + ":" + getTimeDay(cal.get(Calendar.MINUTE));
					int year=cal.get(Calendar.YEAR);
					String month = getMonthString(cal.get(Calendar.MONTH));
					String day = getDay((cal.get(Calendar.DAY_OF_MONTH)));
					String week = getWeekString(cal.get(Calendar.DAY_OF_WEEK));
					String date=week+","+day+" "+month+","+year;
					Message msg=timeHandler.obtainMessage();
					msg.what=MESG_DATE;
					msg.obj=date;
					timeHandler.sendMessage(msg);
					// add for displaying the time in 12-hour or 24-hour system,
					// zjd20160812, start
					boolean isTimeFormat24h = DateFormat.is24HourFormat(LauncherActivity.this);
					Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/BinnerD.ttf");
					String time = null;
					if (!isTimeFormat24h) {
						String hour = getHour_12(cal.get(Calendar.HOUR_OF_DAY));
						String minute = getTimeDay(cal.get(Calendar.MINUTE));
						if (cal.get(Calendar.HOUR_OF_DAY) < 12) {
							time = hour + ":" + minute+":AM";
						} else {
							time = hour + ":" + minute+":PM";
						}
					} else {
						time = getTimeDay(cal.get(Calendar.HOUR_OF_DAY)) + ":" + getTimeDay(cal.get(Calendar.MINUTE));
					}
					// add for displaying the time in 12-hour or 24-hour system,
					// zjd20160812, end
					Log.v("zhanghz", "222--" + time);
					msg=timeHandler.obtainMessage();
					msg.what=MESG_TIME;
					msg.obj=time;
					timeHandler.sendMessage(msg);
				}
			}
		};

			IntentFilter timeFilter = new IntentFilter();
			timeFilter.addAction(Intent.ACTION_TIME_TICK);
			timeFilter.addAction(Intent.ACTION_DATE_CHANGED);
			timeFilter.addAction(Intent.ACTION_TIME_CHANGED);
			timeFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			registerReceiver(mTimeTickReciver, timeFilter);

	}

	private String getDay(int i) {
		String[] days = getResources().getStringArray(R.array.day);
		return days[i-1];
	}

	// 瑙ｅ喅閫夋嫨鐮佹祦鏃堕棿鏃讹紝TV銆丩auncher銆丼etting涓椂闂翠笉涓�鑷撮棶棰� end

	// 灏忎簬10鍓嶉潰琛�0
	private String getTimeDay(int i) {
		if (i < 10) {
			String s = "0" + String.valueOf(i);
			return s;
		} else {
			return String.valueOf(i);
		}
	}

	// add for displaying the time in 12-hour or 24-hour system, zjd20160812,
	// start
	private String getHour_12(int i) {
		int hour = 0;
		if (i > 12) {
			hour = i - 12;
		} else {
			hour = i;
		}
		if (hour < 10) {
			return ("0" + String.valueOf(hour));
		} else {
			return String.valueOf(hour);
		}
	}
	// add for displaying the time in 12-hour or 24-hour system, zjd20160812,
	// end

		String getMonthString(int mouth) {
			String[] monthName = getResources().getStringArray(R.array.month);
			switch (mouth) {
			case Calendar.JANUARY:
				return monthName[0];
			case Calendar.FEBRUARY:
				return monthName[1];
			case Calendar.MARCH:
				return monthName[2];
			case Calendar.APRIL:
				return monthName[3];
			case Calendar.MAY:
				return monthName[4];
			case Calendar.JUNE:
				return monthName[5];
			case Calendar.JULY:
				return monthName[6];
			case Calendar.AUGUST:
				return monthName[7];
			case Calendar.SEPTEMBER:
				return monthName[8];
			case Calendar.OCTOBER:
				return monthName[9];
			case Calendar.NOVEMBER:
				return monthName[10];
			case Calendar.DECEMBER:
				return monthName[11];
			default:
				return null;
			}
		}

		String getWeekString(int week) {
			String[] weekName = getResources().getStringArray(R.array.week);
			switch (week) {
			case Calendar.SUNDAY:
				return weekName[0];
			case Calendar.MONDAY:
				return weekName[1];
			case Calendar.TUESDAY:
				return weekName[2];
			case Calendar.WEDNESDAY:
				return weekName[3];
			case Calendar.THURSDAY:
				return weekName[4];
			case Calendar.FRIDAY:
				return weekName[5];
			case Calendar.SATURDAY:
				return weekName[6];
			default:
				return null;
			}
		}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
			app_layout[0].requestFocus();
			return true;
		}
		return false;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_UP){
			if(!v.hasFocus()){
				v.requestFocus();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onHover(View v, MotionEvent event) {
		if(v.isHovered()){
			if(!v.hasFocus())
				v.requestFocus();
		}
		return false;
	}


	/**
	 * 当前信源不为DTV或当前天线不是对应的天线时切到DTV并切到对应的天线上
	 * @param antennaType
	 */
	private void setDvbRoute(final int antennaType){
		///if (TvCommonManager.getInstance().getCurrentTvInputSource()!=TvCommonManager.INPUT_SOURCE_DTV||TvChannelManager.getInstance().getCurrentDtvRouteIndex()!=antennaType) {
			new Thread() {
				@Override
				public void run() {
					super.run();
					Log.d("panzq","set antenna "+antennaType);
					setRoute(antennaType);
				}
			}.start();
		///}else
		///{
		///	handler.sendEmptyMessage(SETIS_END_COMPLETE);
		///	return;
		///}
	}

	private void setRoute(int nDtvRoute) {
		switch (nDtvRoute) {
			case TvCommonManager.TV_SYSTEM_DVBT:
			case TvCommonManager.TV_SYSTEM_DVBC:
			case TvCommonManager.TV_SYSTEM_DVBS:
			case TvCommonManager.TV_SYSTEM_DVBT2:
			case TvCommonManager.TV_SYSTEM_DVBS2:
			case TvCommonManager.TV_SYSTEM_DTMB:
				TvDvbChannelManager.getInstance().setDtvAntennaType(nDtvRoute);
				break;
			case TvCommonManager.TV_SYSTEM_ATSC:
				TvAtscChannelManager.getInstance().setDtvAntennaType(nDtvRoute);
				TvChannelManager.getInstance().changeToFirstService(
						TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ALL,
						TvChannelManager.FIRST_SERVICE_DEFAULT);
				break;
			case TvCommonManager.TV_SYSTEM_ISDB:
				break;
			default:
				break;
		}
	}
	
	private	 void updateDatabase_systemsetting(String tag,int values) {
		int ret = -1;
		ContentValues vals = new ContentValues();
		vals.put(tag, values);
		try {
			ret = LauncherActivity.this.getContentResolver().update(
					Uri.parse("content://mstar.tv.usersetting/systemsetting"),
					vals, null, null);
		} catch (SQLException e) {
		}
		if (ret == -1) {
			System.out.println("update tbl_SystemSetting ignored");
		}
		
        try {
			TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(T_SystemSetting_IDX);
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private	 void launcherchangesource(int changeinputSource){
		ComponentName componentName = new ComponentName("com.mstar.tv.tvplayer.ui",
				"com.mstar.tv.tvplayer.ui.RootActivity");
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(componentName);
		intent.putExtra("isLauncherChangeSource", true);
		intent.putExtra("launchersource", String.valueOf(changeinputSource));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		LauncherActivity.this.startActivity(intent);
	
	}

}
