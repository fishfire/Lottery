package com.example.liuhui.lottery.handler;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

/**
 * 防止内存泄露
 * Date: 2017-05-24
 *
 * @author liuhui8
 */
public abstract class WeakHandler<T> extends Handler {
    private WeakReference<T> mTargets;

    public WeakHandler(T target) {
        mTargets = new WeakReference<>(target);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T target = mTargets.get();
        if (target != null) {
            handle(target, msg);
        }
    }

    public abstract void handle(T target, Message msg);
}
