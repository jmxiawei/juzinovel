package xcvf.top.readercore.impl.chapterlistparser;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.impl.ChapterParserFactory;
import xcvf.top.readercore.impl.FileDownloader;
import xcvf.top.readercore.impl.path.PathGeneratorFactory;
import xcvf.top.readercore.interfaces.IChapterParser;
import xcvf.top.readercore.utils.Constant;

public class BaseChapterParser implements IChapterParser {

    private HashMap<String,Integer> listName = new HashMap<>();

    /**
     * 文件过期时间，3小时
     */
    public static final long EXPIRES_MILS = 1000 * 60 * 60 * 12L;

    protected int getChapterId(String url) {
        if (TextUtils.isEmpty(url)) {
            return 0;
        } else {
            String[] strings = url.split("/");
            if (strings.length > 0) {
                try {
                    String filename = strings[strings.length - 1];
                    String fileid = filename.split("\\.")[0];
                    return Integer.parseInt(fileid);
                } catch (Exception e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public Chapter getEmptyChapter(Book book){
        Chapter chapter = new Chapter();
        chapter.setBookid(book.bookid);
        chapter.setEngine_domain(book.engine_domain);
        chapter.setExtern_bookid(book.extern_bookid);
        return chapter;
    }


    /**
     *
     * @param engine
     * @return
     */
    protected HashMap<String,String> getHeaders(String engine){


        return null;

    }

    /**
     * 下载章节文件
     *
     * @param url
     * @return
     */
    @Override
    public File getChapterFile(Context context,String url) {
        String path = PathGeneratorFactory.get().generate(context,url);
        File file = new File(path);
        long current = System.currentTimeMillis();
        if (file.exists()) {
            if (current - file.lastModified() < EXPIRES_MILS) {
                //3小时
                LogUtils.e("文件未过期，先不更新");
                return file;
            } else {
                file.delete();
            }
        }
        boolean ok = FileDownloader.downloadUrl(url, path);
        if (ok) {
            return file;
        }
        return null;
    }

    @Override
    public List<Chapter> parser(Context context, Book book, String url) {

        File file = getChapterFile(context,url);
        if(file!=null){

        }

        return null;
    }

    /**
     * 是否需要添加到列表
     * @param chapter
     * @return
     */
    public boolean checkAddToList(Chapter chapter){
        return !listName.containsKey(chapter.chapter_name);
    }


}
