package cn.ktc.android.oobe.internet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.security.KeyStore;

import javax.security.auth.login.LoginException;

import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;

public class WireAutoPage extends BasePage 
		implements View.OnClickListener,View.OnKeyListener,EthernetManager.Listener{
	private Button btnConnect,btnBack, btnNext;

	private ConnectivityManager mCManager;
	private EthernetManager mEManager;
	private boolean isEthAvailable=false;
	private final String TAG = getClass().getSimpleName();
	
/*	public WireAutoPage() {
	}*/	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCManager= (ConnectivityManager) getActivity().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mEManager = (EthernetManager)mContext.getSystemService(Context.ETHERNET_SERVICE);
		mEManager.addListener(this);
		mEManager.setEnabled(true);
	}

	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_wire_auto, null);

		btnConnect = (Button) layout.findViewById(R.id.btnWireConnect);
		btnBack =(Button) layout.findViewById(R.id.btn_wireauto_back); 
		btnNext =(Button) layout.findViewById(R.id.btn_wireauto_next);
		
		btnConnect.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		
		btnConnect.setOnKeyListener(this);
		btnBack.setOnKeyListener(this);
		btnNext.setOnKeyListener(this);
		btnConnect.requestFocus();
		return layout;
	}

    @Override
    public void onResume() {
        super.onResume();
    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mEManager.removeListener(this);
		super.onDestroy();
		
	}

//	@Override
//	public void active() {
//		// TODO Auto-generated method stub
//		super.active();
//		Log.e(TAG, "active: on");
//
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run () {
//		if(btnConnect != null){
//			btnConnect.requestFocus();
//				}
//		}
//		},50);
//	}
		
//	@Override
//	public void inactive () {
//		Log.e(TAG, "inactive: on");
//		super.inactive();
//	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {    		
		case KeyEvent.KEYCODE_BACK:			 
			mLocalManager.back(WireAutoPage.class.getSimpleName());
    		 return true;   		    		 
         default:
             break;
		}
		if (!isActive())
			return false;
		switch (v.getId()) {
			case R.id.btnWireConnect: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						btnBack.requestFocus();
					}
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						btnBack.requestFocus();
					}
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						btnNext.requestFocus();
					}
					return true;
				}
			} break;
			
			case R.id.btn_wireauto_back: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						btnConnect.requestFocus();
					}
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						btnNext.requestFocus();
					}
					return true;
				}
			} break;
			
			case R.id.btn_wireauto_next: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						btnConnect.requestFocus();
					}
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						btnBack.requestFocus();
					}
					return true;
				} 
			} break;
			default:
				break;
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btnWireConnect: {
				
				NetworkInfo info = mCManager.getActiveNetworkInfo();
				if(info != null){
					if(info.getType() == ConnectivityManager.TYPE_ETHERNET
							&& info.isConnected()){
						Toast.makeText(getActivity(), R.string.eth_conneted, Toast.LENGTH_SHORT).show();
						return;
					}
				}
				
				if(isEthAvailable) {
					mLocalManager.replace(new WireConnectingPage());
				}
				else{
					Toast.makeText(getActivity(), R.string.eth_no_devices,Toast.LENGTH_SHORT).show();
				}
			} break;
			case R.id.btn_wireauto_back: {
				mLocalManager.back(WireAutoPage.class.getSimpleName());
			} break;
			case R.id.btn_wireauto_next: {
				mActivityListListener.onNextActivity();
			} break;
			default:break;
		}
	}
	
	
	@Override
	public void onAvailabilityChanged (boolean b) {
		Log.e("onAvailabilityChanged", String.valueOf(b));
		isEthAvailable = b;
	}
}
