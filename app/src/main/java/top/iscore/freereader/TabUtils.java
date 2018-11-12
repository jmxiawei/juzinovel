package top.iscore.freereader;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * Created by xiaw on 2018/9/18.
 */

public class TabUtils {
    //
	public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip, int topDip, int bottomDip) {
		Class<?> tabLayout = tabs.getClass();
		Field tabStrip = null;
		try {
			tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		tabStrip.setAccessible(true);
		LinearLayout ll_tab = null;
		try {
			ll_tab = (LinearLayout) tabStrip.get(tabs);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		int left = (int) (getDisplayMetrics(context).density * leftDip);
		int right = (int) (getDisplayMetrics(context).density * rightDip);
		int top=(int) (getDisplayMetrics(context).density * topDip);
		int bottom=(int) (getDisplayMetrics(context).density * bottomDip);
		for (int i = 0; i < ll_tab.getChildCount(); i++) {
			View child = ll_tab.getChildAt(i);
			child.setPadding(0, 0, 0, 0);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
			params.leftMargin = left;
			params.rightMargin = right;
			params.bottomMargin=bottom;
			params.topMargin=top;
			child.setLayoutParams(params);
			child.invalidate();
		}
	}


    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

}
