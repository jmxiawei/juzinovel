package xcvf.top.readercore.impl.downloader;

import android.content.Context;

import java.util.ArrayList;

import xcvf.top.readercore.impl.path.PathGeneratorFactory;


/**
 * 下载文件 59小说
 */
public class ChapterDownloader59XS extends BaseChapterFileDownloader {


    private ChapterDownloader59XS(String engine) {
        super(engine);
    }


    public static BaseChapterFileDownloader newOne(String engine) {
        return new ChapterDownloader59XS(engine);
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
