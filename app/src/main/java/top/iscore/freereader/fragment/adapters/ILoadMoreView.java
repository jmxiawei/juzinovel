/*
 *
 *         佛祖保佑       永无BUG
 *
 * Copyright (c) 2017 . All rights reserved
 *
 * Created by xiaw  On 17-9-15 下午5:05
 *
 * FileName : ILoadMoreView.java
 *
 * Last modified  17-4-19 下午1:52
 *
 */

package top.iscore.freereader.fragment.adapters;

/**
 * Created by xiaw on 2017/4/19 0019.
 */

public interface ILoadMoreView {


    int STATE_LOADING = 0;//正在加载
    int STATE_COMPLETE = 1;//完成
    int STATE_NOMORE = 2;//没有更多

    void setState(int state);

    int getState();

    void onLoading();

    void loadingComplete();

}
