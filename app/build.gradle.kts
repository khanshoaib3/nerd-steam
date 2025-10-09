import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
//    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.jetbrains.kotlin.serialization)
    id("kotlin-parcelize")
    // Keep this at last (https://stackoverflow.com/questions/70550883/warning-the-following-options-were-not-recognized-by-any-processor-dagger-f)
    id("kotlin-kapt")
    id("com.autonomousapps.dependency-analysis") version "2.19.0"
}

android {
    namespace = "com.github.khanshoaib3.nerdsteam"
    compileSdk = 36

    val secretProperties = Properties()
    val secretPropertiesFile = File(rootDir, "secret.properties")
    if (secretPropertiesFile.exists() && secretPropertiesFile.isFile) {
        secretPropertiesFile.inputStream().let {
            secretProperties.load(it)
        }
    } else {
        throw IllegalStateException(
            "Missing configuration file: 'secret.properties'.\n"
                    + "Please create this file in the project root and define required keys.n"
        )
    }

    if (!secretProperties.containsKey("IS_THERE_ANY_DEAL_API_KEY")) {
        throw IllegalStateException("Missing API key: Please add IS_THERE_ANY_DEAL_API_KEY to secret.properties")
    }


    defaultConfig {
        applicationId = "com.github.khanshoaib3.nerdsteam"
        minSdk = 30
        targetSdk = 36
        // Following versionCode convention from f-droid repo: https://gitlab.com/fdroid/fdroidclient/-/blob/master/metadata/en-US/changelogs
        versionCode = 1000004
        versionName = "1.0.4"
        // Ref: https://stackoverflow.com/a/44969974/12026423
        setProperty("archivesBaseName", "nerd-steam-v$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "IS_THERE_ANY_DEAL_API_KEY",
            secretProperties["IS_THERE_ANY_DEAL_API_KEY"].toString()
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = true
            isDebuggable = false
        }
        debug {
            isDebuggable = true
            versionNameSuffix = "-debug"
        }
    }
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "mozilla/public-suffix-list.txt"
            excludes += "META-INF/INDEX.LIST"
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

// Workaround to fix baseline.profile not being reproducible bug
// https://gitlab.com/fdroid/fdroiddata/-/merge_requests/26227#note_2766495135
tasks.whenTaskAdded {
    if (name.contains("ArtProfile")) {
        enabled = false
    }
}

dependencies {
    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    runtimeOnly(libs.coil.gif)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Jsoup
    implementation(libs.jsoup)

    // Datetime
    implementation(libs.kotlinx.datetime)
    implementation(libs.timeago)

    // Preferences Datastore
    implementation(libs.androidx.datastore.preferences)

    // Material Icons & Adaptive Layouts
    implementation(libs.material.icons.extended)
    implementation(libs.androidx.material.adaptive)
    implementation(libs.androidx.material.adaptive.layout)
    implementation(libs.androidx.material.adaptive.navigation)
    implementation(libs.androidx.compose.material.adaptive.navigation.suite)

    // Compose Material Window Size Class
    implementation(libs.androidx.compose.materialWindow)

    // Dagger-Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit & Serialization
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.json)

    // AndroidX + Compose Core
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.splashscreen)

    // Navigation 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)

    // WorkManager
    implementation(libs.androidx.work.runtime)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    // Logging
    implementation(libs.timber)

    // Debug-only tools
    debugImplementation(libs.androidx.ui.tooling)
    debugRuntimeOnly(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
