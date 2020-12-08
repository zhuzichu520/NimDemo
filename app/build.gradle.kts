plugins {
    id("com.android.application")
    id("com.github.ben-manes.versions")
    kotlin("android")
    kotlin("kapt")
}

Config.initJenkinsProperties(project)

android {

    compileSdkVersion(Config.compileSdkVersion())

    signingConfigs {
        create("release") {
            keyAlias = Config.keyAlias()
            keyPassword = Config.keyPassword()
            storePassword = Config.storePassword()
            storeFile = file(Config.storeFile())
        }

        getByName("debug") {
            storeFile = file("debug.keystore")
        }
    }

    defaultConfig {
        applicationId = Config.applicationId()
        minSdkVersion(Config.minSdkVersion())
        targetSdkVersion(Config.targetSdkVersion())
        versionCode = Config.versionCode()
        versionName = Config.versionName()
        multiDexEnabled = true
        signingConfig = signingConfigs.getByName("debug")
//        renderscriptTargetApi = 18
//        renderscriptSupportModeEnabled = true
        resValue("string", "app_name_new", Config.appName())
        manifestPlaceholders.apply {
            put("ic_launcher_new", "@mipmap/ic_launcher")
            put("ic_launcher_round_new", "@mipmap/ic_launcher_round")
            put("NIM_KEY", Config.appKeyNim())
        }

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }

        ndk {
            abiFilters.clear()
            abiFilters.add("armeabi-v7a")
        }

    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isShrinkResources = true
            isMinifyEnabled = true
            isZipAlignEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders.apply {
                put("AMAP_KEY", Config.appKeyAmapRelease())
            }
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            manifestPlaceholders.apply {
                put("AMAP_KEY", Config.appKeyAmapDebug())
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures.dataBinding = true

    sourceSets {
        sourceSets["main"].apply {
            java.srcDir("src/main/kotlin")
        }
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to "*.jar"))
    Config.denpendModules(project)
    api(project(path = ":library-shared"))
    kapt(Kapts.AROUTER_COMPILER)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.5")
}
