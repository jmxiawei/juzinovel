package xcvf.top.readercore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.iscore.freereader.R;
import top.iscore.freereader.mvp.presenters.BookReadPresenter;
import top.iscore.freereader.mvp.view.BookReadView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.impl.ChapterDisplayedImpl;
import xcvf.top.readercore.interfaces.Area;
import xcvf.top.readercore.interfaces.IAreaClickListener;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.views.ReaderView;

/**
 * 阅读页面
 * Created by xiaw on 2018/6/30.
 */
public class ReaderActivity extends MvpActivity<BookReadView, BookReadPresenter> implements IAreaClickListener, IPageScrollListener, BookReadView {


    Book book;

    @BindView(R.id.readerView)
    ReaderView readerView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_book)
    TextView tvBook;
    @BindView(R.id.ll_setting_top)
    LinearLayout llSettingTop;
    @BindView(R.id.iv_mode)
    ImageView ivMode;
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.iv_font)
    ImageView ivFont;
    @BindView(R.id.tv_font)
    TextView tvFont;
    @BindView(R.id.iv_cache)
    ImageView ivCache;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.iv_list)
    ImageView ivList;
    @BindView(R.id.tv_list)
    TextView tvList;
    @BindView(R.id.ll_setting_bottom)
    LinearLayout llSettingBottom;

    int currentChapter = 0;

    ChapterDisplayedImpl mChapterDisplayedImpl;

    /**
     * 阅读页面
     *
     * @param activity
     * @param book
     */
    public static void toReadPage(Activity activity, Book book) {
        Intent intent = new Intent(activity, ReaderActivity.class);
        intent.putExtra("book", book);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        ButterKnife.bind(this);
        presenter.attachView(this);
        book = getIntent().getParcelableExtra("book");
        if (book != null) {
            book.save();
        }
        ScreenUtils.setFullScreen(this);
        initReadView();
        loadData(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        int height = readerView.getHeight();
        ToastUtils.showShort("lines=" + height);
    }

    @NonNull
    @Override
    public BookReadPresenter createPresenter() {
        return new BookReadPresenter();
    }

    private void initReadView() {
        mChapterDisplayedImpl = new ChapterDisplayedImpl();
        llSettingTop.setVisibility(View.INVISIBLE);
        llSettingBottom.setVisibility(View.INVISIBLE);
        readerView.setAreaClickListener(this);
        readerView.setPageScrollListener(this);

    }

    @Override
    public void clickArea(Area area) {
        ToastUtils.showShort("点击了中心区域");
    }

    @Override
    public void onScroll(int current, int total, int nextOrPre) {
        if (nextOrPre == IPageScrollListener.PRE_CHAPTER) {
            //上一章节
            if (currentChapter > 0) {
                currentChapter--;
                mChapterDisplayedImpl.showChapter(readerView, true, book.chapters.get(currentChapter));
            }
        } else if (nextOrPre == IPageScrollListener.NEXT_CHAPTER) {
            //下一章节
            if (currentChapter < book.chapters.size() - 1) {
                //最后一章
                currentChapter++;
                mChapterDisplayedImpl.showChapter(readerView, false, book.chapters.get(currentChapter));
            }
        }
    }

    @Override
    public void onLoadChapterList(ArrayList<Chapter> chapters) {
        book.chapters = chapters;
        showContent();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {
        mChapterDisplayedImpl.showChapter(readerView, false, book.chapters.get(currentChapter));
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(Chapter data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadChapters(book);
    }
}
