import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder

project.version = "2.0.4"
project.description = "Плагин для автоматической выдачи доната!"

plugins {
    kotlin("jvm") version "2.0.21"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
}

dependencies {
    // lombok
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    // paper
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    // others
    compileOnly("net.luckperms:api:5.4")
    implementation("com.github.technicallycoded:FoliaLib:main-SNAPSHOT")
    implementation("org.jetbrains:annotations:22.0.0")

    library(kotlin("stdlib"))
    library(kotlin("reflect"))
}

kotlin {
    jvmToolchain(17)
}

tasks.compileKotlin {
    compilerOptions.javaParameters = true
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

val pluginPackage = "wtf.n1zamu"
tasks.shadowJar {

    archiveFileName.set("SkyDonatePlugin-$version.jar")

    exclude(
        "DebugProbesKt.bin",
        "*.SF", "*.DSA", "*.RSA", "META-INF/**", "OSGI-INF/**",
        "deprecated.properties", "driver.properties", "mariadb.properties", "mozilla/public-suffix-list.txt",
        "org/slf4j/**", "org/apache/logging/slf4j/**", "org/apache/logging/log4j/**", "Log4j-*"
    )

    dependencies {
        exclude(dependency("org.jetbrains.kotlin:.*"))
        exclude(dependency("org.jetbrains.kotlinx:.*"))
        exclude(dependency("org.checkerframework:.*"))
        exclude(dependency("org.jetbrains:annotations"))
        exclude(dependency("org.slf4j:.*"))
    }

    rootDir.resolve("gradle").resolve("relocations.txt").takeIf { it.isFile }?.forEachLine {
        relocate(it, "$pluginPackage.__relocated__.$it")
    }
}

bukkit {
    main = "$pluginPackage.SkyDonatePlugin"
    version = project.version.toString()
    apiVersion = "1.16"
    description = project.description
    load = PluginLoadOrder.STARTUP
    softDepend = listOf("LuckPerms")
    foliaSupported = true

    authors = listOf(
        "github.com/n1zamu",
        "WhyZerVellasskx"
    )

    commands {
        create("skydonate")
            .permission = "skydonate.admin"
    }
}
