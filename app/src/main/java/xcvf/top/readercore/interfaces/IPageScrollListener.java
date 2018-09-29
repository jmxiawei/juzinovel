package xcvf.top.readercore.interfaces;

/**页面切换
 * Created by xiaw on 2018/7/11.
 */
public interface IPageScrollListener {


    int CURRENT_CHAPTER = 0;

    int NEXT_CHAPTER = 1;

    int PRE_CHAPTER = 2;



    /**
     * 页面滑动监听
     * @param current 当前页码
     * @param total 总共页码
     * @param nextOrPre  1 下一章节 2 上一章节  0 本章节
     */
    void onScroll(int current, int total, int nextOrPre);

}
