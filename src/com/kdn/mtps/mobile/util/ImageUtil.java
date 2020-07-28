package com.kdn.mtps.mobile.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.media.ExifInterface;

public class ImageUtil {

	public static int getImageOrientation(String paramString)
    {
		int i = 0;
      if (paramString == null)
        return 0;
      
      try {
    	  int j = new ExifInterface(paramString).getAttributeInt("Orientation", 0);
          if (j == 6)
            i = 90;
          else if (j == 3)
        	  i = 180;
          else if (j == 8)
        	  i = 270;
      } catch (Exception e){
    	  e.printStackTrace();
      }
      
      return i;
    }
	
	public static Bitmap getRotatedBitmap(Bitmap paramBitmap, float paramFloat)
	  {
	    try
	    {
	      Matrix localMatrix = new Matrix();
	      localMatrix.postRotate(paramFloat);
	      Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
	      return localBitmap;
	    }
	    catch (Exception localException)
	    {
	      return null;
	    }
	  }
	
	public static Bitmap getBitmap(String filePath, int sampleSize) {
		BitmapFactory.Options options=new BitmapFactory.Options();
		
		options.inJustDecodeBounds = false;
		options.inSampleSize= sampleSize;
		options.inPurgeable=true; //purgeable to disk
		
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
		Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return result;
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 10;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);


        return output;

    }
}
