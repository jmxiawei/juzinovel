package top.iscore.freereader.update;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.vector.update_app.HttpManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Callable;

import bolts.Task;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import xcvf.top.readercore.utils.Constant;

/**
 * 更新
 * Created by xiaw on 2018/11/7.
 */

public class UpdateAppHttpUtil implements HttpManager {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        String vcode = params.get("vcode");
        BaseHttpHandler.create().getProxy(BookService.class).update("User.Update", vcode).enqueue(new retrofit2.Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                if (response != null && response.isSuccessful()) {
                    callBack.onResponse(response.body().getData());
                } else {
                    callBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                callBack.onError(null);
            }
        });


    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, final @NonNull Callback callBack) {
        String vcode = params.get("vcode");
        BaseHttpHandler.create().getProxy(BookService.class).update("User.Update", vcode).enqueue(new retrofit2.Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                if (response != null && response.isSuccessful()) {
                    callBack.onResponse(response.body().getData());
                } else {
                    callBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                callBack.onError(null);
            }
        });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {
        File file = new File(path, fileName);
        downloadUrl(url, file.getAbsolutePath(), callback);
    }

    /**
     * 进度
     */
    private class ProgressRunnable implements Runnable {

        long total;
        float progress;
        FileCallback callback;

        public ProgressRunnable(FileCallback callback, long total, long progress) {
            this.total = total;
            this.progress = progress;
            this.callback = callback;
        }

        @Override
        public void run() {
            if (callback != null) {
                callback.onProgress(progress*1.0f/total, total);
            }
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param destPath
     * @return
     */
    public boolean downloadUrl(String url, String destPath, final HttpManager.FileCallback fileCallback) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Request request = new Request.Builder().url(url).build();
        FileUtils.createOrExistsDir(Constant.getDir(destPath));
        final File file = new File(destPath);
        if (file.exists()) {
            file.delete();
        }
        if (fileCallback != null) {
            fileCallback.onBefore();
        }
        builder.build().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (fileCallback != null) {
                    fileCallback.onError("下载失败！");
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                Task.callInBackground(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        FileOutputStream fileOutputStream = null;
                        InputStream inputStream = null;
                        try {
                            if (response.code() == 200) {
                                fileOutputStream = new FileOutputStream(file);
                                inputStream = response.body().byteStream();
                                long total_length = response.body().contentLength();
                                long read_length = 0;
                                int len;
                                byte[] buff = new byte[16*1024];
                                while ((len = inputStream.read(buff)) != -1) {
                                    fileOutputStream.write(buff, 0, len);
                                    read_length += len;
                                    mHandler.post(new ProgressRunnable(fileCallback, total_length, read_length));
                                }
                                fileOutputStream.close();
                                inputStream.close();
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (fileCallback != null) {
                                        fileCallback.onResponse(file);
                                    }
                                }
                            });


                        } catch (IOException e) {
                            e.printStackTrace();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (fileCallback != null) {
                                        fileCallback.onError("下载失败");
                                    }
                                }
                            });

                        } finally {
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        return null;
                    }
                });

            }
        });

        return false;
    }


}
