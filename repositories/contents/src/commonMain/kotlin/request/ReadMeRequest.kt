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

package dev.nycode.github.repositories.contents.request

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.repositories.contents.model.FileRepositoryContent
import dev.nycode.github.request.get
import io.ktor.client.call.receive
import io.ktor.client.features.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.ContentType

public class ReadMeRequest(
    @PublishedApi
    internal val githubClient: GitHubClientImpl,
    @PublishedApi
    internal val owner: String,
    @PublishedApi
    internal val repo: String,
    @PublishedApi
    internal val path: String,
    @PublishedApi
    internal val ref: String?
) {
    /**
     * Gets the readme content in raw text.
     */
    public suspend inline fun raw(): String? = request<String> {
        accept(ContentType("application", "vnd.github.v3.html"))
    }

    /**
     * Gets the readme content in rendered html.
     */
    public suspend inline fun html(): String? = request<String> {
        accept(ContentType("application", "vnd.github.v3.html"))
    }

    /**
     * Gets the readme content in a regular [FileRepositoryContent].
     */
    public suspend inline fun regular(): FileRepositoryContent? = request()

    @PublishedApi
    internal suspend inline fun <reified T> request(noinline builder: HttpRequestBuilder.() -> Unit = {}): T? {
        val response = githubClient.get<HttpResponse>("repos", owner, repo, "readme", path) {
            request {
                builder()
                parameter("ref", ref)
                expectSuccess = false
            }
        }
        println(response.request.headers)
        return when (response.status.value) {
            200 -> response.receive()
            else -> null
        }
    }
}
