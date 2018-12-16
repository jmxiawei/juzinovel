package xcvf.top.readercore.bean;

import android.support.v4.view.ViewPager;

/**
 * 翻页动画
 */
public class AnimItem {


    public String key;
    public String name;
    public ViewPager.PageTransformer pageTransformer;

    public AnimItem(String key, String name, ViewPager.PageTransformer pageTransformer) {
        this.key = key;
        this.name = name;
        this.pageTransformer = pageTransformer;
    }


}
