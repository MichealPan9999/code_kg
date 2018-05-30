package cn.ktc.android.oobe.internet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.BasePageActivity;
import cn.ktc.android.oobe.FinishActivity;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.StartupActivity;
import cn.ktc.android.oobe.language.LanguageActivity;
import cn.ktc.android.oobe.time.TimeSetActivity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import com.mstar.android.wifi.MWifiManager;

public class InternetActivity extends BasePageActivity {
	
	private final static String TAG = "InternetActivity";
	private LocalFragmentManager mLocalManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
		mLocalManager = LocalFragmentManager.initNewFragmentManager(
				InternetActivity.this,
				R.id.rllyContainer,
				getFragmentManager());
		mLocalManager.replace(new NetworkSelectPage());
		
	}
	
	@Override
	protected void onResume () {
		super.onResume();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK: {
				int pageIndex = getCurrentItem();
				if (pageIndex == 0) {
					onPrevActivity();
				}
				return true;
			}
			default: break;

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPrevActivity() {		 
		// TODO Auto-generated method stub
		Intent intent = new Intent(StartupActivity.INTENT_ACTION_SWITCH_ACTIVITY);
		intent.putExtra(StartupActivity.INTENT_EXTRA_ACTIVITY_CLASS,LanguageActivity.class.getName());
		sendBroadcast(intent);
	}
	
	@Override
	public void onNextActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(StartupActivity.INTENT_ACTION_SWITCH_ACTIVITY);
		intent.putExtra(StartupActivity.INTENT_EXTRA_ACTIVITY_CLASS, FinishActivity.class.getName());
		sendBroadcast(intent);
	}

	@Override
	public void onFinishActivity() {
		// TODO Auto-generated method stub
		super.onFinishActivity();
	}

	@Override
	protected List<BasePage> createPages() {
		// TODO Auto-generated method stub

//		if (mWifiManager == null)
//			mWifiManager = (WifiManager) (getApplicationContext().getSystemService(Context.WIFI_SERVICE));
//
//		if (mEthernetManager == null)
//			mEthernetManager = (EthernetManager)getSystemService(Context.ETHERNET_SERVICE);
//
//		netSelectPage = new NetworkSelectPage();
//		wirelessScanPage = new WirelessScanPage(this.mWifiManager);
//		wireAutoPage = new WireAutoPage();
//		wirelessConnectingPage = new WirelessConnectingPage();
//		wireConnectingPage = new WireConnectingPage();
//		wirelessResultPage = new WirelessResultPage();
//		wireResultPage = new WireResultPage();
//
//		netSelectPage.setPageIndex(0);
//		wirelessScanPage.setPageIndex(1);
//		wireAutoPage.setPageIndex(1);
//		wirelessConnectingPage.setPageIndex(2);
//		wireConnectingPage.setPageIndex(2);
//		wirelessResultPage.setPageIndex(3);
//		wireResultPage.setPageIndex(3);
//
//		List<BasePage> pages = new ArrayList<BasePage>();
//		pages.add(netSelectPage);
//		pages.add(wireAutoPage);
//		pages.add(wirelessConnectingPage);
//		pages.add(wirelessResultPage);
//		return pages;
		return new ArrayList<>();
	}
	
	@Override
	protected void onDestroy () {
		super.onDestroy();
		mLocalManager.removeInstance(InternetActivity.this);
	}
}
