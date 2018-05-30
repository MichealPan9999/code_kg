package cn.ktc.android.oobe.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import cn.ktc.android.oobe.R;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.ktc.android.oobe.utils.DialogOnKeyListener;



public class ZoneListSelectDialog{
    private static final String TAG = "ZoneListSelectDialog";
    private static final String KEY_ID = "id";
    private static final String KEY_DISPLAYNAME = "name";
    private static final String KEY_GMT = "gmt";
    private static final String KEY_OFFSET = "offset";
    private static final String XMLTAG_TIMEZONE = "timezone";

    private static final int HOURS_1 = 60 * 60000;
    private static final int UPDATE_TIMEZONE = 1;
    private static final int COUNT_SIZE = 5;

    private Context mContext;
    private SimpleAdapter mTimezoneAdapter;
    private Handler mUpdateHandler;
    private ListView mZoneList;
    private TextView mTotalPage;
    private TextView mCurrentPage;
	private AlertDialog mDialog;

    //private int mTotalPageCount;
    //private int mCurrentPageCount;
    private int mDefault = 0;
    private int mTotalCount = 0;

    public ZoneListSelectDialog(Context mContext, Handler mHandler) {
        this.mContext = mContext;
        this.mUpdateHandler = mHandler;
	    create();
    }
	
	
    protected void create() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.time_zone_list,null);
	    findViews(view);
	    registerListeners();
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
			    .setView(view);
	    mDialog = builder.create();
        mDialog.setOnKeyListener(DialogOnKeyListener.mOnKeyListener);
    }
	
    public void show(){
	    mDialog.show();
	    new Handler().postDelayed(new Runnable() {
		    @Override
		    public void run () {
			    if(mZoneList != null){
				    mZoneList.smoothScrollToPosition(mDefault);
			    }
		    }
	    },10);
    }
    
    public void setCustomTitle(View view){
	    mDialog.setCustomTitle(view);
    }

    private void findViews(View view) {
        //mTotalPage = (TextView) findViewById(R.id.page_total_count);
        //mCurrentPage = (TextView) findViewById(R.id.page_current_count);

        mZoneList = (ListView) view.findViewById(R.id.timezone_select);
        mZoneList.setDividerHeight(0);
        //mZoneList.setVerticalScrollBarEnabled(false);

        String[] from = new String[] {KEY_DISPLAYNAME, KEY_GMT};
        int[] to = new int[] {R.id.aboutItem, R.id.content};

        Log.d(TAG, "mDefault  " + mDefault);

        List<HashMap<String, String>> timezoneSortedList = getZones();
        mTotalCount = timezoneSortedList.size();

        //mCurrentPageCount = mDefault / COUNT_SIZE + 1;
        //mCurrentPage.setText("" + mCurrentPageCount);
        //mTotalPageCount = timezoneSortedList.size() / COUNT_SIZE + 1;
        //mTotalPage.setText("/" + mTotalPageCount);

        mTimezoneAdapter = new SimpleAdapter(mContext, timezoneSortedList,
                R.layout.about_item_text, from, to);
        mZoneList.setAdapter(mTimezoneAdapter);
        mZoneList.setSelection(mDefault);
    }

    private void registerListeners() {
        mZoneList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = (Map) mZoneList.getItemAtPosition(position);
                AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                alarm.setTimeZone((String) map.get(KEY_ID));
                mUpdateHandler.sendEmptyMessage(UPDATE_TIMEZONE);
                mDialog.dismiss();
            }
        });

        mZoneList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDefault = position;
                //mCurrentPageCount = mDefault / COUNT_SIZE + 1;
                //mCurrentPage.setText("" + mCurrentPageCount);
                Log.d(TAG, "onItemSelected mDefault, " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
	
    private List<HashMap<String, String>> getZones() {
        List<HashMap<String, String>> myData = new ArrayList<HashMap<String, String>>();
        long date = Calendar.getInstance().getTimeInMillis();
        XmlResourceParser xrp = null;
        try {
            xrp = mContext.getResources().getXml(R.xml.timezones);
            while (xrp.next() != XmlResourceParser.START_TAG)
                continue;
            xrp.next();
            while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                while (xrp.getEventType() != XmlResourceParser.START_TAG) {
                    if (xrp.getEventType() == XmlResourceParser.END_DOCUMENT) {
                        return myData;
                    }
                    xrp.next();
                }
                if (xrp.getName().equals(XMLTAG_TIMEZONE)) {
                    String id = xrp.getAttributeValue(0);
                    String displayName = xrp.nextText();
                    addItem(myData, id, displayName, date);
                }
                while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                    xrp.next();
                }
                xrp.next();
            }
            xrp.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to read timezones.xml file");
        } finally {
            if (xrp != null) {
                xrp.close();
            }
        }

        return myData;
    }

    protected void addItem(List<HashMap<String, String>> myData, String id, String displayName,
            long date) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_ID, id);
        map.put(KEY_DISPLAYNAME, displayName);
        TimeZone tz = TimeZone.getTimeZone(id);
        int offset = tz.getOffset(date);
        int p = Math.abs(offset);
        StringBuilder name = new StringBuilder();
        name.append("GMT");

        if (offset < 0) {
            name.append('-');
        } else {
            name.append('+');
        }

        name.append(p / (HOURS_1));
        name.append(':');

        int min = p / 60000;
        min %= 60;

        if (min < 10) {
            name.append('0');
        }
        name.append(min);

        map.put(KEY_GMT, name.toString());
        map.put(KEY_OFFSET, String.valueOf(offset));

        if (id.equals(TimeZone.getDefault().getID())) {
            mDefault = myData.size();
        }

        myData.add(map);
    }

}
