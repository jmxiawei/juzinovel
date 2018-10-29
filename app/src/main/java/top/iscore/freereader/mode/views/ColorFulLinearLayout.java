package top.iscore.freereader.mode.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import top.iscore.freereader.mode.IBackgroundColorView;

/**
 * Created by xiaw on 2018/10/29.
 */

public class ColorFulLinearLayout extends LinearLayout implements IBackgroundColorView{
    public ColorFulLinearLayout(Context context) {
        super(context);
    }

    public ColorFulLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorFulLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getAttr() {
        return 0;
    }
}
