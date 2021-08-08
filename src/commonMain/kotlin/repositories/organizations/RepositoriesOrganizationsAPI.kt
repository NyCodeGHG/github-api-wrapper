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

import de.nycode.github.repositories.Repository
import de.nycode.github.request.paginatedGet
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.jvm.JvmInline

@JvmInline
public value class RepositoriesOrganizationsAPI(private val httpClient: HttpClient) {

    public suspend fun listOrganizationRepositories(
        organization: String,
        page: Int? = null,
        perPage: Int? = null,
        block: ListOrganizationRepositoriesRequestBuilder.() -> Unit = {}
    ): List<Repository> =
        httpClient.paginatedGet(page, perPage) {
            val builder = ListOrganizationRepositoriesRequestBuilder().apply(block)
            parameter("type", builder.type)
            parameter("sort", builder.sort)
            parameter("direction", builder.direction)
            url {
                path("orgs", organization, "repos")
            }
        }

    public suspend fun createOrganizationRepository(
        organization: String,
        name: String,
        block: CreateOrganizationRepositoryRequestBuilder.() -> Unit
    ): Repository =
        httpClient.post {
            val builder = CreateOrganizationRepositoryRequestBuilder(name).apply(block)
            url {
                path("orgs", organization, "repos")
            }
            contentType(ContentType.Application.Json)
            body = builder
        }
}
