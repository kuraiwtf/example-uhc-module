plugins {
  java
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

repositories {
  mavenCentral()
  mavenLocal()

  maven("https://repo.lunarclient.dev")
  maven("https://repo.j4c0b3y.net/public/")
  maven("https://repo.codemc.io/repository/maven-releases/")
  maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
  compileOnly(libs.apollo.api)
  compileOnly(libs.spigot)
  compileOnly(libs.uhc.api)
  compileOnly(libs.packetevents.spigot)
  compileOnly(libs.lombok)
  annotationProcessor(libs.lombok)
}
