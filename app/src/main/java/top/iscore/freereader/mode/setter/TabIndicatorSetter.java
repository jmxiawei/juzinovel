package top.iscore.freereader.mode.setter;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.view.View;

import top.iscore.freereader.R;


public class TabIndicatorSetter extends ViewSetter {



    public TabIndicatorSetter(View targetView, int resId) {
        super(targetView, resId);
    }

    @Override
    public void setValue(Resources.Theme newTheme, int themeId) {
        if(mView!=null){
            TabLayout tabLayout = (TabLayout) mView;
            tabLayout.setSelectedTabIndicatorColor(getColor(newTheme));
        }
    }
}
