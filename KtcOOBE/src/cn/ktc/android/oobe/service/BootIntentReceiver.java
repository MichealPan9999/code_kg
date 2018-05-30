package cn.ktc.android.oobe.service;


import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import cn.ktc.android.oobe.StartupActivity;

public class BootIntentReceiver extends BroadcastReceiver{
    private final static String TAG = "BootIntentReceiver";
    private SharedPreferences mfirstFlag;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action = " + action);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "oobe receive ACTION_BOOT_COMPLETED.");
            
            Intent oobeIntent = new Intent(context, StartupActivity.class);
            oobeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if(isFirst(context)){
				//context.startActivity(oobeIntent);
			}
        }
    }
    
	
	public boolean isFirst(Context context) {
		mfirstFlag = context.getSharedPreferences("first_init", Context.MODE_PRIVATE);//zjd20150731
		int flag = mfirstFlag.getInt("flag", 0);
		Log.d(TAG,"Is Fist:" + flag);
		if (flag == 0) {
			Editor e = mfirstFlag.edit();
			e.putInt("flag", 1);
			e.apply();
			
			Log.d(TAG,"Is Fist:" + mfirstFlag.getInt("flag", 0));
			return true;
		} else {
			return false;
		}
	}
}


