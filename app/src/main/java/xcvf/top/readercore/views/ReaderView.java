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
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.interfaces.IPageScrollListener;

/**
 * 阅读的view
 * Created by xiaw on 2018/6/27.
 */
public class ReaderView extends FrameLayout {

    private static final int CACHE_CHAPTER = 5;

    Book mBook;
    BookContentView mBookContentView;
    BookContentAdapter mBookContentAdapter;
    //BookChapterContentAdapter mBookChapterContentAdapter;
    IPageScrollListener pageScrollListener;
    IAreaClickListener iAreaClickListener;

    LinearLayoutManager mLayoutManager;


    public void setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        mBookContentView.setPageScrollListener(this.pageScrollListener);
        mBookContentAdapter.setPageScrollListener(pageScrollListener);

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
        //mBookChapterContentAdapter = new BookChapterContentAdapter();
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
     * @return
     */
    public Chapter getCurrentChapter() {
        int page = mBookContentView.getCurrentPage();
        return mBookContentAdapter.getCurrentChapter(page);
    }

    /**
     * 初始设置章节
     *
     * @param mChapter   章节
     * @param toLastPage 是否直接跳转到最后一页
     * @param page       直接跳转到哪一页
     */
    public void setChapter(boolean reset, Chapter mChapter, final boolean toLastPage, int page) {
        mBookContentAdapter.setChapter(mBookContentView,reset, mChapter, page);
    }

}
