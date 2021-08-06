package de.nycode.github.repositories

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.jvm.JvmInline

@JvmInline
public value class RepositoriesAPI(private val httpClient: HttpClient) {

    public val organizations: RepositoriesOrganizationsAPI
        get() = RepositoriesOrganizationsAPI(httpClient)

    @JvmInline
    public value class RepositoriesOrganizationsAPI(private val httpClient: HttpClient) {
        public suspend fun listOrganizationRepositories(organization: String): List<Repository> =
            httpClient.get {
                url {
                    path("orgs", organization, "repos")
                }
            }
    }

    public suspend fun getRepository(owner: String, repo: String): Repository =
        httpClient.get {
            url {
                path("repos", owner, repo)
            }
        }

}
