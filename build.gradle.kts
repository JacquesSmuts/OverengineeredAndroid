
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.3")
        classpath(kotlin("gradle-plugin", version = "1.3.70"))
        classpath("com.squareup.sqldelight:gradle-plugin:1.3.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
    apply {
        from("$rootDir/ktlint.gradle")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}