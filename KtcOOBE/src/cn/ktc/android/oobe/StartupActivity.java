package cn.ktc.android.oobe;

import com.mstar.android.tv.TvCommonManager;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;

public class StartupActivity extends ActivityGroup {	
	public static final String INTENT_ACTION_SWITCH_ACTIVITY = 
			"cn.ktc.android.philips.oobe.intent.action.switch_activity";
	public static final String INTENT_EXTRA_ACTIVITY_CLASS = 
			"cn.ktc.android.philips.oobe.intent.extra.activity_class";
	public static final String INTENT_EXTRA_DIR = 
			"cn.ktc.android.philips.oobe.intent.extra.dir";
	
	//public static final int DIR_NON = -1;
	//public static final int DIR_LEFT = 0;	
	//public static final int DIR_RITHT = 1;
	
	private FrameLayout fllyContainer;
	
	private static String CurrentClassName = WelcomeActivity.class.getName();
	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (INTENT_ACTION_SWITCH_ACTIVITY.equals(intent.getAction())) {
				String clsName = intent.getStringExtra(INTENT_EXTRA_ACTIVITY_CLASS);
				CurrentClassName = clsName;
				//int dir = intent.getIntExtra(INTENT_EXTRA_DIR, DIR_NON);
				switchActivity(clsName);
			}
		}		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_main);
		Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 1);		
		fllyContainer = (FrameLayout) findViewById(R.id.fllyContainer);		
		switchActivity(CurrentClassName);
		TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();		
		IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_ACTION_SWITCH_ACTIVITY);
        registerReceiver(mIntentReceiver, filter);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(mIntentReceiver);
		super.onStop();
	}
	
	//@Override
	public void switchActivity(String clsName) {
		// TODO Auto-generated method stub
		Class<?> cls = null;
		try {
			cls = Class.forName(clsName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cls != null && BasePageActivity.class.isAssignableFrom(cls)) {			
			View curView = fllyContainer.getChildAt(0);
			fllyContainer.removeAllViews();
			View nextView = getLocalActivityManager().startActivity("Activity",
	                new Intent(StartupActivity.this, cls).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
	                .getDecorView();
			fllyContainer.addView(nextView);
		}
	}
	
}
