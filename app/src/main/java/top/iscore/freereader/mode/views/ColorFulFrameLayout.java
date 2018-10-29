package top.iscore.freereader.mode.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import top.iscore.freereader.mode.IBackgroundColorView;

/**
 * Created by xiaw on 2018/10/29.
 */

public class ColorFulFrameLayout extends FrameLayout implements IBackgroundColorView{
    public ColorFulFrameLayout(Context context) {
        super(context);
    }

    public ColorFulFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorFulFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getAttr() {
        return 0;
    }
}
