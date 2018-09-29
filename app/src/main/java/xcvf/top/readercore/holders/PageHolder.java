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
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.interfaces.IPage;

/**
 * Created by xiaw on 2018/7/11.
 */

public class PageHolder extends RecyclerView.ViewHolder {

    IPage page;
    TextView tvChapterName;
    TextView tv;
    TextView tvTime;
    TextView tvProgress;
    Chapter chapter;
    View pageBackground;

    public PageHolder(Context context, ViewGroup parentView) {
        super(LayoutInflater.from(context).inflate(R.layout.item_page_content, parentView, false));
        tvChapterName = itemView.findViewById(R.id.tv_chapter_name);
        tv = itemView.findViewById(R.id.tv);
        tvTime = itemView.findViewById(R.id.tv_time);
        tvProgress = itemView.findViewById(R.id.tv_progress);
        pageBackground = itemView.findViewById(R.id.page_background);
    }

    public void setPage(Chapter chapter, IPage page) {
        this.page = page;
        this.chapter = chapter;
        TextConfig textConfig = TextConfig.getConfig();
        textConfig.apply(tv);
        textConfig.applyColor(tvTime);
        textConfig.applyColor(tvChapterName);
        textConfig.applyColor(tvProgress);
        pageBackground.setBackgroundColor(textConfig.backgroundColor);
        tvTime.setText(TimeUtils.millis2String(System.currentTimeMillis(), new SimpleDateFormat("HH:mm")));
        tvChapterName.setText(chapter.getChapter_name());
        tv.setText(page.toString());
        tvProgress.setText(page.getIndex() + "/" + chapter.getPages().size());
    }

}
