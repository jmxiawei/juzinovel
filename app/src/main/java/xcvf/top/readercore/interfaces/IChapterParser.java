package xcvf.top.readercore.interfaces;

import android.content.Context;

import java.io.File;
import java.util.List;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;

/**
 * 书籍的目录读取书籍
 */
public interface IChapterParser {

    File getChapterFile(Context context,String url);

    List<Chapter> parser(Context context, Book book, String url);

}
