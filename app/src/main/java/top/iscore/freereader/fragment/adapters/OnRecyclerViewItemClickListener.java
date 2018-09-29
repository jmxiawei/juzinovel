/*
 *
 *         佛祖保佑       永无BUG
 *
 * Copyright (c) 2017 . All rights reserved
 *
 * Created by xiaw  On 17-9-25 下午2:56
 *
 * FileName : OnaRecyclerViewItemClickListener.java
 *
 * Last modified  17-9-25 下午2:56
 *
 */

package top.iscore.freereader.fragment.adapters;

/**
 * Created by xiaw on 2017/9/25 0025.
 */

public interface OnRecyclerViewItemClickListener<T> {

    void onRecyclerViewItemClick(CommonViewHolder holder, int position, T item);
}
