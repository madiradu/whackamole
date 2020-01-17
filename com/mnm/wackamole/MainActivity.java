package com.mnm.wackamole;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static android.graphics.Bitmap.createScaledBitmap;

public class MainActivity extends Activity {
    static int dim; static int radX; static int radY;static int nrBuc=108;
    static int N=0; static int score=0; static String tag;
    static BitmapDrawable daisy; static BitmapDrawable white; static BitmapDrawable down; static BitmapDrawable mole; static DisplayMetrics displayMetrics;
    static int tries=45;
    static boolean initialized=false;
    static long lastTime;
    static FragmentManager fragMan;
    long mLastClickTime=0;
    static CountDownTimer cdt;
    static int lastTag;
    static int diff =800;
    static GridLayout layout;

    public void initialize(){
        radX=(int)Math.sqrt(nrBuc/12)*3;
        radY=(int)Math.sqrt(nrBuc/12)*4;
        displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dim=(displayMetrics.heightPixels -displayMetrics.widthPixels/3)/radY <(displayMetrics.widthPixels-10)/radX?(displayMetrics.heightPixels -displayMetrics.widthPixels/3)/radY:(displayMetrics.widthPixels-10)/radX;
        if(((float)displayMetrics.widthPixels / (float)displayMetrics.heightPixels)>0.7f) dim=(int)(dim*0.8);

        InputStream fis = null;
        try {
            fis = MainActivity.this.getAssets().open("mole"+".jpg", AssetManager.ACCESS_STREAMING);


        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bt = BitmapFactory.decodeStream(fis);
        Bitmap dt = createScaledBitmap(
                bt
                , dim, dim, false);
        daisy = new BitmapDrawable(dt);
        try {
            fis = MainActivity.this.getAssets().open("white.png", AssetManager.ACCESS_STREAMING);


        } catch (IOException e) {
            e.printStackTrace();
        }

         bt = BitmapFactory.decodeStream(fis);
         dt = createScaledBitmap(
                bt
                , dim, dim, false);
        white = new BitmapDrawable(dt);
        try {
            fis = MainActivity.this.getAssets().open("red.jpg", AssetManager.ACCESS_STREAMING);


        } catch (IOException e) {
            e.printStackTrace();
        }

        bt = BitmapFactory.decodeStream(fis);
        dt = createScaledBitmap(
                bt
                , dim, dim, false);
        down = new BitmapDrawable(dt);
        try {
            fis = MainActivity.this.getAssets().open("mole"+new Integer((int) (System.currentTimeMillis()%15)).toString()+".jpg", AssetManager.ACCESS_STREAMING);


        } catch (IOException e) {
            e.printStackTrace();
        }
         bt = BitmapFactory.decodeStream(fis);
         dt = createScaledBitmap(
                bt
                , radX*dim, radY*dim, false);
        mole = new BitmapDrawable(dt)
        ;

        cdt = new CountDownTimer(diff, diff) {
            @Override
            public void onFinish() {
                /
                N++;

                if((N ==tries)) {
                    LevelDialog ld = new LevelDialog();
                    ld.setCancelable(false);
                    ld.show(MainActivity.fragMan, "score");
                }

                int j = (int) (System.currentTimeMillis() % 64);
                lastTime = System.currentTimeMillis();


                View dV = layout.findViewWithTag(j);
                View sV = layout.findViewWithTag(lastTag);
                lastTag = j;

                    sV.setBackground(white);
                sV.setAlpha(0);
                    dV.setBackground(daisy);
                    dV.setAlpha(1);
        
                this.start();
            }

            @Override
            public void onTick(long milliesUntilFinished) {
            }
        };


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(	getWallpaper());
        int orientation = getRequestedOrientation();
        fragMan = getFragmentManager();

        if(initialized==false){initialized=true; initialize();}

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final GridLayout layout = new GridLayout(this);
        MainActivity.layout = layout;
        layout.setColumnCount(radX);
        layout.setRowCount(radY);
        if(((float)displayMetrics.widthPixels / (float)displayMetrics.heightPixels)>0.7f) dim=(int)(dim*0.8);

        int i=0;
        MainActivity.tag = new Integer((int) (System.currentTimeMillis()%64)).toString();
        lastTag = Integer.parseInt(tag);
        while(i<nrBuc){
            View v = new ImageView(this);
            v.setTag(i);

            if(i==Integer.parseInt(MainActivity.tag))
            {
                lastTag=i;
                v.setBackground(daisy);
                v.setAlpha(1);
            }
            else {v.setBackground(white);v.setAlpha(0);};
          
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cdt.cancel();
                    N++;
                    int delay=0;
                    if(v.getTag().equals(lastTag)) {
                        lastTime = System.currentTimeMillis();

                        View dV = layout.findViewWithTag(v.getTag());

                        dV.setBackground(down);dV.setAlpha(1);
                        score++;
                        delay = 700;
                    }

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if((N ==tries)) {
                                        LevelDialog ld = new LevelDialog();
                                        ld.setCancelable(false);
                                        ld.show(MainActivity.fragMan, "score");
                                    }

                                    lastTime = System.currentTimeMillis();
                                    int j = (int) (System.currentTimeMillis() % 64);

                                    View dV = layout.findViewWithTag(j);
                                    View sV = layout.findViewWithTag(lastTag);
                                    lastTag = j;

                                    sV.setBackground(white);sV.setAlpha(0);
                                    dV.setBackground(daisy); dV.setAlpha(1);

                                    cdt.start();
                                }
                            }
                            , delay );
                }
            });

            ((ImageView) v).setMinimumHeight(dim);((ImageView) v).setMinimumWidth(dim);
            layout.addView(v);
            i++;
        }

        layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        cdt.start();
                    }
                }
        );
        LinearLayout lay = new LinearLayout(this);
        lay.setBackgroundColor(Color.WHITE);
        ((View)layout).setBackground(mole);layout.setAlpha((float) 0.2);
        lay.addView(layout);

        this.addContentView(lay, layoutParam);

    }

    @Override
    public void onBackPressed() {;}

}

