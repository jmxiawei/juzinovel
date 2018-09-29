package xcvf.top.readercore.holders;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.interfaces.IPage;

/**
 * Created by xiaw on 2018/7/11.
 */

public class BookContentAdapter extends RecyclerView.Adapter<PageHolder> {

    List<IPage> pageList = new ArrayList<>();
    Chapter mChapter;



    public List<IPage> getPageList() {
        return pageList;
    }

    public void setChapter(Chapter mChapter) {
        this.mChapter = mChapter;
        setPageList(this.mChapter.getPages());
    }

    public void appendChapter(Chapter chapter) {
        this.pageList.addAll(chapter.getPages());
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
        holder.setPage(mChapter, page);
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }
}
