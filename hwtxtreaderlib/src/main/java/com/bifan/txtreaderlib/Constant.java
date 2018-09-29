package com.bifan.txtreaderlib;

import android.os.Environment;

/**
 * Created by xiaw on 2018/6/27.
 */

public class Constant {

    public static final String BOOK_ROOT_FOLDER = "/freeBook/";


    public static String getChapterPath(String sub) {
        return Environment.getExternalStorageDirectory() + BOOK_ROOT_FOLDER + sub;

    }

}
