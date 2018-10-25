package xcvf.top.readercore.impl;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
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
    public void getChapter(final int type, final String chapterid, final Chapter chapter, final IChapterListener chapterListener) {
        Task.callInBackground(new Callable<Chapter>() {
            @Override
            public Chapter call() throws Exception {
                if (type == IChapterProvider.TYPE_NEXT) {
                    Chapter next = Chapter.getNextChapter(chapterid);
                    if (next == null) {
                        //接口请求
                        // BaseHttpHandler.create().getProxy(BookService.class).
                    }
                    return next;
                } else if (type == IChapterProvider.TYPE_PRE) {
                    Chapter pre = Chapter.getPreChapter(chapterid);
                    return pre;
                } else if (type == IChapterProvider.TYPE_DETAIL) {
                    Chapter chp = Chapter.getChapter(chapterid);
                    return chp;
                }
                return null;
            }
        }).continueWith(new Continuation<Chapter, Chapter>() {
            @Override
            public Chapter then(Task<Chapter> task) throws Exception {
                Chapter chapter1 = task.getResult();
                if (chapterListener != null) {
                    chapterListener.onChapter(chapter, chapter1);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
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
                    chapterListener.onChapter(null, null);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
