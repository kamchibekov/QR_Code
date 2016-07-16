package com.example.kamch.qrcode;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.backendless.Backendless;
import com.google.zxing.WriterException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridView;
    private NumberPicker np;
    private Bitmap qrImage;
    private QR_Code qrCode;
    GridLayout layout;
    private ArrayList<String> listId = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backendless.initApp(this, "92632DE0-DE22-B967-FF1F-70DB5853FA00", "9BA799D1-B4A5-B254-FF5F-B34868CBBD00", "v1");

        layout = (GridLayout) findViewById(R.id.gridView);
        final Button qrBut = (Button) findViewById(R.id.qrBut);
        final Button save = (Button) findViewById(R.id.save);
        gridView = (GridLayout) findViewById(R.id.gridView);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMaxValue(20);
        np.setMinValue(0);


//------------------------------------------------------------------------------------
        qrBut.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                qrCode = new QR_Code();

                layout.removeAllViews();

                final int num = np.getValue();
                Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int j = 0, k =0;
                        for (int i = 0; i < num; i++) {
                            Codes codes = new Codes();
                            if (np.getValue() != 0) {
                                codes.setNum(np.getValue());
                            }
                            Codes newObj = Backendless.Persistence.of(Codes.class).save(codes);
                            if(newObj != null){
                                listId.add(i,newObj.getObjectId());
                                Backendless.Persistence.of(Codes.class).remove(newObj);

                                try {
                                    qrImage = qrCode.encodeAsBitmap(listId.get(i));
                                    Bitmap bt = qrCode.bitmapText(qrImage, listId.get(i));
                                    ImageView imageView = new ImageView(MainActivity.this);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    imageView.setImageBitmap(bt);
                                    layout.addView(imageView);

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }

                });

                thread.start();
                try {
                    thread.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                }


        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = qrCode.getBitmap(gridView);
                qrCode.saveChart(bitmap, gridView.getMeasuredHeight(), gridView.getMeasuredWidth());


            }
        });


    }

}