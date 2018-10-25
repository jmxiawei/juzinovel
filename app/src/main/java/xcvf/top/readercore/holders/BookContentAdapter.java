package xcvf.top.readercore.holders;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.interfaces.IPageScrollListener;

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
    int startPage;

    public List<IPage> getPageList() {
        return pageList;
    }

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
    public void setChapter(boolean reset, Chapter mChapter, int startPage) {
        if (reset) {
            mCacheChapterList.clear();
        }
        if (!mCacheChapterList.contains(mChapter)) {
            mCacheChapterList.addLast(mChapter);
            this.startPage = startPage;
            removeLoadingPage();
            appendList(mChapter.getPages());
        }
    }


    private void removeLoadingPage() {
        Iterator<IPage> iPageIterator = this.getPageList().iterator();
        while (iPageIterator.hasNext()) {
            IPage page = iPageIterator.next();
            if (page.getIndex() == IPage.LOADING_PAGE) {
                iPageIterator.remove();
                break;
            }
        }
    }

    private void appendPage(IPage page) {
        this.pageList.add(page);
        notifyDataSetChanged();
    }

    private void appendList(List<IPage> pageList) {
        this.pageList.addAll(pageList);
        notifyItemRangeInserted(this.pageList.size() - pageList.size(), this.pageList.size());
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
