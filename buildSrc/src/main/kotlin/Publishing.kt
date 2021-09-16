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

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom

fun MavenPom.configurePom(project: Project) {
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
