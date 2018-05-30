package com.ktc.launcher;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.ktc.launcher.R;
import com.ktc.launcher.constants.Constants;
import com.mstar.android.MKeyEvent;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

public class SlectAppListActivity extends Activity {
	private GridView appGridView;
	private List<ResolveInfo> listAllApps;
	private List<ResolveInfo> listValidApps;
	private AllAppAdapter mAllAppAdapter;
	private Context mContext;
	private String app_layout;
	private String TAG="SlectAppListActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectapplication);
		app_layout=getIntent().getStringExtra("app_layout");
		appGridView = (GridView) findViewById(R.id.appgridview);
		mContext = this;
		registApkFreshReceiver();
		initGridView();
		appGridView.setSelection(0);
	}
	
	private void initGridView() {
		final PackageManager packageManager = getPackageManager();
		final Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
		mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		listValidApps = new ArrayList<ResolveInfo>();
		listAllApps = packageManager.queryIntentActivities(mIntent, 0);
		listValidApps.clear();
		for(int i=0;i<listAllApps.size();i++)
		{
			ResolveInfo info = listAllApps.get(i);
			String packageName = info.activityInfo.packageName;
			if(filterApk(packageName))
			{
				continue;
			}
			listValidApps.add(info);
		}
		mAllAppAdapter = new AllAppAdapter(this, listValidApps);
		appGridView.setAdapter(mAllAppAdapter);
		appGridView.setOnItemClickListener(new AllAppItemcListener());
	}



	/**
	 * 过滤掉不需要显示的应用 @ packagenName @ return boolean
	 */
	public boolean filterApk(String packagenName) {
		if(packagenName.equals("com.android.gallery3d")){
			return true;
		}else if(packagenName.equals("com.android.contacts")){
			return true;
		}else if(packagenName.equals("com.android.phone")){
			return true;
		}else if(packagenName.equals("mstar.factorymenu.ui")){
		    return true;
		}else if(packagenName.equals("com.broadcom.bluetoothmonitor")){
		    return true;
		}else if(packagenName.equals("com.awox.quickcontrolpoint")){
		    return true;
		}else if(packagenName.equals("com.awox.renderer3")){
		    return true;
		}else if(packagenName.equals("com.android.dummyactivity")){
			return true;
		}else if(packagenName.equals("com.awox.server")){
			return true;
		}else if(packagenName.equals("com.android.camera2")){
			return true;
		}else if(packagenName.equals("com.android.deskclock")){
			return true;
		}else if(packagenName.equals("com.android.calculator2")){
			return true;
		}else if(packagenName.equals("com.android.music")){
			return true;
		}else if(packagenName.equals("com.mstar.android.tv.app")){
                       return true;
        }else if(packagenName.equals("com.ktc.launcher")){
                       return true;
        }else if(packagenName.equals("com.google.android.googlequicksearchbox")){
			return true;
		}
		
		/*else if(packagenName.equals("mstar.tvsetting.ui")){
			return true;
		}
		else if(packagenName.equals("com.jrm.localmm")){
			return true;
		}else if(packagenName.equals("com.android.apkinstaller")){
		    return true;
		}
		else if(packagenName.equals("com.android.email")){
			return true;
		}*/					
		return false;
	}


	private class AllAppItemcListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ResolveInfo appInfo = (ResolveInfo) parent
					.getItemAtPosition(position);
			Editor edit=getSharedPreferences(Constants.SAVE_APPLIST, MODE_PRIVATE).edit();
			Log.i(Constants.TAG, "onItemClick:"+app_layout+"  "+appInfo.activityInfo.packageName);
			edit.putString(app_layout, appInfo.activityInfo.packageName);
			edit.commit();
			Log.i(Constants.TAG, appInfo.activityInfo.packageName);
		    finish();
		}
	}
	
	/**
	 * 系统卸载和安装后页面刷新的广播
	 */
	private BroadcastReceiver apkFreshReceiver  = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(Intent.ACTION_PACKAGE_ADDED) ||action.equals(Intent.ACTION_PACKAGE_REMOVED)){
				initGridView();
				appGridView.setSelection(0);
			}
		}
		
	};
	
	


	
	private void registApkFreshReceiver()
	{
		IntentFilter apkFreshFilter = new IntentFilter();
		apkFreshFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		apkFreshFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		apkFreshFilter.addDataScheme("package");
		mContext.registerReceiver(apkFreshReceiver, apkFreshFilter);
	}

	
	@Override
	protected void onDestroy() {
		unregisterReceiver(apkFreshReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if((event.getKeyCode() == KeyEvent.KEYCODE_TV_INPUT)){
            event.setSource(KeyEvent.ACTION_MULTIPLE); 
            //lixq 20151119 Add for TV to shut down when TV no single start
            {
				short sourcestatus[] = null;
				try {
					sourcestatus = TvManager.getInstance()
							.setTvosCommonCommand("SetAutoSleepOnStatus");
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
          //lixq 20151119 Add for TV to shut down when TV no single end
            ComponentName componentName = new ComponentName(
                    "com.mstar.tv.tvplayer.ui", "com.mstar.tv.tvplayer.ui.RootActivity");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(componentName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}

}
