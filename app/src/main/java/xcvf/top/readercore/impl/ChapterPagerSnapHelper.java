package xcvf.top.readercore.impl;

import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import xcvf.top.readercore.interfaces.OnPageChangedListener;

/**
 * Created by xiaw on 2018/10/25.
 */

public class ChapterPagerSnapHelper extends PagerSnapHelper {

    OnPageChangedListener onPageChangedListener;

    public OnPageChangedListener getOnPageChangedListener() {
        return onPageChangedListener;
    }

    public ChapterPagerSnapHelper setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener;
        return this;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int target = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        if (onPageChangedListener != null) {
            onPageChangedListener.onPageSelected(target);
        }
        return target;
    }
}
