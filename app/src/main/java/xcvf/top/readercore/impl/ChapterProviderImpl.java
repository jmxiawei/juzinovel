package xcvf.top.readercore.impl;

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
    public void getChapter(final int type, final String bookid, final String chapterid, final Chapter chapter, final IChapterListener chapterListener) {
        Task.callInBackground(new Callable<Chapter>() {
            @Override
            public Chapter call() throws Exception {
                if (type == IChapterProvider.TYPE_NEXT) {
                    Chapter next = Chapter.getNextChapter(bookid, chapterid);
                    if (next == null) {
                        //接口请求  0本章节,1上一章节，2下一章节
                        LogUtils.e("本地没有，从服务器获取");
                        next = getChapterFromNet(chapterid, 2);
                    }
                    return next;
                } else if (type == IChapterProvider.TYPE_PRE) {
                    Chapter pre = Chapter.getPreChapter(bookid, chapterid);
                    if (pre == null) {
                        LogUtils.e("本地没有，从服务器获取");
                        pre = getChapterFromNet(chapterid, 1);
                    }
                    return pre;
                } else if (type == IChapterProvider.TYPE_DETAIL) {
                    Chapter chp = Chapter.getChapter(bookid, chapterid);
                    if (chp == null) {
                        LogUtils.e("本地没有，从服务器获取");
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
                    chapterListener.onChapter(IChapterListener.CODE_OK, chapter, chapter1, null);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    /**
     * 网络获取章节
     *
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
        Task.callInBackground(new Callable<List<Chapter>>() {
            @Override
            public List<Chapter> call() throws Exception {

                // 第一次 直接使用原始列表
                // 第二次 先查所有的，再插入
                LogUtils.e("start read chapter " + System.currentTimeMillis());
                List<Chapter> chapters = Chapter.find(Chapter.class, null, null, null, " chapterid ASC ", null);
                LogUtils.e("finish read chapter " + System.currentTimeMillis());
                if (chapters == null || chapters.size() == 0) {
                    //没有数据
                    chapters = chapterList;
                } else {
                    chapters.addAll(chapterList);
                }
                LogUtils.e("start save chapter " + System.currentTimeMillis());
                Chapter.saveInTx(chapterList);
                LogUtils.e("finish save chapter " + System.currentTimeMillis());
                if (chapterList != null && chapterList.size() > 0) {
                    //保存最大的章节id
                    Chapter chapter = chapterList.get(chapterList.size() - 1);
                    SPUtils.getInstance().put(chapter.extern_bookid, chapter.chapterid);
                }
                return chapters;
            }
        }).continueWith(new Continuation<List<Chapter>, Object>() {
            @Override
            public Object then(Task<List<Chapter>> task) throws Exception {
                if (chapterListener != null) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put(KEY_CHAPTER_LIST, task.getResult());
                    chapterListener.onChapter(IChapterListener.CODE_OK, null, null, params);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
