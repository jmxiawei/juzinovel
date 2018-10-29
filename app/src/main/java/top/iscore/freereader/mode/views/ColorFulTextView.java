package top.iscore.freereader.mode.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import top.iscore.freereader.mode.ITextColorView;

public class ColorFulTextView extends AppCompatTextView implements ITextColorView {

    Activity mActivity;

    public ColorFulTextView(Context context) {
        this(context,null);
    }

    public ColorFulTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        mActivity = (Activity) context;
    }

    public ColorFulTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
