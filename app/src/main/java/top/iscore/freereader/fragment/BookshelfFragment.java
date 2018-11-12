package top.iscore.freereader.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.iscore.freereader.R;
import top.iscore.freereader.SwitchModeHandler;
import top.iscore.freereader.adapter.BookShelfAdapter;
import top.iscore.freereader.adapter.WhiteItemDivider;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.SwitchModeListener;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import top.iscore.freereader.mvp.presenters.BookShelfPresenter;
import top.iscore.freereader.mvp.view.BookShelfView;
import xcvf.top.readercore.ReaderActivity;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.daos.BookDao;
import xcvf.top.readercore.daos.DBManager;
import xcvf.top.readercore.styles.ModeProvider;
import xcvf.top.readercore.views.BookShelfOptionDialog;

/**
 * 书架
 * Created by xiaw on 2018/9/18.
 */
public class BookshelfFragment extends MvpFragment<BookShelfView, BookShelfPresenter> implements BookShelfView, OnRecyclerViewItemClickListener<Book>, SwitchModeListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    BookShelfAdapter mBookShelfAdapter;
    User mUser;
    SwitchModeHandler switchModeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        unbinder = ButterKnife.bind(this, view);
        mUser = User.currentUser();
        presenter.attachView(this);
        initViews(view);
        switchModeListener = new SwitchModeHandler(this, getActivity());
        switchModeListener.onCreate();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMode();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            LogUtils.e("--------------------onHiddenChanged--------------");
        }
    }

    private void updateMode() {
        LogUtils.e("--------------------updateMode--------------" + R.id.tv_name + "-----" + R.id.tv_chapter_name);
        new Colorful.Builder(this)
                .backgroundColor(R.id.fragment_content, R.attr.colorPrimary)
                .setter(new ViewGroupSetter(recycler, R.attr.colorPrimary)
                        .childViewBgColor(R.id.book_content, R.attr.colorPrimary)
                        .childViewTextColor(R.id.tv_name, R.attr.text_color)
                        .childViewTextColor(R.id.tv_chapter, R.attr.text_second_color)
                        .childViewTextColor(R.id.tv_add, R.attr.text_color))
                .create()
                .setTheme(ModeProvider.getCurrentModeTheme());
    }

    private void initViews(View view) {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new WhiteItemDivider(2));
        mBookShelfAdapter = new BookShelfAdapter();
        recycler.setAdapter(mBookShelfAdapter);
        mBookShelfAdapter.setOnRecyclerViewItemClickListener(this);
        mBookShelfAdapter.setOnRecyclerViewItemLongClickListener(new OnRecyclerViewItemClickListener<Book>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Book item) {

                BookShelfOptionDialog dialog = new BookShelfOptionDialog();
                dialog.setBook(item);
                dialog.show(getChildFragmentManager(),"BookShelfOptionDialog");

            }
        });
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
        List<Book> books = DBManager.getDaoSession().getBookDao().queryBuilder().where(BookDao.Properties.Userid.eq(mUser.getUid()))
                .orderDesc(BookDao.Properties.Update_time).list();
        mBookShelfAdapter.setDataList(books);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(List<Book> data) {
        if (data != null) {
            DBManager.getDaoSession().getBookDao().insertOrReplaceInTx(data);
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        showContent();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (pullToRefresh) {
            refreshLayout.autoRefresh();
        }
        presenter.loadBookShelf(mUser.getUid());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        switchModeListener.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Book item) {
        if (item.bookid != -1) {
            ReaderActivity.toReadPage(getActivity(), item);
        }
    }

    @Override
    public void switchMode(Mode mode) {
        updateMode();
    }

    @Override
    public void onLoadBookDetail(Book book) {

    }

    @Override
    public void onLoadAllCate(List<Category> categories) {

    }
}
