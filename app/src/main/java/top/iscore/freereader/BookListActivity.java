package top.iscore.freereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.iscore.freereader.adapter.SearchBookAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.SwitchModeListener;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import top.iscore.freereader.mvp.presenters.SearchPresenter;
import top.iscore.freereader.mvp.view.SearchView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 书籍列表，根据不同的询条件
 */
public class BookListActivity extends MvpActivity<SearchView, SearchPresenter> implements SearchView, SwitchModeListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    SearchBookAdapter mBookAdapter;
    @BindView(R.id.activity_content)
    LinearLayout activityContent;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    SwitchModeHandler switchModeListener;
    int page = 1;
    String title;
    int type;
    String params;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    /**
     * 查询列表
     *
     * @param activity
     * @param title
     * @param type
     * @param params
     */
    public static void toBookList(Activity activity, String title, int type, String params) {
        Intent intent = new Intent(activity, BookListActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        intent.putExtra("params", params);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        presenter.attachView(this);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBookAdapter = new SearchBookAdapter();
        recycler.setAdapter(mBookAdapter);

        tvTitle.setText(title);
        mBookAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Book>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Book item) {
                BookDetailActivity.toBookDetail(BookListActivity.this, item.extern_bookid);
            }
        });
        switchModeListener = new SwitchModeHandler(this, this);
        switchModeListener.onCreate();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                load();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                load();
            }
        });
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(true);

        load();
    }

    private void load() {

        if (type == 0) {
            //根绝类别搜索
            presenter.searchBook(null, params, null, page);
        } else if (type == 1) {
            presenter.searchBook(params, null, null, page);
        } else if (type == 2) {
            //排行榜
            presenter.searchBook(null, null, params, page);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        changeMode();
    }

    private void changeMode() {

        new Colorful.Builder(this).
                setter(new ViewBackgroundColorSetter(activityContent, R.attr.colorPrimary)).
                setter(new ViewBackgroundColorSetter(llTitle, R.attr.colorAccent)).
                setter(new ViewGroupSetter(recycler, R.attr.colorPrimary).
                        childViewTextColor(R.id.tv_name, R.attr.text_color).
                        childViewTextColor(R.id.tv_latest, R.attr.text_second_color)).
                create().
                setTheme(ModeProvider.getCurrentModeTheme());
    }

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    public void onLoad(List<Book> books) {
        if (page == 1) {
            mBookAdapter.setDataList(books);
        } else {
            mBookAdapter.appendDataList(books);
        }
        page++;
        dismissLoading();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void showLoading() {
        llLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        llLoading.setVisibility(View.GONE);
    }

    @Override
    public void switchMode(Mode mode) {
        changeMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        switchModeListener.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void getIntentData() {
        title = getIntent().getStringExtra("title");
        type = getIntent().getIntExtra("type", 0);
        params = getIntent().getStringExtra("params");
    }
}
