package com.laulee.okhttp.http;

public abstract class CallRunnable implements Runnable {

    private String name;

    public CallRunnable(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(name);
        execute();
        Thread.currentThread().setName(oldName);
    }

    protected abstract void execute();
}
