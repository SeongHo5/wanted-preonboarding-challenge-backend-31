plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.wanted.preonboarding"
version = "0.1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

val queryDSLVersion by extra("5.1.0")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    runtimeOnly("org.postgresql:postgresql")
    // Lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:$queryDSLVersion:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:$queryDSLVersion:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    // Local Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.springframework.boot:spring-boot-docker-compose")
    // Testing
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
    }
}

spotless {
    java {
        googleJavaFormat()
            .formatJavadoc(true)
        endWithNewline()
        formatAnnotations()
        removeUnusedImports()
        trimTrailingWhitespace()
    }
}

tasks.jar {
    isEnabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
    reports {
        html.required.set(false)
        junitXml.required.set(false)
    }
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2)
        .coerceAtLeast(1)
        .coerceAtMost(4)
}
