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
import de.nycode.github.model.*
import de.nycode.github.preview.ApiPreview
import de.nycode.github.preview.Previews
import de.nycode.github.preview.preview
import de.nycode.github.repositories.organizations.RepositoriesOrganizationsAPI
import de.nycode.github.repositories.request.*
import de.nycode.github.request.*
import io.ktor.client.features.expectSuccess
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmInline

@JvmInline
public value class RepositoriesAPI(private val gitHubClient: GitHubClient) {

    /**
     * Access APIs related to organization repositories.
     */
    public val organizations: RepositoriesOrganizationsAPI
        get() = RepositoriesOrganizationsAPI(gitHubClient)

    /**
     * Get a repository by its owner and name.
     * The parent and source properties are present when the repository is a fork.
     * parent is the repository this repository was forked from,
     * source is the ultimate source for the network.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return the resolved repository
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun getRepository(owner: String, repo: String): Repository =
        gitHubClient.get("repos", owner, repo)

    /**
     * Update a repository by its owner and name.
     * Note: To edit a repository's topics, use the [replaceRepositoryTopics] endpoint.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#update-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param builder builder for editing properties of the repository
     * @return the edited repository
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun updateRepository(
        owner: String,
        repo: String,
        builder: UpdateRepositoryRequestBuilder.() -> Unit = {}
    ): Repository {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.patch("repos", owner, repo) {
            request {
                contentType(ContentType.Application.Json)
                body = UpdateRepositoryRequestBuilder().apply(builder)
            }
        }
    }

    /**
     * Delete a repository by its owner and name.
     * Deleting a repository requires admin access.
     * If OAuth is used, the delete_repo scope is required.
     * If an organization owner has configured the organization
     * to prevent members from deleting organization-owned repositories,
     * you will get a 403 Forbidden response and an [GitHubRequestException].
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#delete-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun deleteRepository(
        owner: String,
        repo: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo)

    /**
     * Enables automated security fixes for a repository by its owner and name.
     * The authenticated user must have admin access to the repository.
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#enable-automated-security-fixes).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @throws GitHubRequestException when the request fails
     */
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

    /**
     * Disables automated security fixes for a repository by its owner and name.
     * The authenticated user must have admin access to the repository.
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#disable-automated-security-fixes).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @throws GitHubRequestException when the request fails
     */
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

    /**
     * Lists contributors to the specified repository
     * and sorts them by the number of commits per contributor in descending order.
     * This endpoint may return information that is a few hours old
     * because the GitHub REST API v3 caches contributor data to improve performance.
     * GitHub identifies contributors by author email address.
     * This endpoint groups contribution counts by GitHub user,
     * which includes all associated email addresses.
     * To improve performance, only the first 500 author email addresses in the repository link to GitHub users.
     * The rest will appear as anonymous contributors without associated GitHub user information.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-repository-contributors).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param includeAnonymousContributors include anonymous contributors in the results
     * @param builder builder for configuring pagination
     * @return [List] of [Contributor]s
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun listRepositoryContributors(
        owner: String,
        repo: String,
        includeAnonymousContributors: Boolean? = null,
        builder: PaginatedRequestBuilder.() -> Unit = {}
    ): List<Contributor> {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.paginatedGet("repos", owner, repo, "contributors") {
            builder()
            request {
                parameter("anon", includeAnonymousContributors)
            }
        }
    }

    /**
     * You can use this endpoint to trigger a webhook event called `repository_dispatch`
     * when you want activity that happens outside of GitHub to trigger a GitHub Actions workflow or GitHub App webhook.
     * You must configure your GitHub Actions workflow or GitHub App to run when the repository_dispatch event occurs.
     * For an example repository_dispatch webhook payload, see "[RepositoryDispatchEvent](https://docs.github.com/webhooks/event-payloads/#repository_dispatch)."
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#create-a-repository-dispatch-event).
     *
     * @param owner the name of the repository
     * @param repo the name of the repo
     * @param eventType a custom webhook event name
     * @param builder optional builder for providing the client payload
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun createRepositoryDispatchEvent(
        owner: String,
        repo: String,
        eventType: String,
        builder: CreateRepositoryDispatchEventRequestBuilder.() -> Unit = {}
    ) {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.post("repos", owner, repo, "dispatches") {
            request {
                contentType(ContentType.Application.Json)
                body = CreateRepositoryDispatchEventRequestBuilder(eventType).apply(builder)
            }
        }
    }

    /**
     * Lists languages for the specified repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-repository-languages).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return [List] of [Language]s
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun listRepositoryLanguages(
        owner: String,
        repo: String
    ): List<Language> =
        gitHubClient.get<Map<String, Int>>("repos", owner, repo, "languages").map { Language(it.key, it.value) }

    /**
     * Lists tags for the specified repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-repository-tags).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param builder builder for configuring pagination
     * @return [List] of [Tag]s
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun listRepositoryTags(
        owner: String,
        repo: String,
        builder: PaginatedRequestBuilder.() -> Unit = {}
    ): List<Tag> {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.paginatedGet("repos", owner, repo, "tags") {
            builder()
        }
    }

    /**
     * Lists teams of the specified repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-repository-teams).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param builder builder for configuring pagination
     * @return [List] of [Team]s
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun listRepositoryTeams(
        owner: String,
        repo: String,
        builder: PaginatedRequestBuilder.() -> Unit = {}
    ): List<Team> {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.paginatedGet("repos", owner, repo, "teams") {
            builder()
        }
    }

    /**
     * Gets all topics of the specified repository.
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-all-repository-topics).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param builder builder for configuring pagination
     * @return [List] of [String]s
     * @throws GitHubRequestException when the request fails
     */
    @ApiPreview
    public suspend fun getRepositoryTopics(
        owner: String,
        repo: String,
        builder: PaginatedRequestBuilder.() -> Unit = {}
    ): List<String> {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.paginatedGet<RepositoryTopicsRequestResponse>("repos", owner, repo, "topics") {
            builder()
            request {
                preview(Previews.MercyPreview)
            }
        }.names
    }

    /**
     * Replaces all topics of the specified repository.
     * Provide an empty [List] to clear all topics.
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-all-repository-topics).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return the new [List] of topics
     * @throws GitHubRequestException when the request fails.
     */
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

    /**
     * Transfers the specified repository.
     * A transfer request will need to be accepted
     * by the new owner when transferring a personal repository to another user.
     * The response will contain the original owner, and the transfer will continue asynchronously.
     * For more details on the requirements to transfer personal and organization-owned repositories,
     * see [about repository transfers](https://help.github.com/articles/about-repository-transfers/).
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#transfer-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return the current state of the repository.
     * @throws GitHubRequestException when the request fails.
     */
    public suspend fun transferRepository(
        owner: String,
        repo: String,
        newOwner: String,
        builder: TransferRepositoryRequestBuilder.() -> Unit = {}
    ): Repository {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.post("repos", owner, repo, "transfer") {
            request {
                contentType(ContentType.Application.Json)
                body = TransferRepositoryRequestBuilder(newOwner).apply(builder)
            }
        }
    }

    /**
     * Shows whether dependency alerts are enabled or disabled for a repository.
     * The authenticated user must have admin access to the repository.
     * For more information, see ["About security alerts for vulnerable dependencies"](https://help.github.com/en/articles/about-security-alerts-for-vulnerable-dependencies).
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#check-if-vulnerability-alerts-are-enabled-for-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return the current state of the repository.
     * @throws GitHubRequestException when the request fails.
     */
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

    /**
     * Enables dependency alerts and the dependency graph for the specified repository.
     * The authenticated user must have admin access to the repository.
     * For more information, see ["About security alerts for vulnerable dependencies"](https://help.github.com/en/articles/about-security-alerts-for-vulnerable-dependencies).
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#enable-vulnerability-alerts).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @throws GitHubRequestException when the request fails.
     */
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

    /**
     * Disables dependency alerts and the dependency graph for a repository.
     * The authenticated user must have admin access to the repository.
     * For more information, see ["About security alerts for vulnerable dependencies"](https://help.github.com/en/articles/about-security-alerts-for-vulnerable-dependencies).
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#disable-vulnerability-alerts).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @throws GitHubRequestException when the request fails.
     */
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

    /**
     * Creates a new repository using a repository template.
     * Use the [templateOwner] and [templateRepo] route parameters to specify the repository to use as the template.
     * The authenticated user must own or be a member of an organization that owns the repository.
     * To check if a repository is available to use as a template,
     * get the repository's information using the [getRepository] endpoint and check that the isTemplate key is true.
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#create-a-repository-using-a-template).
     * @param templateOwner the owner of the repository
     * @param templateRepo the name of the repo
     * @param name name of the new repo
     * @param builder builder for configuring the new repository
     * @return the new repository
     * @throws GitHubRequestException when the request fails.
     */
    @ApiPreview
    public suspend fun createRepositoryFromTemplate(
        templateOwner: String,
        templateRepo: String,
        name: String,
        builder: CreateRepositoryFromTemplateRequestBuilder.() -> Unit = {}
    ): Repository {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.post("repos", templateOwner, templateRepo, "generate") {
            request {
                preview(Previews.BaptistePreview)
                contentType(ContentType.Application.Json)
                body = CreateRepositoryFromTemplateRequestBuilder(name).apply(builder)
            }
        }
    }

    /**
     * Lists all public repositories in the order that they were created.
     * For GitHub Enterprise Server, this endpoint will only list repositories available to all users on the enterprise.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-public-repositories).
     *
     * @param builder builder for configuring the request
     * @return [List] of the found repositories.
     * @throws GitHubRequestException when the request fails.
     */
    public suspend fun listPublicRepositories(
        builder: ListPublicRepositoriesRequestBuilder.() -> Unit = {}
    ): List<Repository> {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.get("repositories") {
            request {
                parameter("since", ListPublicRepositoriesRequestBuilder().apply(builder).since)
            }
        }
    }

    /**
     * Lists repositories that the authenticated user has explicit permission (:read, :write, or :admin) to access.
     * The authenticated user has explicit permission to access repositories they own,
     * repositories where they are a collaborator,
     * and repositories that they can access through an organization membership.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-repositories-for-the-authenticated-user).
     *
     * @param builder builder for configuring the request
     * @return [List] of repositories
     * @throws GitHubRequestException when the request fails.
     */
    public suspend fun listRepositoriesForAuthenticatedUser(builder: ListRepositoriesForAuthenticatedUserRequestBuilder.() -> Unit = {}): List<Repository> {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.get("user", "repos") {
            request {
                val (visibility, affiliation, type, sort, direction, page, perPage, since, before)
                    = ListRepositoriesForAuthenticatedUserRequestBuilder().apply(builder)
                parameter("visibility", visibility)
                parameter("affiliation", affiliation)
                parameter("type", type)
                parameter("sort", sort)
                parameter("direction", direction)
                parameter("per_page", perPage)
                parameter("page", page)
                parameter("since", since)
                parameter("before", before)
            }
        }
    }

    public suspend fun createRepositoryForAuthenticatedUser(
        name: String,
        builder: CreateRepositoryForAuthenticatedUserRequestBuilder.() -> Unit
    ): Repository {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.post("user", "repos") {
            request {
                contentType(ContentType.Application.Json)
                body = CreateRepositoryForAuthenticatedUserRequestBuilder(name).apply(builder)
            }
        }
    }
}
