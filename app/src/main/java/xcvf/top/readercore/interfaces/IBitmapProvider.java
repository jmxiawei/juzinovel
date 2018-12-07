package xcvf.top.readercore.interfaces;

import android.graphics.Bitmap;

import xcvf.top.readercore.bean.Page;

/**bitmap生成器
 * Created by xiaw on 2018/7/11.
 */
public interface IBitmapProvider {

    /**
     * 当前页
     * @return
     */
    Bitmap getPage(Page page);

    /**
     * 背景
     * @return
     */
    Bitmap getBackground();

}
