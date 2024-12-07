import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val buildNumber = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull() ?: 1

val generateVersionName: () -> String = {
    println("GITHUB_RUN_NUMBER: ${System.getenv("GITHUB_RUN_NUMBER")}")
    val buildMajorVersion = 1
    val buildMinorVersion = 0
    "${buildMajorVersion}.${buildMinorVersion}.${buildNumber}"
}

android {
    namespace = "com.hadadas.contacts"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hadadas.contacts"
        minSdk = 26
        targetSdk = 35

        // Dynamically set versionCode and versionName from environment variables
        versionCode = buildNumber
        versionName = generateVersionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applicationVariants.all { variant ->
            variant.outputs.all { output ->
                val result = if (output is ApkVariantOutputImpl) {
                    val variantName = variant.name
                    val versionName = variant.versionName ?: "1.0" // Default version name
                    val baseName = "Contacts-$versionName"
                    val newFileName = if (variantName.contains("release", true)) {
                        "$baseName.apk"
                    } else {
                        "$baseName-debug.apk"
                    }
                    output.outputFileName = newFileName
                    true
                } else {
                    false
                }
                result
            }
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
