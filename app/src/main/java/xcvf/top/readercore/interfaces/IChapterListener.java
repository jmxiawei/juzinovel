package xcvf.top.readercore.interfaces;

import xcvf.top.readercore.bean.Chapter;

/**
 * 获取章节
 * Created by xiaw on 2018/10/25.
 */
public interface IChapterListener {

    int CODE_OK = 0;
    int CODE_ERROR = 1;
    int CODE_NO_NET = 2;


    /**
     * @param srcChapter
     * @param destChapter
     */
    void onChapter(int code, Chapter srcChapter, Chapter destChapter);
}
