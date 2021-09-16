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
    `maven-publish`
    signing
}

group = rootProject.group
version = rootProject.version

val publishingType = if (project.version.toString().endsWith("SNAPSHOT"))
    PublishingType.SNAPSHOT
else
    PublishingType.RELEASE

publishing {
    publications.filterIsInstance<MavenPublication>().forEach { publication ->
        publication.apply {
            pom.configurePom(project)
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
