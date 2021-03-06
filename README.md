Module-Lifecycle
===============
本项目主要解决组件化/多模块项目开发时，模块内无法感知 Application/Activity 生命周期状态的问题。
利用AOP思想实现组件化/模块化项目感知app的生命周期，无代码侵入。现主要有以下五个生命周期感知：

1. onAppCreate()：app 冷启动，即 Application.onCreate() 时机；
2. onAppStart(Activity activity)：app 进入到前台的时候，参数 activity 为当前前台 Activity；
3. onActivityResume(Activity activity)： Activity 进入 onResume 的回调；
4. onActivityPause(Activity activity)：Activity 进入 onPause 的回调；   
3. onAppStop(Activity activity)：app 退回到后台的时候，参数 activity 为当前栈顶退回到后台的 Activity；

被 @Module 注解的类会自动注册到 ModuleLifecycle 中，具体可以查看 gradle_plugin 模块

latest version:

* module-lifecycle:1.1.2
* module-lifecycle-annotation:1.1.0
* module-lifecycle-compiler:1.1.0
* module-lifecycle-gradle-plugin:1.1.0

Usage
=====
1. add classpath to build.gradle

        dependencies {
            classpath 'com.android.tools.build:gradle:3.3.0'
            classpath 'com.orange.note:module-lifecycle-gradle-plugin:1.1.0'
            // NOTE: Do not place your application dependencies here; they belong
            // in the individual module build.gradle files
        }
        
2. apply plugin and add dependencies to app/build.gradle, besides add arguments in annotationProcessorOptions

        apply plugin: 'com.orange.note.modulelifecycle'
        
        ...
        
        android {
        
            defaultConfig {
                
                javaCompileOptions {
                    annotationProcessorOptions {
                        arguments = [MODULE_LIFECYCLE_MODULE_NAME: project.getName()]
                    }
                }
            }
            
        }
        
        dependencies {
            implementation 'com.orange.note:module-lifecycle:1.1.0'
            implementation 'com.orange.note:module-lifecycle-annotation:1.1.0'
            annotationProcessor 'com.orange.note:module-lifecycle-compiler:1.1.0'
        }

        
3. create a module class, for example :

        @Module
        public class TestModule implements BaseModule {
        
            private static final String TAG = "TAG";

            @Override
            public void onAppCreate() {
                Log.i(TAG, " TestModule onAppCreate");
            }
        
            @Override
            public void onAppStart(Activity activity) {
                Log.i(TAG, "TestModule onAppStart");
            }

            @Override
            public void onAppStop(Activity activity) {
                Log.i(TAG, "TestModule onAppStop");
            }

            @Override
            public void onActivityResume(Activity activity) {
         
            }
         
            @Override
            public void onActivityPause(Activity activity) {
         
            }

        
        }
        
4. finally, just fucking do it !!!

Proguard
========

    #------- module lifecycle ----------
    -keep class com.orange.note.modulelifecycle.** {*;}
    -keep class * implements com.orange.note.modulelifecycle.BaseModule {*;}

Changelog
=========
* v1.1.0 init commit