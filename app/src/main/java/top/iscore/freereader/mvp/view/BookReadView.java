package top.iscore.freereader.mvp.view;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import java.util.ArrayList;

import xcvf.top.readercore.bean.Chapter;

/**书籍阅读
 * Created by xiaw on 2018/9/18.
 */
public interface BookReadView extends MvpLceView<Chapter> {


    /**
     * 加载章节列表
     * @param chapters
     */
    void onLoadChapterList(ArrayList<Chapter> chapters);

}
