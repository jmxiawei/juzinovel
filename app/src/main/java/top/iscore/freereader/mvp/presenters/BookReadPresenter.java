package top.iscore.freereader.mvp.presenters;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
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


    public void loadChapters(Context context, Book book) {
        LogUtils.e();
        loadChapterByUrl(context, book);
    }

    public ArrayList<Chapter> loadChaptersSync(Book book, int startId) {
        return (ArrayList<Chapter>) Chapter.getAllChapter(book.bookid,book.extern_bookid);
    }


    public void saveBookMarker(String chapter_name,String chapterid,String bookid,String extern_bookid,int page){

    }


}
