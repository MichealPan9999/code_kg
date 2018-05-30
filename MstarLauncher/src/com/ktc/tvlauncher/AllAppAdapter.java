package com.ktc.tvlauncher;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AllAppAdapter extends BaseAdapter{
	private Context mContext;
	private PackageManager pm;
	private List<ResolveInfo> mList;
	
	public AllAppAdapter(Context context, List<ResolveInfo> list) {
		mContext = context;
		pm = context.getPackageManager();
		
		mList = new ArrayList<ResolveInfo>();
		for(int i = 0; i<list.size();i++)
		{
			mList.add(list.get(i));
		}
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ResolveInfo appInfo = mList.get(position);
		AppItem appItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.appicon, null);
			
			appItem = new AppItem();
			appItem.mAppIcon = (ImageView)v.findViewById(R.id.appicon);
			appItem.mAppTitle = (TextView)v.findViewById(R.id.apptitle);
			
			v.setTag(appItem);
			convertView = v;
		} else {
			appItem = (AppItem)convertView.getTag();
		}
		
/*		//ota tip, zjd20150512
		String name = appInfo.activityInfo.packageName;
		Log.v("zjd", "ota=" + SystemProperties.getBoolean("persist.sys.ota.available", false));
		boolean isOtaNew = SystemProperties.getBoolean("persist.sys.ota.available", false);
		if(name.equals("android.systemupdate.service") && isOtaNew){
			Log.v("zjd", "ota new");
			appItem.mAppIcon.setBackgroundDrawable(generatorContactCountIcon(appInfo.loadIcon(pm)));
		}else{
			Log.v("zjd", "ota not new");
			appItem.mAppIcon.setBackgroundDrawable(appInfo.loadIcon(pm));
		}
	*/	//ota tip, zjd20150512
		
		appItem.mAppTitle.setText(appInfo.loadLabel(pm));
		
		return convertView;
	}
	
	private class AppItem{
		ImageView mAppIcon;
		TextView mAppTitle;
	}
	
	//ota tip, zjd20150512
	private Drawable generatorContactCountIcon(Drawable drawable){  
		Bitmap icon = drawableToBitamp(drawable);

        int iconSize=(int)mContext.getResources().getDimension(android.R.dimen.app_icon_size);  
        Bitmap contactIcon=Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);  
        Canvas canvas=new Canvas(contactIcon);  

        Paint iconPaint=new Paint();  
        iconPaint.setDither(true);//������  
        iconPaint.setFilterBitmap(true);//��Bitmap�����˲����?�����Ч��  
        Rect src=new Rect(0, 0, icon.getWidth(), icon.getHeight());  
        Rect dst=new Rect(0, 0, iconSize, iconSize);  
        canvas.drawBitmap(icon, src, dst, iconPaint);  
          
        //���ÿ���ݺ�ʹ���豸���ı��־�  
        Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);  
        countPaint.setColor(Color.RED);  
        countPaint.setTextSize(18f);  
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);  
        canvas.drawText("New", iconSize-36, 24, countPaint);  
        Drawable icon_drawable = new BitmapDrawable(contactIcon);
        return icon_drawable;  
    } 
	
	private Bitmap drawableToBitamp(Drawable drawable){
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bitmap = bd.getBitmap();
		return bitmap;
	}

}
