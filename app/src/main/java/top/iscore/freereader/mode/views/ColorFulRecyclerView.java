package top.iscore.freereader.mode.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import top.iscore.freereader.mode.IBackgroundColorView;
import top.iscore.freereader.mode.IRecyclerView;

/**
 * Created by xiaw on 2018/10/29.
 */

public class ColorFulRecyclerView extends RecyclerView implements IRecyclerView{
    public ColorFulRecyclerView(Context context) {
        super(context);
    }

    public ColorFulRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorFulRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
