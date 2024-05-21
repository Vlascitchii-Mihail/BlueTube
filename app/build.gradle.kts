import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.safe.args.plugin)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "com.usm.bluetube"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.usm.bluetube"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties: Properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    //ViewModel
    implementation(libs.lifecycle.viewmodel.ktx)

    //LiveData
    implementation(libs.livedata)
    //RecyclerView
    implementation (libs.recyclerview)

    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.lifecycle.runtime)

    //Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.ksp.hilt.compiler)

    //navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp3)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.moshi)
    implementation(libs.moshi.converter)
    ksp(libs.moshi.compiler)
    implementation(libs.interceptor)

    //datastore
    implementation(libs.datastore)

    //Glide
    implementation(libs.glide)

    //Paging
    implementation(libs.paging)

    //Youtube player
    implementation(libs.youtube.player)
    implementation(libs.youtube.player.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}