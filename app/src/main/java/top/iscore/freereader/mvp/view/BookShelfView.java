package top.iscore.freereader.mvp.view;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import java.util.List;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookCate;
import xcvf.top.readercore.bean.Category;

/**
 * Created by xiaw on 2018/9/18.
 */
public interface BookShelfView extends MvpLceView<List<Book>> {


    void onLoadBookDetail(Book book);

    void onLoadAllCate(List<BookCate> categories);



}
