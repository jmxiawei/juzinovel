package top.iscore.freereader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.adapter.SearchBookAdapter;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.SwitchModeListener;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import top.iscore.freereader.mvp.presenters.SearchPresenter;
import top.iscore.freereader.mvp.view.SearchView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.SearchWord;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 搜索
 * Created by xiaw on 2018/11/2.
 */
public class SearchActivity extends BaseActivity<SearchView, SearchPresenter> implements SearchView, SwitchModeListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.view_search)
    android.support.v7.widget.SearchView viewSearch;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    SearchBookAdapter mBookAdapter;
    android.support.v7.widget.SearchView.SearchAutoComplete searchAutoComplete;
    @BindView(R.id.activity_content)
    LinearLayout activityContent;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    SwitchModeHandler switchModeListener;
    int page = 1;
    @BindView(R.id.word_history)
    RecyclerView wordHistory;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        presenter.attachView(this);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBookAdapter = new SearchBookAdapter();
        recycler.setAdapter(mBookAdapter);
        viewSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                load();
            }
        });
        searchAutoComplete = viewSearch.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.text_gray_light));
        mBookAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Book>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Book item) {
                BookDetailActivity.toBookDetail(SearchActivity.this, item.bookid);
            }
        });
        switchModeListener = new SwitchModeHandler(this, this);
        switchModeListener.onCreate();
        viewSearch.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                page = 1;
                load();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        viewSearch.setIconifiedByDefault(true);
        viewSearch.setFocusable(true);
        viewSearch.setIconified(false);
        viewSearch.requestFocusFromTouch();
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
        loadHistoryWord();
    }

    private BaseRecyclerAdapter<SearchWord> searchWordBaseRecyclerAdapter = new BaseRecyclerAdapter<SearchWord>() {
        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return new ViewHolderCreator() {
                @Override
                public CommonViewHolder<SearchWord> createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                    return new CommonViewHolder<SearchWord>(parent.getContext(), parent, R.layout.item_search_word) {
                        @Override
                        public void bindData(SearchWord searchWord, int position) {
                            TextView tv = itemView.findViewById(R.id.tv_name);
                            tv.setText(searchWord.getWord());
                        }
                    };
                }
            };
        }
    };

    private void loadHistoryWord() {
        wordHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<SearchWord> list = SearchWord.getList();
        searchWordBaseRecyclerAdapter.setDataList(list);
        wordHistory.setAdapter(searchWordBaseRecyclerAdapter);
        searchWordBaseRecyclerAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<SearchWord>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, SearchWord item) {
                viewSearch.setQuery(item.getWord(), true);
            }
        });
    }

    private void load() {

        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (im != null) {
            View view = getCurrentFocus();
            if (view != null) {
                im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
        String text = viewSearch.getQuery().toString();
        if (!TextUtils.isEmpty(text)) {
            SearchWord word = new SearchWord(text, System.currentTimeMillis());
            word.save();
            presenter.searchBook(viewSearch.getQuery().toString(), 0, 0, page);
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
        if (books != null && books.size() > 0) {
            if (page == 1) {
                mBookAdapter.setDataList(books);
            } else {
                mBookAdapter.appendDataList(books);
            }
            page++;
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
            llSearch.setVisibility(View.GONE);
        } else {
            if (page == 1) {
                llEmpty.setVisibility(View.VISIBLE);
            }
            refreshLayout.finishLoadMoreWithNoMoreData();
            refreshLayout.finishRefresh();
        }
        dismissLoading();
    }

    @Override
    public void showLoading() {
        llSearch.setVisibility(View.GONE);
        llEmpty.setVisibility(View.GONE);
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

    @OnClick(R.id.tv_clear)
    public void onViewClicked() {
        SearchWord.deleteAll();
        loadHistoryWord();
    }
}
