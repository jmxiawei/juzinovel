package xcvf.top.readercore.interfaces;

import android.support.v4.widget.ContentLoadingProgressBar;

import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.views.ReaderView;

/**
 * 显示器，将章节内容显示出来
 * Created by xiaw on 2018/9/28.
 */
public interface IDisplayer {

    void showChapter(ReaderView readerView,boolean fromLast,Chapter chapter);

}
