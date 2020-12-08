plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
        arg("rxhttp_okhttp", Versions.OKHTTP)
        arg("rxhttp_rxjava", "rxjava3")
        arg("rxhttp_package", "rxhttp")
    }
}

android {

    compileSdkVersion(Config.compileSdkVersion())

    defaultConfig {
        minSdkVersion(Config.minSdkVersion())
        targetSdkVersion(Config.targetSdkVersion())
        versionCode = Config.versionCode()
        versionName = Config.versionName()
        val fields = Config.getBuildConfigFields()
        fields.forEach {
            buildConfigField(it[0], it[1], it[2])
        }
        consumerProguardFiles("consumer-rules.pro")
    }

    sourceSets {
        getByName("main") {
            res.srcDir("src/main/res")
            jniLibs.srcDir("libs")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to "*.jar"))
    api(kotlin("stdlib-jdk8"))

    api(SupportLibs.ANDROIDX_APPCOMPAT)
    api(SupportLibs.ANDROIDX_CORE_KTX)
    api(SupportLibs.ANDROIDX_CONSTRAINTLAYOUT)
    api(SupportLibs.ANDROIDX_RECYCLERVIEW)
    api(SupportLibs.ANDROIDX_EXIFINTERFACE)
    api(SupportLibs.ANDROIDX_SWIPEREFRESHLAYOUT)
    api(SupportLibs.ANDROIDX_FRAGMENT)
    api(SupportLibs.ANDROIDX_FRAGMENT_KTX)


    kapt(Kapts.RXHTTP_COMPILER)
    kapt(Kapts.ROOM_COMPILER)

    api(Libs.RXLIFE)

    api(Libs.MATERIAL)

    api(Libs.OKHTTP)
    api(Libs.RXANDROID)
    api(Libs.RXBINDING_CORE)
    api(Libs.RXBINDING_VIEWPAGER)
    api(Libs.RXJAVA)
    api(Libs.RXHTTP)
    api(Libs.RXPERMISSIONS)

    api(Libs.BINDING_COLLECTION_ADAPTER)
    api(Libs.BINDING_COLLECTION_ADAPTER_RECYCLERVIEW)
    api(Libs.BINDING_COLLECTION_ADAPTER_PAGING)

    api(Libs.TIMBER)
    api(Libs.LOGBACK_ANDROID)
    api(Libs.SLF4J)

    api(Libs.FLEXBOX)

    api(Libs.AUTOSZIE)
    api(Libs.ONCE)
    api(Libs.MULTIDEX)
    api(Libs.MMKV)

    api(Libs.GUAVA)
    api(Libs.GSON)
    api(Libs.FASTJSON)

    api(Libs.EASYFLOAT)

    api(Libs.AROUTER_API)

    api(Libs.COIL)

    api(Libs.QMUI)
    api(Libs.QMUI_ARCH)

    api(Libs.RECYCLICAL)

    api(Libs.JSOUP)

    api(Libs.ROOM_KTX)
    api(Libs.ROOM_RUNTIME)

    api(MyLibs.DEVELOPER_LIBS)
    api(MyLibs.DEVELOPER_MVVM)
    api(MyLibs.DEVELOPER_WIDGET)

    api(UmSdkLibs.UMSDK_ASMS)
    api(UmSdkLibs.UMSDK_COMMON)
    api(UmSdkLibs.UMSDK_CRASH)

    api(NimLibs.NIM_BASE)
    api(NimLibs.NIM_NRTC)
//    api(NimLibs.NIM_AVCHAT)
//    api(NimLibs.NIM_CHATROOM)
//    api(NimLibs.NIM_RTS)
    api(NimLibs.NIM_LUCENE)
    api(NimLibs.NIM_PUSH)

    api(AmapLibs.AMAP_3D)
    api(AmapLibs.AMAP_LOCATION)
    api(AmapLibs.AMAP_SEARCH)

//    api(project(":library-libs"))
//    api(project(":library-widget"))
//    api(project(":library-mvvm"))

    androidTestImplementation(AndroidTestingLibs.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidTestingLibs.ANDROIDX_TEST_RULES)
    androidTestImplementation(AndroidTestingLibs.ANDROIDX_TEST_RUNNER)
    androidTestImplementation(AndroidTestingLibs.ESPRESSO_CORE)
    testImplementation(TestingLibs.JUNIT)

}