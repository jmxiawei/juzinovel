package xcvf.top.readercore.impl;

import android.util.Base64;

import com.blankj.utilcode.util.LogUtils;
import com.vector.update_app.utils.Md5Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
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


    public static ChapterDisplayedImpl newInstance() {
        return new ChapterDisplayedImpl();
    }

    @Override
    public void showChapter(final boolean reset, final ReaderView readerView, final int jumpCharPosition, final int page, final Chapter chapter) {
        //重新加载
        //下载文件
        String self_page = new String(Base64.decode(chapter.getSelf_page(), Base64.DEFAULT));
        String url = chapter.engine_domain + self_page;
        if (self_page.startsWith("http")) {
            url = self_page;
        }
        LogUtils.e(url);
        FileDownloader.download(url, Constant.getCachePath(readerView.getContext(), Md5Util.bytes2Hex(self_page.getBytes())), new DownloadListener() {
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
                            readerView.setChapter(reset, chapter, jumpCharPosition, page);
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
                    readerView.setChapter(reset, chapter, jumpCharPosition, page);
                }
            }
        });
    }

}

