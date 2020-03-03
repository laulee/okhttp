package com.laulee.okhttp.http;

public interface IHttpRequest {

    //同步请求数据
    public String execute();

    //异步请求
    public void enqueue(CallBackListener callBackListener);
}
