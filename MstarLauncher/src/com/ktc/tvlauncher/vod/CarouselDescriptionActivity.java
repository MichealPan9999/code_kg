
package com.ktc.tvlauncher.vod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class CarouselDescriptionActivity extends Activity implements OnClickListener {
    private Button watch;
    private Button back;
    private ImageView movieImage;
    private TextView director;
    private TextView actor;
    private TextView description;
    private TextView movieName;
    private int movieNumber;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.carousel_description);
        findView();
        Init();
        setListener();
    }
    
    public void setListener(){
        watch.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void findView() {
        watch = (Button) findViewById(R.id.carousel_description_watch);
        back = (Button) findViewById(R.id.carousel_description_back);
        director = (TextView) findViewById(R.id.carousel_description_director_content);
        actor = (TextView) findViewById(R.id.carousel_description_actor_content);
        description = (TextView) findViewById(R.id.carousel_description_description_content);
        movieName = (TextView) findViewById(R.id.carousel_description_movie_name);
        movieImage = (ImageView) findViewById(R.id.carousel_description_image);
    }

    public void Init() {
        
        movieNumber = getIntent().getIntExtra("movieNumber", 0);
        SharedPreferences sp = getSharedPreferences(Constant.SAVE_TENCENT_APPSINFO, MODE_PRIVATE);
        String tx_actor = sp.getString("movie_viewPoint_" + movieNumber, "");
        String tx_description = sp.getString("movie_videoUrl_" + movieNumber, "");
        String tx_movieName = sp.getString("movie_name_" + movieNumber, "");
        

        actor.setText(tx_actor);
        movieName.setText(tx_movieName);
        description.setText("     " + tx_description);
        //movieName.setText(tx_movieName);
        new LoadImage().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carousel_description_back:
                finish();
                break;
            case R.id.carousel_description_watch:
                SharedPreferences sp = getSharedPreferences(Constant.SAVE_TENCENT_APPSINFO, MODE_PRIVATE);
                int id = sp.getInt("movie_id_" + movieNumber, 0);
                Log.i("test","id:"+id);
                Intent intent = new Intent("com.wasuali.action.programinfo");
            	intent.putExtra("Id", id);
            	intent.putExtra("Domain", "bs3-auth.sdk.wasu.tv");
            	intent.putExtra("IsFavorite", false);
            	startActivity(intent);
                break;
            default:
                break;
        }

    }
    class LoadImage extends AsyncTask<Void,Bitmap,Bitmap>{

		@Override
		protected Bitmap doInBackground(Void... arg0) {
			SharedPreferences sp = getSharedPreferences(Constant.SAVE_TENCENT_APPSINFO, MODE_PRIVATE);
			  String imageName = sp.getString("movie_picurl_" + movieNumber, "");
			  Bitmap bitmap =null;  
			 /* try {
		            FileInputStream fis = openFileInput(imageName);
		            bitmap = BitmapFactory.decodeStream(fis);
		            fis.close();
		            
		        } catch (FileNotFoundException e) {
		           // Log.i(Constants.TAG, "load image fail FileNotFoundException");
		        } catch (IOException e) {
		         //   Log.i(Constants.TAG, "load image fail IOException");
		        }*/
			  String url = getApplicationContext().getFilesDir().toString() + "/appsImages/" + imageName;
			 
	            bitmap = BitmapFactory.decodeFile(url);
	            //bitmap = decodeBitmap(url);
	            if (bitmap == null)
	            	Logger.d("AppFragment", "CarouselDescriptionActivity:bitmap == null");
	            else
	            	Logger.d("AppFragment", "CarouselDescriptionActivity:bitmap == not null");
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			movieImage.setImageBitmap(result);
		}

    	
    }
    public static final float DISPLAY_WIDTH = 200;
    public static final float DISPLAY_HEIGHT = 200;
    /**
     * 从path中获取图片信息
     * @param path
     * @return
     */
    private Bitmap decodeBitmap(String path){

    	  BitmapFactory.Options op = new BitmapFactory.Options();
    	  //inJustDecodeBounds 
    	  //If set to true, the decoder will return null (no bitmap), but the out…
    	  op.inJustDecodeBounds = true;
    	  Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
    	  //获取比例大小
    	  int wRatio = (int)Math.ceil(op.outWidth/DISPLAY_WIDTH);
    	  int hRatio = (int)Math.ceil(op.outHeight/DISPLAY_HEIGHT);
    	  //如果超出指定大小，则缩小相应的比例
    	  if(wRatio > 1 && hRatio > 1){
    	    if(wRatio > hRatio){
    	      op.inSampleSize = wRatio;
    	    }else{
    	      op.inSampleSize = hRatio;
    	    }
    	  }
    	  op.inJustDecodeBounds = false;
    	  bmp = BitmapFactory.decodeFile(path, op);
    	  return bmp;
    	}
}
