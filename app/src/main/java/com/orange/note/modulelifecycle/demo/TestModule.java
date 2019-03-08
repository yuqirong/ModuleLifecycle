package com.orange.note.modulelifecycle.demo;

import android.util.Log;

import com.orange.note.modulelifecycle.BaseModule;
import com.orange.note.modulelifecycle.annotation.Module;

/**
 * @author maomao
 * @date 2019/3/7
 */
@Module
public class TestModule implements BaseModule {

    private static final String TAG = "TAG";

    public void onAppCreate() {
        Log.i(TAG, "onAppCreate");
    }

    public void onAppStart() {
        Log.i(TAG, "onAppStart");
    }

    public void onAppStop() {
        Log.i(TAG, "onAppStop");
    }

}
