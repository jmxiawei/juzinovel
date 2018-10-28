package top.iscore.freereader.adapter;

import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    Chapter mCurrentChapter = null;

    public void setCurrentChapter(Chapter mCurrentChapter) {
        this.mCurrentChapter = mCurrentChapter;
    }

    @Override
    public ViewHolderCreator createViewHolderCreator() {
        return new ViewHolderCreator() {
            @Override
            public CommonViewHolder createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
               return new CommonViewHolder<Chapter>(parent.getContext(), parent, R.layout.item_chapter_list) {
                    @Override
                    public void bindData(Chapter cpt, int position) {
                        ImageView img = itemView.findViewById(R.id.iv_status);
                        TextView tv = itemView.findViewById(R.id.tv_chapter_name);
                        tv.setText(cpt.chapter_name);
                        if(mCurrentChapter!=null && mCurrentChapter.chapterid == cpt.chapterid){
                            //当前章节
                            img.setImageResource(R.mipmap.iic_chapter_current);
                            tv.setTextColor(itemView.getResources().getColor(R.color.text_black));
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        }else {
                            img.setImageResource(R.mipmap.ic_chapter_item);
                            tv.setTextColor(itemView.getResources().getColor(R.color.text_gray_light));
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        }
                    }
                };
            }
        };
    }
}
