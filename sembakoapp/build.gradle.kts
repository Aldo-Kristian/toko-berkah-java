plugins {
  alias(libs.plugins.android.application)
}

val apiBaseUrl =
  providers
    .gradleProperty("apiBaseUrl")
    .getOrElse("http://10.0.2.2:8080/")

val useRemoteApi =
  providers
    .gradleProperty("useRemoteApi")
    .map { it.toBoolean() }
    .getOrElse(false)

android {
  namespace = "com.example.sembakoapp"
  compileSdk = 37

  defaultConfig {
    applicationId = "com.example.sembakoapp"
    minSdk = 26
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"
    buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
    buildConfigField("boolean", "USE_REMOTE_API", useRemoteApi.toString())
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

dependencies {
  implementation(libs.appcompat)
  implementation(libs.core)
  implementation(libs.activity)
  implementation(libs.fragment)
  implementation(libs.recyclerview)
  implementation(libs.constraintlayout)
  implementation(libs.material)
  implementation(libs.swiperefreshlayout)
  implementation(libs.navigation.fragment)
  implementation(libs.navigation.ui)
  implementation(libs.lifecycle.viewmodel)
  implementation(libs.lifecycle.livedata)
  implementation(libs.lifecycle.runtime)
  implementation(libs.retrofit)
  implementation(libs.converter.gson)
  implementation(libs.gson)
  implementation(libs.okhttp)

  testImplementation(libs.junit)
  testImplementation(libs.mockwebserver)
}
