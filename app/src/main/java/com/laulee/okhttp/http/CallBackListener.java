package com.laulee.okhttp.http;

public interface CallBackListener {

    public void onSuccess(String inputStream);

    public void onFailed(Exception e);
}
