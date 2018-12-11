package xcvf.top.readercore.holders;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Iterator;
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
public class BookContentAdapter extends RecyclerView.Adapter<PageHolder> {

    List<Page> pageList = new ArrayList<>();

    LinkedList<Chapter> mCacheChapterList = new LinkedList<>();
    IPageScrollListener pageScrollListener;
    ILoadChapter mLoadChapter;
    ILoadChapter mShowChapterListener;

    public void setShowChapterListener(ILoadChapter mShowChapterListener) {
        this.mShowChapterListener = mShowChapterListener;
    }

    public BookContentAdapter setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        notifyDataSetChanged();
        return this;
    }

    public BookContentAdapter setmLoadChapter(ILoadChapter mLoadChapter) {
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
            int total = 0;
            int size = mCacheChapterList.size();
            for (int i = 0; i < size; i++) {
                total += mCacheChapterList.get(i).getPages().size();
            }
            if (total != getItemCount()) {
                mCacheChapterList.remove(chapter);
                Iterator<Page> pageIterator = pageList.iterator();
                while (pageIterator.hasNext()) {
                    Page page = pageIterator.next();
                    if (page.chapterid == chapter.chapterid) {
                        pageIterator.remove();
                    }
                }
            }
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
            pageList.clear();
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
                //Chapter pre = null;
                for (int i = 0; i < size; i++) {
                    Chapter current = mCacheChapterList.get(i);
                    if (i == size - 1) {
                        if (mChapter.chapterid > current.chapterid) {
                            index = size;
                            break;
                        }
                    }
                    if (i == 0) {
                        if (mChapter.chapterid < current.chapterid) {
                            //比最小的小
                            index = 0;
                            break;
                        }
                    }
//                    else {
//                        // 1,2 如果当前章节在1,2之前，那么他的位置就是2
//                        if (mChapter.chapterid > pre.chapterid
//                                && mChapter.chapterid < current.chapterid) {
//                            index = i;
//                            break;
//                        }
//                    }
                    //pre = current;
                }
                LogUtils.e("[ find position =" + index + ",size=" + size + "]");
            }
            if (index == size) {
                mCacheChapterList.addLast(mChapter);
                appendList(mChapter.getPages());
            } else if (index == 0) {
                //添加到前面
                mCacheChapterList.addFirst(mChapter);
                appendListTop(mChapter.getPages());
            }
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
                mCacheChapterList.set(index, mChapter);
                int fronPage = getFrontPage(index);
                List<Page> pageList = mChapter.getPages();
                if (pageList.size() > 0) {
                    replace(fronPage, pageList.get(0));
                    appendList(fronPage + 1, pageList.subList(1, pageList.size()));
                }
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


    /**
     * 本章节之前有多少页数据
     *
     * @param chapterIndex
     * @return
     */
    private int getFrontPage(int chapterIndex) {

        if (chapterIndex == -1) {
            return -1;
        } else if (chapterIndex == 0) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < chapterIndex; i++) {
            count += mCacheChapterList.get(i).getPages().size();
        }
        return count;
    }


    /**
     * 添加到前面
     *
     * @param pages
     */
    private void appendListTop(List<Page> pages) {
        this.pageList.addAll(0, pages);
        notifyItemRangeInserted(0, pages.size());

    }

    private void appendPage(Page page) {
        this.pageList.add(page);
        notifyDataSetChanged();
    }


    private void replace(int position, Page page) {
        this.pageList.set(position, page);
        notifyItemChanged(position);
    }

    /**
     * 加入到列表中间
     *
     * @param startIndex
     * @param pageList
     */
    private void appendList(int startIndex, List<Page> pageList) {
        this.pageList.addAll(startIndex, pageList);
        notifyItemRangeInserted(startIndex, pageList.size());
    }

    private void appendList(List<Page> pageList) {
        this.pageList.addAll(pageList);
        notifyItemRangeInserted(this.pageList.size() - pageList.size(), this.pageList.size());
    }

    private void setPageList(List<Page> pageList) {
        this.pageList.clear();
        if (pageList != null) {
            this.pageList.addAll(pageList);
        }
        notifyDataSetChanged();
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
        Page page = pageList.get(position);
        holder.setPageScrollListener(pageScrollListener);
        holder.setPage(getCurrentChapter(page), page);
    }

    private Chapter getCurrentChapter(Page page) {
        for (int i = 0; i < mCacheChapterList.size(); i++) {
            List<Page> pageList = mCacheChapterList.get(i).getPages();
            if (pageList.contains(page)) {
                return mCacheChapterList.get(i);
            }
        }
        return null;
    }


    public Page getCurrentPage(int page) {
        if (page < pageList.size()) {
            return pageList.get(page);
        }
        return null;
    }

    /**
     * @param page
     * @return 0 位于最开始一章 1位于中间，2位于最后一章 3 只有一章
     */
    public int indexOfCurrentChapter(int page) {
        if (page >= pageList.size() || page < 0) {
            return -1;
        }
        Page page1 = pageList.get(page);
        for (int i = 0; i < mCacheChapterList.size(); i++) {
            List<Page> pageList = mCacheChapterList.get(i).getPages();
            if (pageList.contains(page1)) {
                if (i == 0) {
                    if (mCacheChapterList.size() == 1) {
                        return 3;
                    }
                    return 0;
                } else if (i == mCacheChapterList.size() - 1) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public Chapter getCurrentChapter(int page) {
        if (page >= pageList.size() || page < 0) {
            return null;
        }
        Page page1 = pageList.get(page);
        for (int i = 0; i < mCacheChapterList.size(); i++) {
            List<Page> pageList = mCacheChapterList.get(i).getPages();
            if (pageList.contains(page1)) {
                return mCacheChapterList.get(i);
            }
        }
        return null;
    }


//    public Page getItem(int position) {
//
//    }

    @Override
    public int getItemCount() {

//        int total = 0;
//        int size = mCacheChapterList.size();
//        for (int i = 0; i < size; i++) {
//            total +=
//        }

        return pageList.size();
    }
}
