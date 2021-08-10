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

package de.nycode.github.repositories.organizations

import de.nycode.github.GitHubClient
import de.nycode.github.model.MinimalRepository
import de.nycode.github.model.Repository
import de.nycode.github.repositories.organizations.request.CreateOrganizationRepositoryRequestBuilder
import de.nycode.github.repositories.organizations.request.ListOrganizationRepositoriesRequestBuilder
import de.nycode.github.request.paginatedGet
import de.nycode.github.request.request
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.jvm.JvmInline

@JvmInline
public value class RepositoriesOrganizationsAPI(private val gitHubClient: GitHubClient) {

    public suspend fun listOrganizationRepositories(
        organization: String,
        page: Int? = null,
        perPage: Int? = null,
        block: ListOrganizationRepositoriesRequestBuilder.() -> Unit = {}
    ): List<MinimalRepository> =
        gitHubClient.paginatedGet("orgs", organization, "repos") {
            this.page = page
            this.perPage = perPage
            request {
                val builder = ListOrganizationRepositoriesRequestBuilder().apply(block)
                parameter("type", builder.type)
                parameter("sort", builder.sort)
                parameter("direction", builder.direction)
            }
        }

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
