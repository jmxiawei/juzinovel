package xcvf.top.readercore.impl;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import top.iscore.freereader.App;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.interfaces.DownloadListener;
import xcvf.top.readercore.interfaces.IDisplayer;
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.utils.Constant;
import xcvf.top.readercore.views.ReaderView;

/**
 * 章节显示到视图上
 *
 * @author xiaw
 * @date 2018/9/28
 */
public class ChapterDisplayedImpl implements IDisplayer {


    public static ChapterDisplayedImpl newInsrance() {
        return new ChapterDisplayedImpl();
    }

    @Override
    public void showChapter(final boolean reset, final ReaderView readerView, final boolean toLastPage, final int page, final Chapter chapter) {
        //重新加载
        //下载文件
        FileDownloader.download(App.oss_domain + chapter.self_page, Constant.getCachePath(readerView.getContext(), chapter.self_page), new DownloadListener() {
            @Override
            public void onDownload(int status, final String path) {
                if (status == 0) {
                    //下载成功
                    final TextConfig config = TextConfig.getConfig();
                    Task.callInBackground(new Callable<List<IPage>>() {
                        @Override
                        public List<IPage> call() throws Exception {
                            return HtmlPageProvider.newInstance().providerPages(chapter, path, config.pageWidth, config.maxLine(), config.getSamplePaint());

                        }
                    }).continueWith(new Continuation<List<IPage>, Object>() {
                        @Override
                        public Object then(Task<List<IPage>> task) throws Exception {
                            chapter.setPages(task.getResult());
                            readerView.setChapter(reset, chapter, toLastPage, page);
                            return null;
                        }
                    }, Task.UI_THREAD_EXECUTOR);
                } else {
                    //失败
                    Page errorPage = new Page();
                    errorPage.setIndex(Page.ERROR_PAGE);
                    errorPage.setChapterid(String.valueOf(chapter.chapterid));
                    List<IPage> pages = new ArrayList<>();
                    pages.add(errorPage);
                    chapter.setPages(pages);
                    chapter.setStatus(Chapter.STATUS_ERROR);
                    readerView.setChapter(false, chapter, toLastPage, page);
                }
            }
        });
    }

}

