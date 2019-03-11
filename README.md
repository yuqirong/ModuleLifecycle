Module-Lifecycle
===============
组件化模块之间感知app的生命周期，现主要有以下三个生命周期感知：

1. app 冷启动，即创建 application；
2. app 进入到前台的时候；
3. app 退回到后台的时候；

被 @Module 注解的类会自动注册到 ModuleLifecycle 中，具体可以查看 gradle_plugin 模块

latest version:

* module-lifecycle:1.1.0
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
        
3. must be init ModuleLifecycle in your Application onCreate()

        public class App extends Application {
        
            @Override
            public void onCreate() {
                super.onCreate();
                ModuleLifecycle.init(this);
            }
        }
        
4. create a module class, for example :

        @Module
        public class TestModule implements BaseModule {
        
            private static final String TAG = "TAG";
        
            public void onAppCreate() {
                Log.i(TAG, " TestModule onAppCreate");
            }
        
            public void onAppStart() {
                Log.i(TAG, "TestModule onAppStart");
            }
        
            public void onAppStop() {
                Log.i(TAG, "TestModule onAppStop");
            }
        
        }
        
5. finally, just fucking do it !!!

Proguard
========

    #------- module lifecycle ----------
    -keep class com.orange.note.modulelifecycle.** {*;}
    -keep class * implements com.orange.note.modulelifecycle.BaseModule {*;}

Changelog
=========
* v1.1.0 init commit