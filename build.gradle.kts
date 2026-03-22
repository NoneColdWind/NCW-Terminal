plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.1.5"
}

group = "cn.ncw.javafx"
version = "1.0.5"

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    maven { url = uri("https://maven.aliyun.com/repository/google/") }
    google()
    gradlePluginPortal()

    flatDir {
        dirs("libs")
    }

    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule = "cn.ncw.javafx.ncwjavafx"
    mainClass = "cn.ncw.javafx.ncwjavafx.NCWTerminal"  // 主类全限定名
    applicationName = "NCW-Terminal"
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=javafx.graphics"
    )
}

javafx {
    version = "25.0.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web", "javafx.swing", "javafx.media")
}

dependencies {

    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    implementation("javazoom:jlayer:1.0.1")

    // NCW Logger
    implementation(name ,"NCW-Logger-1.0.4-hotfix2-1")

    // NCW Music
    implementation(name ,"Music-1.0.4-hotfix2")

    // NCW Utils
    implementation(name, "NCWUtils-1.0.1")

    implementation("com.jfoenix:jfoenix:9.0.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.20")
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") {
        exclude(group = "org.openjfx")
    }
    implementation("net.synedra:validatorfx:0.5.0") {
        exclude(group = "org.openjfx")
    }
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
    implementation("eu.hansolo:tilesfx:21.0.3") {
        exclude(group = "org.openjfx")
    }
    implementation("com.github.almasb:fxgl:17.3") {
        exclude(group = "org.openjfx")
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}


tasks.withType<Test> {
    useJUnitPlatform()
}
