plugins {
    id("com.android.application")
}

android {
    namespace = "net.premiumads.example.admob"
    compileSdk = 34

    defaultConfig {
        applicationId = "net.premiumads.example.admob.java"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-ads:25.1.0")
    implementation("androidx.javascriptengine:javascriptengine:1.0.0-beta01")
    implementation("net.premiumads.sdk:admob-adapter-v2:1.0.8")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
