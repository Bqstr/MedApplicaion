import java.util.Properties

plugins {
    //id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    //alias(libs.plugins.google.gms.google.services)

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")

    id("androidx.navigation.safeargs.kotlin")


}


android {
    namespace = "io.oitech.med_application"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.oitech.med_application"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        val properties = Properties()
        properties.load(rootProject.file("local.properties").inputStream())
        val openAiKey = properties.getProperty("OPENAI_API_KEY") ?: ""

        buildConfigField("String", "OPENAI_API_KEY", openAiKey)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
}

dependencies {

    implementation("androidx.compose.compiler:compiler:1.5.2")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation(libs.androidx.activity)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.core:core-splashscreen:1.0.1")


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    //implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries


    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")


    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")


    // Hilt ViewModel extension

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")


    //implementation("androidx.hilt:hilt-lifecyclfoundatione-viewmodel:1.0.0-apha03")

    // Firebase Authentication


    //dots for pager

    implementation("com.tbuonomo:dotsindicator:5.1.0")
    implementation("androidx.compose.foundation:foundation:1.7.8")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")


//Google Services & Maps
    implementation("com.google.maps.android:maps-compose:2.9.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("com.google.maps.android:maps-ktx:5.0.0")

    implementation("com.google.android.gms:play-services-location:21.3.0")



    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2") // or latest version

}