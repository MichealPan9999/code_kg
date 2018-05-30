package cn.ktc.android.oobe.internet.wireless;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import cn.ktc.android.oobe.R;

public class WifiUtils {
	
	public static final int SECURITY_NONE = 0;
	public static final int SECURITY_WEP = 1;
	public static final int SECURITY_PSK = 2;
	public static final int SECURITY_EAP = 3;

    static String connectSSID = "";
    static String connectBSSID = "";
    
    public static enum PskType {
        UNKNOWN,
        WPA,
        WPA2,
        WPA_WPA2
    }
	
	public static PskType getPskType(ScanResult result) {
        boolean wpa = result.capabilities.contains("WPA-PSK");
        boolean wpa2 = result.capabilities.contains("WPA2-PSK");
        if (wpa2 && wpa) {
            return PskType.WPA_WPA2;
        } else if (wpa2) {
            return PskType.WPA2;
        } else if (wpa) {
            return PskType.WPA;
        } else {
            return PskType.UNKNOWN;
        }
    }
	
	public static int getSecurity(ScanResult result) {
		if (TextUtils.isEmpty(result.capabilities)) {
			return SECURITY_NONE;
		}
        if (result.capabilities.contains("WEP")) {
            return SECURITY_WEP;
        } else if (result.capabilities.contains("PSK")) {
            return SECURITY_PSK;
        } else if (result.capabilities.contains("EAP")) {
            return SECURITY_EAP;
        }
        return SECURITY_NONE;
    }
	
	public static String getSecurityString(Context context, 
			ScanResult result, boolean concise) {
		int security = getSecurity(result);
		PskType pskType = getPskType(result);
		
        switch(security) {
            case SECURITY_EAP:
                return concise ? context.getString(R.string.wifi_security_short_eap) :
                    context.getString(R.string.wifi_security_eap);
            case SECURITY_PSK:
                switch (pskType) {
                    case WPA:
                        return concise ? context.getString(R.string.wifi_security_short_wpa) :
                            context.getString(R.string.wifi_security_wpa);
                    case WPA2:
                        return concise ? context.getString(R.string.wifi_security_short_wpa2) :
                            context.getString(R.string.wifi_security_wpa2);
                    case WPA_WPA2:
                        return concise ? context.getString(R.string.wifi_security_short_wpa_wpa2) :
                            context.getString(R.string.wifi_security_wpa_wpa2);
                    case UNKNOWN:
                    default:
                        return concise ? context.getString(R.string.wifi_security_short_psk_generic)
                                : context.getString(R.string.wifi_security_psk_generic);
                }
            case SECURITY_WEP:
                return concise ? context.getString(R.string.wifi_security_short_wep) :
                    context.getString(R.string.wifi_security_wep);
            case SECURITY_NONE:
            default:
                return concise ? context.getString(R.string.wifi_security_none) : "";
        }
    }

    public static boolean isConnected(ScanResult result){
        return connectSSID.equals(result.SSID) && connectBSSID.equals(result.BSSID);

    }

    public static void refreshConnectedID(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            connectSSID = wifiInfo.getSSID().substring(1, wifiInfo.getSSID().length() - 1);
            connectBSSID = wifiInfo.getBSSID();
        } else {
            connectSSID = "";
            connectBSSID = "";
        }
        Log.d("wifi", "SSID: " + connectSSID + ", BSSID: " + connectBSSID);
    }

    public static int isSaved(Context context, ScanResult result){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuration : configurations){
            if(configuration.SSID.equals("\"" + result.SSID + "\"")){
                return configuration.networkId;
            }
        }
        return -1;
    }
}
