import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.4.10"
    id("org.jetbrains.compose") version "1.11.1"
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.10"
}

group = "com.example"
version = "1.0.0"

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material3:material3:1.9.0")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

compose.desktop {
    application {
        mainClass = "com.example.unitconverter.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "UnitConverterDesktop"
            packageVersion = "1.0.0"
        }
    }
}
