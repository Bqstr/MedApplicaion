
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //alias(libs.plugins.google.gms.google.services) apply false

    id ("com.google.dagger.hilt.android") version "2.48.1" apply false





    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false

}
buildscript {
    repositories {
        google()  // Google's Maven repository
    }
    dependencies {
        // Google Services plugin
        classpath ("com.google.gms:google-services:4.3.10")
        // Firebase Crashlytics plugin
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
