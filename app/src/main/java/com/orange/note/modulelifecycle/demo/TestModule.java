package com.orange.note.modulelifecycle.demo;

import android.app.Activity;
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
        Log.i(TAG, " TestModule onAppCreate");
    }

    @Override
    public void onAppStart(Activity activity) {
        Log.i(TAG, "TestModule onAppStart");
    }

    @Override
    public void onActivityResume(Activity activity) {

    }

    @Override
    public void onActivityPause(Activity activity) {

    }

    @Override
    public void onAppStop(Activity activity) {
        Log.i(TAG, "TestModule onAppStop");
    }

}
