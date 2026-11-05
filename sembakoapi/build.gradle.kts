plugins {
  java
  alias(libs.plugins.spring.boot)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

springBoot {
  mainClass.set("com.example.sembakoapi.SembakoApiApplication")
}

dependencies {
  implementation(platform(libs.spring.boot.dependencies))
  implementation(libs.spring.boot.starter.web)
  implementation(libs.spring.boot.starter.jdbc)
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.validation)
  implementation(libs.sqlite.jdbc)

  testImplementation(libs.spring.boot.starter.test)
}
