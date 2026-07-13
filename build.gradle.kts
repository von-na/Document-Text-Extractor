plugins {
    java
    application
    id("com.gradleup.shadow") version "9.4.3"
}

group = "com.von"
version = "1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

application {
    mainClass = "com.von.textextract.Application"
}

tasks.shadowJar {
    archiveBaseName = "text-extract"
    archiveVersion = ""
    archiveClassifier = ""
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    mergeServiceFiles()
}

val lombok = "org.projectlombok:lombok:1.18.46"

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.apache.tika:tika-core:4.0.0-alpha-1")
    implementation("org.apache.tika:tika-parsers-standard-package:4.0.0-alpha-1")
    implementation("org.apache.tika:tika-serialization:4.0.0-alpha-1")

    implementation("ch.qos.logback:logback-classic:1.5.37")
    implementation("org.apache.logging.log4j:log4j-to-slf4j:2.26.1")
    implementation("info.picocli:picocli:4.7.7")

    compileOnly(lombok)
    annotationProcessor(lombok)

    testCompileOnly(lombok)
    testAnnotationProcessor(lombok)
}

tasks.test {
    useJUnitPlatform()
}
