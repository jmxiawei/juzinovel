/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.iscore.freereader.http;


import java.lang.reflect.Proxy;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import top.iscore.freereader.App;

/**
 * Created by xiawei on 2017/3/14.
 */

public class BaseHttpHandler {

    private Retrofit retrofit;
    private Converter.Factory converter;

    public static BaseHttpHandler create() {
        return new BaseHttpHandler(null);
    }

    /**
     * @param converter
     */
    public BaseHttpHandler(Converter.Factory converter) {
        this.converter = converter;

        if (this.converter == null) {
            this.converter = GsonConverterFactory.create();
        }
    }


    public void setConverter(Converter.Factory converter) {
        this.converter = converter;
        if (this.converter == null) {
            this.converter = GsonConverterFactory.create();
        }
    }


    /**
     * 代理
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clazz) {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(App.baseUrl)
                .client(HttpClientManager
                        .getInstance()
                        .getOkHttpClient())
                .addConverterFactory(this.converter)
                .build();
        T t = retrofit.create(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new ProxyHandler(t, this));
    }


}
