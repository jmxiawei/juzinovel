package xcvf.top.readercore.interfaces;

import android.graphics.Paint;

import java.util.List;

/**将一个章节分割成多页
 * Created by xiaw on 2018/9/27.
 */
public interface IPageProvider {

    /**
     * 将一个章节的内容分割成多页
     * @param chapterContent
     * @return
     */
    List<IPage> providerPages(String chapterContent, int maxWidth, int maxLines, Paint paint);

}
