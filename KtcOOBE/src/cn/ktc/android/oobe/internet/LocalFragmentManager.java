package cn.ktc.android.oobe.internet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;

import java.util.WeakHashMap;

import cn.ktc.android.oobe.BasePage;

public class LocalFragmentManager {
	
	private static WeakHashMap<Context, LocalFragmentManager> mInstanceMap;
	private int mContainerId;
	private FragmentManager mFManager;
	
	private LocalFragmentManager (int mContainerId, FragmentManager mFManager) {
		this.mContainerId = mContainerId;
		this.mFManager = mFManager;
	}
	
	public static LocalFragmentManager getInstance (Context context) {
		if (mInstanceMap != null) {
			return mInstanceMap.get(context);
		}
		return null;
	}
	
	public static LocalFragmentManager initNewFragmentManager (Context context, int mContainerId, FragmentManager mFManager) {
		if (mInstanceMap == null) {
			mInstanceMap = new WeakHashMap<>();
		}
		LocalFragmentManager fragmentManager = new LocalFragmentManager(mContainerId, mFManager);
		mInstanceMap.put(context, fragmentManager);
		return fragmentManager;
	}
	
	public static void destroyAllInstance () {
		if (mInstanceMap != null) {
			mInstanceMap.clear();
		}
	}
	
	public void replace (BasePage page,  boolean isAddToBackStack) {
		
	
		FragmentTransaction transaction = mFManager.beginTransaction();
		transaction.replace(mContainerId, page);
		String className = page.getClass().getSimpleName();
		if(isAddToBackStack)
		transaction.addToBackStack(className);
		transaction.commitAllowingStateLoss();
	}
	
	public void replace(BasePage page){
		replace(page, true);
	}
	
	public void back (String className) {
		
		mFManager.popBackStack(className, 1);
//		mFManager.popBackStack();
	}
	
	public void removeInstance (Context context) {
		mInstanceMap.remove(context);
	}
	
}
