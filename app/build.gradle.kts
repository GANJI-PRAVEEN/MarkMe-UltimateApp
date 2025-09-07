import java.util.regex.Pattern.compile

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.attendanceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.attendanceapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("com.airbnb.android:lottie:3.4.0")
//    implementation ("org.apache.poi:poi-ooxml-lite:5.2.5")
//    implementation ("org.apache.xmlbeans:xmlbeans:5.1.1")
//    implementation ("commons-io:commons-io:2.11.0"
    implementation ("com.jakewharton.timber:timber:5.0.1")
    implementation ("com.github.tony19:logback-android:3.0.0")

    implementation("com.sun.mail:android-mail:1.5.5")
    implementation ("com.sun.mail:android-activation:1.5.")

}