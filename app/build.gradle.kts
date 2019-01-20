plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "ca.warp7.android.scouting"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "v2019.1.1-alpha"
        resConfigs("en", "hdpi")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.11")
    implementation ("com.android.support:appcompat-v7:28.0.0")
    implementation ("com.google.zxing:core:3.3.3")
    //implementation 'com.android.volley:volley:1.1.1'
}