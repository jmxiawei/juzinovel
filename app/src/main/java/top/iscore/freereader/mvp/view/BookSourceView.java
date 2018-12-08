package top.iscore.freereader.mvp.view;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import java.util.List;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookCate;

/**
 * Created by xiaw on 2018/9/18.
 */
public interface BookSourceView extends MvpView {


    void onLoadAllSource(List<Book> books);

}
