package com.ktc.tvlauncher.fragment;

import java.lang.reflect.Field;
import java.util.List;
import android.app.Activity;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import android.util.Log;
import com.ktcp.mta.sdk.KtcpMtaSdk;
/**
 * @Description 推荐
 * @author joychang
 * 
 */
public class ArtsFragment extends BaseFragment implements
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
			view = inflater.inflate(R.layout.layout_arts, container, false);
			init();
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		
		//initArtsImages();

		return view;
	}

	private void initArtsImages() {
		SharedPreferences sp = home.getSharedPreferences(
				Constant.SAVE_TENCENT_ARTSINFO, Context.MODE_PRIVATE);
		for (int index = 0; index < Constant.artsImagesNames.length; index++) {

			artsImageIndex = index;

			String imageName = sp.getString("art_picname_" + index, "");
			String picName = "file://"
					+ home.getApplicationContext().getFilesDir().toString()
					+ "/artsImages/" + index + ".jpg";
			if (sp.getBoolean("tencent_art_update", false)){//如果网络更新成功,那么清空缓存重新加载
				Log.d("Jason","综艺清除缓存");
				ImageLoader.getInstance().clearMemoryCache(); 
				ImageLoader.getInstance().clearDiscCache();
				SharedPreferences.Editor editor = context
						.getSharedPreferences(Constant.SAVE_TENCENT_ARTSINFO,
								context.MODE_PRIVATE).edit();
				editor.putBoolean("tencent_art_update", false);
				editor.commit();
			}
			Log.d("Jason","综艺显示");
			ImageLoader.getInstance().displayImage(picName, arts_typeLogs[index]);
		}
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
		//if (home.isArtsLoadSucess == true)
			initArtsImages();
		super.onResume();
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
		arts_fls = new FrameLayout[10];
		arts_typeLogs = new XCRoundRectImageView[10];
		arts_typebgs = new int[10];
		artsbgs = new ImageView[10];
		tvs = new TextView[10];
		animEffect = new ScaleAnimEffect();
	}

	protected void findViewById() {
		arts_fls[0] = (FrameLayout) view.findViewById(R.id.fl_arts_0);
		arts_fls[1] = (FrameLayout) view.findViewById(R.id.fl_arts_1);
		arts_fls[2] = (FrameLayout) view.findViewById(R.id.fl_arts_2);
		arts_fls[3] = (FrameLayout) view.findViewById(R.id.fl_arts_3);
		arts_fls[4] = (FrameLayout) view.findViewById(R.id.fl_arts_4);
		arts_fls[5] = (FrameLayout) view.findViewById(R.id.fl_arts_5);
		arts_fls[6] = (FrameLayout) view.findViewById(R.id.fl_arts_6);
		arts_fls[7] = (FrameLayout) view.findViewById(R.id.fl_arts_7);
		arts_fls[8] = (FrameLayout) view.findViewById(R.id.fl_arts_8);
		arts_fls[9] = (FrameLayout) view.findViewById(R.id.fl_arts_9);

		arts_typeLogs[0] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_0);
		arts_typeLogs[1] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_1);
		arts_typeLogs[2] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_2);
		arts_typeLogs[3] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_3);
		arts_typeLogs[4] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_4);
		arts_typeLogs[5] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_5);
		arts_typeLogs[6] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_6);
		arts_typeLogs[7] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_7);
		arts_typeLogs[8] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_8);
		arts_typeLogs[9] = (XCRoundRectImageView) view
				.findViewById(R.id.iv_arts_9);


		arts_typebgs[0] = R.drawable.fl_arts_1;
		arts_typebgs[1] = R.drawable.fl_arts_1;
		arts_typebgs[2] = R.drawable.fl_arts_1;
		arts_typebgs[3] = R.drawable.fl_arts_0;
		arts_typebgs[4] = R.drawable.fl_arts_1;
		arts_typebgs[5] = R.drawable.fl_arts_1;
		arts_typebgs[6] = R.drawable.fl_arts_0;
		arts_typebgs[7] = R.drawable.fl_arts_1;
		arts_typebgs[8] = R.drawable.fl_arts_1;
		arts_typebgs[9] = R.drawable.fl_arts_1;

		artsbgs[0] = (ImageView) view.findViewById(R.id.arts_bg_0);
		artsbgs[1] = (ImageView) view.findViewById(R.id.arts_bg_1);
		artsbgs[2] = (ImageView) view.findViewById(R.id.arts_bg_2);
		artsbgs[3] = (ImageView) view.findViewById(R.id.arts_bg_3);
		artsbgs[4] = (ImageView) view.findViewById(R.id.arts_bg_4);
		artsbgs[5] = (ImageView) view.findViewById(R.id.arts_bg_5);
		artsbgs[6] = (ImageView) view.findViewById(R.id.arts_bg_6);
		artsbgs[7] = (ImageView) view.findViewById(R.id.arts_bg_7);
		artsbgs[8] = (ImageView) view.findViewById(R.id.arts_bg_8);
		artsbgs[9] = (ImageView) view.findViewById(R.id.arts_bg_9);

		tvs[0] = (TextView) view.findViewById(R.id.tv_arts_0);
		tvs[1] = (TextView) view.findViewById(R.id.tv_arts_1);
		tvs[2] = (TextView) view.findViewById(R.id.tv_arts_2);
		tvs[3] = (TextView) view.findViewById(R.id.tv_arts_3);
		tvs[4] = (TextView) view.findViewById(R.id.tv_arts_4);
		tvs[5] = (TextView) view.findViewById(R.id.tv_arts_5);
		tvs[6] = (TextView) view.findViewById(R.id.tv_arts_6);
		tvs[7] = (TextView) view.findViewById(R.id.tv_arts_7);
		tvs[8] = (TextView) view.findViewById(R.id.tv_arts_8);
		tvs[9] = (TextView) view.findViewById(R.id.tv_arts_9);


	}

	private int getPX(int i) {
		return getResources().getDimensionPixelSize(i);
	}

	protected void setListener() {
		for (int i = 0; i < arts_typeLogs.length; i++) {
			arts_typeLogs[i].setOnClickListener(this);
			// if(ISTV){
			// re_typeLogs[i].setOnFocusChangeListener(this);
			// }
			arts_typeLogs[i].setOnFocusChangeListener(this);
			artsbgs[i].setVisibility(View.GONE);
		}
	}
	// Hisa 2016.02.19 推荐位图片获得焦点500ms以上进行数据上报 start
	Runnable tencentReport = new Runnable(){
		public void run() {
			//int uriNumber = curLine * 3 - pageIndex;
			SharedPreferences sp = context.getSharedPreferences(Constant.SAVE_TENCENT_ARTSINFO, Context.MODE_PRIVATE);
			String itemid = sp.getString("art_item_id_"+artFocusIndex,"");
			int row = (artFocusIndex / 5) + 1;
			int col = (artFocusIndex % 5) + 1;
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
		    Log.d("ktc_tencent","获得推荐位图片焦点art界面:launcher_tab_pv:launcher_tab_pv:data = " + data.toString());
		    KtcpMtaSdk.mtaReport(context,data);			
		}
		
	};
	// Hisa 2016.02.19 推荐位图片获得焦点500ms以上进行数据上报 end
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		int paramInt = 0;
		isArtFeaturedFocus = hasFocus;
		switch (v.getId()) {
		case R.id.iv_arts_0:
			paramInt = 0;
			break;
		case R.id.iv_arts_1:
			paramInt = 1;
			break;
		case R.id.iv_arts_2:
			paramInt = 2;
			break;
		case R.id.iv_arts_3:
			paramInt = 3;
			break;
		case R.id.iv_arts_4:
			paramInt = 4;
			break;
		case R.id.iv_arts_5:
			paramInt = 5;
			break;
		case R.id.iv_arts_6:
			paramInt = 6;
			break;
		case R.id.iv_arts_7:
			paramInt = 7;
			break;
		case R.id.iv_arts_8:
			paramInt = 8;
			break;
		case R.id.iv_arts_9:
			paramInt = 9;
			break;

		}
		if (hasFocus) {
			showOnFocusTranslAnimation(paramInt);
			flyAnimation(paramInt);
		} else {
			showLooseFocusTranslAinimation(paramInt);
		}
		//add by JsonHisa 2015-06-15
		for (TextView tv : tvs) {
			if (tv.getVisibility() != View.GONE) {
				tv.setVisibility(View.GONE);
			}
		}
		
		if (true == isArtFeaturedFocus){
			artFocusIndex = paramInt;
			handler.removeCallbacks(tencentReport);
			handler.postDelayed(tencentReport, 500);
		}else{
			handler.removeCallbacks(tencentReport);
		}

	}
	Handler handler = new Handler();
	/**
	 * 飞框焦点动画
	 * 
	 * @param paramInt
	 */
	private void flyAnimation(int paramInt) {
		int[] location = new int[2];
		arts_typeLogs[paramInt].getLocationOnScreen(location);
		int width = arts_typeLogs[paramInt].getWidth();
		int height = arts_typeLogs[paramInt].getHeight();
		float x = (float) location[0];
		float y = (float) location[1];
		switch (paramInt) {
		case 0:
			width += 42;
			height += 42;
			x = 296;
			y = 311;
			break;
		case 1:

			width += 42;
			height += 42;
			x = 580;
			y = 311;
			break;
		case 2:
			width += 42;
			height += 42;
			x = 866;
			y = 311;
			break;
		case 3:

			width = width + 42;
			height = height + 42;
			x = 1150;
			y = 311;
			break;
		case 4:

			width = width + 42;
			height = height + 42;
			x = 1435;
			y = 311;
			break;
		case 5:

			width = width + 42;
			height = height + 47;
			x = 296;
			y = 610;
			break;
		case 6:
			width = width + 42;
			height = height +47;
			x = 580;
			y = 610;
			break;
		case 7:
			width = width + 42;
			height = height + 47;
			x = 865;
			y = 610;
			break;
		case 8:
			width = width + 42;
			height = height + 47;
			x = 1151;
			y = 610;
			break;
		case 9:
			width = width + 42;
			height = height + 47;
			x = 1435;
			y = 610;
			break;

		}
	}

	private void showOnFocusTranslAnimation(int paramInt) {

		arts_fls[paramInt].bringToFront();// 将当前FrameLayout置为顶层
		Animation mtAnimation = null;
		Animation msAnimation = null;
		switch (paramInt) {
		case 0:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 1:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 2:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 3:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 4:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 5:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 6:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 7:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 8:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 9:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
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
		arts_fls[paramInt].startAnimation(set);
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
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 1:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 2:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 3:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 4:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 5:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 6:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 7:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 8:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
			break;
		case 9:
			mtAnimation = animEffect.translAnimation(0.0f, 0f, 0.0f, 0f);
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
		artsbgs[paramInt].setVisibility(View.GONE);
		arts_fls[paramInt].startAnimation(set);
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
			artsbgs[paramInt].setVisibility(View.VISIBLE);

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

	
		switch (v.getId()) {

		case R.id.iv_arts_0:
			startWasu(0);

			break;
		case R.id.iv_arts_1:
			startWasu(1);
			break;
		case R.id.iv_arts_2:
			startWasu(2);
			break;
		case R.id.iv_arts_3:
			startWasu(3);
			break;
		case R.id.iv_arts_4:
			startWasu(4);
			break;
		case R.id.iv_arts_5:
			startWasu(5);
			break;
		case R.id.iv_arts_6:
			startWasu(6);
			break;
		case R.id.iv_arts_7:
			startWasu(7);
			break;
		case R.id.iv_arts_8:
			startWasu(8);
			break;
		case R.id.iv_arts_9:
			startWasu(9);
			break;

		}
		home.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	private void startWasu(int artNumber) {
/*		SharedPreferences sp = context.getSharedPreferences(
				Constant.SAVE_TENCENT_ARTSINFO, Context.MODE_PRIVATE);
		Intent intent = new Intent("com.wasutv.action.webbrowser");
	
		String link_url = Constant.WASU_BASE_URL + sp.getString("art_linkurl_" + artNumber, null);
		intent.putExtra("Url", link_url);
		startActivity(intent);	*/
		artClickIndex = artNumber; 
		SharedPreferences sp = context.getSharedPreferences(Constant.SAVE_TENCENT_ARTSINFO, Context.MODE_PRIVATE);
    	String uri = sp.getString("art_uri_" + artNumber,"");
		String itemid = sp.getString("art_item_id_"+artNumber,"");
		int row = (artNumber / 5) + 1;
		int col = (artNumber % 5) + 1;
		String number = row + ":" + col;
		String data = "{" +
	    		"\"app_package\":\"com.ktc.tvlauncher\"," + 
	    		"\"event_id\":\"launcher_chosen_clicked\"," +
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
	    Log.d("ktc_tencent","点击推荐位图片art界面:launcher_chosen_clicked:data = " + data.toString());
	    KtcpMtaSdk.mtaReport(context,data);
    	
    	Intent intent = new Intent();
		intent.setData(Uri.parse(uri));
		handleIntent(false, intent);
		
		SystemProperties.set("mstar.str.storage", "1");
		getActivity().sendBroadcast(new Intent("com.jrm.filefly.action"));
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
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//appClickListener = (View.OnClickListener)activity;
	}

	private View view;
	private FrameLayout[] arts_fls;
	public XCRoundRectImageView[] arts_typeLogs;
	private TextView[] tvs;
	private int[] arts_typebgs;
	private ImageView[] artsbgs;
	ScaleAnimEffect animEffect;
	private final String TAG = "SharedPreference";

	public ImageLoader imageLoader;
	private TextView tv_intro = null;
	
	private int artsImageIndex = 0;
	//Hisa 2016.02.19 推荐位图片获得焦点500ms以上进行数据上报 start
  	private int pageIndex = 1;//当前的layout的第几张图片:1,2,3
  	private boolean isArtFeaturedFocus = false;//判断推荐位是否获得焦点
  	private static int artClickIndex = 0;
  	private static int artFocusIndex = 0;
  	//Hisa 2016.02.19 推荐位图片获得焦点500ms以上进行数据上报 end

}
