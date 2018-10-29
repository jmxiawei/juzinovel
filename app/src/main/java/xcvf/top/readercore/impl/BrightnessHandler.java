package xcvf.top.readercore.impl;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * 屏幕亮度
 */
public class BrightnessHandler {


    /**
     * 获取系统屏幕亮度
     * @param context
     * @return
     */
    public static int getScreenBrightness(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        int defVal = 125;
        return Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, defVal);
    }

    /**
     * 设置当前屏幕的亮度
     * @param activity
     * @param brightness
     */
    public static void setWindowBrightness(Activity activity,int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

}
