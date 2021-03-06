package xcvf.top.readercore.impl;

import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xcvf.top.readercore.interfaces.DownloadListener;
import xcvf.top.readercore.utils.Constant;

/**
 * 文件下载
 * Created by xiaw on 2018/9/28.
 */

public class FileDownloader {

    /**
     * @param url
     * @param destPath
     */
    public static void download(final String url, final String destPath, final DownloadListener downloadListener) {

        Task.callInBackground(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (downloadUrl(url, destPath)) return destPath;
                return null;
            }
        }).continueWith(new Continuation<String, Object>() {
            @Override
            public Object then(Task<String> task) throws Exception {

                String result = task.getResult();
                if (result != null) {
                    if (downloadListener != null) {
                        downloadListener.onDownload(0, result);
                    }
                } else {
                    if (downloadListener != null) {
                        downloadListener.onDownload(1, null);
                    }
                }

                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

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


    public static boolean downloadUrl(String url, String destPath) {
        return downloadUrl(url, destPath, null);
    }


}
