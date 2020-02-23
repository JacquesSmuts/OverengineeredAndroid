
plugins {
    id("com.android.application")
    id("com.squareup.sqldelight")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/main.kotlin_module")
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(kotlin("stdlib-jdk7", Version.kotlin))
    implementation(kotlin("reflect", Version.kotlin))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}")

    // Android
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    // Koin
    implementation("org.koin:koin-core:${Version.koin}")
    implementation("org.koin:koin-android:${Version.koin}")
    implementation("org.koin:koin-android-viewmodel:${Version.koin}")

    // HTTP
    implementation("com.squareup.retrofit2:retrofit:2.7.1")
    implementation("com.squareup.retrofit2:converter-moshi:2.7.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.2")
    implementation("com.squareup.okhttp3:okhttp:3.14.4")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.0")

    // DB
    implementation("com.squareup.sqldelight:android-driver:${Version.sqldelight}")
    implementation("com.squareup.sqldelight:coroutines-extensions:${Version.sqldelight}")

    // Jake
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Unit Tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutines}")
    testImplementation("org.koin:koin-test:${Version.koin}")
    testImplementation("com.github.gmazzo:okhttp-mock:1.3.2")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("com.squareup.sqldelight:sqlite-driver:${Version.sqldelight}")

    // InstrumentedTests
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.2.2")
}

object Version {
    const val kotlin = "1.3.61"
    const val koin = "2.0.1"
    const val coroutines = "1.3.3"
    const val sqldelight = "1.2.1"
}