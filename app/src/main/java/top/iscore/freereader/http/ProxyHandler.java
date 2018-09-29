/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.iscore.freereader.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * Created by xiaw on 2017/3/10 0010.
 */

public class ProxyHandler implements InvocationHandler {


    private static String TAG = ProxyHandler.class.getSimpleName();
    private BaseHttpHandler baseHttpHandler;
    private Object proxyObject;

    public ProxyHandler(Object proxyObject, BaseHttpHandler baseHttpHandler) {
        this.proxyObject = proxyObject;
        this.baseHttpHandler = baseHttpHandler;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return method.invoke(proxyObject, args);
    }

}
