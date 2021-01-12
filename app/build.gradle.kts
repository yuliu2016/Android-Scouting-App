import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "ca.warp7.android.scouting"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "2020.2.0"
        resConfigs("en", "hdpi")

        // Read the TBA key from local properties
        val propsFile = rootProject.file("local.properties")
        val key = if (propsFile.isFile) {
            val props = Properties()
            props.load(propsFile.inputStream())
            props.getProperty("tba.key", "")
        } else ""
        buildConfigField("String", "TBA_KEY", "\"$key\"")
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
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.preference:preference:1.1.1")
    implementation("com.google.zxing:core:3.4.0")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.7.0")
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.7.0")
    testRuntimeOnly(group = "org.junit.platform", name = "junit-platform-launcher", version = "1.7.0")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
