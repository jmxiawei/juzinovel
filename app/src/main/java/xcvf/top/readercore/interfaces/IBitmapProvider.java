package xcvf.top.readercore.interfaces;

import android.graphics.Bitmap;

/**bitmap生成器
 * Created by xiaw on 2018/7/11.
 */
public interface IBitmapProvider {

    /**
     * 当前页
     * @return
     */
    Bitmap getPage(IPage page);

    /**
     * 背景
     * @return
     */
    Bitmap getBackground();

}
