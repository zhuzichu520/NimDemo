logger.lifecycle("初始化settings")

rootProject.name = ("NimDemo")

include(
    "app",
    "library-shared",
    "module-main",
    "module-nim",
    "module-media"
)
