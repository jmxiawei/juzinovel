package xcvf.top.readercore.impl;

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
        addEmptyPage(chapter, readerView, reset, jumpCharPosition, page);
        final String url = chapter.getFullPath();
        LogUtils.e(url);
        Task.callInBackground(new Callable<ArrayList<Page>>() {
            @Override
            public ArrayList<Page> call() throws Exception {
                if (BaseChapterFileDownloader.isInTask(url)) {
                    throw new Exception("pass");
                }
                ChapterFileDownloader downloader = ChapterParserFactory.getDownloader(chapter.engine_domain);
                if (downloader != null) {
                    ArrayList<String> list = downloader.download(readerView.getContext(), url);
                    TextConfig config = TextConfig.getConfig();
                    return HtmlPageProvider.newInstance().providerPages(chapter, list, config.pageWidth, config.maxLine(), config.getSamplePaint());
                }
                return null;
            }
        }).continueWith(new Continuation<ArrayList<Page>, Object>() {
            @Override
            public Object then(Task<ArrayList<Page>> task) throws Exception {
                if (task.getError() != null &&
                        "pass".equals(task.getError().getMessage())) {
                    //异常表示pass，忽略
                    return null;
                }
                final ArrayList<Page> list = task.getResult();
                if (list != null && list.size() > 0) {
                    chapter.setPages(task.getResult());
                    readerView.setChapter(reset, chapter, jumpCharPosition, page);
                } else {
                    addEmptyPage(chapter, readerView, reset, jumpCharPosition, page);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

    }

    private void addEmptyPage(Chapter chapter, ReaderView readerView, boolean reset, int jumpCharPosition, int page) {
        Page errorPage = new Page();
        errorPage.setIndex(Page.ERROR_PAGE);
        errorPage.setChapterid(chapter.chapterid);
        List<Page> pages = new ArrayList<>();
        pages.add(errorPage);
        chapter.setPages(pages);
        chapter.setStatus(Chapter.STATUS_ERROR);
        readerView.setChapter(reset, chapter, jumpCharPosition, page);
    }

}

