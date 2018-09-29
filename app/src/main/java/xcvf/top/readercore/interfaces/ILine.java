package xcvf.top.readercore.interfaces;

import java.util.List;

/**
 * 每一行
 * Created by xiaw on 2018/7/11.
 */
public interface ILine {
    void addChar(IChar iChar);

    List<IChar> getChars();
}
