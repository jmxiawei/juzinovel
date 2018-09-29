package xcvf.top.readercore.interfaces;

import java.util.List;

/**
 * 一页数据的内容
 *
 * @author xiaw
 * @date 2018/7/11
 */
public interface IPage {

    int getIndex();
    int getTotalPage();
     void setTotalPage(int totalPage);
    void addLines(ILine line);
    List<ILine> getLines();
}
