package com.ktc.launcher.view;

import java.util.List;

import com.ktc.launcher.R;
import com.ktc.launcher.bean.InputSourceItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.View.OnKeyListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ktc.launcher.utils.ScreenParamsConfig;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import static android.content.ContentValues.TAG;

public class SourceInfoPopWindow extends PopupWindow implements OnKeyListener{
	private Context context;
	private LinearLayout ll_source_root;
	private LinearLayout.LayoutParams layoutParams;
	private List<InputSourceItem> inputSourceItems;
	private TextView [] tv_sources;
	private ColorDrawable dw ;
	private int width=0;
	private OnChoiceListener listener; 
	public interface OnChoiceListener{
		public void selectItem(int position);
	}
	public void setChoiceListener(OnChoiceListener listener){
		this.listener=listener;
	}
	@SuppressLint("ResourceAsColor")
	public SourceInfoPopWindow(Context context,List<InputSourceItem> inputSourceItems) {
		this.context=context;
		this.inputSourceItems=inputSourceItems;
		ll_source_root=new LinearLayout(context);
		ll_source_root.setOrientation(LinearLayout.HORIZONTAL);
		ll_source_root.setBackgroundResource(R.drawable.hha);
		ll_source_root.setGravity(Gravity.CENTER);
		ll_source_root.setPadding(0, 5, 0, 0);
		dw = new ColorDrawable(00000000);
		
		loadView();
		layoutParams=new LayoutParams(width, ScreenParamsConfig.getResolutionValue(100));
		ll_source_root.setLayoutParams(layoutParams);
        this.setWidth(width+ScreenParamsConfig.getResolutionValue(10));
        this.setHeight(ScreenParamsConfig.getResolutionValue(100));
        
        Log.v("zhanghz", width+"width");
        
        this.setBackgroundDrawable(dw);
        this.setContentView(ll_source_root);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setTouchable(true);
        
        initListener();
	}
	private void initListener() {
		for(int i=0;i<tv_sources.length;i++){
			tv_sources[i].setFocusable(true);
			tv_sources[i].setOnClickListener(clickLisntenr);
			tv_sources[i].setOnKeyListener(this);
		}
	}
	OnClickListener clickLisntenr=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.v("zhanghz", v.getId()+"==id");
			SourceInfoPopWindow.this.dismiss();
			listener.selectItem(v.getId());
		}
	};
	private void loadView() {
		tv_sources=new TextView[inputSourceItems.size()];
		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
		for(int i=0;i<inputSourceItems.size();i++){
			TextView tv_source=new TextView(context);
			LinearLayout.LayoutParams lp;
			if ("COMPONENT".equals(inputSourceItems.get(i).getSourceName())){
				Log.d("Maxs800"," COMPONENT ");
				lp = new LinearLayout.LayoutParams(ScreenParamsConfig.getResolutionValue(172),ScreenParamsConfig.getResolutionValue(80));
			}else{
				Log.d("Maxs800","Not COMPONENT ");
				lp = new LinearLayout.LayoutParams(ScreenParamsConfig.getResolutionValue(152),ScreenParamsConfig.getResolutionValue(80));
			}
			
			lp.setMargins(ScreenParamsConfig.getResolutionValue(20), ScreenParamsConfig.getResolutionValue(5), ScreenParamsConfig.getResolutionValue(20), ScreenParamsConfig.getResolutionValue(5));
			tv_source=new TextView(context);
			tv_source.setTextSize(18);
			tv_source.setGravity(Gravity.CENTER);
			tv_source.setTextColor(Color.rgb(255, 255, 255));
			tv_source.setTypeface(font);
			tv_source.setLayoutParams(lp);
			tv_source.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.source_txt_background));
			tv_source.setText(inputSourceItems.get(i).getSourceName());
			tv_source.setId(i);
			tv_source.setFocusable(true);
			tv_source.setFocusableInTouchMode(true);
			tv_source.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.view_launcher_anim_big));
					} else {
						v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.view_launcher_anim_small));
					}
				}
			});	
			tv_source.setOnHoverListener(new OnHoverListener() {
				
				@Override
				public boolean onHover(View v, MotionEvent event) {
					if(v.isHovered())
						if(!v.hasFocus())
							v.requestFocus();
					return false;
				}
			});
			ll_source_root.addView(tv_source);
			tv_sources[i]=tv_source;
			//Ìí¼Ó·Ö¸îÏß
			if(i!=inputSourceItems.size()-1){
				TextView divider=new TextView(context);
				LinearLayout.LayoutParams dividerLp = new LinearLayout.LayoutParams(ScreenParamsConfig.getResolutionValue(1),ScreenParamsConfig.getResolutionValue(50));
				dividerLp.setMargins(0, 5, 0, 3);
				divider.setLayoutParams(dividerLp);
				divider.setGravity(Gravity.CENTER);
				divider.setBackgroundColor(Color.rgb(255, 255, 255));
				ll_source_root.addView(divider);
				width+=1;
			}
			if ("COMPONENT".equals(inputSourceItems.get(i).getSourceName())){
				Log.d("Maxs800","Width :COMPONENT ");
				width += ScreenParamsConfig.getResolutionValue(210);
			}else{
				Log.d("Maxs800","Width :Not COMPONENT ");
				width += ScreenParamsConfig.getResolutionValue(190);
			}


		}
	}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_TV_INPUT)
			return true;
		return false;
	}
	
}
