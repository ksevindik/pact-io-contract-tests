import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("au.com.dius.pact") version "4.1.6"
    id("com.wiredforcode.spawn") version "0.8.2"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
}

extra["springCloudVersion"] = "2020.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springdoc:springdoc-openapi-ui:1.2.27")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("au.com.dius:pact-jvm-provider-junit5:4.0.10")
    testImplementation("au.com.dius:pact-jvm-consumer-junit5:4.0.10")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
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

springBoot() {
    buildInfo()
}

tasks.register<com.wiredforcode.gradle.spawn.SpawnProcessTask>("startService") {
    dependsOn("assemble")
    command = "java -jar build/libs/${project.name}-${project.version}.jar"
    ready = "application started"
}

tasks.register<com.wiredforcode.gradle.spawn.KillProcessTask>("stopService") {
}

tasks.register("stopService2") {
    doLast {
        project.exec {
            executable("/bin/bash")
            args("-c", "kill `cat /tmp/a-service.pid`")
        }
    }
}

pact {

    broker {
        pactBrokerUrl = "http://localhost"
    }

    serviceProviders {
        val s = create("b-service")
        s.stateChangeUrl = uri("http://localhost:8084/pact/stateChange").toURL()
        s.startProviderTask = tasks.named("startService")
        s.terminateProviderTask = tasks.named("stopService")
        s.fromPactBroker(closureOf<Any> {
            this.withGroovyBuilder {
                setProperty("enablePending", true)
                setProperty("providerTags", listOf<String>("latest"))
            }
        })
        s.hasPactsFromPactBroker(
                "http://localhost", closureOf<Any> {}
        )
    }

    publish {
        pactBrokerUrl = "http://localhost"
        version = "${project.version}"
    }
}