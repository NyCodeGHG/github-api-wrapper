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

package dev.nycode.github.repositories.autolinks

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.repositories.RepositoriesAPI
import dev.nycode.github.repositories.autolinks.model.Autolink
import dev.nycode.github.repositories.autolinks.request.CreateRepositoryAutolinkReferenceRequest
import dev.nycode.github.request.delete
import dev.nycode.github.request.get
import dev.nycode.github.request.post
import dev.nycode.github.request.simplePaginatedGet
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

/**
 * Access APIs related to repository autolinks.
 */
public val RepositoriesAPI.autolinks: RepositoryAutolinksAPI
    get() = RepositoryAutolinksAPI(gitHubClient)

/**
 * Access [Repository autolink endpoints](https://docs.github.com/en/rest/reference/repos#autolinks).
 */
@JvmInline
public value class RepositoryAutolinksAPI(private val gitHubClient: GitHubClientImpl) {

    /**
     * Lists all autolinks configured for the given repository.
     * Information about autolinks are only available to repository administrators.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-all-autolinks-of-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return [Flow] of [Autolink]s
     * @throws dev.nycode.github.request.GitHubRequestException when the request fails
     */
    public fun listRepositoryAutolinks(
        owner: String,
        repo: String
    ): Flow<Autolink> =
        gitHubClient.simplePaginatedGet("repos", owner, repo, "autolinks")

    /**
     * Creates an autolink reference for a repository.
     * Only users with admin access to the repository can create an autolink.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#create-an-autolink-reference-for-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param keyPrefix The prefix appended by a number will generate a link any time it is found in an issue, pull request, or commit
     * @param urlTemplate The URL must contain for the reference number
     * @return the new [Autolink]
     * @throws dev.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun createRepositoryAutolinkReference(
        owner: String,
        repo: String,
        keyPrefix: String,
        urlTemplate: String
    ): Autolink =
        gitHubClient.post("repos", owner, repo, "autolinks") {
            request {
                contentType(ContentType.Application.Json)
                setBody(CreateRepositoryAutolinkReferenceRequest(keyPrefix, urlTemplate))
            }
        }

    /**
     * Gets a single autolink reference by [autolinkId] that was configured for the given repository.
     * Information about autolinks are only available to repository administrators.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-an-autolink-reference-of-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param autolinkId the id of the autolink
     * @return the autolink
     * @throws dev.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getRepositoryAutolinkReference(
        owner: String,
        repo: String,
        autolinkId: Int
    ): Autolink =
        gitHubClient.get("repos", owner, repo, "autolinks", autolinkId.toString())

    /**
     * Deletes a single autolink reference by ID that was configured for the given repository.
     * Information about autolinks are only available to repository administrators.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#delete-an-autolink-reference-from-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param autolinkId the id of the autolink
     * @throws dev.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun deleteRepositoryAutolinkReference(
        owner: String,
        repo: String,
        autolinkId: Int
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "autolinks", autolinkId.toString())
}
