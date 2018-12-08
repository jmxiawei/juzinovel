package top.iscore.freereader.mvp.presenters;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import top.iscore.freereader.mvp.view.BookShelfView;
import top.iscore.freereader.mvp.view.BookSourceView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookCate;

/**
 * 书架
 * Created by xiaw on 2018/9/18.
 */
public class BookSourcePresenter extends MvpBasePresenter<BookSourceView> {


    /**
     * 获取书架数据
     *
     * @param
     */
    public void loadBookSource(String name ,String author) {

        Call<BaseModel<ArrayList<Book>>> resCall = BaseHttpHandler.create().getProxy(BookService.class).listSource("Book.ListSource",name,author);
        resCall.enqueue(new Callback<BaseModel<ArrayList<Book>>>() {
            @Override
            public void onResponse(Call<BaseModel<ArrayList<Book>>> call, final Response<BaseModel<ArrayList<Book>>> response) {

                ifViewAttached(new ViewAction<BookSourceView>() {
                    @Override
                    public void run(@NonNull BookSourceView view) {
                        if (response != null && response.isSuccessful()) {
                            view.onLoadAllSource(response.body().getData());
                        } else {
                            view.onLoadAllSource(null);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<BaseModel<ArrayList<Book>>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookSourceView>() {
                    @Override
                    public void run(@NonNull BookSourceView view) {
                        view.onLoadAllSource(null);
                    }
                });
            }
        });
    }


}
