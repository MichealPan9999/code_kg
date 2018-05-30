package cn.ktc.android.oobe.language;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.TimeZone;

import com.mstar.android.tv.TvCountry;
import com.android.internal.app.LocalePicker;
import com.mstar.android.tv.TvChannelManager;
import android.app.ActivityManagerNative;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.utils.DialogOnKeyListener;
//import cn.ktc.android.oobe.internet.InternetActivity;

public class LanguagePage extends BasePage {
	private static final String LOG_TAG = LanguagePage.class.getSimpleName();
	private Button mLanguageBtn,mBackBtn,mNextBtn,mCountryBtn;
	public int focus_index=0;
	public int conutry_index = 0;	 
	private String[] fillName;
	private String IDENTIFY="OobeTimezoneC";
	private int[] countryName = new int[]{TvCountry.BRAZIL,TvCountry.ARGENTINA,
			TvCountry.PERU,TvCountry.ECUADOR, TvCountry.CHILE};
	private String[] countrylist;
	private int TimeZone_index;
	private TvChannelManager mTvChannelManager = null;
	private ArrayAdapter<LocalePicker.LocaleInfo> mLocales; 
	private static final String TAG = "LanguagePage";
	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_language, null);
		mLanguageBtn = (Button)layout.findViewById(R.id.btn_language_set);
		mLanguageBtn.requestFocus();
		mLanguageBtn.setOnClickListener(myShowLanguageDialog);
		mCountryBtn = (Button)layout.findViewById(R.id.btn_country_set);
		mCountryBtn.requestFocus();
		mCountryBtn.setOnClickListener(myShowCountryDialog);
		mTvChannelManager = TvChannelManager.getInstance();
		String language = Locale.getDefault().toString();		 	 
		countrylist = getResources().getStringArray(R.array.country_choose_dialog_list_isdb);		 
		String fillName []= init();	
		String language_list [] = getLanguage();
		for (String count : language_list) {			 
			if (language.equals(count)) {
				break;
			}
			++focus_index;
		}
		if(focus_index == language_list.length){
			focus_index = 0;
		}		 		 
		mLanguageBtn.setText(fillName[focus_index]); 
		String startsetup = SystemProperties.get("persist.sys.startsetup");
	    if(startsetup.equals("true")){
	    	mCountryBtn.setText(countrylist[0]);
	    	mTvChannelManager.setSystemCountryId(countryName[0]);
	    }else{    	
	    	SharedPreferences sharedPref = LanguagePage.this.getActivity().getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
			TimeZone_index = sharedPref.getInt("conutry_index", 0);
			mCountryBtn.setText(countrylist[TimeZone_index]);			
	    }				
		mBackBtn = (Button)layout.findViewById(R.id.btn_language_back);
		mNextBtn = (Button)layout.findViewById(R.id.btn_language_next);		
		mBackBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				 
				/*	mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
					if(mTvSystem == TvCommonManager.TV_SYSTEM_ISDB){
						 
					}else if(mTvSystem == TvCommonManager.TV_SYSTEM_ATSC){
						
					}else if(mTvSystem == TvCommonManager.TV_SYSTEM_DVB){
						
					}else if(mTvSystem == TvCommonManager.TV_SYSTEM_DTMB){
						
					} 
				}*/
				mActivityListListener.onPrevActivity();
			}
		});
		
		mNextBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				mActivityListListener.onNextActivity();
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
    
    private void setSystemTimeZone(int country) {
        String timeZoneString = TvCountry.countryToTimeZone(country);
        Time time = new Time();
        time.setToNow();
        if (timeZoneString.compareTo(time.timezone) == 0) {
            return;
        }
        AlarmManager alarm = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.setTimeZone(timeZoneString);
    }
    
	Button.OnClickListener myShowCountryDialog = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.test)
					.setTitle(R.string.country_set_title)
					.setSingleChoiceItems(R.array.country_choose_dialog_list_isdb,conutry_index, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int item) {																 
								int country = countryName[item];							 							 
								try {
									mTvChannelManager.setSystemCountryId(country);
									setSystemTimeZone(country);
									IActivityManager am = ActivityManagerNative.getDefault();
									Configuration config = am.getConfiguration();
									config.userSetLocale = true;
									am.updateConfiguration(config);
									BackupManager.dataChanged("com.android.providers.settings");
									dialog.dismiss();
									conutry_index = item;
								} catch (RemoteException e) {
									// Intentionally left blank
								}
							}
						}).create();				 
				ListView listview = dialog.getListView();
				if(listview != null){				 
					 listview.setSelector(new ColorDrawable(getResources().getColor(R.color.blue_color)));
				}
				dialog.setOnKeyListener(DialogOnKeyListener.mOnKeyListener);
				dialog.show();
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss (DialogInterface dialog) {
						mCountryBtn.setText(countrylist[conutry_index]);					 				 					 
						SharedPreferences sharedPref = LanguagePage.this.getActivity().getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
					        SharedPreferences.Editor editor = sharedPref.edit();
					        editor.putInt("conutry_index", conutry_index);
					        editor.commit();
					}				
				});
				ListView listView = dialog.getListView();
				if(listView != null){
					listView.smoothScrollToPosition(conutry_index);
				}
			}	
	};	 
	Button.OnClickListener myShowLanguageDialog = new Button.OnClickListener() {
		public void onClick(View arg0) {
			final String [] dialog_Language  = init();			 
			AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.test)
				.setTitle(R.string.language_set_title)
				.setSingleChoiceItems(dialog_Language,focus_index, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int item) {							 
							try {								 
								IActivityManager am = ActivityManagerNative.getDefault();
								Configuration config = am.getConfiguration();							 						 
								setLanguagePreference(item);
								SharedPreferences sharedPref1 = LanguagePage.this.getActivity().getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
						        SharedPreferences.Editor editor = sharedPref1.edit();
						        editor.putBoolean("Language_name", true);
						        editor.commit();
								dialog.dismiss();								
								focus_index = item;
							} catch (RemoteException e) {
								// Intentionally left blank
							}
						}
					}).create();
			ListView listview = dialog.getListView();
			if(listview != null){
				 listview.setSelector(new ColorDrawable(getResources().getColor(R.color.blue_color)));
			}
			dialog.setOnKeyListener(DialogOnKeyListener.mOnKeyListener);
			dialog.show();
			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss (DialogInterface dialog) {				 
					mLanguageBtn.setText(dialog_Language[focus_index]);					
				}
			});
			ListView listView = dialog.getListView();
			if(listView != null){			 
				listView.smoothScrollToPosition(focus_index);
			}
		}

	};
	 private String[] getLanguage(){
		 	mLocales = LocalePicker.constructAdapter(this.getActivity());
	        mLocales.sort(new LocaleComparator());
	        final String[] localeNamese = new String[mLocales.getCount()];
	        String[]  language_system_list = new String[mLocales.getCount()];
	        for(int i = 0 ; i < localeNamese.length; i++){
	        	 Locale target = mLocales.getItem(i).getLocale();	            
	        	 language_system_list [i] = target.toString(); 
	        }
	        return language_system_list; 
	 }
	 private String[] init() {
	      
	        final String[] specialLocaleCodes = getResources().getStringArray(
	                com.android.internal.R.array.special_locale_codes);
	        final String[] specialLocaleNames = getResources().getStringArray(
	                com.android.internal.R.array.special_locale_names);
	        mLocales = LocalePicker.constructAdapter(this.getActivity());
	        mLocales.sort(new LocaleComparator());
	        final String[] localeNames = new String[mLocales.getCount()];
	        String[]  System_list_Language = new String[mLocales.getCount()];
	        Locale currentLocale;
	        try {
	            currentLocale = ActivityManagerNative.getDefault().getConfiguration().locale;
	        } catch (RemoteException e) {
	            currentLocale = null;
	        }
	        for (int i = 0, j=0; i < localeNames.length; i++,j++) {
	            Locale target = mLocales.getItem(i).getLocale();
	            localeNames[i] = getDisplayName(target, specialLocaleCodes, specialLocaleNames);
	            // if this locale's label has a country, use the shortened version
	            // instead
	            if (mLocales.getItem(i).getLabel().contains("(")) {
	                String country = target.getCountry();
	                if (!TextUtils.isEmpty(country)) {
	                    localeNames[i] = localeNames[i] + " (" + target.getCountry() + ")";	                    
	                }
	            }
	            // For some reason locales are not always first letter cased, for example for
	            // in the Spanish locale.
	            if (localeNames[i].length() > 0) {
	                localeNames[i] = localeNames[i].substring(0, 1).toUpperCase(currentLocale) +
	                        localeNames[i].substring(1);
	            }
	            System_list_Language[j] = getSubString(localeNames[i]);	           
	        }
	        return System_list_Language; 
	    }
	 
	 private static String getDisplayName(
	            Locale l, String[] specialLocaleCodes, String[] specialLocaleNames) {
	        String code = l.toString();
	        for (int i = 0; i < specialLocaleCodes.length; i++) {
	            if (specialLocaleCodes[i].equals(code)) {
	                return specialLocaleNames[i];
	            }
	        }

	        return l.getDisplayName(l);
	    }
	 
	 private void setLanguagePreference(int offset) {
	        LocalePicker.updateLocale(mLocales.getItem(offset).getLocale());
	        //pzq set four countries language subtitleencoding 2016-11-25 start
	        SystemProperties.set("sys.ms.subtitle.language", "windows-1252");
	        setEncodingFromPersistProp("CP1252");
	        //pzq set four countries language subtitleencoding 2016-11-25 end
	    }
	 
		private void setEncodingFromPersistProp(String objString)
	  	{
	  		 if (objString != null) {
	  			SystemProperties.set("persist.encode.subtitle",objString);
			}	
	  	}
		
		private String getSubString(String sourceStr){
	    	 if (sourceStr.contains("(")&&sourceStr.contains(")")) {
	             if (!TextUtils.isEmpty(sourceStr)) {
	             	String tmp = sourceStr.trim();
	             	if(tmp.startsWith("(")){
	             		return tmp.substring(tmp.indexOf(")")+1,tmp.length());
	             	}else if(tmp.endsWith(")")){
	             		return tmp.substring(0,tmp.indexOf("("));
	             	}else{
	             		return tmp ;
	             	}
	             }
	         }
	    	 return sourceStr;
	    }
		
	 private static class LocaleComparator implements Comparator<LocalePicker.LocaleInfo> {
	        @Override
	        public int compare(LocalePicker.LocaleInfo l, LocalePicker.LocaleInfo r) {
	            Locale lhs = l.getLocale();
	            Locale rhs = r.getLocale();
	            String lhsCountry = "";
	            String rhsCountry = "";
	            try {
	                lhsCountry = lhs.getISO3Country();
	            } catch (MissingResourceException e) {
	                Log.e(TAG, "LocaleComparator cuaught exception, country set to empty.");
	            }

	            try {
	                rhsCountry = rhs.getISO3Country();
	            } catch (MissingResourceException e) {
	                Log.e(TAG, "LocaleComparator cuaught exception, country set to empty.");
	            }
	            String lhsLang = "";
	            String rhsLang = "";
	            try {
	                lhsLang = lhs.getISO3Language();
	            } catch (MissingResourceException e) {
	                Log.e(TAG, "LocaleComparator cuaught exception, language set to empty.");
	            }
	            try {
	                rhsLang = rhs.getISO3Language();
	            } catch (MissingResourceException e) {
	                Log.e(TAG, "LocaleComparator cuaught exception, language set to empty.");
	            }
	            // if they're the same locale, return 0
	            if (lhsCountry.equals(rhsCountry) && lhsLang.equals(rhsLang)) {
	                return 0;
	            }
	            // prioritize US over other countries
	            if ("USA".equals(lhsCountry)) {
	                // if right hand side is not also USA, left hand side is first
	                if (!"USA".equals(rhsCountry)) {
	                    return -1;
	                } else {
	                    // if one of the languages is english we want to prioritize
	                    // it, otherwise we don't care, just alphabetize
	                    if ("ENG".equals(lhsLang) && "ENG".equals(rhsLang)) {
	                        return 0;
	                    } else {
	                        return "ENG".equals(lhsLang) ? -1 : 1;
	                    }
	                }
	            } else if ("USA".equals(rhsCountry)) {
	                // right-hand side is the US and the other isn't, return greater than 1
	                return 1;
	            } else {
	                // neither side is the US, sort based on display language name
	                // then country name
	                int langEquiv = lhs.getDisplayLanguage(lhs)
	                        .compareToIgnoreCase(rhs.getDisplayLanguage(rhs));
	                if (langEquiv == 0) {
	                    return lhs.getDisplayCountry(lhs)
	                            .compareToIgnoreCase(rhs.getDisplayCountry(rhs));
	                } else {
	                    return langEquiv;
	                }
	            }
	        }
	    }
}
