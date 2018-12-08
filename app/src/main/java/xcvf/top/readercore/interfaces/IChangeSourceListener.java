package xcvf.top.readercore.interfaces;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookMark;

/**
 * 切换来源
 */
public interface IChangeSourceListener {



    void onChangeSource(Book book, BookMark bookMark);

}
