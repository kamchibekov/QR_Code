package com.example.kamch.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.widget.GridLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kamch on 7/15/2016.
 */
public class QR_Code {


    public void saveChart(Bitmap getbitmap, float height, float width){
        File folder = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "QRCodeFolder");
        boolean success = false;
        if (!folder.exists())
        {
            success = folder.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File file = new File(folder.getPath() + File.separator + "/"+timeStamp+".png");

        if ( !file.exists() )
        {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream ostream = null;
        try
        {
            ostream = new FileOutputStream(file);

            System.out.println(ostream);

            Bitmap well = getbitmap;
            Bitmap save = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Canvas now = new Canvas(save);
            now.drawRect(new Rect(0,0,(int) width, (int) height), paint);
            now.drawBitmap(well,
                    new Rect(0,0,well.getWidth(),well.getHeight()),
                    new Rect(0,0,(int) width, (int) height), null);

            if(save == null) {
                System.out.println("NULL bitmap save\n");
            }
            save.compress(Bitmap.CompressFormat.PNG, 100, ostream);

        }catch (NullPointerException e){e.printStackTrace();}

        catch (FileNotFoundException e){ e.printStackTrace();}

        catch (IOException e){e.printStackTrace();}}

    public Bitmap getBitmap(GridLayout layout){
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false);

        return bmp;
    }
    //------------------------------------------------------------------------------------
   public Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 170, 170, null);
        } catch (IllegalArgumentException iae) {
            // колдонулбаган формат
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 170, 0, 0, w, h);
        return bitmap;
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }


    public Bitmap bitmapText(Bitmap bmp1,String text) {
        Bitmap bStr = textAsBitmap(" "+text+" ",8, Color.BLACK);
        Bitmap newBmp  = Bitmap.createBitmap(170, 170, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas( newBmp );
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bStr, 0, 155, null);
        return newBmp;
    }
}
