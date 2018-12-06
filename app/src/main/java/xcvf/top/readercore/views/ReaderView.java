package xcvf.top.readercore.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.util.List;

import top.iscore.freereader.R;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.holders.BookContentAdapter;
import xcvf.top.readercore.interfaces.IAreaClickListener;
import xcvf.top.readercore.interfaces.ILoadChapter;
import xcvf.top.readercore.interfaces.IPageScrollListener;

/**
 * 阅读的view
 * Created by xiaw on 2018/6/27.
 */
public class ReaderView extends FrameLayout {

    private static final int CACHE_CHAPTER = 5;

    Book mBook;
    BookContentView mBookContentView;
    /**
     * 页显示
     */
    BookContentAdapter mBookContentAdapter;
    /**
     * 页面滑动，换页
     */
    IPageScrollListener pageScrollListener;
    /**
     * 页面区域点击
     */
    IAreaClickListener iAreaClickListener;

    LinearLayoutManager mLayoutManager;

    /**
     * 加载失败的张杰
     */
    ILoadChapter mLoadChapter;

    ILoadChapter mShowChapterSuccess;

    public ILoadChapter getmShowChapterSuccess() {
        return mShowChapterSuccess;
    }

    public ReaderView setShowChapterSuccess(ILoadChapter mShowChapterSuccess) {
        this.mShowChapterSuccess = mShowChapterSuccess;
        mBookContentAdapter.setShowChapterListener(this.mShowChapterSuccess);
        return this;
    }

    public void onTextConfigChanged() {
        mBookContentAdapter.notifyDataSetChanged();
    }

    public void setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        mBookContentView.setPageScrollListener(this.pageScrollListener);
        mBookContentAdapter.setPageScrollListener(pageScrollListener);
    }

    public ReaderView setLoadChapter(ILoadChapter mLoadChapter) {
        this.mLoadChapter = mLoadChapter;
        mBookContentAdapter.setmLoadChapter(mLoadChapter);
        return this;
    }

    public void setAreaClickListener(IAreaClickListener iAreaClickListener) {
        this.iAreaClickListener = iAreaClickListener;
        mBookContentView.setAreaClickListener(this.iAreaClickListener);
    }

    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_read_view, this, true);
        mBookContentView = findViewById(R.id.book_content);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mBookContentView.setLayoutManager(mLayoutManager);
        mBookContentAdapter = new BookContentAdapter();
        mBookContentView.setAdapter(mBookContentAdapter);
    }


    /**
     * 获取当前的章节
     *
     * @return
     */
    public Page getCurrentPage() {
        int page = mBookContentView.getCurrentPage();
        return (Page) mBookContentAdapter.getCurrentPage(page);
    }


    /**
     * 获取当前的章节
     *
     * @return 0 位于最开始一章 1位于中间，2位于最后一章
     */
    public int indexOfCurrentChapter() {
        int page = mBookContentView.getCurrentPage();
        return mBookContentAdapter.indexOfCurrentChapter(page);
    }


    public void scrollNextPage() {
        int page = mBookContentView.getCurrentPage();
        mBookContentView.scrollToPosition(page + 1);
    }


    public void scrollPrePage() {
        int page = mBookContentView.getCurrentPage();
        mBookContentView.scrollToPosition(-1);
    }

    /**
     * 获取当前的章节
     *
     * @return
     */
    public Chapter getCurrentChapter() {
        int page = mBookContentView.getCurrentPage();
        return mBookContentAdapter.getCurrentChapter(page);
    }


    /**
     * @param chapter
     * @return
     */
    public boolean needReload(Chapter chapter) {
        List<Chapter> mCacheList = mBookContentAdapter.getCacheChapterList();
        if (mCacheList != null && mCacheList.size() > 0) {
            for (Chapter chapter1 : mCacheList) {
                if (chapter1.equals(chapter)) {
                    if (chapter1.getStatus() == Chapter.STATUS_OK) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 初始设置章节
     *
     * @param mChapter         章节
     * @param jumpCharPosition 是否直接跳转某个位置
     * @param page             直接跳转到哪一页
     */
    public void setChapter(boolean reset, Chapter mChapter, int jumpCharPosition, int page) {
        mBookContentAdapter.setChapter(mBookContentView, reset, mChapter, page, jumpCharPosition);
    }

}
