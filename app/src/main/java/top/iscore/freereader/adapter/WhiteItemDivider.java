/*
 *
 *         佛祖保佑       永无BUG
 *
 * Copyright (c) 2017 . All rights reserved
 *
 * Created by xiawei  On 17-9-12 上午8:06
 *
 * FileName : WhiteItemDivider.java
 *
 * Last modified  17-9-12 上午8:06
 *
 */

package top.iscore.freereader.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 分割线
 * Created by xiawei on 2017/9/12.
 */

public class WhiteItemDivider extends RecyclerView.ItemDecoration {

    private int divider = 4;

    public WhiteItemDivider() {
        divider = 4;
    }

    public WhiteItemDivider(int divider) {
        this.divider = divider;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, this.divider);
    }
}
