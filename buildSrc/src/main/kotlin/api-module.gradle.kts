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
    id("org.jlleitschuh.gradle.ktlint")
    `maven-publish`
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(BOTH) {
        browser()
        nodejs()
    }

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.contracts.ExperimentalContracts")
                optIn("kotlin.time.ExperimentalTime")
                optIn("dev.nycode.github.utils.GitHubWrapperInternals")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
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
