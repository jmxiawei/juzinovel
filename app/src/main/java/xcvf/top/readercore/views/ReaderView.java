package xcvf.top.readercore.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.util.List;

import top.iscore.freereader.R;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.holders.BookChapterContentAdapter;
import xcvf.top.readercore.holders.BookContentAdapter;
import xcvf.top.readercore.interfaces.IAreaClickListener;
import xcvf.top.readercore.interfaces.IPageScrollListener;

/**
 * 阅读的view
 * Created by xiaw on 2018/6/27.
 */
public class ReaderView extends FrameLayout {

    private static final int CACHE_CHAPTER = 5;

    Chapter mChapter;
    Book mBook;
    BookContentView mBookContentView;
    BookContentAdapter mBookContentAdapter;
    BookChapterContentAdapter mBookChapterContentAdapter;
    IPageScrollListener pageScrollListener;
    IAreaClickListener iAreaClickListener;

    LinearLayoutManager mLayoutManager;


    public void setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        mBookContentView.setPageScrollListener(this.pageScrollListener);
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
        mBookChapterContentAdapter = new BookChapterContentAdapter();
        mBookContentView.setAdapter(mBookContentAdapter);

    }


    public Chapter getChapter() {
        return mChapter;
    }

    /**
     * 初始设置章节
     *
     * @param mChapter 章节
     * @param fromLast 是否直接跳转到最后一页
     */
    public void setChapter(Chapter mChapter, final boolean fromLast) {
        this.mChapter = mChapter;
        mBookContentView.newChapter();
        mBookContentAdapter.setChapter(this.mChapter);
        if (fromLast) {
            mBookContentView.scrollToPosition(mBookContentAdapter.getItemCount() - 1);
        } else {
            mBookContentView.scrollToPosition(0);
        }
    }

    /**
     * 设置书籍信息
     *
     * @param book
     * @param index
     * @param page
     */
    public void setBook(Book book, int index, int page) {
        mBookContentView.setAdapter(mBookChapterContentAdapter);
        mBookChapterContentAdapter.setChapters(getSectionChapter(book, index));

    }


    /**
     * @param book
     * @param index
     * @return
     */
    private List<Chapter> getSectionChapter(Book book, int index) {
        int start = 0;
        int end = index;
        int count = book.chapters.size();
        if (index >= CACHE_CHAPTER) {
            //如果不够一段
            start = index - CACHE_CHAPTER;
            end = index + CACHE_CHAPTER;
        }
        end = Math.min(end, count);
        return book.chapters.subList(start, end);
    }

}
