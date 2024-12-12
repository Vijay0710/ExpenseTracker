plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.compose)
}

android {
    namespace = "com.eyeshield.expensetracker"
    compileSdk = 35

    room {
        schemaDirectory("$projectDir/schemas")
    }

    defaultConfig {
        applicationId = "com.eyeshield.expensetracker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://eyeshield.assimilate.cloud\"")
            buildConfigField("String", "API_VALIDATION_KEY", "\"dmlqYXk6QHZpamF5KjcvMA==\"")
        }

        debug {
            buildConfigField("String", "BASE_URL", "\"https://eyeshield.assimilate.cloud\"")
            buildConfigField("String", "API_VALIDATION_KEY", "\"dmlqYXk6QHZpamF5KjcvMA==\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/kotlinx-io.kotlin_module"
            excludes += "META-INF/atomicfu.kotlin_module"
            excludes += "META-INF/kotlinx-coroutines-io.kotlin_module"
            excludes += "META-INF/kotlinx-coroutines-core.kotlin_module"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose Dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testing.manifest)

    // Unit Testing and Feature Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)

    // NavHost Dependency
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Room DB
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Ktor
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.logging)

    //Encrypted Shared Prefs
    implementation(libs.encrypted.shared.prefs)

    //Logging
    implementation(libs.timber)

    //Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
}