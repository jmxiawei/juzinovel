package top.iscore.freereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import top.iscore.freereader.adapter.PriorityBookAdapter;
import top.iscore.freereader.fragment.LoadingFragment;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.TextColorSetter;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import top.iscore.freereader.mvp.presenters.BookShelfPresenter;
import top.iscore.freereader.mvp.view.BookShelfView;
import xcvf.top.readercore.ReaderActivity;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookCate;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.services.DownloadIntentService;
import xcvf.top.readercore.styles.ModeProvider;
import xcvf.top.readercore.views.ContentDialog;
import xcvf.top.readercore.views.LoginDialog;

/**
 * 书籍详情
 */
public class BookDetailActivity extends MvpActivity<BookShelfView, BookShelfPresenter> implements BookShelfView, UserInfoChangedHandler.OnUserChanged {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_setting_top)
    LinearLayout llSettingTop;
    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.tv_book_name)
    TextView tvBookName;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_cate)
    TextView tvCate;
    @BindView(R.id.tv_latest)
    TextView tvLatest;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_interest)
    TextView tvInterest;
    @BindView(R.id.tv_more)
    TextView tvMore;
    PriorityBookAdapter priorityBookAdapter;
    @BindView(R.id.tv_pre_new_chapter)
    TextView tvPreNewChapter;
    @BindView(R.id.tv_cache)
    TextView tvCache;

    private int bookid;
    User mUser;
    Book mBook;
    LoadingFragment loadingFragment;
    UserInfoChangedHandler userInfoChangedHandler;

    public static void toBookDetail(Activity activity, int bookid) {
        Intent intent = new Intent(activity, BookDetailActivity.class);
        intent.putExtra("bookid", bookid);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookid = getIntent().getIntExtra("bookid", 0);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        userInfoChangedHandler = UserInfoChangedHandler.newInstance(this).put(this).register();
        initView();
        mUser = User.currentUser();

    }

    private void initView() {

        recycler.setLayoutManager(new GridLayoutManager(this, 4));
        priorityBookAdapter = new PriorityBookAdapter();
        recycler.setAdapter(priorityBookAdapter);
        priorityBookAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Book>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Book item) {
                BookDetailActivity.toBookDetail(BookDetailActivity.this, item.bookid);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        userInfoChangedHandler.unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeMode();
        presenter.loadBookDetail(mUser.getUid(), bookid);
    }

    @NonNull
    @Override
    public BookShelfPresenter createPresenter() {
        return new BookShelfPresenter();
    }

    private void changeMode() {

        new Colorful.Builder(this)
                .setter(new TextColorSetter(tvCate, R.attr.text_second_color))
                .setter(new TextColorSetter(tvLatest, R.attr.text_second_color))
                .setter(new TextColorSetter(tvPreNewChapter, R.attr.text_second_color))
                .setter(new TextColorSetter(tvDesc, R.attr.text_second_color))
                .setter(new TextColorSetter(tvBookName, R.attr.text_color))
                .setter(new TextColorSetter(tvAdd, R.attr.text_color))
                .setter(new TextColorSetter(tvStart, R.attr.text_color))
                .setter(new TextColorSetter(tvInterest, R.attr.text_second_color))
                .setter(new TextColorSetter(tvMore, R.attr.text_second_color))
                .setter(new ViewBackgroundColorSetter(llSettingTop, R.attr.colorAccent))
                .setter(new ViewBackgroundColorSetter(llContent, R.attr.colorPrimary))
                .setter(new ViewGroupSetter(recycler, R.attr.colorPrimary)
                        .childViewTextColor(R.id.tv_name, R.attr.text_second_color))
                .create()
                .setTheme(ModeProvider.getCurrentModeTheme());
        updateButton();

    }


    private void updateButton() {
        Mode mode = ModeProvider.getCurrentMode();
        if (mode == Mode.NightMode) {
            tvStart.setBackgroundResource(R.color.colorPrimaryDark);
            if (mBook != null) {
                if (mBook.shelfid != null) {
                    tvAdd.setBackgroundResource(R.color.reader_light_notify_clor);
                    tvAdd.setText("- 不追了");
                } else {
                    tvAdd.setText("- 追更新");
                    tvAdd.setBackgroundResource(R.color.colorPrimaryDark);
                }
            } else {
                tvAdd.setText("- 追更新");
                tvAdd.setBackgroundResource(R.color.colorPrimaryDark);
            }
        } else {
            tvStart.setBackgroundResource(R.color.colorAccent);
            if (mBook != null) {
                if (mBook.shelfid != null) {
                    tvAdd.setBackgroundResource(R.color.reader_light_notify_clor);
                    tvAdd.setText("- 不追了");
                } else {
                    tvAdd.setText("- 追更新");
                    tvAdd.setBackgroundResource(R.color.colorAccent);
                }
            } else {
                tvAdd.setText("- 追更新");
                tvAdd.setBackgroundResource(R.color.colorAccent);
            }
        }
    }

    // 0 关键字查找 1 分类 2 作者书籍查找 3 排行榜
    @OnClick({R.id.iv_back, R.id.tv_author, R.id.tv_add, R.id.tv_start, R.id.tv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_author:
                if (mBook != null) {
                    BookListActivity.toBookList(this, mBook.author + "的书籍", BookListActivity.TYPE_AUTHOR, mBook.author, 0);
                }
                break;
            case R.id.tv_add:
                if (mUser.getUid() > 0) {
                    if (mBook.shelfid == null) {
                        presenter.addBookShelf(mUser.getUid(), mBook.getBookid(), mBook);
                    } else {
                        presenter.deleteBookShelf(mUser.getUid(), mBook.shelfid, mBook.bookid);
                    }
                } else {
                    //没登录
                    LoginDialog loginDialog = new LoginDialog();
                    loginDialog.setFinishTask(new UserInfoChangedHandler.OnUserChanged() {
                        @Override
                        public void onChanged(User user) {
                            presenter.addBookShelf(mUser.getUid(), mBook.bookid, mBook);
                        }
                    }).show(getSupportFragmentManager(), "LoginDialog");

                }
                break;
            case R.id.tv_start:
                if (mBook != null) {
                    ReaderActivity.toReadPage(this, mBook);
                }
                break;
            case R.id.tv_more:
                if (mBook != null) {
                    BookListActivity.toBookList(this, "你可能感兴趣", BookListActivity.TYPE_CATE, mBook.cate_name, 0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadBookDetail(Book book) {
        this.mBook = book;
        if (this.mBook != null) {
            showContent();
        }
    }

    @Override
    public void onLoadAllCate(List<BookCate> categories) {

    }


    @Override
    public void showLoading(boolean pullToRefresh) {
        if (pullToRefresh) {
            loadingFragment = LoadingFragment.newOne("waiting...");
            loadingFragment.show(getSupportFragmentManager(), "LoadingFragment");
        } else {
            if (loadingFragment != null) {
                loadingFragment.dismiss();
            }
        }

    }

    @Override
    public void showContent() {
        tvAuthor.setText(mBook.author);
        tvBookName.setText(mBook.name);
        tvLatest.setText("");
        tvCate.setText(mBook.cate_name);
        tvDesc.setText(mBook.desc);
        RoundedCornersTransformation roundedCornersTransformation
                = new RoundedCornersTransformation(this, 10, 0);
        Glide.with(this).load(mBook.cover).placeholder(R.color.text_gray_light).bitmapTransform(roundedCornersTransformation).into(imgCover);
        priorityBookAdapter.setDataList(mBook.getPriorities());
        updateButton();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(List<Book> data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }

    @Override
    public void onChanged(User user) {
        mUser = user;
    }

    @OnClick(R.id.tv_cache)
    public void onViewClicked() {
        if(!NetworkUtils.getWifiEnabled()){
            ContentDialog dialog = new ContentDialog();
            dialog.setTitle("桔子小说友情提示").setContent("你没有连上WIFI哦，缓存会消耗你的流量,是否确定缓存？")
                    .setNegativeListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DownloadIntentService.startDownloadService(v.getContext(),mBook,0);
                            ToastUtils.showShort(mBook.name+"已加入缓存列表！");
                        }
                    }).show(getSupportFragmentManager(),"cacheDialog");
        }else {
            DownloadIntentService.startDownloadService(this,mBook,0);
            ToastUtils.showShort(mBook.name+"已加入缓存列表！");
        }
    }
}
