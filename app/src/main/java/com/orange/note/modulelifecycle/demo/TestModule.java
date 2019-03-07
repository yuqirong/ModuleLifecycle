package com.orange.note.modulelifecycle.demo;

import android.util.Log;

import com.orange.note.lifecycle.BaseModule;
import com.orange.note.lifecycle.annotation.Module;
import com.orange.note.lifecycle.annotation.OnAppCreate;
import com.orange.note.lifecycle.annotation.OnAppStart;
import com.orange.note.lifecycle.annotation.OnAppStop;

/**
 * @author maomao
 * @date 2019/3/7
 */
@Module
public class TestModule implements BaseModule {

    private static final String TAG = "TAG";

    @OnAppCreate
    public void onAppCreate() {
        Log.i(TAG, "onAppCreate");
    }

    @OnAppStart
    public void onAppStart() {
        Log.i(TAG, "onAppStart");
    }

    @OnAppStop
    public void onAppStop() {
        Log.i(TAG, "onAppStop");
    }

}
