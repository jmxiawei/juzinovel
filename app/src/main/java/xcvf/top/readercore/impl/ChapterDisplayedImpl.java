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
        LogUtils.e("[start load chapter " + chapter.chapter_name + "]");
        addEmptyPage(chapter, readerView, reset, jumpCharPosition, page, Page.LOADING_PAGE);
        final String url = chapter.getFullPath();
        if (BaseChapterFileDownloader.isInTask(url)) {
            return;
        }
        //LogUtils.e(url);
        Task.callInBackground(new Callable<ArrayList<Page>>() {
            @Override
            public ArrayList<Page> call() throws Exception {
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
                final ArrayList<Page> list = task.getResult();
                if (list != null && list.size() > 0) {
                    Chapter chapter1 = cppyChapter(chapter);
                    chapter1.setStatus(Chapter.STATUS_OK);
                    chapter1.setPages(task.getResult());
                    readerView.setChapter(reset, chapter1, jumpCharPosition, page);
                    LogUtils.e("[success load chapter " + chapter.chapter_name + "]");
                } else {
                    LogUtils.e("[fail load chapter " + chapter.chapter_name + "]");
                    addEmptyPage(chapter, readerView, reset, jumpCharPosition, page, Page.ERROR_PAGE);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

    }


    private Chapter cppyChapter(Chapter src) {
        Chapter chapter = new Chapter();
        chapter.chapter_name = src.chapter_name;
        chapter.chapterid = src.chapterid;
        chapter.bookid = src.bookid;
        chapter.engine_domain = src.engine_domain;
        chapter.extern_bookid = src.extern_bookid;
        chapter.self_page = src.self_page;

        return chapter;
    }

    /**
     * 添加一个占位的页面
     *
     * @param chapter          章节
     * @param readerView
     * @param reset
     * @param jumpCharPosition
     * @param page
     * @param status           -1 加载中 -2 错误
     */
    public void addEmptyPage(Chapter chapter, ReaderView readerView, boolean reset, int jumpCharPosition, int page, int status) {
        Page errorPage = new Page();
        errorPage.setChapterid(chapter.chapterid);
        List<Page> pages = new ArrayList<>();
        pages.add(errorPage);
        chapter.setPages(pages);
        errorPage.setStatus(status);
        errorPage.setIndex(1);
        chapter.setStatus(Chapter.STATUS_ERROR);
        readerView.setChapter(reset, chapter, jumpCharPosition, page);
    }

}

