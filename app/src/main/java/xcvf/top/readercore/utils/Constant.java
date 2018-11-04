package xcvf.top.readercore.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import top.iscore.freereader.App;

/**
 * Created by xiaw on 2018/6/27.
 */

public class Constant {

    public static final String BOOK_ROOT_FOLDER = "/freeBook/";

    public static final String ACTION_SWITCH_MODE = "ACTION_SWITCH_MODE";

    public static String getChapterPath(String sub) {
        return Environment.getExternalStorageDirectory() + BOOK_ROOT_FOLDER + sub;
    }

    public static String getDir(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(0, index);
    }

    public static String getCachePath(Context context, String sub) {
        File file = new File(context.getCacheDir(), sub);
        return file.getAbsolutePath();
    }
    /**
     * 章节文章
     * @param self_page
     * @return
     */
    public static final String buildChapterFilePath(String self_page){
        return  App.oss_domain + self_page;
    }

}
