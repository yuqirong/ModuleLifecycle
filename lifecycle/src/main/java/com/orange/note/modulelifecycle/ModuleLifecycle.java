package com.orange.note.modulelifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author maomao
 * @date 2019/3/7
 */
public class ModuleLifecycle {

    private static final String TAG = "ModuleLifecycle";
    private static ModuleLifecycle instance;
    private static Map<String, BaseModule> moduleMap = new HashMap<>();
    private static volatile boolean init;
    private AtomicInteger onStartedInteger = new AtomicInteger(0);

    private ModuleLifecycle(Application application) {
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
                    notifyPluginEvent(activity, AppState.Type.ON_APP_START);
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
                    notifyPluginEvent(activity, AppState.Type.ON_APP_STOP);
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

    private static void register() {
        // auto register, for example
        // registerModule("com.orange.note.modulelifecycle.demo.TestModule");
        // registerModule("com.orange.note.problem.ProblemModule");
        // registerModule("com.orange.note.problem.ProblemModule");
    }


    public static ModuleLifecycle getInstance(Application application) {
        if (instance == null) {
            synchronized (ModuleLifecycle.class) {
                if (instance == null) {
                    instance = new ModuleLifecycle(application);
                }
            }
        }
        return instance;
    }

    private static void registerModule(String className) {
        try {
            if (moduleMap.containsKey(className)) {
                Log.e(TAG, className + "has registered");
                return;
            }
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod("getModuleList");
            List<String> list = (List<String>) method.invoke(null, new Object[]{});
            if (list != null && !list.isEmpty()) {
                for (String string : list) {
                    Class<?> moduleClazz = Class.forName(string);
                    BaseModule o = (BaseModule) moduleClazz.newInstance();
                    moduleMap.put(className, o);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void init(Application application) {
        if (init) {
            Log.e(TAG, "has inited, don't init twice");
            return;
        }
        register();
        ModuleLifecycle instance = ModuleLifecycle.getInstance(application);
        instance.notifyPluginEvent(application, AppState.Type.ON_APP_CREATE);
        init = true;
    }

    private void notifyPluginEvent(Context context, @AppState int type) {
        if (!ProcessUtil.isMainProcess(context.getApplicationContext())) {
            return;
        }
        for (BaseModule object : moduleMap.values()) {
            invokeAnnotationMethod(object, type);
        }
    }

    private void invokeAnnotationMethod(BaseModule plugin, @AppState int type) {
        switch (type) {
            case AppState.Type.ON_APP_CREATE:
                plugin.onAppCreate();
                break;
            case AppState.Type.ON_APP_START:
                plugin.onAppStart();
                break;
            case AppState.Type.ON_APP_STOP:
                plugin.onAppStop();
                break;
        }
    }

}
