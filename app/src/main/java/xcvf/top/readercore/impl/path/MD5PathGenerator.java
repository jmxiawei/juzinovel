package xcvf.top.readercore.impl.path;

import android.content.Context;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;

/**
 * 路径生成器
 */
public class MD5PathGenerator extends BasePathGenerator{

    @Override
    public String generate(Context context, String path) {
        String dir  = EncryptUtils.encryptMD5ToString(getDir(path),"free");
        String r =  new File(getCachePath(context,dir), EncryptUtils.encryptMD5ToString(path,"free")).getAbsolutePath();
        LogUtils.e("path="+path+",r="+r);
        return  r;
    }
}
