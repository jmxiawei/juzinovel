package top.iscore.freereader.mode;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by xiaw on 2018/10/29.
 */

public class ColorFulDarkLinearLayout extends LinearLayout implements IDarkBackgroundColorView{
    public ColorFulDarkLinearLayout(Context context) {
        super(context);
    }

    public ColorFulDarkLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorFulDarkLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getAttr() {
        return 0;
    }
}
