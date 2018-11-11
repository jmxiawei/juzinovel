package xcvf.top.readercore.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;
import top.iscore.freereader.mvp.presenters.BookReadPresenter;
import top.iscore.freereader.mvp.presenters.BookShelfPresenter;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.daos.DBManager;
import xcvf.top.readercore.daos.DaoSession;
import xcvf.top.readercore.impl.FileDownloader;
import xcvf.top.readercore.utils.Constant;

/**
 * 下载
 */
public class DownloadIntentService extends IntentService {

    public static String PROGRESS = "DownloadIntentService.progress";
    ArrayList<Chapter> chapterList;
    int mCount;
    int finishCount = 0;
    Chapter chapter;
    Book  mBook;
    BookReadPresenter bookReadPresenter;
    /**
     * 启动一个下载任务
     *
     * @param context
     */
    public static void startDownloadService(Context context, Chapter chapter, ArrayList<Chapter> chapterList) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.putExtra("chapters", chapterList);
        intent.putExtra("chapter", chapter);
        context.startService(intent);
    }


    /**
     * 启动一个下载全本
     *
     * @param context
     */
    public static void startDownloadService(Context context,Book book) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.putExtra("book", book);
        context.startService(intent);
    }


    public DownloadIntentService() {
        super("DownloadIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (intent == null) {
            return;
        }
        mBook =  intent.getParcelableExtra("book");
        if(mBook != null){
            bookReadPresenter = new BookReadPresenter();
            chapterList = bookReadPresenter.loadChaptersSync(mBook,0);
            if(chapterList!=null && chapterList.size()>0){
                chapter = chapterList.get(0);
            }
        }
        chapterList = intent.getParcelableArrayListExtra("chapters");
        chapter = intent.getParcelableExtra("chapter");
        DaoSession session = DBManager.getDaoMaster().newSession();
        mCount = chapterList == null ? 0 : chapterList.size();
        for (int i = 0; i < mCount; i++) {
            final Chapter chapter = chapterList.get(i);
            String dest = Constant.getCachePath(getBaseContext(), chapter.self_page);
            boolean result = FileDownloader.downloadUrl(Constant.buildChapterFilePath(chapter.self_page), dest);
            if (result) {
                chapter.setIs_download(true);
                session.getChapterDao().insertOrReplace(chapter);
            }
            Intent itn = new Intent(PROGRESS);
            finishCount++;
            itn.putExtra("info", "正在下载: " + finishCount + "/" + mCount);
            itn.putExtra("bookid", chapter.extern_bookid);
            itn.putExtra("finish", finishCount == mCount ? 1 : 0);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(itn);
        }
    }
}
