rootProject.name = "ok-marketplace-202308"
include("m1l1-hello")
include("m1l2-basic")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}
