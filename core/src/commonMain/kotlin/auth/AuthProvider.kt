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

package dev.nycode.github.auth

import io.ktor.client.HttpClientConfig
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.BasicAuthCredentials
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

/**
 * Abstraction of different authentication methods for authenticating with the GitHub API.
 */
public sealed interface AuthProvider {

    public fun HttpClientConfig<*>.configureClient() {}
    public fun HttpRequestBuilder.configureAuth() {}

    /**
     * [AuthProvider] for accessing the GitHub API without authentication.
     */
    public object None : AuthProvider

    /**
     * [AuthProvider] for authenticating with [Basic Authentication](https://docs.github.com/en/rest/overview/other-authentication-methods#basic-authentication).
     */
    public class Basic(private val _username: String, private val token: String) : AuthProvider {

        override fun HttpClientConfig<*>.configureClient() {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(_username, token)
                    }
                    sendWithoutRequest { true }
                }
            }
        }
    }

    /**
     * [AuthProvider] for authentication with an [OAuth Token](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/authorizing-oauth-apps) or a [Personal Access Token](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token).
     */
    public class OAuth(private val token: String) : AuthProvider {

        override fun HttpRequestBuilder.configureAuth() {
            header(HttpHeaders.Authorization, "token $token")
        }
    }
}
