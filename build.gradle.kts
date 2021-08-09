plugins {
    kotlin("multiplatform") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
    id("com.diffplug.spotless") version "5.14.2"
    id("at.stnwtr.gradle-secrets-plugin") version "1.0.1"
}

group = "de.nycode"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        browser()
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.serialization)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.java)
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
        val jsTest by getting
    }
}

tasks {
    withType<Test> {
        val githubToken = secrets.getOrEnv("GH_TOKEN")
        if (githubToken != null) {
            environment["GITHUB_TOKEN"] = githubToken
        }
        val repoOwner = secrets.getOrEnv("REPO_OWNER")
        if (repoOwner != null) {
            environment["REPO_OWNER"] = repoOwner
        }
        val repoName = secrets.getOrEnv("REPO_NAME")
        if (repoName != null) {
            environment["REPO_NAME"] = repoName
        }
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

spotless {
    kotlin {
        ktfmt("0.27").kotlinlangStyle()
    }
}
