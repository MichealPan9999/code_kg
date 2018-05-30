package cn.ktc.android.oobe;

import android.graphics.Color;
import android.graphics.Rect;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PrivacyTermsPage extends BasePage {
	private static final String LOG_TAG = PrivacyTermsPage.class.getSimpleName();
	private Button mBackBtn,mNextBtn;
	private TextView mContentWeb;
    private CheckBox mAgreeCheckBox;
    
    @Override
	protected View onCreatePageView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.page_privacy_terms, null);
		mContentWeb = (TextView) layout.findViewById(R.id.web_content);
		mAgreeCheckBox = (CheckBox) layout.findViewById(R.id.cb_agree);
		mBackBtn = (Button)layout.findViewById(R.id.btn_privacy_terms_back);
		mNextBtn = (Button)layout.findViewById(R.id.btn_privacy_terms_next);
        //"file:///android_asset/privacyTerms.html"
		mContentWeb.setText(Html.fromHtml(loadHtml()));
        mContentWeb.setMovementMethod(ScrollingMovementMethod.getInstance());
        mContentWeb.setFocusable(true);
	    mContentWeb.setFocusableInTouchMode(true);
		mContentWeb.requestFocus();
		
		mAgreeCheckBox.setChecked(false);
		mNextBtn.setClickable(false);
		mNextBtn.setTextColor(Color.GRAY);
		
		mAgreeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (mAgreeCheckBox.isChecked()){
					mNextBtn.setClickable(true);
					mNextBtn.setTextColor(Color.WHITE);
				} else {
					mNextBtn.setClickable(false);
					mNextBtn.setTextColor(Color.GRAY);
				}
				
			}
		});
		
		mBackBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivityListListener.onPrevActivity();
			}
		});
		
		mNextBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mAgreeCheckBox.isChecked())
				mActivityListListener.onNextActivity();
			}
		});
		
		return layout;
	}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
	public void active() {
		// TODO Auto-generated method stub
		super.active();
	}

	String loadHtml(){
        String fileName = "privacyTerms.html"; //文件名字
        InputStreamReader inputStreamReader;
        StringBuilder builder=new StringBuilder();
        try{

            InputStream in = getResources().getAssets().open(fileName);
            inputStreamReader=new InputStreamReader(in,"UTF-8");
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String tmp;

            while ((tmp=reader.readLine())!=null){
                builder.append(tmp);
            }
            in.close();
            inputStreamReader.close();
            reader.close();
        }catch(Exception e) {
            e.printStackTrace();

        }
        return builder.toString();
    }

	
}
