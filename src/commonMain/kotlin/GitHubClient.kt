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
    private val baseUrl: String = "https://api.github.com",
    httpBuilder: HttpClientConfig<*>.() -> Unit = {},
    private val authProvider: AuthProvider = AuthProvider.None
) {

    init {
        require(baseUrl.startsWith("https://")) { "GitHub API base url must start with https." }
    }

    private val httpClient = HttpClient() {
        httpBuilder()
        with(authProvider) {
            configureClient()
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        defaultRequest {
            // Set the Accept Header according to
            // https://docs.github.com/en/rest/overview/resources-in-the-rest-api#current-version
            header(HttpHeaders.Accept, "application/vnd.github.v3+json")
            userAgent("NyCodeGHG/github-api-wrapper") // TODO: replace hardcoded repo with build variable
            url.takeFrom(URLBuilder().takeFrom(baseUrl).apply {
                encodedPath += url.encodedPath
            })
        }
    }

    public val repos: RepositoriesAPI = RepositoriesAPI(httpClient)

}
