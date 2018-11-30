package xcvf.top.readercore.impl;

import android.content.Context;

import com.blankj.utilcode.util.FileUtils;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xcvf.top.readercore.interfaces.ChapterFileDownloader;
import xcvf.top.readercore.utils.Constant;

public class BaseChapterFileDownloader implements ChapterFileDownloader {


    String engine;

    protected BaseChapterFileDownloader(String engine) {
        this.engine = engine;
    }


    public static BaseChapterFileDownloader newOne(String engine) {
        return new BaseChapterFileDownloader(engine);
    }


    /**
     * 构造请求头部
     *
     * @param
     * @return
     */
    protected HashMap<String, String> buildHeader() {

        if(engine.equals(ChapterParserFactory.ENGINE.KANKAN)){

        }

        return null;
    }


    /**
     * 下载文件
     *
     * @param url
     * @param destPath
     * @return
     */
    public static boolean downloadUrl(String url, String destPath, HashMap<String, String> headers) {

        try {
            Headers.Builder builder1 = null;
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (headers != null) {
                builder1 = new Headers.Builder();
                for (Map.Entry<String, String> p :
                        headers.entrySet()) {
                    builder1.add(p.getKey(), p.getValue());
                }
            }
            Request request;
            if (builder1 != null) {
                request = new Request.Builder().url(url).headers(builder1.build()).build();
            } else {
                request = new Request.Builder().url(url).build();
            }

            FileUtils.createOrExistsDir(Constant.getDir(destPath));
            File file = new File(destPath);
            if (file.exists()) {
                return true;
            }
            Response response = builder.build().newCall(request).execute();
            if (response.code() == 200) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = response.body().byteStream();
                int len;
                byte[] buff = new byte[16 * 1024];
                while ((len = inputStream.read(buff)) != -1) {
                    fileOutputStream.write(buff, 0, len);
                }
                fileOutputStream.close();
                inputStream.close();
                file.setLastModified(System.currentTimeMillis());
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public ArrayList<String> download(Context context,String chapter_url) {
        return null;
    }
}
