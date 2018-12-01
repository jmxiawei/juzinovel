package xcvf.top.readercore.impl;

import android.content.Context;

import com.vector.update_app.utils.Md5Util;

import java.util.ArrayList;

import xcvf.top.readercore.impl.path.PathGeneratorFactory;
import xcvf.top.readercore.utils.Constant;


/**
 * 下载文件
 */
public class ChapterDownloader7KanKan extends BaseChapterFileDownloader {


    private ChapterDownloader7KanKan(String engine) {
        super(engine);
    }


    public static BaseChapterFileDownloader newOne(String engine) {
        return new ChapterDownloader7KanKan(engine);
    }



    @Override
    public ArrayList<String> download(Context context, String chapter_url) {
        String path = PathGeneratorFactory.get().generate(context,chapter_url);
        boolean ok = downloadUrl(chapter_url, path, buildHeader());
        if (ok) {
            ArrayList<String> list = new ArrayList<>();
            list.add(path);
            return list;
        }
        return null;
    }
}
