package xcvf.top.readercore.impl;

import com.blankj.utilcode.util.LogUtils;

import top.iscore.freereader.App;
import xcvf.top.readercore.bean.Chapter;
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
    public void showChapter(final boolean reset, final ReaderView readerView, final boolean toLastPage, int page, final Chapter chapter) {
        LogUtils.e("show chapter "+chapter.chapter_name);
        //重新加载
        //下载文件
        FileDownloader.download(App.oss_domain + chapter.self_page, Constant.getCachePath(readerView.getContext(), chapter.self_page), new DownloadListener() {
            @Override
            public void onDownload(int status, String path) {
                if (status == 0) {
                    //下载成功
                    TextConfig config = TextConfig.getConfig();
                    chapter.setPages(HtmlPageProvider.newInstance().providerPages(path, config.pageWidth, config.maxLine(), config.getSamplePaint()));
                    readerView.setChapter(reset,chapter, toLastPage, IPage.LOADING_PAGE);
                }
            }
        });
    }

}

