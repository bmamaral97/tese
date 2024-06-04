import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.bmamaral"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.jpa") version "1.8.0"

    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.1.0"

    id("io.freefair.lombok") version "6.6.1"

    `maven-publish`
    antlr
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/uoc/java-lti-1.3-core")
        credentials {
            username = "bmamaral"
            password = "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"
        }
        authentication {
            create<BasicAuthentication>("basic")
        }
    }
    maven {
        url = uri("https://maven.pkg.github.com/uoc/java-lti-1.3-jwt")
        credentials {
            username = "bmamaral"
            password = "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"
        }
        authentication {
            create<BasicAuthentication>("basic")
        }
    }
    maven {
        url = uri("https://maven.pkg.github.com/uoc/java-lti-1.3")
        credentials {
            username = "bmamaral"
            password = "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"
        }
        authentication {
            create<BasicAuthentication>("basic")
        }
    }
    maven {
        url = uri("https://maven.pkg.github.com/uoc/spring-boot-lti-advantage")
        credentials {
            username = "bmamaral"
            password = "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"
        }
        authentication {
            create<BasicAuthentication>("basic")
        }
    }
    maven {
        url = uri("https://maven.pkg.github.com/uoc/spring-boot-lti-advantage-jwks")
        credentials {
            username = "bmamaral"
            password = "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"
        }
        authentication {
            create<BasicAuthentication>("basic")
        }
    }
}

dependencies {
    // SPRING
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mustache")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("org.springframework.security:spring-security-test")
    implementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springframework.security:spring-security-config:5.7.3")

    // OAUTH2
    implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.6.8")

    // KOTLIN
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")
    implementation("org.jetbrains.kotlin:kotlin-maven-allopen:1.8.0")
    implementation("org.jetbrains.kotlin:kotlin-maven-noarg:1.8.0")


    // SWAGGER
    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.14")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
    implementation("com.mercateo.spring:spring-security-jwt:2.1.1")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.springframework.boot:spring-boot-devtools:2.7.8")

    // ANTLR PACKAGES (old version to match IntelliJ antlr version)
    implementation("org.antlr:antlr4-master:4.10.1")
    implementation("org.antlr:antlr4-runtime:4.10.1")

    // LTI PACKAGES
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //implementation("edu.uoc.elc.lti:lti-13:1.0.0")
    //implementation("edu.uoc.elc.lti:lti-13-core:1.0.0")
    //implementation("edu.uoc.elc.lti:lti-13-jwt:1.0.0")
    //implementation("edu.uoc.elc.lti:spring-boot-lti-advantage:1.0.0")

    // NIMBUS JOSE JWT
    implementation("com.nimbusds:nimbus-jose-jwt:9.29")
//    implementation("io.jsonwebtoken:jjwt:0.2")
//    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("org.apache.httpcomponents:httpclient:4.5")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor", "-long-messages")
}