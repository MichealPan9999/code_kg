package cn.ktc.android.oobe;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.ktc.android.oobe.internet.LocalFragmentManager;

public class BasePage extends Fragment {
	protected Context mContext;
	private int mPageIndex;
	private boolean actived = false;
	private View mLayout;
	private ImageView ivIndex;
	protected ActivityListListener mActivityListListener;
	protected PageListListener mPageListListener;
	protected LocalFragmentManager mLocalManager;
	
	private final  String TAG = this.getClass().getSimpleName();
	
	@Override
	public void onAttach (Context context) {
		super.onAttach(context);
		this.mContext = context;
		mActivityListListener = (ActivityListListener) context;
		mPageListListener = (PageListListener) context;
		mLocalManager = LocalFragmentManager.getInstance(context);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mLayout = inflater.inflate(R.layout.page_base, null);
		//ivIndex = (ImageView) mLayout.findViewById(R.id.ivFragmentId);
		RelativeLayout frgContainer = 
				(RelativeLayout) mLayout.findViewById(R.id.rllyFragmentContainer);
		
		View frgView = onCreatePageView(inflater, container);
		if (frgView != null) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 
					RelativeLayout.LayoutParams.MATCH_PARENT);
			frgContainer.addView(frgView, params);
		}
		//refreshBg();
		return mLayout;
	}
	
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		return null;
	}
	
	
	private void refreshBg() {
		int resId = actived ? R.color.panel_background_active : R.color.panel_background_active;
		
		if (mLayout != null)
			mLayout.setBackgroundColor(mContext.getResources().getColor(resId));
	}
	
	public void active() {
		this.actived = true;
		//refreshBg();
		
	}
	
	public void inactive() {
		this.actived = false;
		//refreshBg();
	}
	
	public boolean isActive() {
		return this.actived;
	}
	
	public void setPageIndex(int index) {
		this.mPageIndex = index;
		
		//refreshIndexImage();
	}
	
	public int getPageIndex() {
		return this.mPageIndex;
	}
	
	protected void disableEnableControls(boolean enable, ViewGroup vg){
		for (int i = 0; i < vg.getChildCount(); i++){
			View child = vg.getChildAt(i);
			if (child instanceof ViewGroup){ 
				disableEnableControls(enable, (ViewGroup)child);
			} else {
				child.setEnabled(enable);
			}
		 }
	}
}
