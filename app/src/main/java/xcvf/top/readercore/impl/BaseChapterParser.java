package xcvf.top.readercore.impl;

import android.content.Context;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.vector.update_app.utils.Md5Util;

import java.io.File;
import java.util.List;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.interfaces.IChapterParser;
import xcvf.top.readercore.utils.Constant;

public class BaseChapterParser implements IChapterParser {


    /**
     * 文件过期时间，3小时
     */
    public static final long EXPIRES_MILS = 10800000L;

    /**
     * 下载章节文件
     *
     * @param url
     * @return
     */
    @Override
    public File getChapterFile(Context context, String url) {
        String path = Constant.getCachePath(context, EncryptUtils.encryptMD5ToString(url));
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
}
