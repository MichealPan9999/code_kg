package cn.ktc.android.oobe.internet;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;

public class WireResultPage extends BasePage 
		implements View.OnClickListener, View.OnKeyListener {
	
	private TextView tvPrompt;
	private Button btnFinish, btnBack, btnNext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_wire_result, null);

		tvPrompt = (TextView) layout.findViewById(R.id.tv_connected_prompt);
		
		btnBack =(Button)layout.findViewById(R.id.btn_wireresult_back); 
		btnNext =(Button)layout.findViewById(R.id.btn_wireresult_next);
		
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		
		btnBack.setOnKeyListener(this);
		btnNext.setOnKeyListener(this);
		btnNext.requestFocus();
		
		Bundle bundle = getArguments();
		int state = bundle.getInt("state");
		if(state == WireConnectingPage.CONNECT_SUCCESS){
			success();
		}else{
			failed();
		}
		return layout;
	}

    @Override
    public void onResume() {
        super.onResume();
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_wireresult_back: {
				mLocalManager.back(WireConnectingPage.class.getSimpleName());
			} break;
			
			case R.id.btn_wireresult_next: {
				mActivityListListener.onNextActivity();
			} break;
			
			default:break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (!isActive())
			return false;
		
		switch (v.getId()) {
		
		case R.id.btn_wireresult_back: {
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					btnNext.requestFocus();
				}
				return true;
			}
		} break;
		
		case R.id.btn_wireresult_next: {
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					btnBack.requestFocus();
				}
				return true;
			}
		} break;
			
			default:break;
			
		}
		
		return false;
	}
	
	public void success() {
		tvPrompt.setText(R.string.wire_connected_successed_prompt);
	}
	
	public void failed() {
		tvPrompt.setText(R.string.wire_connected_failed_prompt);
	}

}