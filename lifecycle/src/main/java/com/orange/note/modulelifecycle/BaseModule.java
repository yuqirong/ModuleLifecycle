package com.orange.note.modulelifecycle;

import android.app.Activity;

/**
 * @author maomao
 * @date 2019/3/7
 */
public interface BaseModule {

    /**
     * 在app冷启动的时候回调
     */
    void onAppCreate();

    /**
     * 在app进入前台的时候回调
     */
    void onAppStart(Activity activity);

    /**
     * 在app进入前台 onResumed 之后
     * @param activity
     */
    void onAppResume(Activity activity);

    /**
     * 在app进入前台 onPause 之后
     * @param activity
     */
    void onAppPause(Activity activity);

    /**
     * 在app进入后台的时候回调
     */
    void onAppStop(Activity activity);

}
