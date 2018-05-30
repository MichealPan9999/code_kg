package cn.ktc.android.oobe;

import java.util.ArrayList;
import java.util.List;

import cn.ktc.android.oobe.R;
//import cn.ktc.android.oobe.internet.InternetActivity;
import cn.ktc.android.oobe.language.LanguageActivity;
import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends BasePageActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
//		setPageActivityTitle(getResources().getString(R.string.welcome_title));
	}

	@Override
	public void onNextActivity() {
		// TODO Auto-generated method stub		
		Intent intent = new Intent(StartupActivity.INTENT_ACTION_SWITCH_ACTIVITY);
		intent.putExtra(StartupActivity.INTENT_EXTRA_ACTIVITY_CLASS, LanguageActivity.class.getName());
		//intent.putExtra(StartupActivity.INTENT_EXTRA_DIR, StartupActivity.DIR_RITHT);
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
		WelcomePage welPage = new WelcomePage();
		welPage.setPageIndex(0);
		
		List<BasePage> pages = new ArrayList<BasePage>();
		pages.add(welPage);
		
		return pages;
	}

}
