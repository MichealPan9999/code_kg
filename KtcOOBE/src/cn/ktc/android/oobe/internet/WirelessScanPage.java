package cn.ktc.android.oobe.internet;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.internet.wireless.WifiEditDialog;
import cn.ktc.android.oobe.internet.wireless.WifiEditDialog.OnWifiApListener;
import cn.ktc.android.oobe.language.LanguagePage;
import cn.ktc.android.oobe.internet.wireless.WifiListAdapter;
import cn.ktc.android.oobe.internet.wireless.WifiUtils;
import cn.ktc.android.oobe.widget.CustomListView;

@SuppressLint("ValidFragment")
public class WirelessScanPage extends BasePage
		implements View.OnClickListener, View.OnKeyListener, OnItemClickListener, OnWifiApListener {
	
	static final int FINISH = 0;
	static final int RECEIVER = 3;
	private final static String TAG = WirelessScanPage.class.getSimpleName();
	private static final int WIFI_RESCAN_INTERVAL_MS = 5 * 1000;
	private static boolean SCAN_STOPED = false;//to solve input pwd slow
	protected WifiManager mWifiManager;
	
	private Button btnScanAgain, btnBack, btnNext;
	private View llyProgress;
	private CustomListView mListView;
	private List<ScanResult> mScanResults;
	private WifiListAdapter mAdapter;
	private ScanResult result;
	private String password;
	private IntentFilter mFilter;
	private String IDENTIFY="WirelessS";
	private BroadcastReceiver mReceiver;
	private AtomicBoolean mConnected = new AtomicBoolean(false);
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage (android.os.Message msg) {
			super.handleMessage(msg);
			Log.d("wifi", "handle message");
			switch (msg.what) {
				case RECEIVER:
					Log.e(TAG, "handleMessage: got data");
					llyProgress.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					int selectedItem = mListView.getSelectedItemPosition();
					mAdapter.notifyDataSetChanged();
					if (selectedItem > (mScanResults.size() - 1))
						selectedItem = mScanResults.size() - 1;
					else if (selectedItem < 0)
						selectedItem = 0;
					
					mListView.setSelection(selectedItem);
					llyProgress.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					break;
			}
		}
		
		;
	};
	
	public WirelessScanPage (WifiManager wifiManager) {
		this.mWifiManager = wifiManager;
	}
	
	public WirelessScanPage () {
	
	}
	
	public static boolean isExistInScanList (WifiConfiguration config,
	                                         List<ScanResult> scanlists) {
		
		if (null == config || null == scanlists)
			return false;
		
		for (ScanResult scan : scanlists) {
			String str = "\"" + scan.SSID + "\"";
			if (str.equals(config.SSID)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mWifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager != null) {
			mWifiManager.setWifiEnabled(true);
			mScanResults = mWifiManager.getScanResults();
		}
		
		if (mScanResults == null) {
			mScanResults = new ArrayList<ScanResult>();
		}
		
		mFilter = new IntentFilter();
		mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive (Context context, Intent intent) {
				
				String action = intent.getAction();
				Log.i(TAG, "action=" + action);
				handleEvent(context, intent);
			}
		};
		
	}
	
	@Override
	protected View onCreatePageView (LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View mParent = inflater.inflate(R.layout.page_wireless_scan, null);
		
		btnScanAgain = (Button) mParent.findViewById(R.id.btnScanAgain);
		btnBack = (Button) mParent.findViewById(R.id.btn_wireless_back);
		btnNext = (Button) mParent.findViewById(R.id.btn_wireless_next);
		llyProgress = mParent.findViewById(R.id.llyScanningProgress);
		mListView = (CustomListView) mParent.findViewById(R.id.lvWireless);
		
		btnScanAgain.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		
		btnScanAgain.setOnKeyListener(this);
		btnBack.setOnKeyListener(this);
		btnNext.setOnKeyListener(this);
		mListView.setOnKeyListener(this);
		
		mAdapter = new WifiListAdapter(mContext, mScanResults);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(this);
		
		llyProgress.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.INVISIBLE);
		btnScanAgain.requestFocus();
		return mParent;
	}
	
	
	private void handleEvent (Context context, Intent intent) {
		String action = intent.getAction();
		Log.d("wifi", "handle event: " + action);
		if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action) ||
				WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
			updateSignalList();
		} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
			Log.d(TAG, "NETWORK_STATE_CHANGED_ACTION");
			NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(
					WifiManager.EXTRA_NETWORK_INFO);
			mConnected.set(info.isConnected());
			WifiUtils.refreshConnectedID(mContext);
			if (mConnected.get()) {
				mListView.invalidateViews();
			}
		}
		mListView.invalidateViews();
	}
	
	public void updateSignalList () {
		
		ArrayList<ScanResult> accessPoints = new ArrayList<ScanResult>();
		final List<ScanResult> results = mWifiManager.getScanResults();
		if (results != null) {
			for (ScanResult result : results) {
				if (TextUtils.isEmpty(result.SSID) || result.capabilities.contains("[IBSS]")) {
					continue;
				}
				accessPoints.add(result);
			}
		}
		if (null == mScanResults) {
			mScanResults = new ArrayList<ScanResult>();
		}
		mScanResults.clear();
		mScanResults.addAll(accessPoints);
		mAdapter.notifyDataSetChanged();
		mHandler.sendEmptyMessage(RECEIVER);
	}
	
	@Override
	public void onStart () {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public void onResume () {
		super.onResume();
		mWifiManager.setWifiEnabled(true);
		mWifiManager.startScan();
		
		mContext.registerReceiver(mReceiver, mFilter);
	}
	
	@Override
	public void onPause () {
		// TODO Auto-generated method stub
		super.onPause();
		mContext.unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onClick (View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
			case R.id.btnScanAgain: {
				if (llyProgress != null)
					llyProgress.setVisibility(View.VISIBLE);
				if (mListView != null)
					mListView.setVisibility(View.INVISIBLE);
				mScanResults.clear();
				mAdapter.notifyDataSetChanged();
				mWifiManager.startScan();
			}
			break;
			
			case R.id.btn_wireless_back: {
				mLocalManager.back(WirelessScanPage.class.getSimpleName());
			}
			break;
			
			case R.id.btn_wireless_next: {
				mActivityListListener.onNextActivity();
			}
			break;
			
			default:
				break;
		}
	}
	
	@Override
	public boolean onKey (View v, int keyCode, KeyEvent event) {
		
		switch (keyCode) {    		
		case KeyEvent.KEYCODE_BACK:		 
			mLocalManager.back(WirelessScanPage.class.getSimpleName());
    		 return true;   		    		 
         default:
             break;
		}
		// TODO Auto-generated method stub
		if (!isActive())
			return false;
		
		switch (v.getId()) {
			case R.id.btnScanAgain: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnBack.requestFocus();
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnBack.requestFocus();
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnNext.requestFocus();
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
					mListView.requestFocus();
					mListView.setSelection(mListView.getAdapter().getCount() - 1);
					return true;
				}
			}
			break;
			
			case R.id.lvWireless: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
//					if(mListView.getSelectedItem() == mListView.getChildCount()-1)
//					btnScanAgain.requestFocus();
//					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnBack.requestFocus();
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnNext.requestFocus();
					return true;
				}
			}
			break;
			
			case R.id.btn_wireless_back: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnNext.requestFocus();
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnScanAgain.requestFocus();
					return true;
				}
			}
			break;
			
			case R.id.btn_wireless_next: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnBack.requestFocus();
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
					btnScanAgain.requestFocus();
					return true;
				}
			}
			break;
			default:
				break;
		}		
		return false;
	}
	
	@Override
	public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		ScanResult result = (ScanResult) mAdapter.getItem(position);
		//WifiUtils.refreshConnectedID(mContext);
		if (WifiUtils.isConnected(result)) {
			SharedPreferences sharedPref = WirelessScanPage.this.getActivity().getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
	        SharedPreferences.Editor editor = sharedPref.edit();
	        editor.putBoolean("wifi_index", true);
	        editor.commit();
			//Toast.makeText(mContext, getString(R.string.wireless_connected_successed_prompt), Toast.LENGTH_SHORT).show();	      
			WifiEditDialog wpsDialog = new WifiEditDialog(mContext, result);
			wpsDialog.setOnWifiApListener(this);
			//wpsDialog.show();			 
		} 	
		int security = WifiUtils.getSecurity(result);
		if (security != WifiUtils.SECURITY_NONE) {	
			WifiEditDialog wpsDialog = new WifiEditDialog(mContext, result);
			wpsDialog.setOnWifiApListener(this);
			wpsDialog.show();
		} else {
			setConnectRequest(result, null);
			}		
	}
	
	private void setConnectRequest (ScanResult result, String password) {
		WirelessConnectingPage connectingPage = new WirelessConnectingPage();
		Bundle bundle = new Bundle();
		bundle.putParcelable("result", result);
		bundle.putString("password", password);
		connectingPage.setArguments(bundle);
		mLocalManager.replace(connectingPage);
	}
	
	@Override
	public void onPassword (ScanResult result, String password) {
		// TODO Auto-generated method stub
		
		if (!TextUtils.isEmpty(password)) {
			setConnectRequest(result, password);
		}
	}
	
	@Override
	public void onWps () {
	
	}
	
	@Override
	public void onCancel () {
	
	}
	
	public void openWifi () {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run () {
				// TODO Auto-generated method stub
				if (!mWifiManager.isWifiEnabled()) {
					mWifiManager.setWifiEnabled(true);
				}
				Log.d(TAG, "===>getWifiState()=" + mWifiManager.getWifiState());
			}
		}, 1500);
	}
	
}
