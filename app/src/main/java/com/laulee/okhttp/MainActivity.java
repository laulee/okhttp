package com.laulee.okhttp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.laulee.okhttp.http.CallBackListener;
import com.laulee.okhttp.http.OkHttp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttp.sendGetRequest("https://www.baidu.com", "", new CallBackListener() {

            @Override
            public void onSuccess(String inputStream) {

            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}
