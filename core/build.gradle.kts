/*
 *    Copyright 2021 NyCode
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

plugins {
    kotlin("multiplatform") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
    id("org.jetbrains.dokka") version "1.5.0"
    id("com.diffplug.spotless") version "5.14.2"
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
        all {
            languageSettings {
                useExperimentalAnnotation("kotlin.RequiresOptIn")
                useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
            }
        }
        commonMain {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.serialization)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
            }
        }
        commonTest {
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
        val secretLoader = SecretLoader(rootProject.file("secrets.properties"))
        environment["GITHUB_TOKEN"] = secretLoader["GH_TOKEN"]
        environment["REPO_OWNER"] = secretLoader["REPO_OWNER"]
        environment["REPO_NAME"] = secretLoader["REPO_NAME"]
    }
}

spotless {
    kotlin {
        ktfmt("0.27").kotlinlangStyle()
    }
}
