package com.padeoe.virtualvolume;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by padeoe on 2015/6/30.
 */
public class VolumButton {
    static VolumButton volumButton;
    Context context;
    Context applicationContext;
    AudioManager audioManager;
    WindowManager wm;
    WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
    private int height=150;
    private int width=200;
    private static int screenlong;
    private static int screenshort;
    private static int longgap;
    private static int shortgap;
    public static Button fb;

    public static VolumButton getInstance(Context context,Context applicationContext){
        if(volumButton==null){
            System.out.println("正在创建按钮");
            volumButton=new VolumButton(context,applicationContext);
        }
        else
            System.out.println("已存在按钮");
        return volumButton;
    }
    private VolumButton(Context context,Context applicationContext){
        this.context=context;
        this.applicationContext=applicationContext;
        init_constant();
        init();
    }
    private void init_constant(){
        audioManager= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        wm=(WindowManager)applicationContext.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenlong=size.y;
        screenshort=size.x;
        longgap=(2048)/(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)+1);
        shortgap=(1536)/(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)+1);
/*        System.out.println("屏幕长"+screenlong+" 单位长："+longgap);
        System.out.println("屏幕宽"+screenshort+"单位短"+shortgap);*/
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        System.out.println("屏幕长"+height);
        System.out.println("屏幕宽"+width);

    }
    private void init(){
        fb=new Button(context);
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        fb.setText("音量");
        fb.getBackground().setAlpha(0);
        wmParams.type=2002;
        wmParams.format=1;
        wmParams.width=width;
        wmParams.height=height;
        wmParams.x=0;
        wmParams.y=getScreenVHeight(context)-height;
        wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        fb.setOnTouchListener(new MyButtonMoveListener(wmParams,wm,fb));
        wmParams.flags=40;
        wm.addView(fb, wmParams);  //创建View
     //   wm.removeView(fb);
    }
    class MyButtonMoveListener implements View.OnTouchListener {
        int x=0;int y=0;
        int now;
        Button button;
        WindowManager.LayoutParams wmParams;
        WindowManager wm;
        public MyButtonMoveListener(WindowManager.LayoutParams wmParams,WindowManager wm,Button button){
            this.wmParams=wmParams;
            this.wm=wm;
            this.button=button;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    button.getBackground().setAlpha(150);
                    x= (int) event.getX();
                    y =(int) event.getY();
                    Log.i("x",String.valueOf(x));
                    Log.i("y",String.valueOf(y));
                    Log.i("ACTION_DOWN", "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    wmParams.y=(int) event.getRawY()-y;
                    System.out.println( event.getRawY());
                    break;
                case MotionEvent.ACTION_UP:
                    button.getBackground().setAlpha(0);
                    break;
            }
            int current=(int)(event.getRawY())/getGap(context);
            if(now!=current){
                System.out.println("原始y坐标为"+event.getRawY());
                System.out.println("当前单位长度是"+getGap(context));
                System.out.println("当前单位数是"+(event.getRawY())/getGap(context));
                now=current;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15-now, AudioManager.FLAG_SHOW_UI);
            }
            button.setText(String.valueOf(15-now));
            wm.updateViewLayout(v, wmParams);
            return false;
        }
    }
    public static int getScreenVHeight(Context context){
        if(context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            return screenshort;
        }
        else{
            return screenlong;
        }
    }
    public static int getScreenHWidth(Context context){
        if(context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            return screenlong;
        }
        else{
            return screenshort;
        }
    }
    public static int getGap(Context context){
        if(context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            System.out.println("横屏");
            return shortgap;
        }
        else{
            System.out.println("竖屏");
            return longgap;
        }
    }
}
