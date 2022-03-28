import Versions.coroutinesKtx

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Hilt
    kotlin("kapt")
    id("dagger.hilt.android.plugin")

    // nav safe args
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-android-extensions")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.ravitej.flickrsearch"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
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

    // viewBinding & DataBinding
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    // app libraries
    implementation(AppDependencies.appLibraries)

    // test libraries
    testImplementation(AppDependencies.testLibraries)
    androidTestImplementation(AppDependencies.androidTestLibraries)
    testRunTime(AppDependencies.testRuntimeEngines)

    // dependency injection(HILT)
    implementation(AppDependencies.hiltLibraries)
    kapt(AppDependencies.hiltAnnotationProcessorLibraries)

    // lifecycle libraries
    implementation(AppDependencies.lifeCycleComponentLibraries)
    testImplementation(AppDependencies.lifeCycleTestComponentLibraries)

    // navigation graph libraries
    implementation(AppDependencies.navGraphLibraries)
    androidTestImplementation(AppDependencies.navGraphTestingLibraries)

    // Room libraries
    implementation(AppDependencies.roomLibraries)
    kapt(AppDependencies.roomAnnotationProcessorLibraries)
    testImplementation(AppDependencies.roomTestingLibraries)

    // Networking libraries
    implementation(AppDependencies.networkingLibraries)
    kapt(AppDependencies.networkingAnnotationProcessor)

    // Glide
    implementation(AppDependencies.glideLibraries)
    kapt(AppDependencies.glideAnnotationLibraries)

    //FIXME: Move to dependency layer
    testImplementation("io.mockk:mockk:1.12.2")
}
