package cn.ktc.android.oobe.internet.wireless;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
//import android.net.LinkProperties;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiConfiguration.AuthAlgorithm;
//import android.net.wifi.WifiConfiguration.IpAssignment;
//import android.net.wifi.WifiConfiguration.KeyMgmt;
//import android.net.wifi.WifiConfiguration.ProxySettings;
//import android.net.wifi.WpsInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.internet.WirelessScanPage;
import cn.ktc.android.oobe.language.LanguagePage;
import cn.ktc.android.oobe.utils.DialogOnKeyListener;
import cn.ktc.android.oobe.widget.CustomToast;
import cn.ktc.android.oobe.internet.wireless.WifiListAdapter;

public class WifiEditDialog
		implements View.OnClickListener {

	private static final String LOG_TAG = WifiEditDialog.class.getSimpleName();

	private Context mContext;

	private Dialog dialog;
	private TextView tvTitle;
	private EditText edtPassword;
	private TextView pwdHint;
	private Button btnClear;
	private Button btnDone;
	private Button btnCancel;
	private CheckBox checkbox;
	private Boolean showPassword = true;
	private ScanResult mScanResult;
	private Button forget_net;
	private String IDENTIFY="WirelessS";
	private OnWifiApListener mOnWifiApListener;
	private Boolean forget_network;
	private WifiManager mWifiManager;	 
	 
	//public IpAssignment mIpAssignment = IpAssignment.UNASSIGNED;
	//public ProxySettings mProxySettings = ProxySettings.UNASSIGNED;
	//public LinkProperties mLinkProperties = new LinkProperties();

	public WifiEditDialog(Context context, ScanResult result) {
		this.mContext = context;
		this.mScanResult = result;
	}

	private void preShow() {
		View layout = LayoutInflater.from(mContext).inflate(
				R.layout.dlg_wifi_ap, null);
		dialog = new Dialog(mContext, R.style.CustomDialog);
		dialog.setContentView(layout);
		dialog.setOnKeyListener(DialogOnKeyListener.mOnKeyListener);

		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
//        lp.y = 140;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;

        dialogWindow.setAttributes(lp);

        tvTitle = (TextView) layout.findViewById(R.id.alertTitle);
        edtPassword = (EditText) layout.findViewById(R.id.edtPassword);
		pwdHint = (TextView) layout.findViewById(R.id.pwd_hint);
        btnClear = (Button) layout.findViewById(R.id.btnClear);
        btnDone = (Button) layout.findViewById(R.id.btnDone);
        btnCancel = (Button) layout.findViewById(R.id.btnCancel);
        checkbox = (CheckBox) layout.findViewById(R.id.password_show);
        forget_net = (Button) layout.findViewById(R.id.forget_net);
        btnClear.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);       
        checkbox.setOnClickListener(this);      
        forget_net.setOnClickListener(this);
        edtPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String mText = edtPassword.getText().toString();
                int len = mText.length();
                if (len >= 8) {
                	btnDone.setEnabled(true);
                    btnDone.setFocusable(true);
                    btnDone.setFocusableInTouchMode(true);
	                pwdHint.setVisibility(View.INVISIBLE);
                } else {
                	btnDone.setEnabled(false);
                    btnDone.setFocusable(false);
                    btnDone.setFocusableInTouchMode(false);
	                pwdHint.setVisibility(View.VISIBLE);
                }
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange (View v, boolean hasFocus) {
				if(!hasFocus){
					InputMethodManager methodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					if(methodManager != null){
						methodManager.hideSoftInputFromWindow(edtPassword.getWindowToken(),0);
					}
				}
			}
		});

        btnDone.setEnabled(false);
        btnDone.setFocusable(false);
        btnDone.setFocusableInTouchMode(false);

        tvTitle.setText(mScanResult.SSID);
	}
	
	private void forgetShow() {
		View layout = LayoutInflater.from(mContext).inflate(
				R.layout.dlg_wifi_ap, null);
		dialog = new Dialog(mContext, R.style.CustomDialog);
		dialog.setContentView(layout);
		dialog.setOnKeyListener(DialogOnKeyListener.mOnKeyListener);

		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
//        lp.y = 140;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;

        dialogWindow.setAttributes(lp);

        tvTitle = (TextView) layout.findViewById(R.id.alertTitle);
        edtPassword = (EditText) layout.findViewById(R.id.edtPassword);
		pwdHint = (TextView) layout.findViewById(R.id.pwd_hint);
        btnClear = (Button) layout.findViewById(R.id.btnClear);
        btnDone = (Button) layout.findViewById(R.id.btnDone);
        btnCancel = (Button) layout.findViewById(R.id.btnCancel);
        checkbox = (CheckBox) layout.findViewById(R.id.password_show);
        forget_net = (Button) layout.findViewById(R.id.forget_net);        
        forget_net.setOnClickListener(this);
        edtPassword.setVisibility(View.GONE);
        checkbox.setVisibility(View.GONE);
        btnClear.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnDone.setVisibility(View.GONE);
        forget_net.setVisibility(View.VISIBLE);
        pwdHint.setText(R.string.forget_net_prompt_text);             
        tvTitle.setText(mScanResult.SSID);
	}
	
	public void show() {
		SharedPreferences sharedPref = mContext.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		forget_network = sharedPref.getBoolean("wifi_index",false);	     		 	 
		if(forget_network){
			forgetShow();			
			dialog.show();						        
		}else{			 
			preShow();
			dialog.show();
		}
		SharedPreferences sharedPref1 = mContext.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putBoolean("wifi_index", false);
        editor.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

			case R.id.btnClear: {
				edtPassword.setText("");
				edtPassword.requestFocus();
			} break;

			case R.id.btnDone: {
				String ssid = tvTitle.getText().toString();
				String password = edtPassword.getText().toString().trim();
				int security = WifiUtils.getSecurity(mScanResult);
				if (((mScanResult == null
						/* || mConfiguration.networkId == -1 */)
						|| ((security == WifiUtils.SECURITY_WEP && password.length() == 0)
						|| (security == WifiUtils.SECURITY_PSK && password.length() < 8)))) {
					CustomToast.makeText(
							mContext,
							mContext.getResources()
									.getString(R.string.ssid_password_error),
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					dialog.dismiss();
					if (mOnWifiApListener != null)
						mOnWifiApListener.onPassword(mScanResult, edtPassword.getText().toString());
				}
			} break;

			case R.id.btnCancel: {
				dialog.dismiss();
				if (mOnWifiApListener != null)
					mOnWifiApListener.onCancel();
			} break;
			
			case R.id.password_show:{
				if (showPassword) {// ÏÔÊ¾ÃÜÂë
	                //iv_showPassword.setImageDrawable(getResources().getDrawable(R.drawable.eye_o));
					edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					edtPassword.setSelection(edtPassword.getText().toString().length());
	                showPassword = !showPassword;
	            } else {// Òþ²ØÃÜÂë
	               // iv_showPassword.setImageDrawable(getResources().getDrawable(R.drawable.eye_c));
	            	edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
	            	edtPassword.setSelection(edtPassword.getText().toString().length());
	                showPassword = !showPassword;
	            }
	            break;
			}
			case R.id.forget_net: {
				 int networkId = getWifiNetworkId();
			        if (networkId != -1) {
			            mWifiManager.forget(networkId, null);
			        }
			         
			        dialog.dismiss();
			} break;    
			default:break;

		}
	}
	
	 public int getWifiNetworkId() {
		 	mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
	        if (wifiInfo != null) {	       
	            return wifiInfo.getNetworkId();
	        } else {
	            return -1;
	        }
	    }
   /* private WpsInfo getWpsConfig() {*/

		//WpsInfo config = new WpsInfo();
		/*
		 * switch (mNetworkSetupSpinner.getSelectedItemPosition()) { case
		 * WPS_PBC: config.setup = WpsInfo.PBC; break; case WPS_KEYPAD:
		 * config.setup = WpsInfo.KEYPAD; break; case WPS_DISPLAY: config.setup
		 * = WpsInfo.DISPLAY; break; default: config.setup = WpsInfo.INVALID;
		 * Log.e(TAG, "WPS not selected type"); return config; }
		 */
		//config.setup = WpsInfo.PBC;
		//Log.d(LOG_TAG, "mScanResult.BSSID = " + mScanResult.BSSID);
		//config.BSSID = (mScanResult != null) ? mScanResult.BSSID : null;
		//config.proxySettings = ProxySettings.NONE;;
		//config.ipAssignment = IpAssignment.DHCP;;
		//mLinkProperties.clear();
		//config.linkProperties = new LinkProperties(mLinkProperties);
		//return config;
  /*  }*/

	public void setOnWifiApListener(OnWifiApListener l) {
		this.mOnWifiApListener = l;
	}

	public static interface OnWifiApListener {
		public void onPassword(ScanResult result, String password);

		public void onWps();

		public void onCancel();
	}
}
