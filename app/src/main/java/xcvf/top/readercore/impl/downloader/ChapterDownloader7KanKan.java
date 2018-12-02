package xcvf.top.readercore.impl.downloader;

import android.content.Context;

import java.util.ArrayList;

import xcvf.top.readercore.impl.path.PathGeneratorFactory;


/**
 * 下载文件 7kankan
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
