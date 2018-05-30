package com.ktc.launcher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.mstar.android.pppoe.PppoeManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.net.EthernetManager;

public class NetTool {
	
	private EthernetManager mEthernetManager;

    private WifiManager mWifiManager;
	
    private Context mContext;
    
	public NetTool(Context mContext) {
		this.mContext=mContext;
	}

    public WifiManager getWifiManager() {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        }
        return mWifiManager;
    }
    public boolean isWifiConnected() {
        WifiManager wifiManager = getWifiManager();
        // wifi is disabled
        if (!wifiManager.isWifiEnabled()) {
            return false;
        }

        // wifi have not connected
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info == null || info.getSSID() == null
               ) {
            return false;
        }
       // || info.getNetworkId() == WifiConfiguration.INVALID_NETWORK_ID
        return true;
    }
    public boolean isNetworkConnected(Context context) { 
    	if (context != null) { 
    	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
    	.getSystemService(Context.CONNECTIVITY_SERVICE); 
    	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
    	if (mNetworkInfo != null) { 
    	return mNetworkInfo.isAvailable(); 
    	} 
    	} 
    	return false; 
    	}

    private boolean getEhernetNetworkType(Context context) {
    	if (context != null) { 
        	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
        	.getSystemService(Context.CONNECTIVITY_SERVICE); 
        	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
        	if (mNetworkInfo != null) { 
        		 if (mNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)
        			 return true;
        	}
    	}
    	return false;
    }

    private boolean getWifiNetworkType(Context context) {
    	if (context != null) { 
        	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
        	.getSystemService(Context.CONNECTIVITY_SERVICE); 
        	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
        	if (mNetworkInfo != null) { 
        		 if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
        			 return true;
        	}
    	}
    	return false;
    }
	public String getNetType(){
		if (isEthernetEnabled() && isNetInterfaceAvailable("eth0") && getEhernetNetworkType(mContext)) {
			return "Ethernet";
		}else if(isWifiConnected() && getWifiNetworkType(mContext)){
			return "Wifi";
		}else if (isPppoeConnected()){
			return "Pppoe";
		}else {
			return null;
		}
		
	}
	
	private boolean isPppoeConnected(){
		if(PppoeManager.getInstance(mContext).getPppoeStatus().equals(PppoeManager.PPPOE_STATE_CONNECT)){
			return true ;
		}
		return false ;
	}
	
	public boolean isEthernetEnabled() {
        EthernetManager ethernet = getEthernetManager();
        if (ethernet.isEnabled()) {
            return true;
        }

        return false;
    }
	public EthernetManager getEthernetManager() {
        if (mEthernetManager == null) {
            mEthernetManager = (EthernetManager) mContext.getSystemService(Context.ETHERNET_SERVICE);
        }
        return mEthernetManager;
    }
	private boolean isNetInterfaceAvailable(String ifName) {
        String netInterfaceStatusFile = "/sys/class/net/" + ifName + "/carrier";
        return isStatusAvailable(netInterfaceStatusFile);
    }
	private boolean isStatusAvailable(String statusFile) {
        char st = readStatus(statusFile);
        if (st == '1') {
            return true;
        }

        return false;
    }
	private synchronized char readStatus(String filePath) {
        int tempChar = 0;
        File file = new File(filePath);
        if (file.exists()) {
            Reader reader = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file));
                tempChar = reader.read();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return (char) tempChar;
    }
}
