package top.iscore.freereader.adapter;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import top.iscore.freereader.mode.setter.ViewSetter;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 章节列表
 */
public class ChapterListAdapter extends BaseRecyclerAdapter<Chapter> {

    Chapter mCurrentChapter = null;

    int colorSecondText = 0;
    public void setCurrentChapter(Chapter mCurrentChapter) {
        this.mCurrentChapter = mCurrentChapter;
    }

    public void setColorSecond(int color){
        this.colorSecondText = color;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderCreator createViewHolderCreator() {
        return new ViewHolderCreator() {
            @Override
            public CommonViewHolder createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                return new CommonViewHolder<Chapter>(parent.getContext(), parent, R.layout.item_chapter_list) {
                    @Override
                    public void bindData(Chapter cpt, int position) {
                        View img = itemView.findViewById(R.id.iv_status);
                        ImageView iv_current = itemView.findViewById(R.id.iv_current);
                        TextView tv = itemView.findViewById(R.id.tv_chapter_name);
                        TextView tv_download_status = itemView.findViewById(R.id.tv_download_status);
                        tv.setText(cpt.chapter_name);

                        if (!cpt.is_download) {
                            tv_download_status.setVisibility(View.INVISIBLE);
                        } else {
                            tv_download_status.setVisibility(View.VISIBLE);
                        }

                        if (mCurrentChapter != null && mCurrentChapter.chapterid == cpt.chapterid) {
                            //当前章节
                            iv_current.setVisibility(View.VISIBLE);
                            img.setVisibility(View.GONE);
                            tv.setTextColor(itemView.getResources().getColor(R.color.text_focused_color));
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        } else {
                            iv_current.setVisibility(View.GONE);
                            img.setVisibility(View.VISIBLE);
                            tv.setTextColor(colorSecondText);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        }
                    }
                };
            }
        };
    }
}
