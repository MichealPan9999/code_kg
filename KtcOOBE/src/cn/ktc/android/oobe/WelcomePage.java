package cn.ktc.android.oobe;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import cn.ktc.android.oobe.R;
import android.app.IActivityManager;
import android.app.ActivityManagerNative;

public class WelcomePage extends BasePage {
	private static final String LOG_TAG = WelcomePage.class.getSimpleName();
	private Button btNext;
	private String IDENTIFY="OobeTimezoneC";
	private Boolean system_language;

	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_welcome, null);
		SharedPreferences sharedPref = WelcomePage.this.getActivity().getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		system_language = sharedPref.getBoolean("Language_name", false);	 
		if (system_language){			 
			Locale locale = getResources().getConfiguration().locale;		 
			try {			 		 
				IActivityManager am = ActivityManagerNative.getDefault();
				Configuration config = am.getConfiguration();
				config.locale = locale;
				config.userSetLocale = true;
				am.updateConfiguration(config);		
			} catch (RemoteException e) {
				// Intentionally left blank
			}
		}else{
			try {			 
				String language_def = getProp("/system/build.prop", "persist.sys.language");
				String country_def = getProp("/system/build.prop", "persist.sys.country");
				Locale l = new Locale(language_def, country_def);			 
				IActivityManager am = ActivityManagerNative.getDefault();
				Configuration config = am.getConfiguration();
				config.locale = l;
				config.userSetLocale = true;
				am.updateConfiguration(config);		
			} catch (RemoteException e) {
				// Intentionally left blank
		   }
		}
		btNext = (Button) layout.findViewById(R.id.btn_wel_next);
		btNext.requestFocus();
		btNext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivityListListener.onNextActivity();
			}
		});
		return layout;
	}
		 
		private static String getProp(String file, String key)
	    {
	            String value = "";
	            Properties props = new Properties();
	            InputStream in;
	            try {
	                    in = new BufferedInputStream(new FileInputStream(file));
	                    props.load(in);
	                    value = props.getProperty(key);
	                    in.close();
	            } catch (Exception e) {
	                    e.printStackTrace();
	            }
	            if(value != null){
	            	String[] array = value.split(";");
	            	if (array[0].length() > 0){
	            		value = array[0];
	            	}
	            	value = value.replace("\"", "");
	            	value = value.trim();
	            	 return value;
	            } else {
	            	 return "";
	            }
	                   
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

}
