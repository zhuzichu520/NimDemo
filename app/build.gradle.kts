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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
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
    implementation(kotlin("stdlib-jdk8"))
    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(SupportLibs.ANDROIDX_CORE_KTX)
    implementation(SupportLibs.ANDROIDX_CONSTRAINTLAYOUT)
    implementation(SupportLibs.ANDROIDX_RECYCLERVIEW)
    implementation(SupportLibs.ANDROIDX_EXIFINTERFACE)
    implementation(SupportLibs.ANDROIDX_SWIPEREFRESHLAYOUT)
    implementation(SupportLibs.ANDROIDX_FRAGMENT)
    implementation(SupportLibs.ANDROIDX_FRAGMENT_KTX)
    implementation(files("libs/com.heytap.msp_push_2.1.0.aar"))
    Config.denpendModules(project)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.5")
}
