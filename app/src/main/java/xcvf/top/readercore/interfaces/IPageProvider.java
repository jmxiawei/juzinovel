package xcvf.top.readercore.interfaces;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;

/**将一个章节分割成多页
 * Created by xiaw on 2018/9/27.
 */
public interface IPageProvider {

    /**
     * 将一个章节的内容分割成多页
     * @return
     */
    List<IPage> providerPages(Chapter chapter, ArrayList<String> filelist, int maxWidth, int maxLines, Paint paint);

}
