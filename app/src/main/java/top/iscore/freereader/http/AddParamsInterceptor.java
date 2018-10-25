package top.iscore.freereader.http;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * udid=7c7e8036375b40abb81c7f9a5f73293ccb6969b7&
 * vc=175
 * &vn=3.4.3
 * &deviceModel=Android%20SDK%20built%20for%20x86
 * &first_channel=eyepetizer_baidu_market
 * &last_channel=eyepetizer_baidu_market
 * &system_version_code=25
 *
 * @author xiaw
 * @date 2017/4/7 0007
 */

public class AddParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl url = oldRequest.url();
        /**
         * 版本
         */
        Headers headers = oldRequest.headers();
        Headers newHeaders = headers.newBuilder()
                .build();
        /**
         *
         */
        HttpUrl.Builder builder = url.newBuilder();


        FormBody formBody = (FormBody) oldRequest.body();

        StringBuilder stringBuilder = new StringBuilder("[");

        int size = formBody == null ? 0 : formBody.size();

        for (int i = 0; i < size; i++) {
            stringBuilder.append(formBody.name(i)).append("=").append(formBody.value(i)).append(",");
        }
        stringBuilder.append("]");

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), formBody)
                .url(builder.build())
                .headers(newHeaders)
                .build();
        long t1 = System.nanoTime();
        LogUtils.e(String.format("发送请求 %s on %s",
                newRequest.url() + "?" + stringBuilder.toString(), newRequest.headers()));
        Response response = chain.proceed(newRequest);
        long t2 = System.nanoTime();

        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        String data = responseBody.string();


        LogUtils.e(String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                response.request().url(),
                data,
                (t2 - t1) / 1e6d,
                response.headers()));
        return response;
    }
}
