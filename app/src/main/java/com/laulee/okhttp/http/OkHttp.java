package com.laulee.okhttp.http;

public class OkHttp {

    //1、创建request对象(区分get、post)
    //2、创建请求管理类(创建线程池、管理请求的task任务)
    //3、创建httpengine引擎请求数据
    //4、获取数据进行解析,创建callback监听
    //5、重试机制 请求失败进行重试，重试1次后throw异常

    /**
     * 通过get请求
     *
     * @param url
     * @param data
     * @param callBackListener
     */
    public static void sendGetRequest(String url, String data, CallBackListener callBackListener) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.setMethod("GET");
        httpRequest.enqueue(callBackListener);
    }

    /**
     * post请求
     *
     * @param url
     * @param data
     * @param callBackListener
     */
    public void sendPostRequest(String url, String data, CallBackListener callBackListener) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.setMethod("POST");
        httpRequest.enqueue(callBackListener);
    }
}
