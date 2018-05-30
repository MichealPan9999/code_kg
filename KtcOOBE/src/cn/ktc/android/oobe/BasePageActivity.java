package cn.ktc.android.oobe;

import java.util.ArrayList;
import java.util.List;
import android.app.FragmentTransaction;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mstar.android.MKeyEvent;
import android.graphics.Color;

public abstract class BasePageActivity extends FragmentActivity
		implements ActivityListListener,PageListListener {
	private static final int BASE_PAGEVIEW_ID = 100;
	/**
	 * 当前page标签，最低从0开始
	 */
	private int mCurrentItem;
	private List<BasePage> mPages;
	private List<View> mPageViews;
	private RelativeLayout container;
	private Handler mHandler = new Handler();
//	private String title;

	protected static final int RESULT_CLOSE_ALL = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		//tvTitle = (TextView) findViewById(R.id.tvTitle);
		container = (RelativeLayout) findViewById(R.id.rllyContainer);
		mPages = new ArrayList<BasePage>();
		List<BasePage> pages = createPages();
		if (pages != null)
			mPages.addAll(pages);

		addFragments();
		mCurrentItem = 0;
		setCurrentItem(mCurrentItem);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("onActivityResult:" + resultCode);
	    switch(resultCode){
		    case RESULT_CLOSE_ALL:
		        setResult(RESULT_CLOSE_ALL);
		        finish();
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction() == KeyEvent.ACTION_UP){
			return false ;
		}
//		String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
//		if (!deviceName.equals("MStar Smart TV Keypad"))
//			return false;
		
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
//			case KeyEvent.KEYCODE_TV_INPUT:
            case KeyEvent.KEYCODE_MENU:
			case KeyEvent.KEYCODE_PROG_RED:
			case KeyEvent.KEYCODE_PROG_GREEN:
			case KeyEvent.KEYCODE_PROG_YELLOW:
			case 294:
            case MKeyEvent.KEYCODE_EPG:
            case MKeyEvent.KEYCODE_TTX:
            	return true;
            
			case KeyEvent.KEYCODE_CHANNEL_UP:
				//keyInjection(KeyEvent.KEYCODE_DPAD_UP);
				return true;
			case KeyEvent.KEYCODE_CHANNEL_DOWN:
				//keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
				return true;
			case KeyEvent.KEYCODE_VOLUME_UP:
				//keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				//keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
				return true;
			case KeyEvent.KEYCODE_TV_INPUT:
				
				//keyInjection(KeyEvent.KEYCODE_DPAD_CENTER);
				return true;
            //case MKeyEvent.KEYCODE_FREEZE:
            //case MKeyEvent.KEYCODE_MTS:  //benq, zjd20150317
            //case MKeyEvent.KEYCODE_SOUND_MODE:   //benq, zjd20150317
            //case MKeyEvent.KEYCODE_PICTURE_MODE: //benq, zjd20150317
			
			default:break;
		}
		
		
		return super.onKeyDown(keyCode, event);
	}
	
	private static void keyInjection(final int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
				|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				|| keyCode == KeyEvent.KEYCODE_ENTER
				|| KeyEvent.KEYCODE_DPAD_CENTER == keyCode) {
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(keyCode);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}

	@Override
	public void onPrevActivity() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNextActivity() {
		// TODO Auto-generated method stub
	}
	
	public void onFinishActivity() {
		// TODO Auto-generated method stub
		setResult(RESULT_CLOSE_ALL);
		finish();
	}

	@Override
	public void onPrevPage(BasePage page) {
		// TODO Auto-generated method stub
		if (mCurrentItem > 0) {
			View view = mPageViews.get(mCurrentItem - 1);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
			view.setVisibility(View.VISIBLE);
			mPages.get(mCurrentItem - 1).active();
			final int oldItem = mCurrentItem;
			mCurrentItem -= 1;
			mPages.get(oldItem).inactive();
		}
	}

	@Override
	public void onNextPage(BasePage page) {
		// TODO Auto-generated method stub
		if (mCurrentItem < mPages.size() - 1) {
			View view = mPageViews.get(mCurrentItem);
			view.setVisibility(View.INVISIBLE);
			mPages.get(mCurrentItem + 1).active();
			final int oldItem = mCurrentItem;
			mCurrentItem += 1;
			mPages.get(oldItem).inactive();
		}

	}

	public void activePage(int pageIndex) {
		if (pageIndex >= 0 && pageIndex < mPages.size()) {
			mPages.get(pageIndex).active();
			final int oldItem = mCurrentItem;
			if (pageIndex > mCurrentItem) {
				for (int i = mCurrentItem; i < pageIndex; i++) {
					View view = mPageViews.get(i);
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
					mPages.get(oldItem).inactive();
				}
			} else {
				for (int i = mCurrentItem - 1; i >= pageIndex; i--) {
					View view = mPageViews.get(i);
					RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) view.getLayoutParams();
					mPages.get(oldItem).inactive();
				}
			}
			mCurrentItem = pageIndex;
		}
		
	}

	private void addFragments() {
		int id = BASE_PAGEVIEW_ID;

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		mPageViews = new ArrayList<View>();
		for (int i = mPages.size() - 1; i >= 0; i--) {
			FrameLayout fragmentLayout = new FrameLayout(this);
			fragmentLayout.setId(id);

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(id, mPages.get(i));
			ft.commit();

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(0, 0, 0, 0);
			layout.addView(fragmentLayout, layoutParams);
			mPageViews.add(0, fragmentLayout);
			id++;
		}

		layout.setClipChildren(true);

		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
		layout.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);

		container.addView(layout, params);
	}

	/**
	 * 设置当前活动页，无动画
	 * @param index
	 */
	private void activeItem(int index) {
		for (int i = 0; i < mPages.size(); i++) {
			if (i == index) {
				mPages.get(i).active();
				mPageViews.get(i).setFocusable(true);
				mPageViews.get(i).setFocusableInTouchMode(true);
				mPageViews.get(i).setActivated(true);
			} else {
				mPages.get(i).inactive();
				mPageViews.get(i).setFocusable(false);
				mPageViews.get(i).setFocusableInTouchMode(false);
				mPageViews.get(i).setActivated(false);
			}
		}
	}

	public void setCurrentItem(int index) {
		this.mCurrentItem = index;
		activeItem(mCurrentItem);
	}

	public int getCurrentItem() {
		return this.mCurrentItem;
	}

	public BasePage getCurrentPage() {
		return getPage(mCurrentItem);
	}

	public BasePage getPage(int index) {
		return this.mPages.get(index);
	}

	protected abstract List<BasePage> createPages();

	protected void setPage(int index, BasePage page) {
		if (mPages.get(index).equals(page))
			return;

		mPages.set(index, page);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(mPageViews.get(index).getId(), mPages.get(index));
		ft.commit();
	}

	static interface OnSmoothPaddingFinishedListener {
		void onSmoothPaddingFinished();
	}

	/*public void setPageActivityTitle(String title) {
		this.title = title;
		tvTitle.setText(title);
	}*/
}
