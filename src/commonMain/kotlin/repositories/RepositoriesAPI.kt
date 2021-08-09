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

package de.nycode.github.repositories

import de.nycode.github.GitHubClient
import de.nycode.github.preview.ApiPreview
import de.nycode.github.preview.Previews
import de.nycode.github.preview.preview
import de.nycode.github.repositories.organizations.RepositoriesOrganizationsAPI
import de.nycode.github.request.*
import io.ktor.client.features.expectSuccess
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.jvm.JvmInline

@JvmInline
public value class RepositoriesAPI(private val gitHubClient: GitHubClient) {

    public val organizations: RepositoriesOrganizationsAPI
        get() = RepositoriesOrganizationsAPI(gitHubClient)

    public suspend fun getRepository(owner: String, repo: String): Repository =
        gitHubClient.get("repos", owner, repo)

    public suspend fun updateRepository(
        owner: String,
        repo: String,
        builder: UpdateRepositoryRequestBuilder.() -> Unit = {}
    ): Repository =
        gitHubClient.patch("repos", owner, repo) {
            request {
                contentType(ContentType.Application.Json)
                body = UpdateRepositoryRequestBuilder().apply(builder)
            }
        }

    public suspend fun deleteRepository(
        owner: String,
        repo: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo)

    @ApiPreview
    public suspend fun enableAutomatedSecurityFixes(
        owner: String,
        repo: String
    ): Unit =
        gitHubClient.put("repos", owner, repo, "automated-security-fixes") {
            request {
                preview(Previews.LondonPreview)
            }
        }

    @ApiPreview
    public suspend fun disableAutomatedSecurityFixes(
        owner: String,
        repo: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "automated-security-fixes") {
            request {
                preview(Previews.LondonPreview)
            }
        }

    public suspend fun listRepositoryContributors(
        owner: String,
        repo: String,
        includeAnonymousContributors: Boolean? = null,
        builder: PaginatedRequestBuilder.() -> Unit = {}
    ): List<Contributor> =
        gitHubClient.paginatedGet("repos", owner, repo, "contributors") {
            builder()
            request {
                parameter("anon", includeAnonymousContributors)
            }
        }

    public suspend fun createRepositoryDispatchEvent(
        owner: String,
        repo: String,
        eventType: String,
        builder: CreateRepositoryDispatchEventRequestBuilder.() -> Unit = {}
    ): Unit =
        gitHubClient.post("repos", owner, repo, "dispatches") {
            request {
                contentType(ContentType.Application.Json)
                body = CreateRepositoryDispatchEventRequestBuilder(eventType).apply(builder)
            }
        }

    public suspend fun listRepositoryLanguages(
        owner: String,
        repo: String
    ): List<Language> =
        gitHubClient.get<Map<String, Int>>("repos", owner, repo, "languages").map { Language(it.key, it.value) }

    public suspend fun listRepositoryTags(
        owner: String,
        repo: String,
        builder: PaginatedRequestBuilder.() -> Unit = {}
    ): List<Tag> =
        gitHubClient.paginatedGet("repos", owner, repo, "tags") {
            builder()
        }

    public suspend fun listRepositoryTeams(
        owner: String,
        repo: String,
        builder: PaginatedRequestBuilder.() -> Unit = {}
    ): List<Team> =
        gitHubClient.paginatedGet("repos", owner, repo, "teams") {
            builder()
        }

    @ApiPreview
    public suspend fun getRepositoryTopics(
        owner: String,
        repo: String,
        builder: PaginatedRequestBuilder.() -> Unit = {}
    ): List<String> =
        gitHubClient.paginatedGet<RepositoryTopicsRequestResponse>("repos", owner, repo, "topics") {
            builder()
            request {
                preview(Previews.MercyPreview)
            }
        }.names

    @ApiPreview
    public suspend fun replaceRepositoryTopics(
        owner: String,
        repo: String,
        names: List<String>
    ): List<String> =
        gitHubClient.put<RepositoryTopicsRequestResponse>("repos", owner, repo, "topics") {
            request {
                preview(Previews.MercyPreview)
                contentType(ContentType.Application.Json)
                body = names
            }
        }.names

    public suspend fun transferRepository(
        owner: String,
        repo: String,
        newOwner: String,
        builder: TransferRepositoryRequestBuilder.() -> Unit = {}
    ): Repository =
        gitHubClient.post("repos", owner, repo, "transfer") {
            request {
                contentType(ContentType.Application.Json)
                body = TransferRepositoryRequestBuilder(newOwner).apply(builder)
            }
        }

    @ApiPreview
    public suspend fun checkVulnerabilityAlertsEnabled(
        owner: String,
        repo: String
    ): Boolean {
        val statusCode = gitHubClient.get<HttpStatusCode>("repos", owner, repo, "vulnerability-alerts") {
            request {
                preview(Previews.DorianPreview)
                expectSuccess = false
            }
        }
        return statusCode.value == 204
    }

    @ApiPreview
    public suspend fun enableVulnerabilityAlerts(
        owner: String,
        repo: String
    ): Unit =
        gitHubClient.put("repos", owner, repo, "vulnerability-alerts") {
            request {
                preview(Previews.DorianPreview)
            }
        }

    @ApiPreview
    public suspend fun disableVulnerabilityAlerts(
        owner: String,
        repo: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "vulnerability-alerts") {
            request {
                preview(Previews.DorianPreview)
            }
        }

    @ApiPreview
    public suspend fun createRepositoryFromTemplate(
        templateOwner: String,
        templateRepo: String,
        name: String,
        builder: CreateRepositoryFromTemplateRequestBuilder.() -> Unit = {}
    ): Repository =
        gitHubClient.post("repos", templateOwner, templateRepo, "generate") {
            request {
                preview(Previews.BaptistePreview)
                contentType(ContentType.Application.Json)
                body = CreateRepositoryFromTemplateRequestBuilder(name).apply(builder)
            }
        }

    public suspend fun listPublicRepositories(
        builder: ListPublicRepositoriesRequestBuilder.() -> Unit = {}
    ): List<Repository> =
        gitHubClient.get("repositories") {
            request {
                parameter("since", ListPublicRepositoriesRequestBuilder().apply(builder).since)
            }
        }

    public suspend fun listRepositoriesForAuthenticatedUser(builder: ListRepositoriesForAuthenticatedUserRequestBuilder.() -> Unit): List<Repository> =
        gitHubClient.get("user", "repos") {
            request {
                contentType(ContentType.Application.Json)
                body = ListRepositoriesForAuthenticatedUserRequestBuilder().apply(builder)
            }
        }
}
