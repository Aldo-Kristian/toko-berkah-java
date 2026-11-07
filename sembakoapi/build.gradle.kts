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

  // Pastikan di libs.versions.toml ini mengarah ke spring-boot-starter-data-jdbc
  implementation(libs.spring.boot.starter.jdbc)

  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.validation)
  implementation(libs.sqlite.jdbc)

  // TAMBAHAN BARU: Library untuk mendeteksi Dialect SQLite
  implementation("io.arconia:arconia-data-jdbc-sqlite:0.32.0")

  testImplementation(libs.spring.boot.starter.test)
}
