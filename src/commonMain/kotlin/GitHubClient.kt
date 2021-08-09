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

package de.nycode.github

import de.nycode.github.auth.AuthProvider
import de.nycode.github.repositories.RepositoriesAPI
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

public class GitHubClient(
    public val baseUrl: String = "https://api.github.com",
    httpBuilder: HttpClientConfig<*>.() -> Unit = {},
    private val authProvider: AuthProvider = AuthProvider.None
) {

    init {
        require(baseUrl.startsWith("https://")) { "GitHub API base url must start with https." }
    }

    internal val httpClient = HttpClient() {
        httpBuilder()
        with(authProvider) {
            configureClient()
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        defaultRequest {
            with(authProvider) {
                configureAuth()
            }
            // Set the Accept Header according to
            // https://docs.github.com/en/rest/overview/resources-in-the-rest-api#current-version
            header(HttpHeaders.Accept, "application/vnd.github.v3+json")
            userAgent("NyCodeGHG/github-api-wrapper") // TODO: replace hardcoded repo with build variable
        }
    }

    public val repos: RepositoriesAPI = RepositoriesAPI(this)

}
