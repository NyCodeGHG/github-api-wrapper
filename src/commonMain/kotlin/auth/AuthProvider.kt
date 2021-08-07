package de.nycode.github.auth

import io.ktor.client.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*

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
                    basic {
                        credentials {
                            BasicAuthCredentials(_username, token)
                        }
                    }
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
