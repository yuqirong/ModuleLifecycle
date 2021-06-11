package com.orange.note.modulelifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author maomao
 * @date 2019/3/7
 */
public class ModuleLifecycle {

    private static final String TAG = "ModuleLifecycle";
    private static ModuleLifecycle instance;
    private static List<BaseModule> moduleList = new ArrayList<>();
    private static volatile boolean init = false;
    private AtomicInteger onStartedInteger = new AtomicInteger(0);
    private AtomicInteger onResumedInteger = new AtomicInteger(0);

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
                onResumedInteger.incrementAndGet();
                // 如果有异常情况小于0，就重置
                if (onResumedInteger.get() < 0) {
                    onResumedInteger = new AtomicInteger(0);
                }
                if (onResumedInteger.get() == 1) {
                    notifyPluginEvent(activity, AppState.Type.ON_APP_RESUME);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                onResumedInteger.decrementAndGet();
                // 如果有异常情况小于0，就重置
                if (onResumedInteger.get() < 0) {
                    onResumedInteger = new AtomicInteger(0);
                }
                if (onResumedInteger.get() == 0) {
                    notifyPluginEvent(activity, AppState.Type.ON_APP_PAUSE);
                }
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

    // className for example : com.orange.note.modulelifecycle.template.Module$$Lifecycle$$app
    private static void registerModule(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod("getModuleList");
            List<String> list = (List<String>) method.invoke(null, new Object[]{});
            if (list == null || list.isEmpty()) {
                return;
            }
            for (String moduleClassName : list) {
                if (TextUtils.isEmpty(moduleClassName)) {
                    continue;
                }
                Class<?> moduleClazz = Class.forName(moduleClassName);
                BaseModule module = (BaseModule) moduleClazz.newInstance();
                moduleList.add(module);
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
        for (BaseModule object : moduleList) {
            invokeAnnotationMethod(object, null, type);
        }
    }

    private void notifyPluginEvent(Activity activity, @AppState int type) {
        if (!ProcessUtil.isMainProcess(activity.getApplicationContext())) {
            return;
        }
        for (BaseModule object : moduleList) {
            invokeAnnotationMethod(object, activity, type);
        }
    }

    private void invokeAnnotationMethod(BaseModule plugin, Activity activity, @AppState int type) {
        switch (type) {
            case AppState.Type.ON_APP_CREATE:
                plugin.onAppCreate();
                break;
            case AppState.Type.ON_APP_START:
                plugin.onAppStart(activity);
                break;
            case AppState.Type.ON_APP_RESUME:
                plugin.onAppResume(activity);
                break;
            case AppState.Type.ON_APP_PAUSE:
                plugin.onAppPause(activity);
                break;
            case AppState.Type.ON_APP_STOP:
                plugin.onAppStop(activity);
                break;
        }
    }

}
