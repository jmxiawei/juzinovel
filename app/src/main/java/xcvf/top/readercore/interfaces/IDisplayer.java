package xcvf.top.readercore.interfaces;

import android.support.v4.widget.ContentLoadingProgressBar;

import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.views.ReaderView;

/**
 * 显示器，将章节内容显示出来
 * Created by xiaw on 2018/9/28.
 */
public interface IDisplayer {

    /**
     *
     * @param readerView
     * @param fromLast
     * @param page 直接跳到第几页
     * @param chapter
     */
    void showChapter(boolean reset,ReaderView readerView,boolean fromLast,int page,Chapter chapter);

}
