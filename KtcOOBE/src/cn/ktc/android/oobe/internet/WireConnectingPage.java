package cn.ktc.android.oobe.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;

//import com.mstar.android.ethernet.EthernetManager;

public class WireConnectingPage extends BasePage {
	private static final String LOG_TAG = WireConnectingPage.class.getSimpleName();
	public static final int CONNECT_IDLE = 0;
	public static final int CONNECTING = 1;
	public static final int CONNECT_FAILED = 2;
	public static final int CONNECT_SUCCESS = 3;
	
	private boolean start_flag = false;
	private EthernetManager mEthernetManager;
	private ConnectivityManager mConnectivityManager;
	private Handler mHandler = new Handler();
	private NetworkInfo netInfo;
	
	
	
	Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage (Message msg) {
			setConnectResult(msg.what);
			return true;
		}
	});
	
	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mEthernetManager = (EthernetManager)mContext.getSystemService(Context.ETHERNET_SERVICE);
		mConnectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.mContext = getContext();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(mEthStateReceiver, intentFilter);
	}

	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return  inflater.inflate(R.layout.page_network_connecting, null);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		connectAutoIp();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(mEthStateReceiver);
		super.onDestroy();
	}
	
	public void connectAutoIp() {

		IpConfiguration config = new IpConfiguration();
		config.setIpAssignment(IpAssignment.DHCP);
		mEthernetManager.setConfiguration(config);
		//zjd20161124,end
		mEthernetManager.setEnabled(true);
		handler.sendEmptyMessageDelayed(CONNECT_FAILED,10*1000);
		
		//handleEthStateChanged(EVENT_INTERFACE_CONFIGURATION_SUCCEEDED, CONNECT_IDLE);
	}
	
	private void setConnectResult(int state){
		WireResultPage resultPage = new WireResultPage();
		Bundle bundle = new Bundle();
		bundle.putInt("state", state);
		resultPage.setArguments(bundle);
		mLocalManager.replace(resultPage);
	}
	
	private BroadcastReceiver mEthStateReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action != null)
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Network[] list = mConnectivityManager.getAllNetworks();
				for(Network network : list){
					NetworkInfo info = mConnectivityManager.getNetworkInfo(network);
					if(info.getType() == ConnectivityManager.TYPE_ETHERNET){
						netInfo = info;
						break;
					}
				}
				
				if (netInfo != null){
						if (netInfo.isConnected()) {
							if(isVisible()){
								handler.sendEmptyMessage(CONNECT_SUCCESS);
							}else{
								handler.sendEmptyMessageDelayed(CONNECT_SUCCESS, 1000);
							}
							
						}else {
							if(isVisible()) {
								handler.sendEmptyMessage(CONNECT_FAILED);
							}else{
								handler.sendEmptyMessageDelayed(CONNECT_FAILED, 1000);
							}
					    }
				}
			}
		}

	};
}
