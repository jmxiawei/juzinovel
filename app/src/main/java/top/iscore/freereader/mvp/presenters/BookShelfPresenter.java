package top.iscore.freereader.mvp.presenters;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import top.iscore.freereader.mvp.view.BookShelfView;
import xcvf.top.readercore.bean.Book;

/**
 * 书架
 * Created by xiaw on 2018/9/18.
 */
public class BookShelfPresenter extends MvpBasePresenter<BookShelfView> {


    /**
     * 获取书架数据
     *
     * @param userid
     */
    public void loadBookShelf(String userid) {

        Call<BaseModel<ArrayList<Book>>> resCall = BaseHttpHandler.create().getProxy(BookService.class).getBookShelf("Book.getBookshelf", userid);
        resCall.enqueue(new Callback<BaseModel<ArrayList<Book>>>() {
            @Override
            public void onResponse(Call<BaseModel<ArrayList<Book>>> call, final Response<BaseModel<ArrayList<Book>>> response) {

                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.setData(response.body().getData());
                    }
                });

            }

            @Override
            public void onFailure(Call<BaseModel<ArrayList<Book>>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.setData(null);
                    }
                });
            }
        });
    }


    /**
     * 新增书架
     *
     * @param userid
     */
    public void addBookShelf(String userid, String bookid) {

        Call<BaseModel<String>> resCall = BaseHttpHandler.create().getProxy(BookService.class).addBookShelf("Book.getBookshelf", userid, bookid);
        resCall.enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, final Response<BaseModel<String>> response) {

                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.setData(null);
                    }
                });

            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.setData(null);
                    }
                });
            }
        });

    }

    /**
     * 删除书架
     *
     * @param userid
     */
    public void deleteBookShelf(String userid, String bookid) {

        Call<BaseModel<String>> resCall = BaseHttpHandler.create().getProxy(BookService.class).addBookShelf("Book.getBookshelf", userid, bookid);
        resCall.enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, final Response<BaseModel<String>> response) {

                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.setData(null);
                    }
                });

            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.setData(null);
                    }
                });
            }
        });

    }


}
