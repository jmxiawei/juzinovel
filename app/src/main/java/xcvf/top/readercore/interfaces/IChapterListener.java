package xcvf.top.readercore.interfaces;

import xcvf.top.readercore.bean.Chapter;

/**
 * 获取章节
 * Created by xiaw on 2018/10/25.
 */
public interface IChapterListener {
    /**
     *
     * @param srcChapter
     * @param destChapter
     */
    void onChapter(Chapter srcChapter,Chapter destChapter);
}
