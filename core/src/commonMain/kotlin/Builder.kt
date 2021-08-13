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
import de.nycode.github.request.GitHubErrorResponse
import de.nycode.github.request.GitHubRequestException
import de.nycode.github.utils.receiveOrNull
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.userAgent
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Creates a new [GitHubClient] by applying [builder].
 */
public inline fun GitHubClient(builder: GitHubClientBuilder.() -> Unit = {}): GitHubClient {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    return GitHubClientBuilder().apply(builder).build()
}

/**
 * Shared API of [GitHubClientBuilder] on all platforms.
 */
public abstract class GitHubClientBuilderBase {

    /**
     * The base url of the GitHub API.
     */
    public var baseUrl: String = "https://api.github.com"

    /**
     * The [AuthProvider] used to authenticate with the GitHub API.
     *
     * Default: [AuthProvider.None]
     *
     * @see AuthProvider
     */
    public var authProvider: AuthProvider = AuthProvider.None

    /**
     * Sets the [HttpClientEngine] used for communication with Apple.
     *
     * This acts as a shortcut to `httpClient = HttpClient(ENGINE)` so setting both at the same time doesn't work
     *
     * @see httpClient
     * @see httpClientEngine
     * @see HttpClientEngine
     */
    public var httpClientEngine: HttpClientEngine? = null
        set(value) {
            requireNotNull(value)
            require(httpClient == null) {
                "Cannot set httpClient and httpClientEngine at the same time" +
                    "Use HttpClient(ENGINE) instead."
            }
            httpClient = HttpClient(value)
            field = value
        }

    /**
     * Sets the [HttpClient] used for communication with apple.
     *
     * if you just want to set the engine use [httpClientEngine]
     *
     * @see httpClientEngine
     * @see HttpClient
     */
    public var httpClient: HttpClient? = null
        set(value) {
            require(httpClientEngine == null) {
                "Cannot set httpClient and httpClientEngine at the same time" +
                    "Use HttpClient(ENGINE) instead."
            }
            field = value
        }

    /**
     * Decides how to create an [HttpClient] based on [httpClient] and [httpClientEngine].
     */
    protected fun decideOnClient(): HttpClient {
        val preconfiguredClient = httpClient
        val engine = httpClientEngine

        val client = when {
            preconfiguredClient != null -> preconfiguredClient
            engine != null -> HttpClient(engine)
            else -> HttpClient()
        }

        return client.configure()
    }

    private fun HttpClient.configure(): HttpClient = config {
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
        HttpResponseValidator {
            handleResponseException { exception ->
                if (exception !is ClientRequestException) return@handleResponseException
                val errorResponse = exception.response.receiveOrNull<GitHubErrorResponse>()
                if (errorResponse != null) {
                    throw GitHubRequestException(errorResponse)
                }
            }
        }
    }

    /**
     * Creates a new [GitHubClient] with the specified options.
     */
    public open fun build(): GitHubClient = GitHubClient(baseUrl, decideOnClient())
}

/**
 * Platform agnostic builder for [GitHubClient].
 */
public expect class GitHubClientBuilder constructor() : GitHubClientBuilderBase
