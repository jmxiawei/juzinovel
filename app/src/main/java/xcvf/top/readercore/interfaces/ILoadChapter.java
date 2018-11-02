package xcvf.top.readercore.interfaces;

import xcvf.top.readercore.bean.Chapter;

/**加载一个章节
 * Created by xiaw on 2018/11/2.
 */
public interface ILoadChapter {


    /**
     * 加载一个章节
     * @param type
     * @param chapter
     */
    void load(int type, Chapter chapter);

}
