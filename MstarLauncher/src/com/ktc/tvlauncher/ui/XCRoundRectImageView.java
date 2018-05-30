package com.ktc.tvlauncher.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * 自定义的圆角矩形ImageView，可以直接当组件在布局中使用。
 * @author 
 *
 */
public class XCRoundRectImageView extends ImageView{

    private Paint paint;
    
    public XCRoundRectImageView(Context context) {  
        this(context,null);  
    }  
  
    public XCRoundRectImageView(Context context, AttributeSet attrs) {  
        this(context, attrs,0);  
    }  
  
    public XCRoundRectImageView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle); 
        paint  =new Paint( Paint.ANTI_ALIAS_FLAG);//去锯齿
    }  
  
    /**
     * 绘制圆角矩形图片
     * @author caizhiming
     */
    @Override  
    protected void onDraw(Canvas canvas) {  
  
        Drawable drawable = getDrawable();  
        if (null != drawable) {  
        //如果图片没有获取到处理
        	try {
        	      Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();  
                  Bitmap b = getRoundBitmap(bitmap, 20);  
                  final Rect rectSrc = new Rect(0, 0, b.getWidth()+10, b.getHeight()+10);  
                  final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
                  paint.reset();  
                  canvas.drawBitmap(b, rectSrc, rectDest, paint); 
			} catch (Exception e) {
				Log.e("XCRoundRectImageView", "image load error");
				 super.onDraw(canvas);  
			}
       
  
        } else {  
            super.onDraw(canvas);  
        }  
    }  
  
    /**
     * 获取圆角矩形图片方法
     * @param bitmap
     * @param roundPx,一般设置成14
     * @return Bitmap
     * @author 
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {  
       //创建和原图一样大小的Bitmap
    	Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
                bitmap.getHeight(), Config.ARGB_8888);  
      //创建带有位图的画布
    	Canvas canvas = new Canvas(output);  
          
        final int color = 0xff424242;
       //创建和原始图片一样大小的矩形
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);
     //去锯齿
        paint.setAntiAlias(true);  
       
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        
        //画和原始图片一样大小的圆角矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        //###########设置交互式模式
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
        
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  
        
        
    }  
}  