package top.iscore.freereader.mode.setter;

import android.content.res.Resources;
import android.view.View;

import top.iscore.freereader.R;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class FastScrollBarSetter extends ViewSetter {


    public FastScrollBarSetter(View targetView, int resId) {
        super(targetView, resId);
    }

    @Override
    public void setValue(Resources.Theme newTheme, int themeId) {

        if (mView != null) {
            VerticalRecyclerViewFastScroller scroller = (VerticalRecyclerViewFastScroller) mView;
            scroller.setBarColor(getColor(newTheme, R.attr.text_color));
            scroller.setHandleColor(getColor(newTheme, R.attr.text_second_color));
        }
    }
}
