package xcvf.top.readercore.interfaces;

import java.util.List;

import xcvf.top.readercore.bean.Chapter;

/**章节
 * Created by xiaw on 2018/10/25.
 */
public interface IChapterProvider {

    int TYPE_NEXT = 1;
    int TYPE_PRE = 2;
    int TYPE_DETAIL = 3;
    void getChapter(int type,String bookid,String chapterid,Chapter chapter,IChapterListener chapterListener);

    void saveChapter(List<Chapter> chapterList,IChapterListener chapterListener);

}
