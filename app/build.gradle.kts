
plugins {
    id("com.android.application")
    id("com.squareup.sqldelight")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("plugin.serialization") version "1.3.70"
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.jacquessmuts.overengineered"
        minSdkVersion(14)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    testOptions {
        tasks.withType<Test>().all {
            useJUnitPlatform()
            reports {
                html.setEnabled(false)
                junitXml.setEnabled(true)
            }
            maxHeapSize = "1024m"
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/main.kotlin_module")
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(kotlin("stdlib-jdk7", Version.kotlin))
    implementation(kotlin("reflect", Version.kotlin))

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-android:0.11.1")

    // Android
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta7")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.multidex:multidex:2.0.1")

    // Koin
    implementation("org.koin:koin-core:${Version.koin}")
    implementation("org.koin:koin-android:${Version.koin}")
    implementation("org.koin:koin-android-viewmodel:${Version.koin}")

    // HTTP
    implementation("io.ktor:ktor-client-cio:${Version.ktor}")
    implementation("io.ktor:ktor-client-json-jvm:${Version.ktor}")
    implementation("io.ktor:ktor-client-serialization-jvm:${Version.ktor}")
    implementation("io.ktor:ktor-client-logging-jvm:${Version.ktor}")
//    implementation("io.ktor:ktor-client-json:${Version.ktor}")
//    implementation("io.ktor:ktor-client-serialization:${Version.ktor}")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    // DB
    implementation("com.squareup.sqldelight:android-driver:${Version.sqldelight}")
    implementation("com.squareup.sqldelight:coroutines-extensions:${Version.sqldelight}")

    // Jake
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Jacques
//    implementation("com.jacquessmuts.namedargslinter:0.1.4")

    // Unit Tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
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
    const val kotlin = "1.3.70"
    const val koin = "2.0.1"
    const val ktor = "1.3.2"
    const val coroutines = "1.3.3"
    const val sqldelight = "1.2.1"
}