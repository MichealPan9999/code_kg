package cn.ktc.android.oobe;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import cn.ktc.android.oobe.language.LanguageActivity;
import cn.ktc.android.oobe.time.TimeSetActivity;

public class PrivacyTermsActivity extends BasePageActivity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
	}

	@Override
	public void onNextActivity() {
		// TODO Auto-generated method stub		
		Intent intent = new Intent(StartupActivity.INTENT_ACTION_SWITCH_ACTIVITY);
		intent.putExtra(StartupActivity.INTENT_EXTRA_ACTIVITY_CLASS, TimeSetActivity.class.getName());
		sendBroadcast(intent);
	}

	@Override
	public void onPrevActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(StartupActivity.INTENT_ACTION_SWITCH_ACTIVITY);
		intent.putExtra(StartupActivity.INTENT_EXTRA_ACTIVITY_CLASS, LanguageActivity.class.getName());
		sendBroadcast(intent);
	}
	
	@Override
	public void onFinishActivity() {
		// TODO Auto-generated method stub
		super.onFinishActivity();
	}

	@Override
	public void onPrevPage(BasePage page) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onNextPage(BasePage page) {
		// TODO Auto-generated method stub		
	}

	@Override
	protected List<BasePage> createPages() {
		// TODO Auto-generated method stub
		PrivacyTermsPage privacyTermsPage = new PrivacyTermsPage();
		privacyTermsPage.setPageIndex(0);
		
		List<BasePage> pages = new ArrayList<BasePage>();
		pages.add(privacyTermsPage);
		
		return pages;
	}



}
