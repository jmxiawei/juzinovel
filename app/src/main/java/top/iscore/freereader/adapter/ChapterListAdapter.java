package top.iscore.freereader.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import xcvf.top.readercore.bean.Chapter;

/**
 * 章节列表
 */
public class ChapterListAdapter extends BaseRecyclerAdapter<Chapter> {
    @Override
    public ViewHolderCreator createViewHolderCreator() {
        return new ViewHolderCreator() {
            @Override
            public CommonViewHolder createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
               return new CommonViewHolder<Chapter>(parent.getContext(), parent, R.layout.item_chapter_list) {
                    @Override
                    public void bindData(Chapter cpt, int position) {
                        TextView tv = itemView.findViewById(R.id.tv_chapter_name);
                        tv.setText(cpt.chapter_name);
                    }
                };
            }
        };
    }
}
