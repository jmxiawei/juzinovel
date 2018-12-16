package xcvf.top.readercore.holders;

import android.view.View;

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
public class BookContentAdapter extends OpenPagerAdapter<Page> {

    List<Page> pageList = new ArrayList<>();

    LinkedList<Chapter> mCacheChapterList = new LinkedList<>();
    IPageScrollListener pageScrollListener;
    ILoadChapter mLoadChapter;
    ILoadChapter mShowChapterListener;
    BookContentView bookContentView;

    public BookContentAdapter(BookContentView bookContentView) {
        this.bookContentView = bookContentView;
    }

    public void setShowChapterListener(ILoadChapter mShowChapterListener) {
        this.mShowChapterListener = mShowChapterListener;
    }

    public BookContentAdapter setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 文字颜色改了，动态修改，不刷新页面
     */
    public void onTextColorChange() {
        int size = getCount();
        for (int i = 0; i < size; i++) {
            View view = getCachedItem(i);
            if (view != null) {
                PageHolder holder = (PageHolder) view.getTag();
                if (holder != null) {
                    holder.textColorChanged();
                }

            }
        }
    }

    public BookContentAdapter setmLoadChapter(ILoadChapter mLoadChapter) {
        this.mLoadChapter = mLoadChapter;
        return this;
    }

    public LinkedList<Chapter> getCacheChapterList() {
        return mCacheChapterList;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    /**
     * 如果之前有
     *
     * @param chapter
     */
    private synchronized void checkChapterList(Chapter chapter) {


        int index = mCacheChapterList.indexOf(chapter);
        if (index == -1) {
            return;
        } else {
            if (chapter.getStatus() == Chapter.STATUS_OK) {
                int total = 0;
                int size = mCacheChapterList.size();
                for (int i = 0; i < size; i++) {
                    total += mCacheChapterList.get(i).getPages().size();
                }
                if (total != getCount()) {
                    mCacheChapterList.remove(chapter);
                    Iterator<Page> pageIterator = pageList.iterator();
                    while (pageIterator.hasNext()) {
                        Page page = pageIterator.next();
                        if (page.chapterid == chapter.chapterid) {
                            pageIterator.remove();
                        }
                    }
                    notifyDataSetChanged();
                }

            }
        }
    }

    /**
     * 设置需要显示的章节，放到后面
     * @param jumpCharPosition 修改字体的时候，跳转到指定字符
     * @param mChapter
     * @param startPage
     */
    public void setChapter(BookContentView bookContentView, boolean reset, Chapter mChapter, int startPage, int jumpCharPosition) {
        if (reset) {
            mCacheChapterList.clear();
            pageList.clear();
            notifyDataSetChanged();
        }
        //checkChapterList(mChapter);
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
                for (int i = 0; i < size; i++) {
                    Chapter next = mCacheChapterList.get(i);
                    if (i == size - 1) {
                        if (next.chapterid < mChapter.chapterid) {
                            //比最大的大
                            index = size;
                            break;
                        }
                    } else if (i == 0) {
                        if (mChapter.chapterid < next.chapterid) {
                            //比最小的小
                            index = 0;
                            break;
                        }
                    }
                }
                LogUtils.e("[ find position =" + index + ",size=" + size + "]");
            }
            if (index == size) {
                mCacheChapterList.addLast(mChapter);
                appendList(mChapter.getPages());
            } else {
                //添加到前面
                mCacheChapterList.addFirst(mChapter);
                appendListTop(mChapter.getPages());
            }

            if (reset) {
                if (startPage > 0) {
                    bookContentView.setCurrentItem(startPage - 1);
                } else {
                    int page = findPageByPosition(jumpCharPosition, mChapter.getPages());
                    bookContentView.setCurrentItem(page);
                }
            } else {
                if (startPage != Page.LOADING_PAGE && startPage > 0) {
                    //历史记录
                    bookContentView.setCurrentItem(startPage - 1);
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
        notifyDataSetChanged();

    }

    @Override
    public View getItem(int position) {
        PageHolder holder = new PageHolder(bookContentView.getContext(), bookContentView, mLoadChapter);
        Page page = getItemData(position);
        holder.setPageScrollListener(pageScrollListener);
        holder.setPage(getCurrentChapter(position), page);
        holder.itemView.setTag(holder);
        return holder.itemView;
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    @Override
    protected Page getItemData(int position) {
        if (getCount() > position) {
            return pageList.get(position);
        }
        return null;
    }

    @Override
    protected boolean dataEquals(Page oldData, Page newData) {
        if (oldData == null) {
            return false;
        }
        boolean eq = oldData.equals(newData);
        if (eq) {
            //显示的时候跟状态有关，状态不一样需要重新显示
            eq = oldData.getStatus() == newData.getStatus();
        }
        return eq;
    }

    @Override
    protected int getDataPosition(Page data) {
        if(data == null)return -1;
        int size = pageList.size();
        for (int i = 0; i < size ; i++) {
            Page page = pageList.get(i);
            if(page.equals(data) && page.getStatus() == data.getStatus()){
                return i;
            }
        }
        return -1;
    }


    private void appendPage(Page page) {
        this.pageList.add(page);
        notifyDataSetChanged();
    }


    private void replace(int position, Page page) {
        this.pageList.set(position, page);
        notifyDataSetChanged();
    }

    /**
     * 加入到列表中间
     *
     * @param startIndex
     * @param pageList
     */
    private void appendList(int startIndex, List<Page> pageList) {
        this.pageList.addAll(startIndex, pageList);
        notifyDataSetChanged();
    }

    private void appendList(List<Page> pageList) {
        this.pageList.addAll(pageList);
        notifyDataSetChanged();
    }

    private void setPageList(List<Page> pageList) {
        this.pageList.clear();
        if (pageList != null) {
            this.pageList.addAll(pageList);
        }
        notifyDataSetChanged();
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


}
