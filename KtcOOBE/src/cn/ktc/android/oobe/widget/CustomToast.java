package cn.ktc.android.oobe.widget;

import cn.ktc.android.oobe.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast extends Toast {

	public CustomToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static Toast makeText(Context context, int resId, int duration) {
		String text = context.getString(resId);
		return CustomToast.makeText(context, text, duration);
	}

	public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast result = new Toast(context);
 
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.tvToastText);
        tv.setText(text);
 
        result.setView(v);
        //setGravity方法用于设置位置，此处为垂直居中
        result.setGravity(Gravity.CENTER, 0, 0);
        result.setDuration(duration);
        return result;
    }
}
