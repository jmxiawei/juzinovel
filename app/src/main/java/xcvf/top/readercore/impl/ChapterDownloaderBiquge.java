package xcvf.top.readercore.impl;

import android.content.Context;

import com.vector.update_app.utils.Md5Util;

import java.util.ArrayList;

import xcvf.top.readercore.utils.Constant;


/**
 * 下载文件
 */
public class ChapterDownloaderBiquge extends BaseChapterFileDownloader {


    private ChapterDownloaderBiquge(String engine) {
        super(engine);
    }


    public static BaseChapterFileDownloader newOne(String engine) {
        return new ChapterDownloaderBiquge(engine);
    }

    @Override
    public ArrayList<String> download(Context context, String chapter_url) {
        String path = Constant.getCachePath(context, Md5Util.bytes2Hex(chapter_url.getBytes()));
        boolean ok = downloadUrl(chapter_url, path, buildHeader());
        if (ok) {
            ArrayList<String> list = new ArrayList<>();
            list.add(path);
            return list;
        }
        return null;
    }
}
