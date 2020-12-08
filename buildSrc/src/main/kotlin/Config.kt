import com.google.gson.Gson
import extension.getPropertyByKey
import extension.quotes
import extension.base64
import extension.toInt2
import module.ModuleHandler
import module.Modules
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

object Config {

    private lateinit var parentProject: Project

    private lateinit var rootPath: String

    private lateinit var modules: Modules

    private enum class GradleKey(val key: String) {
        IS_JENKINS("IS_JENKINS"),
        ENVIRONMENT("ENVIRONMENT"),
        COMPILE_SDK_VERSION("COMPILE_SDK_VERSION"),
        TARGET_SDK_VERSION("TARGET_SDK_VERSION"),
        MIN_SDK_VERSION("MIN_SDK_VERSION"),
        APP_SERVER_URL("APP_SERVER_URL"),
        BUILD_TYPE("BUILD_TYPE"),
        PRODUCT_CHANNEL("PRODUCT_CHANNEL"),
        RELEASE_SIGN_CONFIGS_PATH("RELEASE_SIGN_CONFIGS_PATH");
    }

    private enum class ConfigKey(val key: String) {
        APP_NAME("APP_NAME"),
        APPLICATION_ID("APPLICATION_ID"),
        APP_VERSION_NAME("APP_VERSION_NAME"),
        APP_VERSION_CODE("APP_VERSION_CODE");
    }

    private enum class SignKey(val key: String) {
        SIGN_KEY_ALIAS("SIGN_KEY_ALIAS"),
        SIGN_KEY_PASSWORD("SIGN_KEY_PASSWORD"),
        SIGN_STORE_FILE("SIGN_STORE_FILE"),
        SIGN_STORE_PASSWORD("SIGN_STORE_PASSWORD");
    }

    private enum class BuildKey(val key: String) {
        STRING_NIM_APPKEY("STRING_NIM_APPKEY"),
        STRING_AMAP_APPKEY_DEBUG("STRING_AMAP_APPKEY_DEBUG"),
        STRING_AMAP_APPKEY_RELEASE("STRING_AMAP_APPKEY_RELEASE");
    }

    @JvmStatic
    fun init(project: Project) {
        parentProject = project
        @Suppress("MISSING_DEPENDENCY_CLASS")
        rootPath = parentProject.projectDir.toString().plus(File.separator)
        Log.init(parentProject)
        Log.l("rootPath", rootPath)
        initModules()
    }

    private val resourcesPath by lazy {
        rootPath.plus("buildSrc")
            .plus(File.separator)
            .plus("src")
            .plus(File.separator)
            .plus("main")
            .plus(File.separator)
            .plus("resources")
            .plus(File.separator)
    }

    private val defaultGradleProperties by lazy {
        loadProperties(
            rootPath.plus("gradle.properties")
        )
    }

    private val localGradleProperties by lazy {
        try {
            loadProperties(
                rootPath.plus("local.properties")
            )
        } catch (e: Exception) {
            Properties()
        }
    }

    private val gradleProperties by lazy {
        val properties = defaultGradleProperties
        localGradleProperties.mapKeys {
            properties.put(it.key, it.value)
        }
        properties
    }

    private val defaultConfigProperties by lazy {
        loadProperties(
            resourcesPath.plus("default-config.properties")
        )
    }

    private val flavorConfigProperties by lazy {
        loadProperties(
            resourcesPath.plus("flavor-config.properties")
        )
    }

    private val moduleProperties by lazy {
        loadProperties("module-config.properties")
    }

    private val configProperties by lazy {
        val properties = defaultConfigProperties
        flavorConfigProperties.mapKeys {
            properties.put(it.key, it.value)
        }
        properties
    }

    private val buildConfigProperties by lazy {
        loadProperties(
            resourcesPath.plus("build-config.properties")
        )
    }

    private val signatureProperties by lazy {
        try {
            loadProperties(getReleaseSignPath())
        } catch (e: Exception) {
            loadProperties(
                resourcesPath.plus("signature.properties")
            )
        }
    }

    @JvmStatic
    fun keyAlias(): String {
        return signatureProperties.getPropertyByKey(
            SignKey.SIGN_KEY_ALIAS.key
        ).apply {
            Log.l("keyAlias", this)
        }
    }

    @JvmStatic
    fun keyPassword(): String {
        return signatureProperties.getPropertyByKey(
            SignKey.SIGN_KEY_PASSWORD.key
        ).apply {
            Log.l("keyPassword", this)
        }
    }

    @JvmStatic
    fun storeFile(): String {
        return signatureProperties.getPropertyByKey(
            SignKey.SIGN_STORE_FILE.key
        ).apply {
            Log.l("storeFile", this)
        }
    }

    @JvmStatic
    fun storePassword(): String {
        return signatureProperties.getPropertyByKey(
            SignKey.SIGN_STORE_PASSWORD.key
        ).apply {
            Log.l("storePassword", this)
        }
    }

    /**
     * App名称
     */
    @JvmStatic
    fun appName(): String {
        return configProperties.getPropertyByKey(
            ConfigKey.APP_NAME.key
        ).apply {
            Log.l("appName", this)
        }
    }

    /**
     * sdk版本
     */
    @JvmStatic
    fun compileSdkVersion(): Int {
        return gradleProperties.getPropertyByKey(
            GradleKey.COMPILE_SDK_VERSION.key
        ).toInt2().apply {
            Log.l("compileSdkVersion", this)
        }
    }

    /**
     * 包名
     */
    @JvmStatic
    fun applicationId(): String {
        return configProperties.getPropertyByKey(
            ConfigKey.APPLICATION_ID.key
        ).apply {
            Log.l("applicationId", this)
        }
    }

    /**
     * Android最小版本
     */
    @JvmStatic
    fun minSdkVersion(): String {
        return gradleProperties.getPropertyByKey(
            GradleKey.MIN_SDK_VERSION.key
        ).apply {
            Log.l("minSdkVersion", this)
        }
    }

    /**
     * targetVersion Android版本
     */
    @JvmStatic
    fun targetSdkVersion(): String {
        return gradleProperties.getPropertyByKey(
            GradleKey.TARGET_SDK_VERSION.key
        ).apply {
            Log.l("targetSdkVersion", this)
        }
    }

    /**
     * 版本号
     */
    @JvmStatic
    fun versionCode(): Int {
        return configProperties.getPropertyByKey(
            ConfigKey.APP_VERSION_CODE.key
        ).toInt2().apply {
            Log.l("versionCode", this)
        }
    }

    /**
     * 版本名称
     */
    @JvmStatic
    fun versionName(): String {
        return configProperties.getPropertyByKey(
            ConfigKey.APP_VERSION_NAME.key
        ).apply {
            Log.l("versionName", this)
        }
    }

    /**
     * 运行环境
     */
    @JvmStatic
    fun getRunEnvironment(): String {
        return gradleProperties.getPropertyByKey(
            GradleKey.ENVIRONMENT.key
        ).apply {
            Log.l("getRunEnvironment", this)
        }
    }

    /**
     * 初始化Jenkins参数
     */
    @JvmStatic
    fun initJenkinsProperties(project: Project) {
        project.extensions.getByType(ExtraPropertiesExtension::class)
            .properties.mapKeys {
                gradleProperties.put(it.key, project.properties[it.key])
            }
    }

    @JvmStatic
    fun appKeyNim(): String {
        return buildConfigProperties.getPropertyByKey(
            BuildKey.STRING_NIM_APPKEY.key
        ).apply {
            Log.l("appKeyNim", this)
        }
    }


    @JvmStatic
    fun appKeyAmapDebug(): String {
        return buildConfigProperties.getPropertyByKey(
            BuildKey.STRING_AMAP_APPKEY_DEBUG.key
        ).apply {
            Log.l("appKeyAmapDebug", this)
        }
    }

    @JvmStatic
    fun appKeyAmapRelease(): String {
        return buildConfigProperties.getPropertyByKey(
            BuildKey.STRING_AMAP_APPKEY_RELEASE.key
        ).apply {
            Log.l("appKeyAmapRelease", this)
        }
    }


    /**
     * 签名路径
     */
    private fun getReleaseSignPath(): String {
        return gradleProperties.getPropertyByKey(
            GradleKey.RELEASE_SIGN_CONFIGS_PATH.key
        )
            .apply {
                Log.l("getReleaseSignPath", this)
            }
    }

    /**
     * 获取build-config数据
     */
    @JvmStatic
    fun getBuildConfigFields(): List<Array<String>> {
        val list = mutableListOf<Array<String>>()
        configProperties.mapKeys {
            val configKeyArray = it.key.toString().split("_")
            if (configKeyArray.size < 5)
                return@mapKeys
            if (configKeyArray[0].plus("_").plus(configKeyArray[1]) != "BUILD_CONFIG")
                return@mapKeys
            val environment = getEnvironment(
                configKeyArray
            ).toLowerCase(Locale.getDefault())
            val dataType =
                getDataType(configKeyArray, 3)
            val constantName =
                getConstantName(configKeyArray, 4)
            val runEnvironment = getRunEnvironment().toLowerCase(Locale.getDefault())
            if (runEnvironment == environment) {
                list.add(arrayOf(dataType, constantName, it.value.toString().quotes()))
            }
        }
        buildConfigProperties.mapKeys {
            val configKeyArray = it.key.toString().split("_")
            if (configKeyArray.size <= 2)
                return@mapKeys
            val dataType =
                getDataType(configKeyArray, 0)
            val constantName =
                getConstantName(configKeyArray, 1)
            list.add(arrayOf(dataType, constantName, it.value.toString().quotes()))
        }
        return list.apply {
            val baseJson = Gson().toJson(modules).base64().quotes()
            add(
                arrayOf(
                    "String",
                    "MODULE_JSON",
                    baseJson
                )
            )
        }
    }

    private fun initModules() {
        try {
            // 1.实例化SAXParserFactory对象
            val factory: SAXParserFactory = SAXParserFactory.newInstance()
            // 2.创建解析器
            val parser: SAXParser = factory.newSAXParser()
            // 3.获取需要解析的文档，生成解析器,最后解析文档
            val file = File(resourcesPath.plus("module.xml"))
            val handler = ModuleHandler()
            parser.parse(file, handler)
            modules = handler.modules
            Log.l("初始化Modules", modules.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getEnvironment(configKeyArray: List<String>): String {
        return configKeyArray[2]
    }

    private fun getConstantName(configKeyArray: List<String>, index: Int): String {
        val sb = StringBuilder()
        configKeyArray.drop(index).forEach {
            sb.append(it).append("_")
        }
        return sb.toString().dropLast(1)
    }

    private fun getDataType(configKeyArray: List<String>, index: Int): String {
        val dataType = configKeyArray[index]
        return if (dataType.indexOf("STRING") >= 0) "String" else dataType.toLowerCase(Locale.getDefault())
    }

    @Throws(Exception::class)
    private fun loadProperties(path: String): Properties {
        return Properties().apply {
            load(InputStreamReader(FileInputStream(path)))
        }
    }

    fun denpendModules(project: Project) {
        project.dependencies {
            this@Config.modules.data.forEach { module ->
                if (!module.app) {
                    add("implementation", project(mapOf("path" to ":${module.name}")))
                }
            }
        }
    }

    fun isAppModule(name: String): Boolean {
        modules.data.forEach {
            if (it.name == name) {
                return it.app
            }
        }
        return false
    }

}