package top.iscore.freereader.mode.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import top.iscore.freereader.mode.IBackgroundColorView;
import top.iscore.freereader.mode.IPrimaryBackgroundColorView;

/**
 * Created by xiaw on 2018/10/29.
 */

public class PrimaryColorFulFrameLayout extends FrameLayout implements IPrimaryBackgroundColorView{
    public PrimaryColorFulFrameLayout(Context context) {
        super(context);
    }

    public PrimaryColorFulFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PrimaryColorFulFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getAttr() {
        return 0;
    }
}
