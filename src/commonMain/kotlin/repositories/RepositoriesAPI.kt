package de.nycode.github.repositories

import de.nycode.github.repositories.organizations.RepositoriesOrganizationsAPI
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.jvm.JvmInline

@JvmInline
public value class RepositoriesAPI(private val httpClient: HttpClient) {

    public val organizations: RepositoriesOrganizationsAPI
        get() = RepositoriesOrganizationsAPI(httpClient)

    public suspend fun getRepository(owner: String, repo: String): Repository =
        httpClient.get {
            url {
                path("repos", owner, repo)
            }
        }

    public suspend fun updateRepository(
        owner: String,
        repo: String,
        block: UpdateRepositoryRequestBuilder.() -> Unit
    ): Repository =
        httpClient.patch {
            url {
                path("repos", owner, repo)
            }
            val builder = UpdateRepositoryRequestBuilder().apply(block)
            contentType(ContentType.Application.Json)
            body = builder
        }
}
