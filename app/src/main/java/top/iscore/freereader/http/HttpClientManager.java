/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.iscore.freereader.http;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by xiaw on 2017/3/10 0010.
 */

public class HttpClientManager {


    private OkHttpClient okHttpClient;

    private HttpClientManager() {
        okHttpClient = newClient();
    }

    private OkHttpClient newClient() {

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new AddParamsInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }


    private static HttpClientManager instance;

    public static HttpClientManager getInstance() {

        if (instance == null) {
            synchronized (HttpClientManager.class) {
                if (instance == null) {
                    instance = new HttpClientManager();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}
