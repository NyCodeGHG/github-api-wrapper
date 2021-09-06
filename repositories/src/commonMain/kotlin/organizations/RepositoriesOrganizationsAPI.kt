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

package dev.nycode.github.repositories.organizations

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.model.MinimalRepository
import dev.nycode.github.model.Repository
import dev.nycode.github.repositories.organizations.request.CreateOrganizationRepositoryRequestBuilder
import dev.nycode.github.repositories.organizations.request.ListOrganizationRepositoriesRequestBuilder
import dev.nycode.github.request.request
import dev.nycode.github.request.simplePaginatedGet
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

/**
 * Provides APIs regarding repositories in organizations.
 */
@JvmInline
public value class RepositoriesOrganizationsAPI internal constructor(private val gitHubClient: GitHubClientImpl) {

    /**
     * Lists repositories for the specified organization.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-organization-repositories).
     *
     * @param organization the organization to list the repos from
     * @param block builder for configuring list and pagination options
     * @return [List] of the organizations repositories
     * @throws dev.nycode.github.request.GitHubRequestException when the request fails
     */
    public fun listOrganizationRepositories(
        organization: String,
        block: ListOrganizationRepositoriesRequestBuilder.() -> Unit = {}
    ): Flow<MinimalRepository> =
        gitHubClient.simplePaginatedGet("orgs", organization, "repos") {
            val (type, sort, direction, perPage) = ListOrganizationRepositoriesRequestBuilder().apply(block)
            perPage?.let {
                this.perPage = it
            }
            request {
                parameter("type", type)
                parameter("sort", sort)
                parameter("direction", direction)
            }
        }

    /**
     * Creates a new repository in the specified organization.
     * The authenticated user must be a member of the organization.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#create-an-organization-repository).
     *
     * @param organization the organization to create the repo in
     * @param name the name of the new repository
     * @param block builder for configuring options of the new repo
     * @return the new [Repository]
     * @throws dev.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun createOrganizationRepository(
        organization: String,
        name: String,
        block: CreateOrganizationRepositoryRequestBuilder.() -> Unit
    ): Repository =
        gitHubClient.request("orgs", organization, "repos") {
            request {
                val builder = CreateOrganizationRepositoryRequestBuilder(name).apply(block)
                contentType(ContentType.Application.Json)
                body = builder
            }
        }
}
