package com.orange.note.modulelifecycle.launch

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.orange.note.modulelifecycle.core.RegisterTransform
import com.orange.note.modulelifecycle.utils.ScanSetting
import com.orange.note.modulelifecycle.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Simple version of AutoRegister plugin for ARouter
 * @author billy.qi email: qiyilike@163.com
 * @since 17/12/06 15:35
 */
public class PluginLaunch implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        def isApp = project.plugins.hasPlugin(AppPlugin)
        //only application module needs this plugin to generate register code
        if (isApp) {
            Logger.make(project)

            Logger.i('Project enable module-lifecycle-register plugin')

            def android = project.extensions.getByType(AppExtension)
            def transformImpl = new RegisterTransform(project)

            //init arouter-auto-register settings
            ArrayList<ScanSetting> list = new ArrayList<>(1)
            list.add(new ScanSetting('Module$$Lifecycle'))
            RegisterTransform.registerList = list
            //register this plugin
            android.registerTransform(transformImpl)
        }
    }

}
