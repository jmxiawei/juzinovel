package top.iscore.freereader.mode.setter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import top.iscore.freereader.mode.StatusBarUtil;

/**
 * statusbar的颜色
 * Created by xiaw on 2018/11/5.
 */
public class StatusBarSetter extends ViewSetter {
    Activity activity;
    boolean isDart = false;

    public StatusBarSetter(View targetView, int resId, boolean isDart, Activity activity) {
        super(targetView, resId);
        this.isDart = isDart;
        this.activity = activity;
    }

    @Override
    public void setValue(Resources.Theme newTheme, int themeId) {
        if (activity != null) {
            StatusBarUtil.setStatusTextColor(isDart, activity);
        }

    }
}
