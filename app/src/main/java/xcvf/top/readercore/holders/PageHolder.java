package xcvf.top.readercore.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;

import top.iscore.freereader.R;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.views.PageTextView;

/**
 * Created by xiaw on 2018/7/11.
 */

public class PageHolder extends RecyclerView.ViewHolder {

    IPage page;
    TextView tvChapterName;
    PageTextView tv;
    TextView tvTime;
    TextView tvProgress;
    Chapter chapter;
    View pageBackground;
    View llProgress;
    IPageScrollListener pageScrollListener;

    public PageHolder(Context context, ViewGroup parentView) {
        super(LayoutInflater.from(context).inflate(R.layout.item_page_content, parentView, false));
        tvChapterName = itemView.findViewById(R.id.tv_chapter_name);
        tv = itemView.findViewById(R.id.tv);
        tvTime = itemView.findViewById(R.id.tv_time);
        tvProgress = itemView.findViewById(R.id.tv_progress);
        pageBackground = itemView.findViewById(R.id.page_background);
        llProgress = itemView.findViewById(R.id.ll_progress);

    }


    public PageHolder setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        return this;
    }

    public void setPage(Chapter chapter, IPage page) {
        this.page = page;

        if (this.page.getIndex() > 0) {
            this.chapter = chapter;
            TextConfig textConfig = TextConfig.getConfig();
            //textConfig.apply(tv);
            textConfig.applyColor(tvTime);
            textConfig.applyColor(tvChapterName);
            textConfig.applyColor(tvProgress);

            itemView.setBackgroundColor(itemView.getResources().getColor(textConfig.getBackgroundColor()));
            tvTime.setText(TimeUtils.millis2String(System.currentTimeMillis(), new SimpleDateFormat("HH:mm")));
            tvChapterName.setText(chapter.getChapter_name());
            //tv.setText(page.toString());
            tv.setPage((Page) page);
            tvProgress.setText(page.getIndex() + "/" + chapter.getPages().size());
            llProgress.setVisibility(View.GONE);
        } else {
            llProgress.setVisibility(View.VISIBLE);
            LogUtils.e("LOADING_PAGE");
            if (page.getIndex() == IPage.LOADING_PAGE) {
                //加载下一页
                if (pageScrollListener != null) {

                }
            }
        }

    }

}
