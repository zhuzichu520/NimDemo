logger.lifecycle("初始化buildSrc")
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.2.0-beta02")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")
    implementation("com.google.code.gson:gson:2.8.6")
}