package xcvf.top.readercore.impl;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by xiaw on 2018/10/29.
 */

public class FullScreenHandler {


    public static final int DELAY_HIDE = 2000;
    public static final int ANIM_DELAY_HIDE = 200;

    AppCompatActivity mActivity;
    View contentView;
    View controllerView;
    Handler mHandler = new Handler(Looper.getMainLooper());

    public FullScreenHandler(AppCompatActivity mActivity, View contentView, View controllerView) {
        this.mActivity = mActivity;
        this.contentView = contentView;
        this.controllerView = controllerView;
    }

    private final Runnable mShowSettingRunnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            android.support.v7.app.ActionBar actionBar = mActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            controllerView.setVisibility(View.VISIBLE);
        }
    };

    /**
     *
     */
    private final Runnable mHideSettingRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };


    private void showSetting() {
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);


        // Schedule a runnable to display UI elements after a delay
        mHandler.removeCallbacks(mHideSettingRunnable);
        mHandler.postDelayed(mShowSettingRunnable, ANIM_DELAY_HIDE);

        delayHide(DELAY_HIDE);

    }

    private void hideSetting() {
        android.support.v7.app.ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        controllerView.setVisibility(View.GONE);
        mHandler.removeCallbacks(mShowSettingRunnable);
        mHandler.postDelayed(mHideSettingRunnable, ANIM_DELAY_HIDE);
    }


    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideSetting();
        }
    };


    private void delayHide(int mils) {
        mHandler.removeCallbacks(mHideRunnable);
        mHandler.postDelayed(mHideRunnable, mils);
    }

    public void hide() {
        delayHide(0);
    }

    public void check() {
        if (controllerView.getVisibility() != View.VISIBLE) {
            //显示设置界面
            showSetting();
        } else {
            //更新隐藏的时间
            delayHide(DELAY_HIDE);
        }
    }


}
