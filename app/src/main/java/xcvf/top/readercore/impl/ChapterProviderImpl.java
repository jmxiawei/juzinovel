package xcvf.top.readercore.impl;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.interfaces.IChapterListener;
import xcvf.top.readercore.interfaces.IChapterProvider;

/**
 * 获取章节
 * Created by xiaw on 2018/10/25.
 */
public class ChapterProviderImpl implements IChapterProvider {


    public static ChapterProviderImpl newInstance() {
        return new ChapterProviderImpl();
    }

    @Override
    public void getChapter(final int type,final String bookid,final String chapterid, final Chapter chapter, final IChapterListener chapterListener) {
        Task.callInBackground(new Callable<Chapter>() {
            @Override
            public Chapter call() throws Exception {
                if (type == IChapterProvider.TYPE_NEXT) {
                    Chapter next = Chapter.getNextChapter(bookid,chapterid);
                    if (next == null) {
                        //接口请求  0本章节,1上一章节，2下一章节
                        next = getChapterFromNet(chapterid, 2);
                    }
                    return next;
                } else if (type == IChapterProvider.TYPE_PRE) {
                    Chapter pre = Chapter.getPreChapter(bookid,chapterid);
                    if (pre == null) {
                        pre = getChapterFromNet(chapterid, 1);
                    }
                    return pre;
                } else if (type == IChapterProvider.TYPE_DETAIL) {
                    Chapter chp = Chapter.getChapter(bookid,chapterid);
                    if (chp == null) {
                        chp = getChapterFromNet(chapterid, 0);
                    }
                    return chp;
                }
                return null;
            }
        }).continueWith(new Continuation<Chapter, Chapter>() {
            @Override
            public Chapter then(Task<Chapter> task) throws Exception {
                Chapter chapter1 = task.getResult();
                if (chapterListener != null) {
                    chapterListener.onChapter(IChapterListener.CODE_OK, chapter, chapter1);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    /**
     * 网络获取章节
     * @param chapterid
     * @param type
     * @return
     * @throws Exception
     */
    private Chapter getChapterFromNet(String chapterid, int type) throws Exception {
        try {
            Response<BaseModel<Chapter>> chapter = BaseHttpHandler.create().getProxy(BookService.class).getOneChapter("Chapter.getSingleChapter", chapterid, type).execute();
            if (chapter != null && chapter.isSuccessful()) {
                return chapter.body().getData();
            }
        } catch (Exception e) {
            throw new Exception(String.valueOf(IChapterListener.CODE_NO_NET));
        }
        return null;
    }

    @Override
    public void saveChapter(final List<Chapter> chapterList, final IChapterListener chapterListener) {
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Chapter.saveInTx(chapterList);
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                if (chapterListener != null) {
                    chapterListener.onChapter(IChapterListener.CODE_OK, null, null);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
