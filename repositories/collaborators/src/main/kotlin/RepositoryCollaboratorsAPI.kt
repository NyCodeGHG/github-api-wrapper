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

package dev.nycode.github.repositories.collaborators

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.repositories.collaborators.model.RepositoryPermissionReponse
import dev.nycode.github.repositories.collaborators.request.AddRepositoryCollaboratorRequestBuilder
import dev.nycode.github.repositories.model.Collaborator
import dev.nycode.github.request.delete
import dev.nycode.github.request.get
import dev.nycode.github.request.put
import dev.nycode.github.request.simplePaginatedGet
import io.ktor.client.call.receive
import io.ktor.client.features.expectSuccess
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

/**
 * Provides APIs regarding repository collaborators.
 */
@JvmInline
public value class RepositoryCollaboratorsAPI(@PublishedApi internal val gitHubClient: GitHubClientImpl) {
    /**
     * Lists all collaborators of the specified repository.
     * For organization-owned repositories,
     * the list of collaborators includes outside collaborators,
     * organization members that are direct collaborators,
     * organization members with access through team memberships,
     * organization members with access through default organization permissions,
     * and organization owners.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-repository-collaborators).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param perPage results per page
     * @return A [Flow] of the repositories [Collaborator]s
     * @throws GitHubRequestException when the request fails
     */
    public fun listRepositoryCollaborators(
        owner: String,
        repo: String,
        perPage: Int? = null
    ): Flow<Collaborator> =
        gitHubClient.simplePaginatedGet("repos", owner, repo, "collaborators") {
            if (perPage != null) {
                this.perPage = perPage
            }
        }

    /**
     * Checks if a user is a collaborator in the specified repository.
     * For organization-owned repositories,
     * the list of collaborators includes outside collaborators,
     * organization members that are direct collaborators,
     * organization members with access through team memberships,
     * organization members with access through default organization permissions,
     * and organization owners.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#check-if-a-user-is-a-repository-collaborator).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param username the user to check
     * @return true if the user is a collaborator, otherwise false
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun isUserRepositoryCollaborator(
        owner: String,
        repo: String,
        username: String
    ): Boolean {
        val response = gitHubClient.get<HttpResponse>("repos", owner, repo, "collaborators", username) {
            request {
                expectSuccess = false
            }
        }
        return when (response.status.value) {
            204 -> true
            404 -> false
            else -> error("Invalid response")
        }
    }

    /**
     * Invites a repository collaborator.
     * This endpoint triggers [notifications](https://docs.github.com/en/github/managing-subscriptions-and-notifications-on-github/about-notifications).
     * Creating content too quickly using this endpoint may result in secondary rate limiting.
     * See "[Secondary rate limits](https://docs.github.com/rest/overview/resources-in-the-rest-api#secondary-rate-limits)" and "[Dealing with secondary rate limits](https://docs.github.com/rest/guides/best-practices-for-integrators#dealing-with-secondary-rate-limits)" for details.
     *
     * For more information the permission levels, see "[Repository permission levels for an organization](https://help.github.com/en/github/setting-up-and-managing-organizations-and-teams/repository-permission-levels-for-an-organization#permission-levels-for-repositories-owned-by-an-organization)".
     *
     * The invitee will receive a notification that they have been invited to the repository,
     * which they must accept or decline.
     * They may do this via the notifications page, the email they receive, or by using the [repository invitations API endpoints](https://docs.github.com/rest/reference/repos#invitations).
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#add-a-repository-collaborator).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param username the user to invite
     * @param builder builder for settings permissions in an organization repository
     * @throws GitHubRequestException when the request fails
     */
    public suspend inline fun addRepositoryCollaborator(
        owner: String,
        repo: String,
        username: String,
        builder: AddRepositoryCollaboratorRequestBuilder.() -> Unit = {}
    ): Unit = gitHubClient.put("repos", owner, repo, "collaborators", username) {
        request {
            val builder = AddRepositoryCollaboratorRequestBuilder().apply(builder)
            if (builder.permissions != null || builder.permission != null) {
                contentType(ContentType.Application.Json)
                body = builder
            }
        }
    }

    /**
     * Removes a repository collaborator.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#remove-a-repository-collaborator).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param username the user to remove
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun removeRepositoryCollaborator(
        owner: String,
        repo: String,
        username: String
    ): Unit = gitHubClient.delete("repos", owner, repo, "collaborators", username)

    /**
     * Gets the permissions of a user in a repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-repository-permissions-for-a-user).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param username the user to check
     * @throws GitHubRequestException when the request fails
     */
    public suspend fun getRepositoryPermissions(
        owner: String,
        repo: String,
        username: String
    ): RepositoryPermissionReponse? {
        val response = gitHubClient.get<HttpResponse>("repos", owner, repo, "collaborators", username, "permission") {
            request {
                expectSuccess = false
            }
        }
        if (response.status.isSuccess()) {
            return response.receive()
        }

        return null
    }
}
