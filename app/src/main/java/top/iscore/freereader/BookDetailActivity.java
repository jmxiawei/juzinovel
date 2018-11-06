package top.iscore.freereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import top.iscore.freereader.adapter.PriorityBookAdapter;
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
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 书籍详情
 */
public class BookDetailActivity extends MvpActivity<BookShelfView, BookShelfPresenter> implements BookShelfView {

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

    private String bookid;
    User mUser;
    Book mBook;

    public static void toBookDetail(Activity activity, String bookid) {
        Intent intent = new Intent(activity, BookDetailActivity.class);
        intent.putExtra("bookid", bookid);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookid = getIntent().getStringExtra("bookid");
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        initView();
        mUser = User.currentUser();
        presenter.loadBookDetail(mUser == null ? null : mUser.getUid(), bookid);
    }

    private void initView() {

        recycler.setLayoutManager(new GridLayoutManager(this, 4));
        priorityBookAdapter = new PriorityBookAdapter();
        recycler.setAdapter(priorityBookAdapter);
        priorityBookAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Book>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Book item) {
                BookDetailActivity.toBookDetail(BookDetailActivity.this, item.extern_bookid);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeMode();
    }

    @NonNull
    @Override
    public BookShelfPresenter createPresenter() {
        return new BookShelfPresenter();
    }

    private void changeMode() {

        new Colorful.Builder(this)
                .setter(new TextColorSetter(tvBookName, R.attr.text_color))
                .setter(new TextColorSetter(tvCate, R.attr.text_second_color))
                .setter(new TextColorSetter(tvLatest, R.attr.text_second_color))
                .setter(new TextColorSetter(tvDesc, R.attr.text_second_color))
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
                    tvAdd.setText("-不追了");
                } else {
                    tvAdd.setBackgroundResource(R.color.colorPrimaryDark);
                }
            } else {
                tvAdd.setBackgroundResource(R.color.colorPrimaryDark);
            }
        } else {
            tvStart.setBackgroundResource(R.color.colorAccent);
            if (mBook != null) {
                if (mBook.shelfid != null) {
                    tvAdd.setBackgroundResource(R.color.reader_light_notify_clor);
                    tvAdd.setText("-不追了");
                } else {
                    tvAdd.setBackgroundResource(R.color.colorAccent);
                }
            } else {
                tvAdd.setBackgroundResource(R.color.colorAccent);
            }
        }


    }

    @OnClick({R.id.iv_back, R.id.tv_author, R.id.tv_add, R.id.tv_start, R.id.tv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_author:
                if (mBook != null) {
                    BookListActivity.toBookList(this, mBook.author + "的书籍", 1, mBook.author);
                }
                break;
            case R.id.tv_add:
                if (mUser != null && mBook != null) {
                    presenter.addBookShelf(mUser.getUid(), mBook.getExtern_bookid());
                } else {

                }
                break;
            case R.id.tv_start:
                if (mBook != null) {
                    ReaderActivity.toReadPage(this, mBook);
                }
                break;
            case R.id.tv_more:
                if (mBook != null) {
                    BookListActivity.toBookList(this, "你可能感兴趣的书籍", 0, mBook.cate_name);
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
    public void onLoadAllCate(List<Category> categories) {

    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {
        tvAuthor.setText(mBook.author);
        tvBookName.setText(mBook.name);
        tvLatest.setText(mBook.latest_chapter_name);
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
}
