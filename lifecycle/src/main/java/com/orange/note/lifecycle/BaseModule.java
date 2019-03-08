package com.orange.note.lifecycle;

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
    void onAppStart();

    /**
     * 在app进入后台的时候回调
     */
    void onAppStop();

}
