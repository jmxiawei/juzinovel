package xcvf.top.readercore.impl;

import android.os.SystemClock;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.daos.ChapterDao;
import xcvf.top.readercore.daos.DBManager;
import xcvf.top.readercore.interfaces.IChapterListener;
import xcvf.top.readercore.interfaces.IChapterProvider;

/**
 * 获取章节
 * Created by xiaw on 2018/10/25.
 */
public class ChapterProviderImpl implements IChapterProvider {

    public static final String KEY_CHAPTER_LIST = "chapters";

    public static ChapterProviderImpl newInstance() {
        return new ChapterProviderImpl();
    }

    @Override
    public void getChapter(final int type, final String extern_bookid, final String chapterid, final Chapter chapter, final IChapterListener chapterListener) {
        Task.callInBackground(new Callable<Chapter>() {
            @Override
            public Chapter call() throws Exception {
                if (type == IChapterProvider.TYPE_NEXT) {
                    return Chapter.getNextChapter(extern_bookid, chapterid);
                } else if (type == IChapterProvider.TYPE_PRE) {
                    return Chapter.getPreChapter(extern_bookid, chapterid);
                } else if (type == IChapterProvider.TYPE_DETAIL) {
                    return Chapter.getChapter(extern_bookid, chapterid);
                }
                return null;
            }
        }).continueWith(new Continuation<Chapter, Chapter>() {
            @Override
            public Chapter then(Task<Chapter> task) throws Exception {
                Chapter chapter1 = task.getResult();
                if (chapterListener != null) {
                    chapterListener.onChapter(IChapterListener.CODE_OK, chapter, chapter1, null);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }



    @Override
    public void saveChapter(final String bookid, final List<Chapter> chapterList, final IChapterListener chapterListener) {
        Task.callInBackground(new Callable<List<Chapter>>() {
            @Override
            public List<Chapter> call() throws Exception {
                // 第一次 直接使用原始列表
                // 第二次 先查所有的，再插入
                long start = SystemClock.elapsedRealtime();
                ChapterDao chapterDao = DBManager.
                        getDaoMaster().
                        newSession().
                        getChapterDao();
                List<Chapter> chapters = chapterList;
                if (chapterList != null) {
                    chapterDao.queryBuilder().where(ChapterDao.Properties.Extern_bookid.eq(bookid))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                    chapterDao.insertOrReplaceInTx(chapterList);
                } else {
                    chapters = chapterDao.queryBuilder()
                            .where(ChapterDao.Properties.Extern_bookid.eq(bookid))
                            .orderAsc(ChapterDao.Properties.Chapterid).list();
                }
                LogUtils.e("finish save chapter =" + ((SystemClock.elapsedRealtime() - start)/1000.f));
                return chapters;
            }
        }).continueWith(new Continuation<List<Chapter>, Object>() {
            @Override
            public Object then(Task<List<Chapter>> task) throws Exception {
                if (chapterListener != null) {
                    if (task.getError() != null) {
                        LogUtils.e(task.getError());
                    }
                    HashMap<String, Object> params = new HashMap<>(1);
                    params.put(KEY_CHAPTER_LIST, task.getResult());
                    chapterListener.onChapter(IChapterListener.CODE_OK, null, null, params);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
