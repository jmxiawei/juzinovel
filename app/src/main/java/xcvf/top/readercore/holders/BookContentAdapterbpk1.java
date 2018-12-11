package xcvf.top.readercore.holders;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import java.util.LinkedList;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.interfaces.ILoadChapter;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.views.BookContentView;

/**
 * 显示数据内容
 * 1.所有章节一直保存
 * 2.
 * Created by xiaw on 2018/7/11.
 */
public class BookContentAdapterbpk1 extends RecyclerView.Adapter<PageHolder> {

    LinkedList<Chapter> mCacheChapterList = new LinkedList<>();
    IPageScrollListener pageScrollListener;
    ILoadChapter mLoadChapter;
    ILoadChapter mShowChapterListener;

    public void setShowChapterListener(ILoadChapter mShowChapterListener) {
        this.mShowChapterListener = mShowChapterListener;
    }

    public BookContentAdapterbpk1 setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        notifyDataSetChanged();
        return this;
    }

    public BookContentAdapterbpk1 setmLoadChapter(ILoadChapter mLoadChapter) {
        this.mLoadChapter = mLoadChapter;
        return this;
    }

    public LinkedList<Chapter> getCacheChapterList() {
        return mCacheChapterList;
    }


    /**
     * 如果之前有
     *
     * @param chapter
     */
    private void checkChapterList(Chapter chapter) {
        if (mCacheChapterList.contains(chapter)
                && chapter.getStatus() == Chapter.STATUS_OK) {
            //其实移除的是之前的记录
            mCacheChapterList.remove(chapter);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置需要显示的章节，放到后面
     *
     * @param mChapter
     * @param startPage
     */
    public void setChapter(BookContentView bookContentView, boolean reset, Chapter mChapter, int startPage, int jumpCharPosition) {
        if (reset) {
            mCacheChapterList.clear();
            notifyDataSetChanged();
        }
        checkChapterList(mChapter);
        if (!mCacheChapterList.contains(mChapter)) {
            int size = mCacheChapterList.size();
            int index = size;
            if (size == 1) {
                Chapter chapter = mCacheChapterList.get(0);
                if (chapter.chapterid < mChapter.chapterid) {
                    index = size;
                } else {
                    index = 0;
                }
            } else {
                //  size >= 2
                Chapter pre = null;
                for (int i = 0; i < size; i++) {
                    Chapter current = mCacheChapterList.get(i);
                    if (i == 0) {
                        if (mChapter.chapterid < current.chapterid) {
                            //比最小的小
                            index = 0;
                            break;
                        }
                    } else {
                        // 1,2 如果当前章节在1,2之前，那么他的位置就是2
                        if (mChapter.chapterid > pre.chapterid
                                && mChapter.chapterid < current.chapterid) {
                            index = i;
                            break;
                        }
                    }
                    pre = current;
                }
                LogUtils.e("[ find position =" + index + ",size=" + size + "]");
            }
            mCacheChapterList.add(index, mChapter);
            if (reset) {
                if (startPage > 0) {
                    bookContentView.scrollToPosition(startPage - 1);
                } else {
                    int page = findPageByPosition(jumpCharPosition, mChapter.getPages());
                    bookContentView.scrollToPosition(page);
                }
            } else {
                if (startPage != Page.LOADING_PAGE && startPage > 0) {
                    //历史记录
                    bookContentView.scrollToPosition(startPage - 1);
                }
            }
        } else {
            //存在。如果是失败的页面
            int index = mCacheChapterList.indexOf(mChapter);
            Chapter chapter = mCacheChapterList.get(index);
            if (chapter.getStatus() == Chapter.STATUS_ERROR) {
                //之前的记录是错误的，就替换
                mCacheChapterList.set(index, mChapter);
                notifyDataSetChanged();
            }
        }

        if (mShowChapterListener != null) {
            mShowChapterListener.load(0, mChapter);
        }

    }


    /**
     * 找到
     *
     * @return
     */
    private int findPageByPosition(int p, List<Page> pages) {
        int size = pages == null ? 0 : pages.size();
        for (int i = 0; i < size; i--) {
            Page page = (Page) pages.get(i);
            if (page.getStartPositionInChapter() >= p) {
                return i;
            }
        }
        return 0;
    }


    @Override
    public PageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PageHolder holder = new PageHolder(parent.getContext(), parent, mLoadChapter);
        //LogUtils.e("onCreateViewHolder========================================" + holder.hashCode());
        return holder;
    }

    @Override
    public void onBindViewHolder(PageHolder holder, int position) {
        //LogUtils.e("onBindViewHolder========================================" + holder.hashCode());
        Page page = getItem(position);
        holder.setPageScrollListener(pageScrollListener);
        holder.setPage(getCurrentChapter(position), page);
    }


    /**
     * @param page
     * @return 0 位于最开始一章 1位于中间，2位于最后一章 3 只有一章
     */
    public int indexOfCurrentChapter(int page) {
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public Chapter getCurrentChapter(int position) {
        int size = mCacheChapterList.size();
        int t = 0;
        for (int i = 0; i < size; i++) {
            Chapter chapter = mCacheChapterList.get(i);
            int c_size = chapter.getPages().size();
            if (position >= t && position < (c_size + t)) {
                LogUtils.e("current chapter = " + i + "," + chapter.chapter_name);
                return chapter;
            }
            t += c_size;
        }
        return null;

    }

    /**
     * 获取分页对象
     *
     * @param position
     * @return
     */
    public Page getItem(int position) {
        int size = mCacheChapterList.size();
        int t = 0;
        for (int i = 0; i < size; i++) {
            Chapter chapter = mCacheChapterList.get(i);
            int c_size = chapter.getPages().size();
            if (position >= t && position < (c_size + t)) {
                int p = position - t;
                LogUtils.e("current chapter = " + i + ",page=" + p + "," + chapter.chapter_name);
                return chapter.getPages().get(position - t);
            }
            t += c_size;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        int total = 0;
        int size = mCacheChapterList.size();
        for (int i = 0; i < size; i++) {
            total += mCacheChapterList.get(i).getPages().size();
        }
        return total;
    }
}
