package com.example.liuhui.lottery.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.example.liuhui.lottery.event.BaseEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 * Date: 2017-05-24
 *
 * @author liuhui8
 */
public abstract class BaseActivity extends FragmentActivity{
    private static final String LOG_TAG = BaseActivity.class.getSimpleName();
    protected View mRootView;
    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(getContentView(), null);
        setContentView(mRootView);

        mUnbinder = ButterKnife.bind(this);
        getIntentData();
        getIntentData(savedInstanceState);
        initHeaderView();
        initContentView();
        initFooterView();
        initSavedInstancesState(savedInstanceState);
        initPresenter();
        initData();
    }



    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseEvent event) {
        onEventMainThread(event);
    }

    protected abstract int getContentView();

    protected abstract void initPresenter();

    protected abstract void initData();

    protected void initContentView() {
    }

    protected void initHeaderView() {

    }

    private void initFooterView() {

    }

    protected void getIntentData() {

    }

    protected void getIntentData(Bundle savedInstanceState) {

    }

    protected void initSavedInstancesState(Bundle savedInstanceState) {

    }

    protected void onEventMainThread(BaseEvent event) {

    }
}
