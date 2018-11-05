package xcvf.top.readercore.interfaces;

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
     * @param jumpCharPosition
     * @param page 直接跳到第几页
     * @param chapter
     */
    void showChapter(boolean reset,ReaderView readerView, int jumpCharPosition,int page,Chapter chapter);

}
