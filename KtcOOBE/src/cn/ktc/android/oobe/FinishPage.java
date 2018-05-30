package cn.ktc.android.oobe;

import com.mstar.android.MIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.internet.LocalFragmentManager;
import cn.ktc.android.oobe.language.LanguagePage;
import android.os.SystemProperties;

public class FinishPage extends BasePage {
	private static final String LOG_TAG = FinishPage.class.getSimpleName();
	private Button btNext,btPre;
	private String IDENTIFY="OobeTimezoneC";
	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_finish, null);
		btNext = (Button) layout.findViewById(R.id.btn_finish_done);
		btNext.requestFocus();
		btNext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//for benq_en,  sync time, zjd20170609, start
				Boolean isAutoDateTime = false;
				try {
					isAutoDateTime = Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME) > 0;
		        } catch (SettingNotFoundException e) {
		        	e.printStackTrace();
		        }
		        Intent timeChanged = new Intent(MIntent.ACTION_TV_AUTO_TIME_SYNC);
		        timeChanged.putExtra(MIntent.EXTRA_KEY_TV_AUTO_TIME, isAutoDateTime);
		        mContext.sendBroadcast(timeChanged);
		        //for benq_en,  sync time, zjd20170609, end
				
				// remove this activity from the package manager.
		        finishSetupWizard();
		        // TODO Auto-generated method stub
				mActivityListListener.onFinishActivity();
			}
		});
		
		btPre =(Button)layout.findViewById(R.id.btn_finish_back);
		btPre.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivityListListener.onPrevActivity();
			}
		});
		return layout;
	}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
	public void active() {
		// TODO Auto-generated method stub
		super.active();
	}

    private void finishSetupWizard() {
    	SystemProperties.set("persist.sys.startsetup", "false");
    	Settings.System.putInt(mContext.getContentResolver(), "home_hot_key_disable", 0);
    	String isOobedone = SystemProperties.get("persist.sys.oobedone");
		//Log.i("yzh", "OOBE_oobedone:  "+isOobedone);
        //force to start launcher
    	SharedPreferences sharedPref1 = FinishPage.this.getActivity().getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putBoolean("Language_name", false);
        editor.commit();
		 // Add a persistent setting to allow other apps to know the device has been provisioned.
         Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 1);
         Settings.Secure.putInt(mContext.getContentResolver(), Settings.Secure.USER_SETUP_COMPLETE, 1);

        // remove this activity from the package manager.
  ////      PackageManager pm = mContext.getPackageManager();
  ////      ComponentName name = new ComponentName(this.getActivity(),StartupActivity.class);
 ////       pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        ComponentName componentName = new ComponentName(
				 "com.mstar.tv.tvplayer.ui","com.mstar.tv.tvplayer.ui.RootActivity");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
               | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mContext.startActivity(intent);
	    LocalFragmentManager.destroyAllInstance();
	    android.os.Process.killProcess(android.os.Process.myPid());
    }


    
}
