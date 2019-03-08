package com.orange.note.modulelifecycle.demo;

import android.app.Application;

import com.orange.note.modulelifecycle.ModuleLifecycle;


/**
 * @author maomao
 * @date 2019/3/7
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ModuleLifecycle.init(this);
    }
}
