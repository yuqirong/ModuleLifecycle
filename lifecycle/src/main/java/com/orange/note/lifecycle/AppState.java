package com.orange.note.lifecycle;

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
@IntDef({AppState.Type.ON_APP_CREATE, AppState.Type.ON_APP_START, AppState.Type.ON_APP_STOP})
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
         * app退到后台
         */
        static final int ON_APP_STOP = 2;
    }

}
