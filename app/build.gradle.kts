plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //Serialització
    kotlin("plugin.serialization") version "2.0.21"
    // Identificacio de google a través de Firebase
    id("com.google.gms.google-services")
    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "cat.institutmontilivi.tasquesfirebase"
    compileSdk = 35

    defaultConfig {
        applicationId = "cat.institutmontilivi.tasquesfirebase"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //Navegació
    implementation("androidx.navigation:navigation-compose:2.8.5")
    //Biblioteca extesa d'icones
    implementation("androidx.compose.material:material-icons-extended")
    //Serialització
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    //Firebase BoM (Bill of Materials)
    //És BoM qui assigna la versió correcta a cadascuna de les
    //biblioteques de firebase
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    // When using the BoM, don't specify versions in Firebase dependencies
    //Firebase analytics
    implementation("com.google.firebase:firebase-analytics")
    //Firebase autentificació
    implementation ("com.google.firebase:firebase-auth-ktx")
    //Firebase Realtime Dababase
    implementation ("com.google.firebase:firebase-database-ktx")
    //Firebase FireStore
    implementation ("com.google.firebase:firebase-firestore-ktx")
    //Firebase DataStorage
    implementation ("com.google.firebase:firebase-storage-ktx")
    //Firebase Crashlytics
    implementation ("com.google.firebase:firebase-crashlytics-ktx")
    //Firebase Remote config
    implementation ("com.google.firebase:firebase-config-ktx")
    //Firebase Messaging
    implementation ("com.google.firebase:firebase-messaging-ktx")

    //Google play services (per a la identificació a través de Google
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    //Noves dependencies per al sistema d'autentificació per credencials
    implementation ("androidx.credentials:credentials:1.3.0")
    implementation ("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")


    //Descàrregues d'imatges d'Internet
    implementation ("io.coil-kt:coil-compose:2.5.0")

    //ColorPicker
    implementation("com.github.skydoves:colorpicker-compose:1.1.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}