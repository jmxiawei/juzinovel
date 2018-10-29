package top.iscore.freereader.mode.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import top.iscore.freereader.R;
import top.iscore.freereader.mode.IBackgroundColorView;

/**
 * Created by xiaw on 2018/10/29.
 */

public class ColorFulToolBar extends TabLayout implements IBackgroundColorView{
    public ColorFulToolBar(Context context) {
        super(context);
    }

    public ColorFulToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorFulToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getAttr() {
        return 0;
    }
}
