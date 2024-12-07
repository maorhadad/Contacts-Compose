import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.hadadas.contacts"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hadadas.contacts"
        minSdk = 26
        targetSdk = 35

        // Dynamically set versionCode and versionName from environment variables
        versionCode = System.getenv("VERSION_CODE")?.toInt() ?: 1
        versionName = System.getenv("VERSION_NAME") ?: "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        // Modify the existing debug signing configuration
        getByName("debug") {
            storeFile = file("${rootProject.rootDir}/debug.keystore.jks")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        val releasePropertiesFile = file("${rootProject.rootDir}/release.properties")
        val releaseProperties = Properties()

        if (releasePropertiesFile.exists()) {
            releaseProperties.load(releasePropertiesFile.inputStream())
        }
        // Release signing configuration
        create("release") {
            println("Key Alias: ${System.getenv("KEY_ALIAS")}")
            println("Keystore Path: ${System.getenv("KEYSTORE_FILE_PATH")}")
            println("Keystore Password: ${System.getenv("KEYSTORE_PASSWORD")}")
            println("Key Password: ${System.getenv("KEY_PASSWORD")}")
            storeFile = file(System.getenv("KEYSTORE_FILE_PATH") ?: "${rootProject.rootDir}/release.keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: releaseProperties.getProperty("release_store_password", "release_password")
            keyAlias = System.getenv("KEY_ALIAS") ?: releaseProperties.getProperty("release_key_alias", "release_key")
            keyPassword = System.getenv("KEY_PASSWORD") ?: releaseProperties.getProperty("release_key_password", "release_password")
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug" // Optional, to differentiate debug builds
            versionNameSuffix = "-debug"  // Optional, to differentiate debug builds
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.coil.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
