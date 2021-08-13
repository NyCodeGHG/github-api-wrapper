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
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    id("com.diffplug.spotless")
}

group = rootProject.group
version = rootProject.version

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
                implementation(project(":core"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
    }
}

tasks {
    withType<Test> {
        val secretLoader = SecretLoader(File(rootProject.rootDir, "secrets.properties"), project)
        secretLoader["GH_TOKEN"]?.let {
            if (it.isBlank()) return@let
            environment["GITHUB_TOKEN"] = it
        }
        secretLoader["REPO_OWNER"]?.let {
            if (it.isBlank()) return@let
            environment["REPO_OWNER"] = it
        }
        secretLoader["REPO_NAME"]?.let {
            if (it.isBlank()) return@let
            environment["REPO_NAME"] = it
        }
    }
}

spotless {
    kotlin {
        ktfmt("0.27").kotlinlangStyle()
    }
}
