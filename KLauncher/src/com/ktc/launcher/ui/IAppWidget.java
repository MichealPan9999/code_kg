package com.ktc.launcher.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
//lixq 20160516 start
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.SystemProperties;
//lixq 20160516 end
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktc.launcher.AllAppListActivity;
import com.ktc.launcher.R;
import com.ktc.launcher.SlectAppListActivity;
import com.ktc.launcher.constants.Constants;
import com.ktc.launcher.mode.AppInfo;
import com.ktc.launcher.utils.ScrollTextView;

import java.util.HashMap;

public class IAppWidget extends RelativeLayout implements OnKeyListener {
	private ImageView[] ic_app_image = new ImageView[8];
	private ScrollTextView[] app_name = new ScrollTextView[8];
	private RelativeLayout[] app_layout = new RelativeLayout[8];
	private ScrollTextView app_center_name;
	private RelativeLayout app_center_layout;
	private Button popup_dismiss;
	private Button popup_replace;
	private Button popup_delete;

	private String [] miniAppPackageNames=new String [8];
	private HashMap<String, AppInfo> miniAppMap = new HashMap<String, AppInfo>();
	// private HashMap<String,AppInfo> largeAppMap=new
	// HashMap<String,AppInfo>();

	private Context context;
	private Handler changeSourceHandler;
	private boolean DEBUG = false;

	private int windowsWidth = 0;
	private int windowsHeight = 0;

	public IAppWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.iapp_widget, this);
		findView();
		setListener();
		setFocusable();
	}

	public void InitAppWidget() {
		SharedPreferences share = context.getSharedPreferences(
				Constants.SAVE_APPLIST, context.MODE_PRIVATE);
		for (int i = 0; i < 8; i++) {
			miniAppPackageNames[i] = share.getString("right_" + i,"");
			if (miniAppPackageNames[i] == null) {
				miniAppPackageNames[i] = "";
			}
			if (DEBUG)
				Log.i(Constants.TAG, i + ":" + miniAppPackageNames[i]);
		}
		HashMap<String, AppInfo> miniAppList = InitAppWidgetData(
				miniAppPackageNames);
		setMiniApp(miniAppList);
		 app_center_name.setText(R.string.application_center);
	}

	public HashMap<String, AppInfo> InitAppWidgetData(String[] appPackageName) {
		HashMap<String, AppInfo> appMap = new HashMap<String, AppInfo>(); // 用来存储获取的应用信息数据
		PackageManager packageManager = context.getPackageManager();
		Resources res = context.getResources();
		Drawable ic_add = res.getDrawable(R.drawable.ic_add);
		for (int i = 0; i < appPackageName.length; i++) {
			AppInfo tmpInfo = new AppInfo();
			try {
				// if(DEBUG) Log.i(Constants.TAG, "appPackageName:" + i);
				if (!appPackageName[i].equals("")) {
					PackageInfo packageInfo;
					packageInfo = packageManager.getPackageInfo(
							appPackageName[i], PackageManager.GET_PERMISSIONS);
					int status=packageManager.getApplicationEnabledSetting(packageInfo.packageName);
					if (packageInfo != null && status==PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
						tmpInfo.appName = packageInfo.applicationInfo
								.loadLabel(context.getPackageManager())
								.toString();
						tmpInfo.packageName = packageInfo.packageName;
						tmpInfo.versionName = packageInfo.versionName;
						tmpInfo.versionCode = packageInfo.versionCode;
						tmpInfo.appIcon = packageInfo.applicationInfo
								.loadIcon(context.getPackageManager());
						//lixq 20160516 start
						boolean isOtaNew = SystemProperties.getBoolean("persist.sys.ota.available", false);
						if(tmpInfo.packageName.equals("android.systemupdate.service") && isOtaNew){
							tmpInfo.appIcon = generatorContactCountIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
						}
						//lixq 20160516 end
					} else {
						if(status==PackageManager.COMPONENT_ENABLED_STATE_DISABLED&&i>=3){
							SharedPreferences share = context.getSharedPreferences(
									Constants.SAVE_APPLIST, context.MODE_PRIVATE);
							Editor edit = share.edit();
							edit.putString("right_" + i,"");
							edit.commit();
						}
						tmpInfo.appName = "";
						tmpInfo.packageName = "";
						tmpInfo.versionName = "";
						tmpInfo.versionCode = 0;
						tmpInfo.appIcon = ic_add;//lixq 20160516
					}
				} else {
					tmpInfo.appName = "";
					tmpInfo.packageName = "";
					tmpInfo.versionName = "";
					tmpInfo.versionCode = 0;
					tmpInfo.appIcon = ic_add;//lixq 20160516
				}
			} catch (NameNotFoundException e) {
				tmpInfo.appName = "";
				tmpInfo.packageName = "";
				tmpInfo.versionName = "";
				tmpInfo.versionCode = 0;
				tmpInfo.appIcon = ic_add; //lixq 20160516
			}
			appMap.put(""+i, tmpInfo);
		}
		if (DEBUG)
			Log.i(Constants.TAG, "appMap.size:" + appMap.size());
		return appMap;
	}

	public void findView() {
		ic_app_image[0] = (ImageView) findViewById(R.id.app_widget_1_ic);
		ic_app_image[1] = (ImageView) findViewById(R.id.app_widget_2_ic);
		ic_app_image[2] = (ImageView) findViewById(R.id.app_widget_3_ic);
		ic_app_image[3] = (ImageView) findViewById(R.id.app_widget_4_ic);
		ic_app_image[4] = (ImageView) findViewById(R.id.app_widget_5_ic);
		ic_app_image[5] = (ImageView) findViewById(R.id.app_widget_6_ic);
		ic_app_image[6] = (ImageView) findViewById(R.id.app_widget_7_ic);
		ic_app_image[7] = (ImageView) findViewById(R.id.app_widget_8_ic);

		app_name[0] = (ScrollTextView) findViewById(R.id.app_widget_1_name);
		app_name[1] = (ScrollTextView) findViewById(R.id.app_widget_2_name);
		app_name[2] = (ScrollTextView) findViewById(R.id.app_widget_3_name);
		app_name[3] = (ScrollTextView) findViewById(R.id.app_widget_4_name);
		app_name[4] = (ScrollTextView) findViewById(R.id.app_widget_5_name);
		app_name[5] = (ScrollTextView) findViewById(R.id.app_widget_6_name);
		app_name[6] = (ScrollTextView) findViewById(R.id.app_widget_7_name);
		app_name[7] = (ScrollTextView) findViewById(R.id.app_widget_8_name);

		app_layout[0] = (RelativeLayout) findViewById(R.id.app_widget_1);
		app_layout[1] = (RelativeLayout) findViewById(R.id.app_widget_2);
		app_layout[2] = (RelativeLayout) findViewById(R.id.app_widget_3);
		app_layout[3] = (RelativeLayout) findViewById(R.id.app_widget_4);
		app_layout[4] = (RelativeLayout) findViewById(R.id.app_widget_5);
		app_layout[5] = (RelativeLayout) findViewById(R.id.app_widget_6);
		app_layout[6] = (RelativeLayout) findViewById(R.id.app_widget_7);
		app_layout[7] = (RelativeLayout) findViewById(R.id.app_widget_8);

		app_center_name = (ScrollTextView) findViewById(R.id.app_widget_appcenter_name);
		app_center_layout = (RelativeLayout) findViewById(R.id.app_widget_appcenter);
	}

	public void setListener() {
		for (int i = 0; i < 8; i++) {
			//遥控事件
			app_name[i].setOnClickListener(new AppWidgetOnClick());
			//鼠标事件
			app_layout[i].setOnClickListener(new AppWidgetMouseOnClick());
			app_layout[i].setOnLongClickListener(new AppWidgetMouseOnLongClick());
			app_layout[i].setOnKeyListener(this);
		}
		//遥控事件
		app_center_name.setOnClickListener(new AppWidgetOnClick());
		//鼠标事件
		app_center_layout.setOnLongClickListener(new AppWidgetMouseOnLongClick());
		app_center_layout.setOnClickListener(new AppWidgetMouseOnClick());
		app_center_layout.setOnKeyListener(this);
		
	}

	public void setFocusable() {
		for (int i = 0; i < 8; i++) {
			app_layout[i].setFocusable(true);
		}
		app_center_layout.setFocusable(true);
	}

	public void setMiniApp(HashMap<String, AppInfo> appMap) {
		if (DEBUG)
			Log.i(Constants.TAG, "appMap.size():" + appMap.size());
		for (int i = 0; i < appMap.size(); i++) {
			Log.v("zhanghz", "img="+i);
			if (appMap.get(i+"") != null) {
				app_name[i].setText(appMap.get(i+"").appName);
				if (appMap.get(i+"").appIcon != null&&!appMap.get(i+"").appIcon.equals("")) {
					ic_app_image[i]
							.setImageDrawable(appMap.get(i+"").appIcon);
				} else {
					ic_app_image[i].setImageDrawable(new BitmapDrawable());
				}
			}
		}
		miniAppMap = appMap;
	}
	//dp-px
	public static int dip2px(Context context, float dpValue){
		
		   final float scale = context.getResources().getDisplayMetrics().density;
		   return (int)(dpValue*scale + 0.5f);
		}


	public void showPopUp(View v, String app_layout) {
		int popwin_w = 0;
		int popwin_h = 0;
		if (windowsHeight == 1080 && windowsWidth == 1920) {
			popwin_w = 198;
			popwin_h = 150;
		} else if (windowsHeight == 2160 && windowsWidth == 3840) {
			popwin_w = 198;
			popwin_h = 150;
		} else {
			popwin_w = 150;
			popwin_h = 135;
		}
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.iapp_popup, null);

		PopupWindow popupWindow = new PopupWindow(view, popwin_w, popwin_h);
		initPopView(view, popupWindow, app_layout);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		int[] location = new int[2];
		v.getLocationOnScreen(location);

		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
				location[0] + v.getWidth(), location[1] + v.getHeight());

		// popupWindow.showAsDropDown(v);
		popupWindow.update();
	}

	public void setChangeSourceHandler(Handler handler) {
		this.changeSourceHandler = handler;
	}

	public void initPopView(View view, final PopupWindow popupWindow,
			final String app_layout) {

		popup_delete = (Button) view.findViewById(R.id.popup_delete);
		popup_replace = (Button) view.findViewById(R.id.popup_replace);
		popup_dismiss = (Button) view.findViewById(R.id.popup_dismiss);
		popup_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Editor edit = context.getSharedPreferences(
						Constants.SAVE_APPLIST, context.MODE_PRIVATE).edit();
				edit.putString(app_layout, "");
				edit.commit();
				InitAppWidget();
				popupWindow.dismiss();
				if (DEBUG)
					Log.i(Constants.TAG, app_layout);
			}
		});
		popup_replace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (DEBUG)
					Log.i(Constants.TAG, "replace");
				Intent intent = new Intent(context, SlectAppListActivity.class);
				intent.putExtra("app_layout", app_layout);
				context.startActivity(intent);
				popupWindow.dismiss();
			}
		});
		popup_dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (DEBUG)
					Log.i(Constants.TAG, "dismiss");
				popupWindow.dismiss();

			}
		});
	}

	@Override
	public boolean onKey(View v, int codeKey, KeyEvent keyEvent) {
		if (DEBUG)
			Log.i(Constants.TAG, "" + codeKey);
		if (codeKey == keyEvent.KEYCODE_1
				&& keyEvent.getAction() == keyEvent.ACTION_UP) {
			switch (v.getId()) {
			case R.id.app_widget_1:
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// Log.i(Constants.TAG, "1");
				// showPopUp(app_layout[0],"mini_app0");
				return true;
			case R.id.app_widget_2:
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// showPopUp(app_layout[1],"mini_app1");
				return true;
			case R.id.app_widget_3:
				if (DEBUG)
					Log.i(Constants.TAG, "3");
				/*Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();*/
				showPopUp(app_layout[2],"right_2");
				return true;
			case R.id.app_widget_4:
				 showPopUp(app_layout[3],"right_3");
				return true;
			case R.id.app_widget_5:
				
				 showPopUp(app_layout[4], "right_4");
				return true;
			case R.id.app_widget_6:
				if (DEBUG)
					Log.i(Constants.TAG, "6");
				showPopUp(app_layout[5], "right_5");
				return true;
			case R.id.app_widget_7:
				if (DEBUG)
					Log.i(Constants.TAG, "7");
				showPopUp(app_layout[6], "right_6");
				return true;
			case R.id.app_widget_8:
				if (DEBUG)
					Log.i(Constants.TAG, "8");
				showPopUp(app_layout[7], "right_7");
				return true;
			case R.id.app_widget_appcenter:
				if (DEBUG)
					Log.i(Constants.TAG, "app center");
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// showPopUp(app_center_layout, "center_app");
				return true;
			}
		}

		if (codeKey == keyEvent.KEYCODE_MENU
				&& keyEvent.getAction() == keyEvent.ACTION_UP) {
			switch (v.getId()) {
			case R.id.app_widget_1:
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// showPopUp(app_layout[0],"mini_app0");
				return true;
			case R.id.app_widget_2:
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// showPopUp(app_layout[1],"mini_app1");
				return true;
			case R.id.app_widget_3:
				/*Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();*/
				showPopUp(app_layout[2],"right_2");
				return true;
			case R.id.app_widget_4:
				showPopUp(app_layout[3], "right_3");
				return true;
			case R.id.app_widget_5:
				 showPopUp(app_layout[4], "right_4");
				return true;
			case R.id.app_widget_6:
				showPopUp(app_layout[5], "right_5");
				return true;
			case R.id.app_widget_7:
				showPopUp(app_layout[6], "right_6");
				return true;
			case R.id.app_widget_8:
				showPopUp(app_layout[7], "right_7");
				return true;
			case R.id.app_widget_appcenter:
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// showPopUp(app_center_layout, "center_app");
				return true;
			}
		}
		return false;
	}
	/**
	 * 鼠标长按
	 * @author zengjf
	 *
	 */
	class AppWidgetMouseOnLongClick implements OnLongClickListener {
		@Override
		public boolean onLongClick(View v) {
			if (DEBUG)
				Log.i(Constants.TAG, "AppWidgetOnLongClick");
			switch (v.getId()) {
			case R.id.app_widget_1:
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// Log.i(Constants.TAG, "1");
				// showPopUp(app_layout[0],"mini_app0");
				return true;
			case R.id.app_widget_2:
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// showPopUp(app_layout[1],"mini_app1");
				return true;
			case R.id.app_widget_3:
				if (DEBUG)
					Log.i(Constants.TAG, "3");
				/*Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();*/
				showPopUp(app_layout[2],"right_2");
				return true;
			case R.id.app_widget_4:
				if (DEBUG)
					Log.i(Constants.TAG, "4");
				 showPopUp(app_layout[3], "right_3");
				return true;
			case R.id.app_widget_5:
				if (DEBUG)
					Log.i(Constants.TAG, "5");
				 showPopUp(app_layout[4], "right_4");
				return true;
			case R.id.app_widget_6:
				if (DEBUG)
					Log.i(Constants.TAG, "6");
				showPopUp(app_layout[5], "right_5");
				return true;
			case R.id.app_widget_7:
				if (DEBUG)
					Log.i(Constants.TAG, "7");
				showPopUp(app_layout[6], "right_6");
				return true;
			case R.id.app_widget_8:
				if (DEBUG)
					Log.i(Constants.TAG, "8");
				showPopUp(app_layout[7], "right_7");
				return true;
			case R.id.app_widget_appcenter:
				if (DEBUG)
					Log.i(Constants.TAG, "app center");
				Toast.makeText(context,
						getResources().getString(R.string.hint), 2000).show();
				// showPopUp(app_center_layout, "center_app");
				return true;
			default:
				break;
			}
			return true;
		}
	}

	class AppWidgetOnClick implements OnClickListener {

		public void startActivity(String packAgeName) {
			if (!packAgeName.equals("")) {

				PackageManager packageManager = context.getPackageManager();
				Intent intent = new Intent();
				intent = packageManager.getLaunchIntentForPackage(packAgeName);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				try {
					context.startActivity(intent);
				} catch (Exception e) {
					Log.e("IAppWidget", "startActivity " + packAgeName
							+ "error");
				}
				Message msg = changeSourceHandler
						.obtainMessage(Constants.SETSOURCE_MS);
				Bundle b = new Bundle();
				b.putString("packAgeName", packAgeName);
				msg.setData(b);
				changeSourceHandler.sendMessage(msg);
			}
		}

		@Override
		public void onClick(View v) {
			int id = v.getId();
			AppInfo appInfo = null;
			switch (id) {
			case R.id.app_widget_1_name:
				Log.i(Constants.TAG, "1");
				appInfo = miniAppMap.get("0");
				if (appInfo != null) {
					startActivity(appInfo.packageName);// "com.jrm.localmm"
				}
				break;
			case R.id.app_widget_2_name:
				Log.i(Constants.TAG, "2");
				appInfo = miniAppMap.get("1");
				if (appInfo != null) {
					startActivity(appInfo.packageName);// "com.jrm.localmm"
				}
				break;
			case R.id.app_widget_3_name:
				Log.i(Constants.TAG, "3");
				appInfo = miniAppMap.get("2");
				if (appInfo != null) {
					startActivity(appInfo.packageName);//
					if (appInfo.packageName == "") {
						showPopUp(app_layout[3], "right_2");
					}
				}
				break;
			case R.id.app_widget_4_name:
				Log.i(Constants.TAG, "4");
				appInfo = miniAppMap.get("3");
				if (appInfo != null) {
					startActivity(appInfo.packageName);// 
					if (appInfo.packageName == "") {
						showPopUp(app_layout[3], "right_3");
					}
				}
				break;
			case R.id.app_widget_5_name:
				Log.i(Constants.TAG, "5");
				appInfo = miniAppMap.get("4");
				if (appInfo != null) {
					startActivity(appInfo.packageName);// 
					if (appInfo.packageName == "") {
						showPopUp(app_layout[4], "right_4");
					}
				}
				break;
			case R.id.app_widget_6_name:
				Log.i(Constants.TAG, "6");
				appInfo = miniAppMap.get("5");
				if (appInfo != null) {
					startActivity(appInfo.packageName);//
					if (appInfo.packageName == "") {
						showPopUp(app_layout[5], "right_5");
					}
				}
				break;
			case R.id.app_widget_7_name:
				Log.i(Constants.TAG, "7");
				appInfo = miniAppMap.get("6");
				if (appInfo != null) {
					startActivity(appInfo.packageName);
					if (appInfo.packageName == "") {
						showPopUp(app_layout[6], "right_6");
					}
				}
				break;
			case R.id.app_widget_8_name:
				Log.i(Constants.TAG, "8");
				appInfo = miniAppMap.get("7");
				if (appInfo != null) {
					startActivity(appInfo.packageName);
					if (appInfo.packageName == "") {
						showPopUp(app_layout[7], "right_7");
					}
				}

				break;
			case R.id.app_widget_appcenter_name:
				Log.i(Constants.TAG, "app center");
				Intent intent = new Intent(context, AllAppListActivity.class);
				context.startActivity(intent);
				Message msg = changeSourceHandler
						.obtainMessage(Constants.SETSOURCE_MS);
				Bundle b = new Bundle();
				b.putString("packAgeName",
						"com.android.mslauncher.AllAppListActivity");
				msg.setData(b);
				changeSourceHandler.sendMessage(msg);
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 鼠标点击事件
	 * @author zengjf
	 *
	 */
	class AppWidgetMouseOnClick implements OnClickListener {

		public void startActivity(String packAgeName) {
			if (!packAgeName.equals("")) {
				PackageManager packageManager = context.getPackageManager();
				Intent intent = new Intent();
				intent = packageManager.getLaunchIntentForPackage(packAgeName);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				try {
					context.startActivity(intent);
				} catch (Exception e) {
					Log.e("IAppWidget", "startActivity " + packAgeName
							+ "error");
				}
				Message msg = changeSourceHandler
						.obtainMessage(Constants.SETSOURCE_MS);
				Bundle b = new Bundle();
				b.putString("packAgeName", packAgeName);
				msg.setData(b);
				changeSourceHandler.sendMessage(msg);
			}
		}

		@Override
		public void onClick(View v) {
			int id = v.getId();
			String packageName = null;
			AppInfo appInfo = null;
			switch (id) {
			case R.id.app_widget_1:
				Log.i(Constants.TAG, "1");
				appInfo = miniAppMap.get("0");
				if (appInfo != null) {
					startActivity(appInfo.packageName);// "com.jrm.localmm"
				}
				break;
			case R.id.app_widget_2:
				Log.i(Constants.TAG, "2");
				appInfo = miniAppMap.get("1");
				if (appInfo != null) {
					startActivity(appInfo.packageName);// "com.android.browser"

				}
				break;
			case R.id.app_widget_3:
				Log.i(Constants.TAG, "3");
				appInfo = miniAppMap.get("2");
				if (appInfo != null) {
					startActivity(appInfo.packageName);
					if (appInfo.packageName == "") {
						showPopUp(app_layout[2], "right_2");
					}
				}
				break;
			case R.id.app_widget_4:
				Log.i(Constants.TAG, "4");
				appInfo = miniAppMap.get("3");
				if (appInfo != null) {
					startActivity(appInfo.packageName);
					if (appInfo.packageName == "") {
						showPopUp(app_layout[3], "right_3");
					}
				}
				break;
			case R.id.app_widget_5:
				Log.i(Constants.TAG, "5");
				appInfo = miniAppMap.get("4");
				if (appInfo != null) {
					startActivity(appInfo.packageName);// 
					if (appInfo.packageName == "") {
						showPopUp(app_layout[4], "right_4");
					}
				}
				break;
			case R.id.app_widget_6:
				Log.i(Constants.TAG, "6");
				appInfo = miniAppMap.get("5");
				if (appInfo != null) {
					startActivity(appInfo.packageName);//
					if (appInfo.packageName == "") {
						showPopUp(app_layout[5], "right_5");
					}
				}
				break;
			case R.id.app_widget_7:
				Log.i(Constants.TAG, "7");
				appInfo = miniAppMap.get("6");
				if (appInfo != null) {
					startActivity(appInfo.packageName);
					if (appInfo.packageName == "") {
						showPopUp(app_layout[6], "right_6");
					}
				}
				break;
			case R.id.app_widget_8:
				Log.i(Constants.TAG, "8");
				appInfo = miniAppMap.get("7");
				if (appInfo != null) {
					startActivity(appInfo.packageName);
					if (appInfo.packageName == "") {
						showPopUp(app_layout[7], "right_7");
					}
				}
				break;
			case R.id.app_widget_appcenter:
				Log.i(Constants.TAG, "app center");
				Intent intent = new Intent(context, AllAppListActivity.class);
				context.startActivity(intent);
				Message msg = changeSourceHandler
						.obtainMessage(Constants.SETSOURCE_MS);
				Bundle b = new Bundle();
				b.putString("packAgeName",
						"com.android.mslauncher.AllAppListActivity");
				msg.setData(b);
				changeSourceHandler.sendMessage(msg);
				break;
			default:
				break;
			}
		}
	}
	public void setWindowsWidth(int windowsWidth) {
		this.windowsWidth = windowsWidth;
	}

	public void setWindowsHeight(int windowsHeight) {
		this.windowsHeight = windowsHeight;
	}
	
	
     //lixq 20160516 start
	private Drawable generatorContactCountIcon(Drawable drawable){  
		Bitmap icon = drawableToBitamp(drawable);

        int iconSize=(int)context.getResources().getDimension(android.R.dimen.app_icon_size);  
        Bitmap contactIcon=Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);  
        Canvas canvas=new Canvas(contactIcon);  

        Paint iconPaint=new Paint();  
        iconPaint.setDither(true); 
        iconPaint.setFilterBitmap(true); 
        Rect src=new Rect(0, 0, icon.getWidth(), icon.getHeight());  
        Rect dst=new Rect(0, 0, iconSize, iconSize);  
        canvas.drawBitmap(icon, src, dst, iconPaint);  
          
        Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);  
        countPaint.setColor(Color.RED);  
        countPaint.setTextSize(18f);  
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);  
        canvas.drawText("New", iconSize-36, 24, countPaint);  
        Drawable icon_drawable = new BitmapDrawable(contactIcon);
        return icon_drawable;  
    } 
	
	private Bitmap drawableToBitamp(Drawable drawable){
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bitmap = bd.getBitmap();
		return bitmap;
	}
	//lixq 20160516 end
	
	//zhanghz
}
