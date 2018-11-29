package xcvf.top.readercore.interfaces;

import android.content.Context;

import java.util.ArrayList;

/**
 * 章节下载，可能一个章节分多个文件
 */
public interface ChapterFileDownloader {


    ArrayList<String> download(Context context,String chapter_url);
}
