package com.ktc.launcher.mode;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppInfo {
    public String appName="";
    public String packageName="";
    public String versionName="";
    public int versionCode=0;
    public Drawable appIcon=null;
    public void print()
    {
        Log.v("app","Name:"+appName+" Package:"+packageName);
        Log.v("app","Name:"+appName+" versionName:"+versionName);
        Log.v("app","Name:"+appName+" versionCode:"+versionCode);
    }
	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packageName=" + packageName + ", versionName=" + versionName
				+ ", versionCode=" + versionCode + ", appIcon=" + appIcon + "]";
	}
    
}
