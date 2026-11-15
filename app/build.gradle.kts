plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

fun buildConfigString(value: String): String =
    "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""

val youtubeApiKey = providers.gradleProperty("YOUTUBE_DATA_API_KEY").orElse("").get()
val googleWebClientId = providers.gradleProperty("GOOGLE_WEB_CLIENT_ID").orElse("").get()

android {
    namespace = "com.subhadip.subhuplay"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.subhadip.subhuplay"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        buildConfigField("String", "YOUTUBE_DATA_API_KEY", buildConfigString(youtubeApiKey))
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", buildConfigString(googleWebClientId))
    }

    // Explicitly sign debug APKs with the stable keystore restored by CI.
    // Local builds use the same standard Android debug keystore path.
    signingConfigs {
        getByName("debug") {
            storeFile = file("${System.getProperty("user.home")}/.android/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.05.01"))
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.5.0-alpha20")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("androidx.credentials:credentials:1.7.0-alpha03")
    implementation("androidx.credentials:credentials-play-services-auth:1.7.0-alpha03")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
