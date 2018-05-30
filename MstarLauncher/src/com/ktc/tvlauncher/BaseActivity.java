package com.ktc.tvlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ktc.tvlauncher.utils.Logger;
import com.ktc.tvlauncher.utils.Utils;

/**
 * @Description 基类
 * @author joychang
 * 
 */
public abstract class BaseActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 设置横屏
		context = BaseActivity.this;
		sp = getSharedPreferences("shenma", MODE_PRIVATE);
		breathingAnimation = AnimationUtils.loadAnimation(context,
				R.anim.breathing);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;
		mHeight = dm.heightPixels;
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2)
				+ Math.pow(dm.heightPixels, 2));
		screenSize = diagonalPixels / (160 * dm.density);
		//from = Utils.getFormInfo(BaseActivity.class, 0);
		//devicetype = Utils.getFormInfo(BaseActivity.class, 3);
		version = Utils.getVersion(this);
/*	    try {
			params = Utils.encode("version=" + version + "&from=" + from + "&devicetype="+ devicetype, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	/**
	 * 初始�?	 */
	protected abstract void initView();

	/**
	 * 加载布局文件
	 */
	protected abstract void loadViewLayout();

	/**
	 * 初始化控�?	 */
	protected abstract void findViewById();

	/**
	 * 设置监听�?	 */
	protected abstract void setListener();

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	
	/**
	 * 应用崩溃toast
	 */
	protected void handleFatalError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "发生了一点意外，程序终止！",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	/**
	 * 内存空间不足
	 */
	protected void handleOutmemoryError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "内存空间不足！", Toast.LENGTH_SHORT)
				.show();
				finish();
			}
		});
	}

	/**
	 * Activity关闭和启动动�?	 */
	public void finish() {
		super.finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	}

	private static final String TAG = "BaseActivity";
	protected Context context;
	protected SharedPreferences sp;
	protected Animation breathingAnimation;
	protected int mWidth;
	protected int mHeight;
	protected String from;
	protected String devicetype;
	protected String version;
	protected String params;

	protected double screenSize;
	protected Toast mToast = null;
	protected AudioManager mAudioManager = null;
	//protected static String exit = "亲！感谢您支持神马视频，诚邀您参与用户体验改进计划，做一款你喜欢的视频聚合！意见反馈QQ群：375132069"; 
	//protected static String exit = "亲爱的小伙伴们！为了保证您更好的观看体验，请及时升级到最新版本！感谢您的支持！意见反馈QQ群：375132069";
	protected static String exit = "";
}
