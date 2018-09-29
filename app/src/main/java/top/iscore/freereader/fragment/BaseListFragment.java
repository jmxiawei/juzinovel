/*
 *
 *         佛祖保佑       永无BUG
 *
 * Copyright (c) 2017 . All rights reserved
 *
 * Created by xiawei  On 17-9-12 上午7:07
 *
 * FileName : SamirBaseListFragment.java
 *
 * Last modified  17-9-12 上午5:00
 *
 */

package top.iscore.freereader.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnReachBottomListener;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;


/**
 * 包含一个 RecyclerView id 必须为 R.id.recycler
 * Created by xiaw on 2017/8/30 0030.
 */
public abstract class BaseListFragment<T> extends Fragment {


    /**
     * id 必须为R.id.recycler
     */
    protected RecyclerView mRecycleView;

    protected SmartRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initRecyclerView(view);
        return view;
    }




    private OnReachBottomListener onReachBottomListener = new OnReachBottomListener() {
        @Override
        public void onReachBottom() {
            onReachBottomLoad();
        }
    };



    public void onReachBottomLoad() {

    }

    public void onRefreshLoad() {

    }

    /**
     * 初始化
     */
    public void initRecyclerView(View view) {
        swipeRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    onRefreshLoad();
                }
            });
            swipeRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    onReachBottomLoad();
                }
            });
        }
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecycleView.setLayoutManager(getLayoutManager());
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setAdapter(getAdapter());
        mAdapter.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
        getAdapter().setReachBottomListener(onReachBottomListener);

    }

    private final OnRecyclerViewItemClickListener<T> onRecyclerViewItemClickListener = new OnRecyclerViewItemClickListener<T>() {
        @Override
        public void onRecyclerViewItemClick(CommonViewHolder holder, int position, T item) {
            onItemClick(holder, position, item);
        }
    };

    /**
     * 列表item 点击事件
     * @param holder
     * @param position
     * @param item
     */
    public void onItemClick(CommonViewHolder holder, int position, T item) {

    }

    /**
     * 子类 只需要实现这个方法 就ok
     *
     * @param parent
     * @param viewType
     * @param p
     * @return
     */
    public abstract CommonViewHolder getHolderCreateByViewGroupAndType(ViewGroup parent, int viewType, Object... p);


    private BaseRecyclerAdapter<T> mAdapter = new BaseRecyclerAdapter<T>() {
        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return vhViewHolderCreator;
        }
    };

    /**
     * holder
     */
    private ViewHolderCreator vhViewHolderCreator = new ViewHolderCreator() {
        @Override
        public CommonViewHolder<T> createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
            return getHolderCreateByViewGroupAndType(parent, viewType, p);
        }
    };

    /**
     * 返回给列表设置的adapter
     *
     * @return
     */
    protected BaseRecyclerAdapter<T> getAdapter() {
        return mAdapter;
    }

    /**
     * 获取 LayoutManager 默认垂直布局
     *
     * @return
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }


}
