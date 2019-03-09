package com.orange.note.modulelifecycle.test_module;

import android.util.Log;

import com.orange.note.modulelifecycle.BaseModule;
import com.orange.note.modulelifecycle.annotation.Module;

/**
 * @author maomao
 * @date 2019/3/9
 */
@Module
public class Test2Module implements BaseModule {

    private static final String TAG = "TAG";

    @Override
    public void onAppCreate() {
        Log.i(TAG, "Test2Module onAppCreate ");
    }

    @Override
    public void onAppStart() {
        Log.i(TAG, "Test2Module onAppStart ");
    }

    @Override
    public void onAppStop() {
        Log.i(TAG, "Test2Module onAppStop ");
    }

}
