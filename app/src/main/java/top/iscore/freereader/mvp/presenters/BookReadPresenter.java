package top.iscore.freereader.mvp.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.iscore.freereader.App;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import top.iscore.freereader.mvp.view.BookReadView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.impl.ChapterParserFactory;

/**
 * 阅读页面
 * Created by xiaw on 2018/9/18.
 */
public class BookReadPresenter extends MvpBasePresenter<BookReadView> {

    /**
     * @param book
     */
    public void loadChapterByUrl(final Context context, final Book book) {

        ifViewAttached(new ViewAction<BookReadView>() {
            @Override
            public void run(@NonNull BookReadView view) {
                view.showLoading(false);
            }
        });
        Task.callInBackground(new Callable<ArrayList<Chapter>>() {
            @Override
            public ArrayList<Chapter> call() throws Exception {
                return (ArrayList<Chapter>) ChapterParserFactory.getChapterParser(book.engine_domain).parser(context, book, book.read_url);

            }
        }).continueWith(new Continuation<ArrayList<Chapter>, Object>() {
            @Override
            public Object then(Task<ArrayList<Chapter>> task) throws Exception {
                final ArrayList<Chapter> chapters = task.getResult();
                ifViewAttached(new ViewAction<BookReadView>() {
                    @Override
                    public void run(@NonNull BookReadView view) {
                        view.onLoadChapterList(chapters);
                    }
                });
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);


    }


    public void loadChapters(Context context, Book book, int startId) {


        loadChapterByUrl(context, book);


//        ifViewAttached(new ViewAction<BookReadView>() {
//            @Override
//            public void run(@NonNull BookReadView view) {
//                view.showLoading(false);
//            }
//        });
//        Call<BaseModel<ArrayList<Chapter>>> baseModelCall = BaseHttpHandler.create().getProxy(BookService.class).getChapterList("Book.GetChapters", book.extern_bookid,startId);
//        baseModelCall.enqueue(new Callback<BaseModel<ArrayList<Chapter>>>() {
//            @Override
//            public void onResponse(Call<BaseModel<ArrayList<Chapter>>> call, final Response<BaseModel<ArrayList<Chapter>>> response) {
//
//                if (response != null && response.isSuccessful()) {
//                    final BaseModel<ArrayList<Chapter>> baseModel = response.body();
//                    if (baseModel.getCode() == 0) {
//                        ifViewAttached(new ViewAction<BookReadView>() {
//                            @Override
//                            public void run(@NonNull BookReadView view) {
//                                view.onLoadChapterList(baseModel.getData());
//                            }
//                        });
//                    } else {
//                        ifViewAttached(new ViewAction<BookReadView>() {
//                            @Override
//                            public void run(@NonNull BookReadView view) {
//                                view.onLoadChapterList(null);
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseModel<ArrayList<Chapter>>> call, Throwable t) {
//                ifViewAttached(new ViewAction<BookReadView>() {
//                    @Override
//                    public void run(@NonNull BookReadView view) {
//                        view.onLoadChapterList(null);
//                    }
//                });
//            }
//        });
    }

    public ArrayList<Chapter> loadChaptersSync(Book book, int startId) {

        ifViewAttached(new ViewAction<BookReadView>() {
            @Override
            public void run(@NonNull BookReadView view) {
                view.showLoading(false);
            }
        });
        Call<BaseModel<ArrayList<Chapter>>> baseModelCall = BaseHttpHandler.create().getProxy(BookService.class).getChapterList("Book.GetChapters", book.extern_bookid, startId);

        try {
            Response<BaseModel<ArrayList<Chapter>>> response = baseModelCall.execute();
            if (response != null && response.isSuccessful()) {
                final BaseModel<ArrayList<Chapter>> baseModel = response.body();
                if (baseModel.getCode() == 0) {
                    return baseModel.getData();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
