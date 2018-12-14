package xcvf.top.readercore.holders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.iscore.freereader.R;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.interfaces.IChapterProvider;
import xcvf.top.readercore.interfaces.ILoadChapter;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.views.PageTextView;

/**
 * Created by xiaw on 2018/7/11.
 */

public class PageHolder extends RecyclerView.ViewHolder {

    Page page;
    TextView tvChapterName;
    PageTextView tv;
    TextView tvTime;
    TextView tvProgress;
    Chapter chapter;
    View pageBackground;
    IPageScrollListener pageScrollListener;
    ILoadChapter mILoadChapter;
    LinearLayout llNotify;

    LinearLayout llProgress;
    ProgressBar progressBar;
    TextView tv_notify_net;
    TextView tvRetry;
    @BindView(R.id.tv_source)
    TextView tvSource;


    public PageHolder(Context context, ViewGroup parentView, ILoadChapter mILoadChapter) {
        super(LayoutInflater.from(context).inflate(R.layout.item_page_content, parentView, false));
        this.mILoadChapter = mILoadChapter;
        ButterKnife.bind(this, itemView);
        tvChapterName = itemView.findViewById(R.id.tv_chapter_name);
        tv = itemView.findViewById(R.id.tv);
        tvTime = itemView.findViewById(R.id.tv_time);
        tv_notify_net = itemView.findViewById(R.id.tv_notify_net);
        tvProgress = itemView.findViewById(R.id.tv_progress);
        pageBackground = itemView.findViewById(R.id.page_background);
        llProgress = itemView.findViewById(R.id.ll_progress);
        llNotify = itemView.findViewById(R.id.ll_notify);
        tvRetry = itemView.findViewById(R.id.tv_retry);
        progressBar = itemView.findViewById(R.id.progress_loading);

    }


    public void textColorChanged() {
        TextConfig textConfig = TextConfig.getConfig();
        textConfig.applyColor(tvTime);
        textConfig.applyColor(tvChapterName);
        textConfig.applyColor(tvProgress);
        textConfig.applyColor(tv_notify_net);
        textConfig.applyColor(tvRetry);
        itemView.setBackgroundColor(itemView.getResources().getColor(textConfig.getBackgroundColor()));
        tv.postInvalidate();
    }


    public PageHolder setPageScrollListener(IPageScrollListener pageScrollListener) {
        this.pageScrollListener = pageScrollListener;
        return this;
    }


    public void setPage(final Chapter chapter, Page page) {
        if (chapter == null || page == null) {
            return;
        }
        this.page = page;
        textColorChanged();
        tvTime.setText(page.getTime());
        tvChapterName.setText(chapter.getChapter_name());
        tvSource.setText("来源:" + chapter.engine_domain);
        if (this.page.getStatus() == Page.OK_PAGE) {
            this.chapter = chapter;
            tv.setPage((Page) page);
            tvProgress.setText(page.getIndex() + "/" + chapter.getPages().size());
            tv.setVisibility(View.VISIBLE);
            llProgress.setVisibility(View.INVISIBLE);
        } else if (this.page.getStatus() == Page.ERROR_PAGE) {
            tv.setVisibility(View.INVISIBLE);
            llProgress.setVisibility(View.VISIBLE);
            tvProgress.setText("-/-");
            llNotify.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            tvRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mILoadChapter != null) {
                        mILoadChapter.load(IChapterProvider.TYPE_DETAIL, chapter);
                    }
                }
            });
        } else if (this.page.getStatus() == Page.LOADING_PAGE) {
            tv.setVisibility(View.INVISIBLE);
            llProgress.setVisibility(View.VISIBLE);
            tvProgress.setText("-/-");
            llNotify.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}
