package cn.ktc.android.oobe.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.BasePageActivity;
import cn.ktc.android.oobe.PrivacyTermsActivity;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.StartupActivity;
import cn.ktc.android.oobe.internet.InternetActivity;
import cn.ktc.android.oobe.language.LanguageActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class TimeSetActivity extends BasePageActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
//		setPageActivityTitle(getResources().getString(R.string.time_title));
	}

	@Override
	public void onNextActivity() {
		// TODO Auto-generated method stub		
		Intent intent = new Intent(StartupActivity.INTENT_ACTION_SWITCH_ACTIVITY);
		intent.putExtra(StartupActivity.INTENT_EXTRA_ACTIVITY_CLASS, InternetActivity.class.getName());
		//intent.putExtra(StartupActivity.INTENT_EXTRA_DIR, StartupActivity.DIR_RITHT);
		sendBroadcast(intent);
	}
	
	@Override
	public void onPrevActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(StartupActivity.INTENT_ACTION_SWITCH_ACTIVITY);
		intent.putExtra(StartupActivity.INTENT_EXTRA_ACTIVITY_CLASS,LanguageActivity.class.getName());
		//intent.putExtra(StartupActivity.INTENT_EXTRA_DIR,StartupActivity.DIR_LEFT);
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
		TimeSetPage timeSetPage = new TimeSetPage();
		timeSetPage.setPageIndex(0);
		
		List<BasePage> pages = new ArrayList<BasePage>();
		pages.add(timeSetPage);		
		return pages;
	}

}
