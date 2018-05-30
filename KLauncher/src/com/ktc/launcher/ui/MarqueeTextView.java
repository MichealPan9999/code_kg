package com.ktc.launcher.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView{
	 public MarqueeTextView(Context context) {  
	        super(context);  
	     }  
	   
	     public MarqueeTextView(Context context, AttributeSet attrs) {  
	         super(context, attrs);  
	     }  
	    
	    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {  
	         super(context, attrs, defStyle);  
	     }  
	   
	   

		@Override
		protected void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
			// TODO Auto-generated method stub
			super.onFocusChanged(arg0, arg1, arg2);
		}  
	

}
