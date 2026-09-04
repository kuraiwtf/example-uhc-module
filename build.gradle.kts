plugins {
  java
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

repositories {
  mavenCentral()
  mavenLocal()

  maven {
    name = "GitHubPackages"
    url = uri("https://maven.pkg.github.com/kuraiwtf/uhc")
    credentials {
      username = (project.findProperty("githubActor") ?: System.getenv("USERNAME")) as String?
      password = (project.findProperty("githubPassword") ?: System.getenv("TOKEN")) as String?
    }
  }

  maven("https://jitpack.io")
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
