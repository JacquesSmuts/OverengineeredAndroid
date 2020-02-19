
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.jacquessmuts.overengineered"
        minSdkVersion(15)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", "1.3.61"))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.2.2")
}