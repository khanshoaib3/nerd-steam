import org.gradle.kotlin.dsl.libs

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
    namespace = "com.github.khanshoaib3.steamcompanion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.github.khanshoaib3.steamcompanion"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "mozilla/public-suffix-list.txt"
            excludes += "META-INF/INDEX.LIST"
        }
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

    // Skrape-it
    implementation(libs.skrapeit.html.parser)
    implementation(libs.skrapeit.http.fetcher)

    // Datetime
    implementation(libs.kotlinx.datetime)

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

    // Navigation 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)

    // WorkManager
    implementation(libs.androidx.work.runtime)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Debug-only tools
    debugImplementation(libs.androidx.ui.tooling)
    debugRuntimeOnly(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}