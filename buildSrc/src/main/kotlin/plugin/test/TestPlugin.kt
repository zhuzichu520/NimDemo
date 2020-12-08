package plugin.test

import Log
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.test.ext.StudentExtension
import plugin.test.task.StudentTask
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class TestPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        Log.l("TestPlugin", "调用了apply()")
        val extension = project.extensions.create("student", StudentExtension::class)
        project.tasks.register("printStudent", StudentTask::class) {
            doLast {
                println("我的昵称是${extension.nickname}")
            }
        }
    }
}