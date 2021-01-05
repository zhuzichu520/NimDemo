import org.gradle.kotlin.dsl.ext

module.InitModule(project) {
    /**
     * 融媒体模块
     */
    add("implementation", project(mapOf("path" to ":module-media")))
    /**
     * 魅族推送
     */
    add("implementation", "com.meizu.flyme.internet:push-internal:3.9.7")
    /**
     * 华为推送
     */
    add("implementation", "com.huawei.hms:push:4.0.4.301")
    /**
     * oppo推送
     * compileOnly 不会将此aar包编译到APK中
     */
    add(
        "compileOnly", files("libs/com.heytap.msp_push_2.1.0.aar")
    )
}