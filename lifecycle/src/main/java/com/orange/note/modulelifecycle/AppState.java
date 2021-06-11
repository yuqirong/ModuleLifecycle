package com.orange.note.modulelifecycle;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * app状态
 *
 * @author maomao
 * @date 2019/3/7
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({AppState.Type.ON_APP_CREATE, AppState.Type.ON_APP_START,
        AppState.Type.ON_ACTIVITY_RESUME,AppState.Type.ON_ACTIVITY_PAUSE,
        AppState.Type.ON_APP_STOP})
@interface AppState {

    class Type {
        /**
         * app冷启动
         */
        static final int ON_APP_CREATE = 0;
        /**
         * app回到前台
         */
        static final int ON_APP_START = 1;

        /**
         * activity到前台并onResumed
         */
        static final int ON_ACTIVITY_RESUME = 2;

        /**
         * activity从前台到onPaused
         */
        static final int ON_ACTIVITY_PAUSE = 3;

        /**
         * app退到后台
         */
        static final int ON_APP_STOP = 4;
    }

}
