package xcvf.top.readercore.holders;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.interfaces.IPage;

/**
 * Created by xiaw on 2018/7/11.
 */

public class BookChapterContentAdapter extends RecyclerView.Adapter<PageHolder> {


    List<Chapter> chapters = new ArrayList<>();


    @Override
    public PageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PageHolder(parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(PageHolder holder, int position) {
        holder.setPage(getChapter(position), getItem(position));
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters.clear();
        if (chapters != null) {
            this.chapters.addAll(chapters);
        }
        notifyDataSetChanged();
    }

    /**
     * @param chapter
     */
    public void appendChapter(Chapter chapter) {
        int count = getItemCount();
        this.chapters.add(chapter);
        notifyItemRangeInserted(count, chapter.getPages().size());
    }


    public int getPosition(int index, int page) {
        int size = chapters.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (i == index) {
                Chapter chapter = chapters.get(i);
                if (page < chapter.getPages().size()) {
                    count += page;
                }
            } else {
                count += chapters.get(i).getPages().size();
            }
        }
        return count;
    }


    public int getProgress(Chapter chapter, int page) {

        int index = this.chapters.indexOf(chapter);
        if (index >= 0) {
            return  getPosition(index,page);
        }
        return  0;

    }

    /**
     * @param position
     * @return
     */
    public Chapter getChapter(int position) {

        int size = chapters.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            count += chapters.get(i).getPages().size();
            if (count >= position) {
                return chapters.get(i);
            }
        }
        return null;
    }

    /**
     * @param position
     * @return
     */
    public IPage getItem(int position) {

        int size = chapters.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            count += chapters.get(i).getPages().size();
            if (count >= position) {
                Chapter chapter = chapters.get(i);
                return chapter.getPages().get(count - position);
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {

        int size = chapters.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            count += chapters.get(i).getPages().size();
        }
        return count;
    }
}
