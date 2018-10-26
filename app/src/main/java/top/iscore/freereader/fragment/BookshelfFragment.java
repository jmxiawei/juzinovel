package top.iscore.freereader.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.iscore.freereader.R;
import top.iscore.freereader.adapter.BookShelfAdapter;
import top.iscore.freereader.adapter.WhiteItemDivider;
import top.iscore.freereader.adapter.holders.BookHolder;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.mvp.presenters.BookShelfPresenter;
import top.iscore.freereader.mvp.view.BookShelfView;
import xcvf.top.readercore.ReaderActivity;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.User;

/**
 * 书架
 * Created by xiaw on 2018/9/18.
 */
public class BookshelfFragment extends MvpFragment<BookShelfView, BookShelfPresenter> implements BookShelfView, OnRecyclerViewItemClickListener<Book> {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.contentView)
    LinearLayout contentView;
    Unbinder unbinder;
    int page = 1;
    BookShelfAdapter mBookShelfAdapter;
    User mUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        unbinder = ButterKnife.bind(this, view);
        mUser = User.currentUser();
        presenter.attachView(this);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new WhiteItemDivider(2));
        mBookShelfAdapter = new BookShelfAdapter();
        recycler.setAdapter(mBookShelfAdapter);
        mBookShelfAdapter.setOnRecyclerViewItemClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(false);
            }
        });
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableAutoLoadMore(true);
        loadData(true);
    }


    @Override
    public BookShelfPresenter createPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(List<Book> data) {
        if (page == 1) {
            mBookShelfAdapter.setDataList(data);
        } else {
            mBookShelfAdapter.appendDataList(data);
        }
        refreshLayout.finishRefresh();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (pullToRefresh) {
            page = 1;
            refreshLayout.autoRefresh();
        }

        presenter.loadBookShelf(page, mUser.getUid());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Book item) {
        if (item.bookid != -1) {
            ReaderActivity.toReadPage(getActivity(), item);
        }
    }
}
