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
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

val publishingType = if (project.version.toString().endsWith("SNAPSHOT"))
    PublishingType.SNAPSHOT
else
    PublishingType.RELEASE

val dokkaJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
    dependsOn(tasks.dokkaHtml)
}

publishing {
    publications.filterIsInstance<MavenPublication>().forEach { publication ->
        publication.apply {
            artifact(dokkaJar.get())
            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/NyCodeGHG/github-api-wrapper")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        name.set("NyCode")
                        email.set("nico@nycode.de")
                        url.set("https://nycode.de")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/NyCodeGHG/github-api-wrapper.git")
                    developerConnection.set("scm:git:ssh://github.com/NyCodeGHG/github-api-wrapper.git")
                    url.set("https://github.com/NyCodeGHG/github-api-wrapper")
                }
            }
        }
    }
    repositories {
        maven {
            name = publishingType.repositoryName
            url = uri(publishingType.repositoryUrl)
            credentials(PasswordCredentials::class)
        }
    }
}

signing {
    val signingKey = project.findProperty("signingKey")?.toString()
    val signingPassword = project.findProperty("signingPassword")?.toString()
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(java.util.Base64.getDecoder().decode(signingKey).decodeToString(), signingPassword)
    }
}
