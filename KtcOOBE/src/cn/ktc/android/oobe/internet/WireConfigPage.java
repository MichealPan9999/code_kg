package cn.ktc.android.oobe.internet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;

//import com.mstar.android.ethernet.EthernetManager;
import android.net.EthernetManager;
//import com.mstar.android.ethernet.EthernetDevInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.widget.IpConfigView;

@SuppressLint("ValidFragment")
public class WireConfigPage extends BasePage
		implements View.OnClickListener,
		View.OnKeyListener {

	private static final String LOG_TAG = WireConfigPage.class.getSimpleName();

	private Button btnCancel;
	private Button btnConnect;

	private IpConfigView ipConfigView;

	private EthernetManager mEthernetManager;

	private boolean mIsWireConfiged = true;
	
    //see device/mstar/common/libraries/ethernet/java/com/mstar/android/ethernet/EthernetStateTracker.java
    public static final int EVENT_DHCP_START                        = 0;
    public static final int EVENT_INTERFACE_CONFIGURATION_SUCCEEDED = 1;
    public static final int EVENT_INTERFACE_CONFIGURATION_FAILED    = 2;
    public static final int EVENT_HW_CONNECTED                      = 3;
    public static final int EVENT_HW_DISCONNECTED                   = 4;
    public static final int EVENT_HW_PHYCONNECTED                   = 5;
    public static final int EVENT_STOP_INTERFACE                    = 6;
    public static final int EVENT_RESET_INTERFACE                   = 7;
    public static final int EVENT_ADDR_REMOVE                       = 8;
    public static final int EVENT_PROXY_CHANGE                      = 9;
    private static final int NOTIFY_ID                              = 10;
    //
    
    // ethernet status change action
    private final String ETHERNET_STATE_CHANGED_ACTION = "com.mstar.android.ethernet.ETHERNET_STATE_CHANGED";
    private final String NETWORK_STATE_CHANGED_ACTION = "com.mstar.android.ethernet.STATE_CHANGE";
    // public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE"; //WifiManager

	public WireConfigPage(EthernetManager ethernetManager) {
		this.mEthernetManager = ethernetManager;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ETHERNET_STATE_CHANGED_ACTION);
		intentFilter.addAction(NETWORK_STATE_CHANGED_ACTION);//? zjd20161122

		mContext.registerReceiver(mEthStateReceiver, intentFilter);
	}

	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_wire_config, null);

		btnCancel = (Button) layout.findViewById(R.id.btnCancel);
		btnConnect = (Button) layout.findViewById(R.id.btnConnect);
		ipConfigView = (IpConfigView) layout.findViewById(R.id.ipConfig);

		btnCancel.setOnClickListener(this);
		btnConnect.setOnClickListener(this);

		btnCancel.setOnKeyListener(this);
		btnConnect.setOnKeyListener(this);
		ipConfigView.setOnKeyListener(this);

		return layout;
	}

    @Override
    public void onResume() {
        super.onResume();
	    btnConnect.requestFocus();
	    setEthEnabled(true);
        if (isActive()) {
			refreshGui();
        }
    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(mEthStateReceiver);

		super.onDestroy();
	}

	@Override
	public void active() {
		// TODO Auto-generated method stub
		super.active();
		refreshGui();
		
		if (btnConnect != null) {
			btnConnect.requestFocus();
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

			case R.id.btnCancel: {
				mActivityListListener.onPrevActivity();
			} break;

			case R.id.btnConnect: {
				mActivityListListener.onNextActivity();
			}

			default:break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (!isActive())
			return false;

		switch (v.getId()) {

			case R.id.btnCancel: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_UP
						|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					return true;
				}
			} break;

			case R.id.btnConnect: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
						|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					return true;
				}
			} break;

			case R.id.ipConfig: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					btnCancel.requestFocus();
					return true;
				}
			} break;

			default:break;

		}

		return false;
	}

	private BroadcastReceiver mEthStateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(LOG_TAG, "mEthStateReceiver action:" + intent.getAction());

			/*zjd20161128
			if (intent.getAction().equals(ETHERNET_STATE_CHANGED_ACTION)) {
				handleEthStateChanged(intent.getIntExtra(
						EthernetManager.EXTRA_ETHERNET_STATE,
						EthernetManager.ETHERNET_STATE_UNKNOWN),
						intent.getIntExtra(
								EthernetManager.EXTRA_PREVIOUS_ETHERNET_STATE,
								EthernetManager.ETHERNET_STATE_UNKNOWN));
			} else if (intent.getAction().equals(
					EthernetManager.NETWORK_STATE_CHANGED_ACTION)) {
				handleNetworkStateChanged((NetworkInfo) intent
						.getParcelableExtra(EthernetManager.EXTRA_NETWORK_INFO));
			}*/
		}

	};

	private void handleEthStateChanged(int ethState, int previousEthState) {
			Log.d(LOG_TAG, "handleEthStateChanged");
			Log.d(LOG_TAG, "ethState: " + ethState);
			Log.d(LOG_TAG, "previousEthState: " + previousEthState);

		switch (ethState) {
			case EVENT_HW_CONNECTED:
					Log.d(LOG_TAG,"EVENT_HW_CONNECTED");
				mIsWireConfiged = true;
				break;
			case EVENT_INTERFACE_CONFIGURATION_SUCCEEDED:
					Log.d(LOG_TAG,"EVENT_INTERFACE_CONFIGURATION_SUCCEEDED");
				mIsWireConfiged = true;
				break;
			case EVENT_HW_DISCONNECTED:
					Log.d(LOG_TAG,"EVENT_HW_DISCONNECTED");
				mIsWireConfiged = false;
				break;
			case EVENT_INTERFACE_CONFIGURATION_FAILED:
					Log.d(LOG_TAG,"EVENT_INTERFACE_CONFIGURATION_FAILED");
				mIsWireConfiged =  false;
				break;
			default:
				break;
		}

		if (isActive()) {
			refreshGui();
		}

	}

	private void handleNetworkStateChanged(NetworkInfo networkInfo) {
			Log.d(LOG_TAG, "Received network state changed to " + networkInfo);
	}

	private void refreshGui() {
		boolean autoIp = false;
		/*
		if (mEthernetManager.isConfigured() && mIsWireConfiged) {
			//EthernetDevInfo mEthInfo = mEthernetManager.getSavedConfig();//zjd20161128
				//Log.d(LOG_TAG, "ifName  " + mEthInfo.getIfName());
				//Log.d(LOG_TAG, "ifName  " + "mac=" + mEthInfo.getMacAddress());

			zjd20161128	
			if (mEthInfo.getConnectMode().equals("dhcp")){//EthernetDevInfo.ETHERNET_CONN_MODE_DHCP
				autoIp = true;
			}

			
			ipConfigView.setIp(mEthInfo.getIpAddress());
			ipConfigView.setSubnetMask(mEthInfo.getNetMask());
			ipConfigView.setGateway(mEthInfo.getRouteAddr());
			ipConfigView.setFirstDns(mEthInfo.getDnsAddr());
			ipConfigView.setSecondDns(mEthInfo.getDns2Addr());
		}
		*/

		ipConfigView.setAutoIp(autoIp);
	}

	/*
    public int getEthState() {
		return mEthernetManager.getState();  
	}
	*/

	public void setEthEnabled(boolean enable) {
        //int state = getEthState();
        if (!mEthernetManager.isEnabled() && enable) { //state != EthernetManager.ETHERNET_STATE_ENABLED
            //if (mEthernetManager.isConfigured() != true) {
                // Now, kick off the setting dialog to get the configurations
            //} else {
            	mEthernetManager.setEnabled(enable);
            //}
        } else {
        	mEthernetManager.setEnabled(enable);
        }
    }

	public boolean isAutoIp() {
		return ipConfigView.isAutoIp();
	}

	public String getIp() {
		return ipConfigView.getIp();
	}

	public String getSubneMask() {
		return ipConfigView.getSubneMask();
	}

	public String getGateway() {
		return ipConfigView.getGateway();
	}

	public String getFirstDns() {
		return ipConfigView.getFirstDns();
	}

	public String getSecondDns() {
		return ipConfigView.getSecondDns();
	}
}
