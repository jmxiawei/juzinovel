package top.iscore.freereader.mvp.view;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

import xcvf.top.readercore.bean.Book;

/**搜索
 * Created by xiaw on 2018/11/2.
 */
public interface SearchView extends MvpView {

    void onLoad(List<Book> books);
    void showLoading();
    void dismissLoading();
}
