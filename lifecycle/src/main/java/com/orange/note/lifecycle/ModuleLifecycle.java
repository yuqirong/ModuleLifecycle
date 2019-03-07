package com.orange.note.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.orange.note.lifecycle.annotation.OnAppCreate;
import com.orange.note.lifecycle.annotation.OnAppStart;
import com.orange.note.lifecycle.annotation.OnAppStop;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author maomao
 * @date 2019/3/7
 */
public class ModuleLifecycle {

    private static ModuleLifecycle instance;
    private Map<String, BaseModule> moduleMap = new HashMap<>();
    private AtomicInteger onStartedInteger = new AtomicInteger(0);

    private ModuleLifecycle() {
        register();
    }

    private void register() {
        // auto register, for example
         registerModule("com.orange.note.modulelifecycle.demo.TestModule");
        // registerModule("com.orange.note.problem.ProblemModule");
        // registerModule("com.orange.note.problem.ProblemModule");
    }


    public static ModuleLifecycle getInstance() {
        if (instance == null) {
            synchronized (ModuleLifecycle.class) {
                if (instance == null) {
                    instance = new ModuleLifecycle();
                }
            }
        }
        return instance;
    }

    private void registerModule(String className) {
        try {
            if (moduleMap.get(className) != null) {
                return;
            }
            Class<?> clazz = Class.forName(className);
            if (!BaseModule.class.isAssignableFrom(clazz)) {
                return;
            }
            BaseModule o = (BaseModule) clazz.newInstance();
            moduleMap.put(className, o);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    public void init(Application application) {
        notifyPluginEvent(application, OnAppCreate.class);
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                onStartedInteger.incrementAndGet();
                // 如果有异常情况小于0，就重置
                if (onStartedInteger.get() < 0) {
                    onStartedInteger = new AtomicInteger(0);
                }
                if (onStartedInteger.get() == 1) {
                    notifyPluginEvent(activity, OnAppStart.class);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                onStartedInteger.decrementAndGet();
                // 如果有异常情况小于0，就重置
                if (onStartedInteger.get() < 0) {
                    onStartedInteger = new AtomicInteger(0);
                }
                if (onStartedInteger.get() == 0) {
                    notifyPluginEvent(activity, OnAppStop.class);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    private void notifyPluginEvent(Context context, @NonNull Class<? extends Annotation> eventAnnotation) {
        if (!ProcessUtil.isMainProcess(context.getApplicationContext())) {
            return;
        }
        for (BaseModule object : moduleMap.values()) {
            invokeAnnotationMethod(object, eventAnnotation);
        }
    }

    private void invokeAnnotationMethod(BaseModule plugin, Class<? extends Annotation> eventAnnotation) {
        for (Method method : plugin.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(eventAnnotation)) {
                continue;
            }
            try {
                method.invoke(plugin);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            break;
        }
    }

}
