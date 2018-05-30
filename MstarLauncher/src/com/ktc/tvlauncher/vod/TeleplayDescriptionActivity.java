package com.ktc.tvlauncher.vod;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktc.tvlauncher.R;
import com.ktc.tvlauncher.utils.Constant;
import com.ktc.tvlauncher.utils.Logger;
import com.ktcp.mta.sdk.KtcpMtaSdk;

public class TeleplayDescriptionActivity extends Activity implements OnClickListener{
    private Button watch;
    private Button back;
    private ImageView teleplayImage;
    private TextView director;
    private TextView actor;
    private TextView description;
    private TextView teleplayName;
    private int teleplayNumber;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.teleplays_description);
        findView();
        Init();
        setListener();
    }
    
    public void setListener(){
        watch.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void findView() {
        watch = (Button) findViewById(R.id.imovie_description_watch);
        back = (Button) findViewById(R.id.imovie_description_back);
        director = (TextView) findViewById(R.id.imovie_description_director_content);
        actor = (TextView) findViewById(R.id.imovie_description_actor_content);
        description = (TextView) findViewById(R.id.imovie_description_description_content);
        teleplayName = (TextView) findViewById(R.id.imovie_description_movie_name);
        teleplayImage = (ImageView) findViewById(R.id.imovie_description_image);
    }

    public void Init() {
        
    	teleplayNumber = getIntent().getIntExtra("teleplayNumber", 0);
        SharedPreferences sp = getSharedPreferences(Constant.SAVE_TENCENT_TELEPLAYSINFO, MODE_PRIVATE);
        String tx_actor = sp.getString("teleplay_actor_" + teleplayNumber, "");
        String tx_director = sp.getString("teleplay_director_" + teleplayNumber, "");
        String tx_description = sp.getString("teleplay_description_" + teleplayNumber, "");
        String tx_movieName = sp.getString("teleplay_name_" + teleplayNumber, "");
        actor.setText(tx_actor);
        director.setText(tx_director);
        description.setText("     " + tx_description);
        teleplayName.setText(tx_movieName);
        new LoadImage().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imovie_description_back:
                finish();
                break;
            case R.id.imovie_description_watch:
            /*    SharedPreferences sp = getSharedPreferences(Constant.SAVE_TENCENT_TELEPLAYSINFO, MODE_PRIVATE);
                int id = sp.getInt("teleplay_id_" + teleplayNumber, 0);
                Log.i("test","id:"+id);
                Intent intent = new Intent("com.wasuali.action.programinfo");
            	intent.putExtra("Id", id);
            	intent.putExtra("Domain", "bs3-auth.sdk.wasu.tv");
            	intent.putExtra("IsFavorite", false);
            	startActivity(intent);*/
            	
            	SharedPreferences sp = getSharedPreferences(Constant.SAVE_TENCENT_TELEPLAYSINFO, MODE_PRIVATE);
            	String uri = sp.getString("teleplay_uri_" + teleplayNumber,"");
            	
            	String itemid = sp.getString("teleplay_item_id_"+teleplayNumber,"");
        		int row = (teleplayNumber / 7) + 1;
        		int col = 1;
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
        	    Log.d("tcl_tencent","点击推荐位图片teleplay界面:launcher_chosen_clicked:data = " + data.toString());
        	    KtcpMtaSdk.mtaReport(getApplicationContext(),data);
        	    
            	Intent intent = new Intent();
        		intent.setData(Uri.parse(uri));
        		handleIntent(false, intent);
                break;
            default:
                break;
        }

    }
    
    private void handleIntent(boolean isBroadcast, Intent intent) {
    	if (isBroadcast) {
    				// 
    			sendBroadcast(intent);
    	} else {
    				// 
    				intent.setAction("com.tencent.qqlivetv.open");
    				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				PackageManager packageManager = getPackageManager();
    				List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
    				boolean isIntentSafe = activities.size() > 0;
    				if (isIntentSafe) {
    					startActivity(intent);
    				} else {
    					//
    				}
    			}
    }
    class LoadImage extends AsyncTask<Void,Bitmap,Bitmap>{

		@Override
		protected Bitmap doInBackground(Void... arg0) {
			SharedPreferences sp = getSharedPreferences(Constant.SAVE_TENCENT_TELEPLAYSINFO, MODE_PRIVATE);
			  String imageName = sp.getString("teleplay_pic_name_" + teleplayNumber, "");
			  Bitmap bitmap =null;  
			  /*			  try {
		            FileInputStream fis = openFileInput(imageName);
		            bitmap = BitmapFactory.decodeStream(fis);
		            fis.close();
		          
		            
		            
		        } catch (FileNotFoundException e) {
		            //Log.i(Constants.TAG, "load image fail FileNotFoundException");
		        } catch (IOException e) {
		           // Log.i(Constants.TAG, "load image fail IOException");
		        }*/
			  String url = getApplicationContext().getFilesDir().toString() + "/teleplaysImages/" + teleplayNumber + ".jpg";
			 // File file = getApplicationContext().getFileStreamPath(imageName);
	            bitmap = BitmapFactory.decodeFile(url);
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			teleplayImage.setImageBitmap(result);
		}

    	
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		watch.setFocusable(true);
		watch.requestFocus();
	}
}
