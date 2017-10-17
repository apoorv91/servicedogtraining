package com.SDT.servicedog;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Created by laitkor on 11/08/16.
 */

public class Imageclass {

    public static Bitmap  bitmapImageValue = null;
    public static int image_getdata_flag=0;


    public static Bitmap ResizeBitmapImage(){

        Bitmap targetBitmap = null;
        AppCompatIntermediateActivity.taskTimerFlag = 0;

        if (bitmapImageValue != null) {

            int targetWidth = 200;
            int targetHeight = 200;
            targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            BitmapShader shader;
            shader = new BitmapShader(bitmapImageValue, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);
            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth) / 2,
                    ((float) targetHeight) / 2,
                    (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            paint.setStyle(Paint.Style.STROKE);
            canvas.clipPath(path);
            Bitmap sourceBitmap = bitmapImageValue;
            canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), null);

            // _profileImageView.setImageBitmap(targetBitmap);   //set the circular image to your imageview
            //captureImageView.setImageBitmap(targetBitmap);
            //System.out.println("gallery image value is: " + targetBitmap);
        }
        return targetBitmap;
    }

}
