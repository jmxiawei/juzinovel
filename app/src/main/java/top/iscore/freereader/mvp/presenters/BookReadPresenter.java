package top.iscore.freereader.mvp.presenters;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import top.iscore.freereader.mvp.view.BookReadView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;

/**
 * 阅读页面
 * Created by xiaw on 2018/9/18.
 */
public class BookReadPresenter extends MvpBasePresenter<BookReadView> {


    public void loadChapters(Book book) {

        ifViewAttached(new ViewAction<BookReadView>() {
            @Override
            public void run(@NonNull BookReadView view) {
                view.showLoading(false);
            }
        });

        Call<BaseModel<ArrayList<Chapter>>> baseModelCall = BaseHttpHandler.create().getProxy(BookService.class).getChapterList("Book.GetChapters", book.extern_bookid, 0);
        baseModelCall.enqueue(new Callback<BaseModel<ArrayList<Chapter>>>() {
            @Override
            public void onResponse(Call<BaseModel<ArrayList<Chapter>>> call, final Response<BaseModel<ArrayList<Chapter>>> response) {

                if (response != null && response.isSuccessful()) {
                    final BaseModel<ArrayList<Chapter>> baseModel = response.body();
                    if (baseModel.getCode() == 0) {
                        ifViewAttached(new ViewAction<BookReadView>() {
                            @Override
                            public void run(@NonNull BookReadView view) {
                                view.onLoadChapterList(baseModel.getData());
                            }
                        });
                    } else {
                        ifViewAttached(new ViewAction<BookReadView>() {
                            @Override
                            public void run(@NonNull BookReadView view) {
                                view.onLoadChapterList(null);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<ArrayList<Chapter>>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookReadView>() {
                    @Override
                    public void run(@NonNull BookReadView view) {
                        view.onLoadChapterList(null);
                    }
                });
            }
        });
    }

}
