import org.gradle.kotlin.dsl.libs

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
//    kotlin("plugin.serialization") version "2.0.21"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.github.khanshoaib3.steamcompanion"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.github.khanshoaib3.steamcompanion"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    implementation(libs.coil.gif)
    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Skrape-it
    implementation(libs.skrapeit.dsl)
    implementation(libs.skrapeit.html.parser)
    implementation(libs.skrapeit.http.fetcher)
    // Datetime
    implementation(libs.kotlinx.datetime)
    // Preferences Datastore
    implementation(libs.androidx.datastore.preferences)
    // Google Fonts
    implementation(libs.androidx.ui.text.google.fonts)
    // Material Icons
    implementation(libs.material.icons.extended)
    // Material Adaptive Layouts
    implementation(libs.androidx.material.adaptive)
    implementation(libs.androidx.material.adaptive.layout)
    implementation(libs.androidx.material.adaptive.navigation)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.material.adaptive.navigation.suite)
    // Dagger-Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // Retrofit
    implementation(libs.retrofit)
    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)
    // Retrofit with Kotlin serialization Converter
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    // Hil nav fragment
    implementation(libs.androidx.hilt.navigation.fragment)

    implementation(libs.androidx.foundation) // Added for AnnotatedString.fromHtml()
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}