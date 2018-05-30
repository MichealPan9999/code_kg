package cn.ktc.android.oobe.internet;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.IpConfiguration.IpAssignment;
import android.net.IpConfiguration.ProxySettings;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.internet.wireless.WifiUtils;


public class WirelessConnectingPage extends BasePage {
	public static final int CONNECT_IDLE = 0;
	public static final int CONNECTING = 1;
	public static final int CONNECT_FAILED = 2;
	public static final int CONNECT_SUCCESS = 3;
	private static final String LOG_TAG = WirelessConnectingPage.class.getSimpleName();
	private static final int MSG_TIME_OUT = 1;
	private static final int MSG_CONNECT_SUCCESS = 2;
	private static final int MSG_CONNECT_FAILURE = 3;
	private static final int CONNECT_TIME_OUT = 15 * 1000;
	private static int COUNT = 0;
	private Context mContext;
	private boolean start_flags = false;
	private WifiManager mWifiManager;
	private ConnectivityManager mCManager;
	private NetWorkReceiver mNetworkReceiver;
	private IpAssignment mIpAssignment = IpAssignment.UNASSIGNED;
	private ProxySettings mProxySettings = ProxySettings.UNASSIGNED;
	private ScanResult mResult;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage (Message msg) {
			
			if (!isVisible()) {
				return;
			}
			
			Log.e("wifiConnecting", "handleMessage: " + String.valueOf(msg.what));
			// TODO Auto-generated method stub
			if (msg.what == MSG_TIME_OUT) {
				mWifiManager.setWifiEnabled(false);
				setConnectResult(CONNECT_FAILED);
			} else if (msg.what == MSG_CONNECT_SUCCESS) {
				setConnectResult(CONNECT_SUCCESS);
			} else if (msg.what == MSG_CONNECT_FAILURE) {
				setConnectResult(CONNECT_FAILED);
			}
			mHandler.removeMessages(MSG_TIME_OUT);
		}
		
	};
	
	private void setConnectResult (int state) {
		WirelessResultPage resultPage = new WirelessResultPage();
		Bundle bundle = new Bundle();
		bundle.putInt("state", state);
		bundle.putParcelable("result", mResult);
		resultPage.setArguments(bundle);
		mLocalManager.replace(resultPage);
	}
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		mCManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		mContext = getContext();
		mNetworkReceiver = new NetWorkReceiver();
		IntentFilter filter = new IntentFilter();
		//filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		mContext.registerReceiver(mNetworkReceiver, filter);
		
	}
	
	@Override
	protected View onCreatePageView (LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.page_network_connecting, null);
	}
	
	@Override
	public void onResume () {
		super.onResume();
		forgetAndConnect();
	}
	
	private void forgetAndConnect () {
		List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration configuration : list) {
			if (configuration.networkId != -1)
				mWifiManager.forget(configuration.networkId, null);
		}
		
		Bundle bundle = getArguments();
		mResult = bundle.getParcelable("result");
		final String password = bundle.getString("password");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run () {
				if (isVisible())
					connect(mResult, password);
			}
		}, 2000);
		
	}
	
	public void connect (ScanResult result, String password) {
		
		final WifiConfiguration config = getConfig(result, password);
	
		mWifiManager.connect(config, null);
		
		if (mWifiManager.isWifiEnabled()) {
			mHandler.sendEmptyMessageDelayed(MSG_TIME_OUT, 15 * 1000);
		} else {
			mHandler.sendEmptyMessage(MSG_CONNECT_FAILURE);
		}
	}
	
	private WifiConfiguration getConfig (ScanResult result, String password) {
		
		if (null == result) {
			Log.e(LOG_TAG, "ScanResult is NULL");
			return null;
		}
		
		WifiConfiguration config = new WifiConfiguration();
		config.SSID = convertToQuotedString(result.SSID);
		int security = WifiUtils.getSecurity(result);
		
		switch (security) {
			case WifiUtils.SECURITY_NONE:
				config.allowedKeyManagement.set(KeyMgmt.NONE);
				break;
			
			case WifiUtils.SECURITY_WEP:
				config.allowedKeyManagement.set(KeyMgmt.NONE);
				config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
				config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
				if (password.length() != 0) {
					int length = password.length();
					// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
					if ((length == 10 || length == 26 || length == 58)
							&& password.matches("[0-9A-Fa-f]*")) {
						config.wepKeys[0] = password;
					} else {
						config.wepKeys[0] = '"' + password + '"';
					}
				}
				break;
			
			case WifiUtils.SECURITY_PSK:
				config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
				if (password.length() != 0) {
					if (password.matches("[0-9A-Fa-f]{64}")) {
						config.preSharedKey = password;
					} else {
						config.preSharedKey = '"' + password + '"';
					}
				}
				break;
			default:
				return null;
		}
		
		// [2012-1-19 add]
		/* old api
		config.proxySettings = ProxySettings.NONE;
		config.ipAssignment = IpAssignment.DHCP;
		config.linkProperties = new LinkProperties(mLinkProperties);*/
		
		//modified for 828-android5.0, zjd20161128, start
		config.setProxySettings(ProxySettings.NONE);
		config.setIpAssignment(IpAssignment.DHCP);
		//modified for 828-android5.0, zjd20161128, end
		
		return config;
	}
	
	private static String convertToQuotedString (String string) {
		return "\"" + string + "\"";
	}
	
	@Override
	public void onDestroy () {
		super.onDestroy();
		mContext.unregisterReceiver(mNetworkReceiver);
		mHandler.removeMessages(MSG_TIME_OUT);
	}
	
	class NetWorkReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive (Context context, Intent intent) {
			String action = intent.getAction();
			if (action != null)
				if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
					NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
					if (networkInfo.isConnected()) {
						WifiInfo info = mWifiManager.getConnectionInfo();
						if (info.getBSSID().equals(mResult.BSSID)) {
							mHandler.sendEmptyMessage(MSG_CONNECT_SUCCESS);
						}
					}
				}
		}
	}
}

