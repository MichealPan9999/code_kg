package cn.ktc.android.oobe.internet;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;

public class NetworkSelectPage extends BasePage
		implements View.OnClickListener {
	
	private Button llyWireless;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	protected View onCreatePageView (LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_network_select, null);
		
		llyWireless = (Button) layout.findViewById(R.id.llyWireless);
		Button llyWired = (Button) layout.findViewById(R.id.llyWired);
		Button btnBack = (Button) layout.findViewById(R.id.btn_net_back);
		Button btnNext = (Button) layout.findViewById(R.id.btn_net_next);
		
		llyWireless.setOnClickListener(this);
		llyWired.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		llyWireless.requestFocus();
		return layout;
	}
	
	@Override
	public void active () {
		// TODO Auto-generated method stub
		super.active();
		Log.e("active-------------", "done");
		if (llyWireless != null)
			llyWireless.requestFocus();
		
	}
	
	@Override
	public void onResume () {
		super.onResume();
		
	}
	
	private void addFragment (BasePage page) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(page, page.getClass().getSimpleName());
		transaction.commit();
	}
	
	@Override
	public void onClick (View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
			case R.id.llyWireless: {
				WirelessScanPage scanPage = new WirelessScanPage();
				mLocalManager.replace(scanPage);
			}
			break;
			case R.id.llyWired: {
				WireAutoPage autoPage = new WireAutoPage();
				mLocalManager.replace(autoPage);
			}
			break;
			case R.id.btn_net_back: {
				mActivityListListener.onPrevActivity();
			}
			break;
			case R.id.btn_net_next: {
				mActivityListListener.onNextActivity();
			}
			break;
			default:
				break;
		}
	}
}
