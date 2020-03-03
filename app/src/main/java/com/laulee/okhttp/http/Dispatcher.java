package com.laulee.okhttp.http;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 管理请求
 */
public class Dispatcher {

    private static Dispatcher dispatcher;

    private List<IHttpRequest> sysRequest = new ArrayList<>();
    private Deque<HttpRequest.AsyRun> runningRun = new ArrayDeque<>();
    private Deque<HttpRequest.AsyRun> readyRun = new ArrayDeque<>();

    private ThreadPoolExecutor executor;
    private int maxRequest = 10;//最大请求数

    private Dispatcher() {
        executor = new ThreadPoolExecutor(0, 60, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                execute(r);
            }
        });
    }

    /**
     * 单例
     *
     * @return
     */
    public static Dispatcher getInstance() {
        if (dispatcher == null) {
            synchronized (Dispatcher.class) {
                if (dispatcher == null) {
                    dispatcher = new Dispatcher();
                }
            }
        }
        return dispatcher;
    }

    /**
     * 添加到线程池
     *
     * @param r
     */
    private void execute(Runnable r) {
        if (r != null) {
            executor.execute(r);
        }
    }


    /**
     * 添加列表
     *
     * @param httpRequest
     */
    public synchronized void execute(IHttpRequest httpRequest) {
        sysRequest.add(httpRequest);
    }

    /**
     * 异步请求
     */
    public synchronized void enqueue(HttpRequest.AsyRun callRunnable) {
        if (runningRun.size() < maxRequest) {
            executor.execute(callRunnable);
            runningRun.add(callRunnable);
        } else {
            readyRun.add(callRunnable);
        }
    }

    /**
     * 移除列表
     *
     * @param httpRequest
     */
    public synchronized void finish(IHttpRequest httpRequest) {
        sysRequest.remove(httpRequest);
    }

    /**
     * 结束
     *
     * @param asyRun
     */
    public synchronized void finish(HttpRequest.AsyRun asyRun) {
        runningRun.remove(asyRun);
        runReady();
    }

    /**
     * 激活下一个线程
     */
    private void runReady() {
        if (runningRun.size() >= maxRequest) return; // Already running max capacity.
        if (readyRun.isEmpty()) return; // No ready calls to promote.

        for (Iterator<HttpRequest.AsyRun> i = readyRun.iterator(); i.hasNext(); ) {
            HttpRequest.AsyRun call = i.next();
            i.remove();
            runningRun.add(call);
            executor.execute(call);
            if (runningRun.size() >= maxRequest) return; // Reached max capacity.
        }
    }
}
