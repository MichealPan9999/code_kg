package com.ktc.tvlauncher.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktc.tvlauncher.R;

public class AppWidget extends RelativeLayout {

	private ImageView appIcon;
	private TextView appName;
	private LinearLayout appLL;
	
	private Drawable drawable;

	public AppWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
					TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.App_Widget);
		
	drawable = a.getDrawable(R.styleable.App_Widget_bg);
	if (isInEditMode()) { return; }
		//appLL.setBackgroundDrawable(drawable);
		//appLL.setBackground(drawable);
		a.recycle();

	}

	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.app_layout, this);
		if (isInEditMode()) { return; }
		appLL = (LinearLayout)findViewById(R.id.ll_appwidget);
		appIcon = (ImageView) findViewById(R.id.iv_ui_appicon);
		appName = (TextView) findViewById(R.id.tv_ui_appname);
	}

	public void setAppIcon(Drawable icon) {
		appIcon.setImageDrawable(icon);
	}
	
	public String getAppName(){
		return appName.getText().toString();
	}

	public void setAppName(String name) {
		appName.setText(name);
	}

}
