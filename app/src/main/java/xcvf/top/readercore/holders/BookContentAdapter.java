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
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.views.BookContentView;

/**
 * 显示数据内容
 * 1.所有章节一直保存
 * 2.
 * Created by xiaw on 2018/7/11.
 */
public class BookContentAdapter extends RecyclerView.Adapter<PageHolder> {

    List<IPage> pageList = new ArrayList<>();

    LinkedList<Chapter> mCacheChapterList = new LinkedList<>();
    IPageScrollListener pageScrollListener;

    public BookContentAdapter setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        notifyDataSetChanged();
        return this;
    }


    /**
     * 设置需要显示的章节，放到后面
     *
     * @param mChapter
     * @param startPage
     */
    public void setChapter(BookContentView bookContentView, boolean reset, Chapter mChapter, int startPage) {
        if (reset) {
            mCacheChapterList.clear();
            pageList.clear();
            notifyDataSetChanged();
        }
        if (!mCacheChapterList.contains(mChapter)) {
            int size = mCacheChapterList.size();
            int index = -1;
            if (size == 0) {
                index = size;
            } else if (size == 1) {
                Chapter chapter = mCacheChapterList.get(0);
                if (chapter.chapterid < mChapter.chapterid) {
                    index = size;
                } else {
                    index = 0;
                }
            } else {
                //2
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

            }
            if (index == size) {
                mCacheChapterList.addLast(mChapter);
                appendList(mChapter.getPages());
                LogUtils.e("addLast chapter_name="+mChapter.chapter_name);
            } else {
                mCacheChapterList.addFirst(mChapter);
                appendListTop(mChapter.getPages());
                LogUtils.e("addFirst chapter_name="+mChapter.chapter_name);
                //添加到前面
            }

            if(reset){
                bookContentView.scrollToPosition(0);
            }else {
                if (startPage != IPage.LOADING_PAGE && startPage > 0) {
                    //历史记录
                    bookContentView.scrollToPosition(startPage - 1);
                }
            }


        }
    }


    /**
     * 添加到前面
     *
     * @param pages
     */
    private void appendListTop(List<IPage> pages) {
        this.pageList.addAll(0, pages);
        notifyItemRangeInserted(0, pages.size());

    }

    private void appendPage(IPage page) {
        this.pageList.add(page);
        notifyDataSetChanged();
    }

    private void appendList(List<IPage> pageList) {
        this.pageList.addAll(pageList);
        notifyItemRangeInserted(this.pageList.size(), this.pageList.size());
    }

    private void setPageList(List<IPage> pageList) {
        this.pageList.clear();
        if (pageList != null) {
            this.pageList.addAll(pageList);
        }
        notifyDataSetChanged();
    }


    @Override
    public PageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PageHolder(parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(PageHolder holder, int position) {
        IPage page = pageList.get(position);
        holder.setPageScrollListener(pageScrollListener);
        holder.setPage(getCurrentChapter(page), page);
    }

    private Chapter getCurrentChapter(IPage page) {
        for (int i = 0; i < mCacheChapterList.size(); i++) {
            List<IPage> pageList = mCacheChapterList.get(i).getPages();
            if (pageList.contains(page)) {
                return mCacheChapterList.get(i);
            }
        }
        return null;
    }


    public IPage getCurrentPage(int page) {
        if (page < pageList.size()) {
            return pageList.get(page);
        }
        return null;
    }


    public Chapter getCurrentChapter(int page) {
        IPage page1 = pageList.get(page);
        for (int i = 0; i < mCacheChapterList.size(); i++) {
            List<IPage> pageList = mCacheChapterList.get(i).getPages();
            if (pageList.contains(page1)) {
                return mCacheChapterList.get(i);
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }
}
