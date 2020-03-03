package com.laulee.okhttp.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest implements IHttpRequest {

    private AsyRun callRunnable;

    private String url;
    private String data;
    private String method;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String execute() {
        Dispatcher.getInstance().execute(this);
        try {
            String result = getResponse();
            return result;
        } finally {
            Dispatcher.getInstance().finish(this);
        }
    }

    @Override
    public void enqueue(CallBackListener callBackListener) {
        callRunnable = new AsyRun(callBackListener);
        Dispatcher.getInstance().enqueue(callRunnable);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public class AsyRun extends CallRunnable {

        private CallBackListener listener;

        public AsyRun(CallBackListener listener) {
            super("HttpRequest url = " + url);
            this.listener = listener;
        }

        @Override
        protected void execute() {
            try {
                String result = getResponse();
                if (listener != null) {
                    listener.onSuccess(result);
                }
            } finally {
                Dispatcher.getInstance().finish(this);
            }
        }
    }

    /**
     * 获取数据
     */
    private String getResponse() {
        try {
            URL uri = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) uri.openConnection();
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            Log.d("HttpRequest", "connect");
            if (code == 200) {
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                try {
                    inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    String result = stringBuffer.toString();
                    Log.d("HttpRequest", "result" + result);
                    return result;
                } catch (Exception e) {

                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                }
            } else {
                Log.d("HttpRequest", "error code" + code);
                retry();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重试
     */
    private void retry() {

    }
}
