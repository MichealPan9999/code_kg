package cn.ktc.android.oobe.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import com.mstar.android.MIntent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import cn.ktc.android.oobe.BasePage;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.utils.DialogOnKeyListener;
import cn.ktc.android.oobe.widget.DatePickerDialog;
import cn.ktc.android.oobe.widget.TimePickerDialog;

public class TimeSetPage extends BasePage {
	private static final String LOG_TAG = TimeSetPage.class.getSimpleName();
	public Button mNext;
	public Button mBack;
	public CheckBox mHFormatCheckBox;
	public CheckBox mAutoTimeCheckBox;
	public RelativeLayout mSetDateBtn;
	public RelativeLayout mTimeZoneBtn;
	public RelativeLayout mSetTimeBtn;
	public RelativeLayout mSetDateFormatBtn;
	private View layout; 
	
	public TextView mSetDate, mSetDate_summary;	
	public TextView mTimeZone, mTimeZone_summary;
	public TextView mSetTime, mSetTime_summary;
	// public TextView mHourFormat_summary;
	public TextView mDateFormat_summary;
	public int date_form_item = 0;
	private Calendar mDummyDate;
	//private static final int DIALOG_DATEPICKER = 0;
	//private static final int DIALOG_TIMEPICKER = 1;

	private static final String HOURS_12 = "12";
	private static final String HOURS_24 = "24";
	String[] formattedDates = null;

	private boolean isAutoDateTime;

	private static final String DATE_TIME_SETTING_PACKAGE = "com.android.settings";
	// private static final String DATE_TIME_SETTING_CLASS
	// ="com.android.settings.ZoneList";
	private static final String DATE_TIME_SETTING_CLASS = "com.android.settings.DateTimeSettings";

	@Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.page_timeset, null);
		
		
		
		findViews();
		initUI();
		UpdateSummaryDisplay();
		
		mHFormatCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
				set24Hour(isChecked);
				UpdateSummaryDisplay();
				timeUpdated();
			}
		});
		
		mNext = (Button)layout.findViewById(R.id.btn_time_back);
		mBack = (Button)layout.findViewById(R.id.btn_time_next);
		
		mNext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivityListListener.onPrevActivity();
			}
		});
		
		mBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivityListListener.onNextActivity();
			}
		});
		
		return layout;
	}
	
	private void findViews() {
    	mAutoTimeCheckBox = (CheckBox)layout.findViewById(R.id.cb_auto_time);
    	mAutoTimeCheckBox.setOnClickListener(mAutoTimeListener);
    	mAutoTimeCheckBox.requestFocus();
    	
    	mSetDateBtn = (RelativeLayout) layout.findViewById(R.id.set_date_btn);
		mSetDateBtn.setOnClickListener(mSetDateBtnListener);
		
		mTimeZoneBtn=(RelativeLayout) layout.findViewById(R.id.set_timezone_btn);
		mTimeZoneBtn.setOnClickListener(mTimeZoneBtnListener);
		mTimeZone = (TextView)layout.findViewById(R.id.tv_datetime_settimezone);
		mTimeZone_summary = (TextView)layout.findViewById(R.id.tv_datetime_settimezone_summary);
		
		mSetTimeBtn = (RelativeLayout) layout.findViewById(R.id.set_time_btn);
		mSetTimeBtn.setOnClickListener(mSetTimeBtnListener);

		mSetDateFormatBtn = (RelativeLayout) layout.findViewById(R.id.date_format_btn);
		mSetDateFormatBtn.setOnClickListener(mSetDateFormatBtnListener);

		mNext = (Button) layout.findViewById(R.id.btn_time_next);
		mBack = (Button) layout.findViewById(R.id.btn_time_back);
		mHFormatCheckBox = (CheckBox) layout.findViewById(R.id.cb_datetime_hourformat_checkbox);

		mSetDate = (TextView) layout.findViewById(R.id.tv_datetime_setdate);
		mSetDate_summary = (TextView) layout.findViewById(R.id.tv_datetime_setdate_summary);
		mSetTime = (TextView) layout.findViewById(R.id.tv_datetime_settime);
		mSetTime_summary = (TextView) layout.findViewById(R.id.tv_datetime_settime_summary);
		// mHourFormat_summary = (TextView)findViewById(R.id.tv_datetime_hourformat_summary);
		mDateFormat_summary = (TextView) layout.findViewById(R.id.tv_datetime_dateformat_summary);

		// Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION,0);		 	       
        String startsetup = SystemProperties.get("persist.sys.startsetup");
        if(startsetup.equals("true")){
        	isAutoDateTime = true;
        	Settings.System.putString(mContext.getContentResolver(),Settings.System.TIME_12_24,"24");
        	//mHFormatCheckBox.setChecked(true);
        }else{
        	isAutoDateTime = getAutoState();
        }
        if (isAutoDateTime) {      	
        	mHFormatCheckBox.setChecked(is24Hour());
        	mAutoTimeCheckBox.setChecked(true);
        	mSetDateBtn.setFocusable(false);
        	mSetTimeBtn.setFocusable(false);
        	//mTimeZoneBtn.setFocusable(false);
        	
        	mSetDate.setTextColor(Color.GRAY);
        	mSetDate_summary.setTextColor(Color.GRAY);
        	//mTimeZone.setTextColor(Color.GRAY);
        	//mTimeZone_summary.setTextColor(Color.GRAY);
        	mSetTime.setTextColor(Color.GRAY);
        	mSetTime_summary.setTextColor(Color.GRAY);
        } else {      	 
        	mAutoTimeCheckBox.setChecked(false);
        	mSetDateBtn.setFocusable(true);
        	mSetTimeBtn.setFocusable(true);
        	mTimeZoneBtn.setFocusable(true); 
        	
        	mSetDate.setTextColor(Color.WHITE);
        	mSetDate_summary.setTextColor(Color.WHITE);
        	mTimeZone.setTextColor(Color.WHITE);
        	mTimeZone_summary.setTextColor(Color.WHITE);
        	mSetTime.setTextColor(Color.WHITE);
        	mSetTime_summary.setTextColor(Color.WHITE);
        }
        
        //benq,zjd20150321
        Intent timeChanged = new Intent(MIntent.ACTION_TV_AUTO_TIME_SYNC);
        timeChanged.putExtra(MIntent.EXTRA_KEY_TV_AUTO_TIME, isAutoDateTime);
        mContext.sendBroadcast(timeChanged);
        //end,zjd20150321
    }
	
	private void initUI() {
		mDummyDate = Calendar.getInstance();
		mDummyDate.set(Calendar.YEAR, 2015);
		mDummyDate.set(Calendar.MONTH, 0);
		mDummyDate.set(Calendar.DAY_OF_MONTH, 15);
		mDummyDate.set(Calendar.HOUR_OF_DAY, 0);
		mDummyDate.set(Calendar.MINUTE, 0);
		mDummyDate.set(Calendar.SECOND, 0);
		long when = mDummyDate.getTimeInMillis();
		/*if (when / 1000 < Integer.MAX_VALUE) {
			SystemClock.setCurrentTimeMillis(when);
		}*/
		//mDummyDate.set(mDummyDate.get(Calendar.YEAR), 11, 31, 13, 0, 0);

		String[] dateFormats = getResources().getStringArray(R.array.date_format_values);
		String patten = Settings.System.getString(mContext.getContentResolver(), Settings.System.DATE_FORMAT);
		if(patten == null){
			Settings.System.putString(mContext.getContentResolver(), Settings.System.DATE_FORMAT, dateFormats[0]);
		}
		formattedDates = new String[dateFormats.length];
		String currentFormat = getDateFormat();
		// Initialize if DATE_FORMAT is not set in the system settings
		// This can happen after a factory reset (or data wipe)
		if (currentFormat == null) {
			currentFormat = "";
		}
		for (int i = 0; i < formattedDates.length; i++) {

			/*  鏂规硶getDateFormatForSetting(context,string)宸茬粡鍦ˋndroid6.0(API 23)涓婅绉婚櫎
			String formatted = DateFormat.getDateFormatForSetting(mContext,
					dateFormats[i]).format(mDummyDate.getTime());
			*/

			//淇敼鏍煎紡鍖栨椂闂存柟寮�  lzj 2017/08/08 start
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateFormats[i], getResources().getConfiguration().locale);
			String formatted = simpleDateFormat.format(mDummyDate.getTime());
			// end


			//if (dateFormats[i].length() == 0) {
				//formattedDates[i] = getResources().getString(
						//R.string.normal_date_format, formatted);
			//} else {
				formattedDates[i] = formatted;
			//}
		}
	}
	
	private boolean getAutoState() {
        try {
            return Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME) > 0;
        } catch (SettingNotFoundException snfe) {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
	    mHFormatCheckBox.setChecked(is24Hour()); //added by lzj
    }

    @Override
	public void active() {
		// TODO Auto-generated method stub
	    
		super.active();
	}
    
	private String getDateFormat() {
		return Settings.System.getString(mContext.getContentResolver(),Settings.System.DATE_FORMAT);
	}

	private void set24Hour(boolean is24Hour) {
		Settings.System.putString(mContext.getContentResolver(),Settings.System.TIME_12_24, is24Hour ? HOURS_24 : HOURS_12);
	}
	
	private Boolean is24Hour(){
		return Objects.equals(HOURS_24, Settings.System.getString(mContext.getContentResolver(), Settings.System.TIME_12_24));
	}

	private void timeUpdated() {
		Intent timeChanged = new Intent(Intent.ACTION_TIME_CHANGED);
		mContext.sendBroadcast(timeChanged);
	}
	
    private class MyDatePickerDialog extends DatePickerDialog {
		public MyDatePickerDialog(Context context, OnDateSetListener callBack,
                int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
			this.setOnKeyListener(DialogOnKeyListener.mOnKeyListener);
        }
		
		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
            //super.onStop();
		}

    }
    
    
	private View.OnClickListener mSetDateBtnListener = new View.OnClickListener() {
		public void onClick(View v) {
			//((Activity) mContext).showDialog(DIALOG_DATEPICKER);
			Dialog d;
			final Calendar calendar = Calendar.getInstance();
			DatePickerDialog.OnDateSetListener dateListener = new MyDatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker datePicker, int year,
						int month, int dayofMonth) {
					calendar.set(Calendar.YEAR, year);
					calendar.set(Calendar.MONTH, month);
					calendar.set(Calendar.DAY_OF_MONTH, dayofMonth);
					long when = calendar.getTimeInMillis();

					if (when / 1000 < Integer.MAX_VALUE) {
						SystemClock.setCurrentTimeMillis(when);
					}
					UpdateSummaryDisplay();
				}

			};

			d = new MyDatePickerDialog(mContext, dateListener,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
		    DatePicker datePicker = ((MyDatePickerDialog) d).getDatePicker();
		    datePicker.setCalendarViewShown(false);
		    //datePicker.setBackground(getResources().getDrawable(R.drawable.bg_bottom_new));
		    ((MyDatePickerDialog) d).setCustomTitle(initCustomTitle(R.string.set_date,false));
			d.show();
		}
	};
    
	private View.OnClickListener mAutoTimeListener = new View.OnClickListener() {
		public void onClick(View v) {
			isAutoDateTime = !isAutoDateTime;
            mAutoTimeCheckBox.setChecked(isAutoDateTime);
            Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME, isAutoDateTime ? 1 : 0);
            if (isAutoDateTime) {            
            	mAutoTimeCheckBox.setChecked(true);
            	mSetDateBtn.setFocusable(false);            	
            	mSetTimeBtn.setFocusable(false);
            	//mTimeZoneBtn.setFocusable(false);
            	
            	mSetDate.setTextColor(Color.GRAY);
            	mSetDate_summary.setTextColor(Color.GRAY);
            	//mTimeZone.setTextColor(Color.GRAY);
            	//mTimeZone_summary.setTextColor(Color.GRAY);
            	mSetTime.setTextColor(Color.GRAY);
            	mSetTime_summary.setTextColor(Color.GRAY);
            } else {            
            	mAutoTimeCheckBox.setChecked(false);
            	mSetDateBtn.setFocusable(true);
            	mSetDate_summary.setTextColor(Color.WHITE);
            	mSetTimeBtn.setFocusable(true);
            	mTimeZoneBtn.setFocusable(true);
            	
            	mSetDate.setTextColor(Color.WHITE);
            	mSetDate_summary.setTextColor(Color.WHITE);
            	mTimeZone.setTextColor(Color.WHITE);
            	mTimeZone_summary.setTextColor(Color.WHITE);
            	mSetTime.setTextColor(Color.WHITE);
            	mSetTime_summary.setTextColor(Color.WHITE);
            }
            Intent timeChanged = new Intent(MIntent.ACTION_TV_AUTO_TIME_SYNC);
            timeChanged.putExtra(MIntent.EXTRA_KEY_TV_AUTO_TIME, isAutoDateTime);//!isAutoDateTime
            mContext.sendBroadcast(timeChanged);
            
		}
	};
    
	private View.OnClickListener mTimeZoneBtnListener = new View.OnClickListener() {
		public void onClick(View v) {
			ZoneListSelectDialog zoneListSelectDialog = new ZoneListSelectDialog(mContext, updateZoneTimeHandler);
			zoneListSelectDialog.setCustomTitle(initCustomTitle(R.string.set_timezone,true));
            zoneListSelectDialog.show();
		}
	};
	
	private Handler updateZoneTimeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
            	UpdateSummaryDisplay();
            }
        }
    };
    
    private class MyTimePickerDialog extends TimePickerDialog {
		public MyTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute,
				boolean is24HourView) {
			super(context, callBack, hourOfDay, minute, is24HourView);
			this.setOnKeyListener(DialogOnKeyListener.mOnKeyListener);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
            //super.onStop();
		}

    }
    
	private View.OnClickListener mSetTimeBtnListener = new View.OnClickListener() {
		public void onClick(View v) {
			// The 24-hour mode may have changed, so recreate the dialog
			//Log.e(TAG, "mSetTimeBtnListener");
			//((Activity) mContext).showDialog(DIALOG_TIMEPICKER);
			AlertDialog d;
			TimePickerDialog.OnTimeSetListener timeListener = new MyTimePickerDialog.OnTimeSetListener() {
				public void onTimeSet(TimePicker timerPicker, int hourOfDay,
						int minute) {
					Calendar c = Calendar.getInstance();
					
					c.set(Calendar.HOUR_OF_DAY, hourOfDay);
					c.set(Calendar.MINUTE, minute);
					long when = c.getTimeInMillis();

					if (when / 1000 < Integer.MAX_VALUE) {
						SystemClock.setCurrentTimeMillis(when);
					}
					UpdateSummaryDisplay();
				}
				
			};
			final Calendar calendar = Calendar.getInstance();
			d = new MyTimePickerDialog(mContext, timeListener,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE),
					DateFormat.is24HourFormat(mContext));
			d.setCustomTitle(initCustomTitle(R.string.set_time,false));
			//d.setTitle(getString(R.string.set_time));
			//setPickerTextSize(d.findViewById(android.R.id.timePicker), 20);
			d.show();
		}
	};

	private View.OnClickListener mSetDateFormatBtnListener = new View.OnClickListener() {
		public void onClick(View v) {
			//Log.e(TAG, "mSetDateFormatBtnListener");
			//showDateFormat();
			AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.test)
					.setTitle(R.string.date_format_title)
					.setSingleChoiceItems(formattedDates, date_form_item,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int which) {
									// String format = preferences.getString(key,
									// getResources().getString(R.string.default_date_format));
									
									String[] date_format_array = getResources()
											.getStringArray(R.array.date_format_values);									
									String format = date_format_array[which];
									date_form_item = which;
									Settings.System.putString(mContext.getContentResolver(),
											Settings.System.DATE_FORMAT, format);								
									UpdateSummaryDisplay();
									dialog.dismiss();
									// finish();
								}
							}).create();
					ListView listview = dialog.getListView();
					if(listview != null){
					listview.setSelector(new ColorDrawable(getResources().getColor(R.color.blue_color)));
					}
					dialog.setOnKeyListener(DialogOnKeyListener.mOnKeyListener);
					dialog.show();
					ListView listView = dialog.getListView();
					if(listView != null){
						listView.smoothScrollToPosition(date_form_item);
					}
		}
	};
    
    
	void showDateFormat() {
		new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.test))
				.setTitle(R.string.date_format_title)
				.setSingleChoiceItems(formattedDates, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								// String format = preferences.getString(key,
								// getResources().getString(R.string.default_date_format));
								String[] date_format_array = getResources()
										.getStringArray(R.array.date_format_values);
								String format = date_format_array[which];
								Settings.System.putString(mContext.getContentResolver(),
										Settings.System.DATE_FORMAT, format);
								UpdateSummaryDisplay();
								// finish();
							}
						})					 
				.setOnKeyListener(DialogOnKeyListener.mOnKeyListener)
				.show();

	}
    
    private void UpdateSummaryDisplay() {
		String patten = Settings.System.getString(mContext.getContentResolver(), Settings.System.DATE_FORMAT);
		SimpleDateFormat dateFormat = new SimpleDateFormat(patten, getResources().getConfiguration().locale);
		Date now = Calendar.getInstance().getTime();
		Date dummyDate = mDummyDate.getTime();		 
		mSetTime_summary.setText(DateFormat.getTimeFormat(mContext).format(now));
		mTimeZone_summary.setText(getTimeZoneText());
		mSetDate_summary.setText(dateFormat.format(now));
		mDateFormat_summary.setText(dateFormat.format(dummyDate));
	}
    
	/* Helper routines to format timezone */
	private String getTimeZoneText() {
		TimeZone tz = java.util.Calendar.getInstance().getTimeZone();
		boolean daylight = tz.inDaylightTime(new Date());
		StringBuilder sb = new StringBuilder();

		sb.append(formatOffset(tz.getRawOffset() + (daylight ? tz.getDSTSavings() : 0))).append(", ")
		  .append(tz.getDisplayName(daylight, TimeZone.LONG));

		return sb.toString();
	}
    
	private char[] formatOffset(int off) {
		off = off / 1000 / 60;

		char[] buf = new char[9];
		buf[0] = 'G';
		buf[1] = 'M';
		buf[2] = 'T';

		if (off < 0) {
			buf[3] = '-';
			off = -off;
		} else {
			buf[3] = '+';
		}

		int hours = off / 60;
		int minutes = off % 60;

		buf[4] = (char) ('0' + hours / 10);
		buf[5] = (char) ('0' + hours % 10);

		buf[6] = ':';

		buf[7] = (char) ('0' + minutes / 10);
		buf[8] = (char) ('0' + minutes % 10);

		return buf;
	}
	
	private View initCustomTitle(int titleRes, boolean showDivideLine){
		View customTitle = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_custom_title,null);
		TextView textView = (TextView) customTitle.findViewById(R.id.custom_title_text);
		View view = customTitle.findViewById(R.id.custom_title_divide);
		if(showDivideLine){
			view.setVisibility(View.VISIBLE);
		}else{
			view.setVisibility(View.GONE);
		}
		textView.setText(getString(titleRes));
		return customTitle;
	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			UpdateSummaryDisplay();
		}
	};
}
