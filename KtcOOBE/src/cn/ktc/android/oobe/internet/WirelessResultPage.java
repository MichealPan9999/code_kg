package cn.ktc.android.oobe.internet;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.internet.wireless.WifiUtils;

public class WirelessResultPage extends BasePage 
		implements View.OnClickListener,
		View.OnKeyListener {
	
	private TextView tvPrompt;
	private View rllySuccessed;
	private TextView tvNetName;
	private TextView tvEncryption;
	private TextView tvKey;
	private Button btnBack, btnNext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_wireless_result, null);

		rllySuccessed = layout.findViewById(R.id.rllyConnectedSuccess);
		tvPrompt = (TextView) layout.findViewById(R.id.tvConnectedPrompt);
		tvNetName = (TextView) layout.findViewById(R.id.tvNetName);
		tvEncryption = (TextView) layout.findViewById(R.id.tvEncryption);
		tvKey = (TextView) layout.findViewById(R.id.tvKey);
		
		btnBack =(Button) layout.findViewById(R.id.btn_wireless_result_back); 
		btnNext =(Button) layout.findViewById(R.id.btn_wireless_result_next);
		
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		
		Bundle bundle = getArguments();
		ScanResult result = bundle.getParcelable("result");
		int state = bundle.getInt("state");
		if(state == WirelessConnectingPage.CONNECT_SUCCESS){
			success(result);
		}else{
			failed(result);
		}
		return layout;
	}

    @Override
    public void onResume() {
        super.onResume();
	    btnNext.requestFocus();
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_wireless_result_back: {
				mLocalManager.back(WirelessConnectingPage.class.getSimpleName());
			} break;
			
			case R.id.btn_wireless_result_next: {
				mActivityListListener.onNextActivity();
			} break;
			
			default:break;
		}
	}
	
	@Override
	public void onDestroyView () {
		super.onDestroyView();
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (!isActive())
			return false;
		
		switch (v.getId()) {
			case R.id.btn_wireless_result_back: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || event.getAction() == KeyEvent.ACTION_DOWN){
					btnNext.requestFocus();
					return true;
				}
			} break;
			
			case R.id.btn_wireless_result_next: {
				if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
					btnBack.requestFocus();
					return true;
				}
			} break;
			
			default:break;
		}
		return false;
	}
	
	public void success(ScanResult result) {
		rllySuccessed.setVisibility(View.VISIBLE);
		tvPrompt.setText(R.string.wireless_connected_successed_prompt);
		tvNetName.setText(result.SSID);
		String security = WifiUtils.getSecurityString(mContext, result, true);
		tvEncryption.setText(WifiUtils.getSecurityString(mContext, result, true));
		
	}
	
	public void failed(ScanResult result) {
		rllySuccessed.setVisibility(View.GONE);
		tvPrompt.setText(R.string.wireless_connected_failed_prompt);
	}

}
