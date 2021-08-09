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
import de.nycode.github.repositories.organizations.RepositoriesOrganizationsAPI
import de.nycode.github.request.delete
import de.nycode.github.request.get
import de.nycode.github.request.paginatedGet
import de.nycode.github.request.patch
import io.ktor.client.request.*
import io.ktor.http.*
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
        block: UpdateRepositoryRequestBuilder.() -> Unit
    ): Repository =
        gitHubClient.patch("repos", owner, repo) {
            request {
                val builder = UpdateRepositoryRequestBuilder().apply(block)
                contentType(ContentType.Application.Json)
                body = builder
            }
        }

    public suspend fun deleteRepository(
        owner: String,
        repo: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo)

    public suspend fun listRepositoryContributors(
        owner: String,
        repo: String,
        includeAnonymousContributors: Boolean? = null,
        page: Int? = null,
        perPage: Int? = null
    ): List<Contributor> =
        gitHubClient.paginatedGet("repos", owner, repo, "contributors") {
            this.page = page
            this.perPage = perPage
            request {
                parameter("anon", includeAnonymousContributors)
            }
        }
}
