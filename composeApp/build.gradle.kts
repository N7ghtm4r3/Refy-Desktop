import org.jetbrains.compose.desktop.application.dsl.TargetFormat.*
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm("desktop") {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.value(JvmTarget.JVM_18)
        }
        withJava()
    }
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            api("moe.tlaster:precompose:1.6.0")
            implementation("com.darkrockstudios:mpfilepicker:3.1.0")
            implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha07")
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-alpha07")
            implementation("com.github.N7ghtm4r3:Equinox:1.0.2")
            implementation("com.github.N7ghtm4r3:APIManager:2.2.3")
            implementation("com.tecknobit.refycore:refycore:1.0.0")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
            implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc05-k2")
            implementation("com.github.N7ghtm4r3:Equinox-Compose:1.0.0") {
                exclude("com.github.N7ghtm4r3.Equinox-Compose", "library-android")
            }
            implementation("com.github.skydoves:colorpicker-compose:1.1.2")
            implementation("org.json:json:20231013")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("com.github.N7ghtm4r3:OctocatKDU:1.0.4")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(Deb, Pkg, Exe)
            modules(
                "java.compiler", "java.instrument", "java.management", "java.naming", "java.net.http", "java.prefs",
                "java.rmi", "java.scripting", "java.security.jgss", "java.sql", "jdk.jfr", "jdk.unsupported"
            )
            packageName = "Refy"
            packageVersion = "1.0.0"
            version = "1.0.0"
            description = "References collector and custom links generator"
            copyright = "© 2024 Tecknobit"
            vendor = "Tecknobit"
            licenseFile.set(project.file("LICENSE"))
            macOS {
                bundleID = "com.tecknobit.refy"
                iconFile.set(project.file("src/desktopMain/resources/logo.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/logo.ico"))
                upgradeUuid = UUID.randomUUID().toString()
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/logo.png"))
                packageName = "com-tecknobit-refy"
                debMaintainer = "infotecknobitcompany@gmail.com"
                appRelease = "1.0.0"
                appCategory = "PERSONALIZATION"
                rpmLicenseType = "MIT"
            }
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
            obfuscate.set(true)
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        moduleName.set("Refy")
        outputDirectory.set(layout.projectDirectory.dir("../docs"))
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customAssets = listOf(file("../docs/logo-icon.svg"))
        footerMessage = "(c) 2024 Tecknobit"
    }
}

configurations.all {
    exclude("commons-logging", "commons-logging")
}