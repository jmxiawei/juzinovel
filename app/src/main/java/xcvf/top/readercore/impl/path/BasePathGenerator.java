package xcvf.top.readercore.impl.path;

import android.content.Context;

import java.io.File;

import xcvf.top.readercore.interfaces.IPathGenerator;

public class BasePathGenerator implements IPathGenerator {

    public static String getDir(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(0, index);
    }

    public static String getCachePath(Context context, String sub) {
        File file = new File(context.getCacheDir(), sub);
        return file.getAbsolutePath();
    }

    @Override
    public String generate(Context context, String path) {
        return null;
    }
}
