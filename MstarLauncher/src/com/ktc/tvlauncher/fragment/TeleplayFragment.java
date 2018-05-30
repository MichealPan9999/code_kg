package com.ktc.tvlauncher.fragment;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import com.ktc.tvlauncher.R;
import com.ktc.tvlauncher.ui.XCRoundRectImageView;
import com.ktc.tvlauncher.utils.Constant;
import com.ktc.tvlauncher.utils.Logger;
import com.ktc.tvlauncher.utils.ScaleAnimEffect;
import com.ktc.tvlauncher.utils.Utils;
import com.ktc.tvlauncher.vod.TeleplayDescriptionActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.util.Log;
import com.ktcp.mta.sdk.KtcpMtaSdk;
/**
 * @Description 推荐
 * @author joychang
 * 
 */
public class TeleplayFragment extends BaseFragment implements
		OnFocusChangeListener, OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		if (null == view) {
			view = inflater.inflate(R.layout.layout_tvplay, container, false);
			init();
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}

		return view;
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		//if (home.isTeleplaysLoadSucess == true)
			iniTeleplaysImages();
	}

	private void iniTeleplaysImages() {
		SharedPreferences sp = home.getSharedPreferences(
				Constant.SAVE_TENCENT_TELEPLAYSINFO, Context.MODE_PRIVATE);

		for (int index = 0; index < Constant.teleplaysImagesNames.length; index++) {
			String teleplayText = sp.getString("teleplay_name_" + index, "");
			tvs[index + 1].setText(teleplayText);
			
			teleplaysImageIndex = index;
			
			String imageName = sp.getString("teleplay_pic_name_" + index, "");
			String picName = "file://"
					+ home.getApplicationContext().getFilesDir().toString()
					+ "/teleplaysImages/" + index + ".jpg";
			System.out.println("teleplay------->picName = " + picName);
			if (sp.getBoolean("tencent_teleplay_update", false)){//如果网络更新成功,那么清空缓存重新加载
				Log.d("Jason","电视剧清除缓存");
				ImageLoader.getInstance().clearMemoryCache(); 
				ImageLoader.getInstance().clearDiscCache();
				SharedPreferences.Editor editor = context
						.getSharedPreferences(Constant.SAVE_TENCENT_TELEPLAYSINFO,
								context.MODE_PRIVATE).edit();
				editor.putBoolean("tencent_teleplay_update", false);
				editor.commit();
			}
			Log.d("Jason","电视剧显示");
			ImageLoader.getInstance().displayImage(picName,
					re_typeLogs[index + 1]);
		}

	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	// 初始化
	private void init() {
		loadViewLayout();
		findViewById();
		setListener();
		// re_fls[0].requestFocus();
	}


	public String getPicName(String pic) {
		String[] picStr = pic.split("/");
		return picStr[picStr.length - 1];
	}



	protected void loadViewLayout() {
		re_fls = new FrameLayout[7];
		re_typeLogs = new XCRoundRectImageView[7];
		re_typebgs = new int[7];
		rebgs = new ImageView[7];
		tvs = new TextView[7];
		animEffect = new ScaleAnimEffect();
	}

	protected void findViewById() {
		re_fls[0] = (FrameLayout) view.findViewById(R.id.fl_teleplay_0);
		re_fls[1] = (FrameLayout) view.findViewById(R.id.fl_teleplay_1);
		re_fls[2] = (FrameLayout) view.findViewById(R.id.fl_teleplay_2);
		re_fls[3] = (FrameLayout) view.findViewById(R.id.fl_teleplay_3);
		re_fls[4] = (FrameLayout) view.findViewById(R.id.fl_teleplay_4);
		re_fls[5] = (FrameLayout) view.findViewById(R.id.fl_teleplay_5);
		re_fls[6] = (FrameLayout) view.findViewById(R.id.fl_teleplay_6);

		re_typeLogs[0] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_teleplay_0);
		re_typeLogs[1] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_teleplay_1);
		re_typeLogs[2] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_teleplay_2);
		re_typeLogs[3] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_teleplay_3);
		re_typeLogs[4] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_teleplay_4);
		re_typeLogs[5] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_teleplay_5);
		re_typeLogs[6] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_teleplay_6);

		re_typebgs[0] = R.drawable.fl_arts_1;
		re_typebgs[1] = R.drawable.fl_arts_1;
		re_typebgs[2] = R.drawable.fl_arts_1;
		re_typebgs[3] = R.drawable.fl_arts_0;
		re_typebgs[4] = R.drawable.fl_arts_1;
		re_typebgs[5] = R.drawable.fl_arts_1;
		re_typebgs[6] = R.drawable.fl_arts_3;

		rebgs[0] = (ImageView) view.findViewById(R.id.teleplay_bg_0);
		rebgs[1] = (ImageView) view.findViewById(R.id.teleplay_bg_1);
		rebgs[2] = (ImageView) view.findViewById(R.id.teleplay_bg_2);
		rebgs[3] = (ImageView) view.findViewById(R.id.teleplay_bg_3);
		rebgs[4] = (ImageView) view.findViewById(R.id.teleplay_bg_4);
		rebgs[5] = (ImageView) view.findViewById(R.id.teleplay_bg_5);
		rebgs[6] = (ImageView) view.findViewById(R.id.teleplay_bg_6);

		tvs[0] = (TextView) view.findViewById(R.id.tv_teleplay_0);
		tvs[1] = (TextView) view.findViewById(R.id.tv_teleplay_1);
		tvs[2] = (TextView) view.findViewById(R.id.tv_teleplay_2);
		tvs[3] = (TextView) view.findViewById(R.id.tv_teleplay_3);
		tvs[4] = (TextView) view.findViewById(R.id.tv_teleplay_4);
		tvs[5] = (TextView) view.findViewById(R.id.tv_teleplay_5);
		tvs[6] = (TextView) view.findViewById(R.id.tv_teleplay_6);

	}

	private int getPX(int i) {
		return getResources().getDimensionPixelSize(i);
	}

	protected void setListener() {
		for (int i = 0; i < re_typeLogs.length; i++) {
			re_typeLogs[i].setOnClickListener(this);
			// if(ISTV){
			// re_typeLogs[i].setOnFocusChangeListener(this);
			// }
			re_typeLogs[i].setOnFocusChangeListener(this);
			rebgs[i].setVisibility(View.GONE);
		}
	}
	// Hisa 2016.02.19 推荐位图片获得焦点500ms以上进行数据上报 start
	Runnable tencentReport = new Runnable(){
		public void run() {
			//int uriNumber = curLine * 3 - pageIndex;
			SharedPreferences sp = context.getSharedPreferences(Constant.SAVE_TENCENT_TELEPLAYSINFO, Context.MODE_PRIVATE);
			String itemid = sp.getString("teleplay_item_id_"+teleplayFocusIndex,"");
			int row = (teleplayFocusIndex / 7) + 1;
			int col = (teleplayFocusIndex % 7) + 1;
			String number = row + ":" + col;
			String data = "{" +
		    		"\"app_package\":\"com.ktc.tvlauncher\"," + 
		    		"\"event_id\":\"launcher_tab_pv\"," +
		    		"\"event_type\":3," + 
		    		"\"data_type\":1," +
		    		"\"pr\":\"LAUNCHER\"," +
		    		"\"data\":{" + 
		    		"\"cid\":\"itemid\"," +
		    		"\"tab\":\"launcher_recom\"," +
		    		"\"pos\":\"number\"" +
		    		"}}";
		    data = data.replace("itemid", itemid);
		    data = data.replace("number", number);
		    Log.d("ktc_tencent","获得推荐位图片焦点teleplay界面:launcher_tab_pv:launcher_tab_pv:data = " + data.toString());
		    KtcpMtaSdk.mtaReport(context,data);			
		}
		
	};
	// Hisa 2016.02.19 推荐位图片获得焦点500ms以上进行数据上报 end
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		int paramInt = 0;
		isTeleplayFeaturedFocus = hasFocus;
		switch (v.getId()) {
		case R.id.iv_teleplay_0:
			paramInt = 0;
			break;
		case R.id.iv_teleplay_1:
			paramInt = 1;
			break;
		case R.id.iv_teleplay_2:
			paramInt = 2;
			break;
		case R.id.iv_teleplay_3:
			paramInt = 3;
			break;
		case R.id.iv_teleplay_4:
			paramInt = 4;
			break;
		case R.id.iv_teleplay_5:
			paramInt = 5;
			break;
		case R.id.iv_teleplay_6:
			paramInt = 6;
			break;

		}
		if (hasFocus) {
			showOnFocusTranslAnimation(paramInt);
		} else {
			showLooseFocusTranslAinimation(paramInt);
		}
		for (TextView tv : tvs) {
			if (tv.getVisibility() != View.GONE) {
				tv.setVisibility(View.GONE);
			}
		}
		if ((true == isTeleplayFeaturedFocus) && (0 != paramInt)){
			teleplayFocusIndex = paramInt;
			handler.removeCallbacks(tencentReport);
			handler.postDelayed(tencentReport, 500);
		}else{
			handler.removeCallbacks(tencentReport);
		}
	}
	Handler handler = new Handler();
	private void showOnFocusTranslAnimation(int paramInt) {

		re_fls[paramInt].bringToFront();// 将当前FrameLayout置为顶层
		Animation mtAnimation = null;
		Animation msAnimation = null;
		switch (paramInt) {
		case 0:
			mtAnimation = animEffect.translAnimation(0.0f, 14.0f, 0.0f, 0.0f);
			break;
		case 1:
			mtAnimation = animEffect.translAnimation(0.0f, 4.0f, 0.0f, 0.0f);
			break;
		case 2:
			mtAnimation = animEffect.translAnimation(0.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 3:
			mtAnimation = animEffect.translAnimation(0.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 4:
			mtAnimation = animEffect.translAnimation(0.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 5:
			mtAnimation = animEffect.translAnimation(0.0f, 7.0f, 0.0f, 0.0f);
			break;
		case 6:
			mtAnimation = animEffect.translAnimation(0.0f, 7.0f, 0.0f, 0.0f);
			break;

		default:
			break;
		}
		msAnimation = animEffect.ScaleAnimation(1.0F, 1.15F, 1.0F, 1.15F);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(msAnimation);
		set.addAnimation(mtAnimation);
		set.setFillAfter(true);
		// set.setFillEnabled(true);
		set.setAnimationListener(new MyOnFocusAnimListenter(paramInt));
		// ImageView iv = re_typeLogs[paramInt];
		// iv.setAnimation(set);
		// set.startNow(); TODO
		re_fls[paramInt].startAnimation(set);
		// re_fls[paramInt].startAnimation(set);

	}

	/**
	 * 失去焦点缩小
	 * 
	 * @param paramInt
	 */
	private void showLooseFocusTranslAinimation(int paramInt) {
		Animation mAnimation = null;
		Animation mtAnimation = null;
		Animation msAnimation = null;
		AnimationSet set = null;
		switch (paramInt) {
		case 0:
			mtAnimation = animEffect.translAnimation(14.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 1:
			mtAnimation = animEffect.translAnimation(4.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 2:
			mtAnimation = animEffect.translAnimation(0.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 3:
			mtAnimation = animEffect.translAnimation(0.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 4:
			mtAnimation = animEffect.translAnimation(0.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 5:
			mtAnimation = animEffect.translAnimation(7.0f, 0.0f, 0.0f, 0.0f);
			break;
		case 6:
			mtAnimation = animEffect.translAnimation(7.0f, 0.0f, 0.0f, 0.0f);
			break;

		default:
			break;

		}
		msAnimation = animEffect.ScaleAnimation(1.15F, 1.0F, 1.15F, 1.0F);
		set = new AnimationSet(true);
		set.addAnimation(msAnimation);
		set.addAnimation(mtAnimation);
		set.setFillAfter(true);
		// set.setFillEnabled(true);
		set.setAnimationListener(new MyLooseFocusAnimListenter(paramInt));
		// ImageView iv = re_typeLogs[paramInt];
		// iv.setAnimation(set);
		// set.startNow();
		// mAnimation.setAnimationListener(new
		// MyLooseFocusAnimListenter(paramInt));
		rebgs[paramInt].setVisibility(View.GONE);
		re_fls[paramInt].startAnimation(set);
	}

	/**
	 * 获取焦点时动画监听
	 * 
	 * @author joychang
	 * 
	 */
	public class MyOnFocusAnimListenter implements Animation.AnimationListener {

		private int paramInt;

		public MyOnFocusAnimListenter(int paramInt) {
			this.paramInt = paramInt;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {

			rebgs[paramInt].setVisibility(View.VISIBLE);
			// Animation localAnimation =animEffect
			// .alphaAnimation(0.0F, 1.0F, 150L, 0L);
			// localImageView.startAnimation(localAnimation);
			if (paramInt >= 0) {
				tvs[paramInt].setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	/**
	 * 获取焦点时动画监听
	 * 
	 * @author joychang
	 * 
	 */
	public class MyLooseFocusAnimListenter implements
			Animation.AnimationListener {

		private int paramInt;

		public MyLooseFocusAnimListenter(int paramInt) {
			this.paramInt = paramInt;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	/**
	 * 根据状态来下载或者打开app
	 * 
	 * @author drowtram
	 * @param apkurl
	 * @param packName
	 */
	private void startOpenOrDownload(String apkurl, String packName,
			String fileName) {
		// 判断当前应用是否已经安装
		for (PackageInfo pack : home.packLst) {
			if (pack.packageName.equals(packName)) {
				// 已安装了apk，则直接打开
				Intent intent = getActivity().getPackageManager()
						.getLaunchIntentForPackage(packName);
				startActivity(intent);
				return;
			}
		}
		// 如果没有安装，则查询本地是否有安装包文件，有则直接安装
		if (!Utils.startCheckLoaclApk(home, fileName)) {
			// 如果没有安装包 则进行下载安装
			Utils.startDownloadApk(home, apkurl, null);
		}
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.iv_teleplay_0:
			
			SystemProperties.set("mstar.str.storage", "1");
			getActivity().sendBroadcast(new Intent("com.jrm.filefly.action"));
			
			Intent intent = new Intent();
    		intent.setData(Uri.parse(Constant.NEW_TELEPLAY_URL));
    		handleIntent(false, intent);
			break;
		case R.id.iv_teleplay_1:
			startActivity(0);
			break;
		case R.id.iv_teleplay_2:
			startActivity(1);
			break;
		case R.id.iv_teleplay_3:
			startActivity(2);
			break;
		case R.id.iv_teleplay_4:
			startActivity(3);
			break;
		case R.id.iv_teleplay_5:
			startActivity(4);
			break;
		case R.id.iv_teleplay_6:
			startActivity(5);
			break;

		}
		home.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}
    
    private void handleIntent(boolean isBroadcast, Intent intent) {
    	if (isBroadcast) {
    				// 广播scheme模式拉起应用
    		context.sendBroadcast(intent);
    	} else {
    				// 隐式调用的方式startActivity
    				intent.setAction("com.tencent.qqlivetv.open");
    				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				PackageManager packageManager = context.getPackageManager();
    				List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
    				boolean isIntentSafe = activities.size() > 0;
    				if (isIntentSafe) {
    					context.startActivity(intent);
    				} else {
    					//Toast.makeText(this, "未安装腾讯视频 ， 无法跳转", Toast.LENGTH_SHORT).show();
    				}
    			}
    }

	private void startActivity(int teleplayNumber) {
        Intent intent;
        intent = new Intent(context, TeleplayDescriptionActivity.class);
        intent.putExtra("teleplayNumber", teleplayNumber);
        System.out.println("--->teleplayNumber" + teleplayNumber);
        context.startActivity(intent);	
        
        SystemProperties.set("mstar.str.storage", "1");
		getActivity().sendBroadcast(new Intent("com.jrm.filefly.action"));
	}

	private View view;
	private FrameLayout[] re_fls;
	public XCRoundRectImageView[] re_typeLogs;
	private TextView[] tvs;
	private int[] re_typebgs;
	private ImageView[] rebgs;
	ScaleAnimEffect animEffect;
	private final String TAG = "MovieFragment";


	public ImageLoader imageLoader;

	private TextView tv_intro = null;

	private int teleplaysImageIndex = 0;
	
	//Hisa 2016.02.19 推荐位图片获得焦点500ms以上进行数据上报 start
  	//private int pageIndex = 1;//当前的layout的第几张图片:1,2,3
  	private boolean isTeleplayFeaturedFocus = false;//判断推荐位是否获得焦点
  	private static int teleplayFocusIndex = 0;
  	//Hisa 2016.02.19 推荐位图片获得焦点500ms以上进行数据上报 end
}
