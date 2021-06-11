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
     * 在activity进入前台 onResumed 之后
     *
     * @param activity
     */
    void onActivityResume(Activity activity);

    /**
     * 在activity进入前台 onPaused 之后
     *
     * @param activity
     */
    void onActivityPause(Activity activity);

    /**
     * 在app进入后台的时候回调
     */
    void onAppStop(Activity activity);

}
