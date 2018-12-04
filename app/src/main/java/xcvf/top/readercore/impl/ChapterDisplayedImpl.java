package xcvf.top.readercore.impl;

import android.util.Base64;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.impl.downloader.BaseChapterFileDownloader;
import xcvf.top.readercore.interfaces.ChapterFileDownloader;
import xcvf.top.readercore.interfaces.IDisplayer;
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.views.ReaderView;

/**
 * 章节显示到视图上
 *
 * @author xiaw
 * @date 2018/9/28
 */
public class ChapterDisplayedImpl implements IDisplayer {


    public static ChapterDisplayedImpl newInstance() {
        return new ChapterDisplayedImpl();
    }

    @Override
    public void showChapter(final boolean reset, final ReaderView readerView, final int jumpCharPosition, final int page, final Chapter chapter) {
        //重新加载
        //下载文件

        boolean needReload = readerView.needReload(chapter);
        if (!needReload && !reset) {
            LogUtils.e("加载成功.无需加载");
            return;
        }

        final String url = chapter.getFullPath();
        LogUtils.e(url);
        Task.callInBackground(new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() throws Exception {

                if (BaseChapterFileDownloader.isInTask(url)) {
                    throw new Exception("pass");
                }

                ChapterFileDownloader downloader = ChapterParserFactory.getDownloader(chapter.engine_domain);
                if (downloader != null) {
                    return downloader.download(readerView.getContext(), url);
                }
                return null;
            }
        }).continueWith(new Continuation<ArrayList<String>, Object>() {
            @Override
            public Object then(Task<ArrayList<String>> task) throws Exception {
                if (task.getError() != null &&
                        "pass".equals(task.getError().getMessage())) {
                    //异常表示pass，忽略
                    return null;
                }
                final ArrayList<String> list = task.getResult();
                if (list != null && list.size() > 0) {
                    //下载成功
                    final TextConfig config = TextConfig.getConfig();
                    Task.callInBackground(new Callable<List<IPage>>() {
                        @Override
                        public List<IPage> call() throws Exception {
                            return HtmlPageProvider.newInstance().providerPages(chapter, list, config.pageWidth, config.maxLine(), config.getSamplePaint());
                        }
                    }).continueWith(new Continuation<List<IPage>, Object>() {
                        @Override
                        public Object then(Task<List<IPage>> task) throws Exception {
                            chapter.setPages(task.getResult());
                            readerView.setChapter(reset, chapter, jumpCharPosition, page);
                            return null;
                        }
                    }, Task.UI_THREAD_EXECUTOR);
                } else {
                    Page errorPage = new Page();
                    errorPage.setIndex(Page.ERROR_PAGE);
                    errorPage.setChapterid(String.valueOf(chapter.chapterid));
                    List<IPage> pages = new ArrayList<>();
                    pages.add(errorPage);
                    chapter.setPages(pages);
                    chapter.setStatus(Chapter.STATUS_ERROR);
                    readerView.setChapter(reset, chapter, jumpCharPosition, page);
                }

                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

    }

}

