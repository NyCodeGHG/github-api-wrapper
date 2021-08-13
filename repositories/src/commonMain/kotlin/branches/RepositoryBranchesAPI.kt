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

package de.nycode.github.repositories.branches

import de.nycode.github.GitHubClient
import de.nycode.github.branches.request.ListBranchesRequestBuilder
import de.nycode.github.repositories.model.BranchWithProtection
import de.nycode.github.repositories.model.ShortBranch
import de.nycode.github.request.get
import de.nycode.github.request.simplePaginatedGet
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

/**
 * Access [Repository branches endpoints](https://docs.github.com/en/rest/reference/repos#branches).
 */
@JvmInline
public value class RepositoryBranchesAPI(private val gitHubClient: GitHubClient) {

    /**
     * Lists all branches of the specified repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-branches).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param builder builder for configuring the request
     * @return [Flow] of [ShortBranch]es
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public fun listBranches(
        owner: String,
        repo: String,
        builder: ListBranchesRequestBuilder.() -> Unit = {}
    ): Flow<ShortBranch> =
        gitHubClient.simplePaginatedGet("repos", owner, repo, "branches") {
            request {
                val (protected, perPage) = ListBranchesRequestBuilder().apply(builder)
                parameter("protected", protected)
                parameter("per_page", perPage)
            }
        }

    /**
     * Gets a specific branch of the specified repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-a-branch).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return the branch with its protection data
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getBranch(
        owner: String,
        repo: String,
        branch: String
    ): BranchWithProtection =
        gitHubClient.get("repos", owner, repo, "branches", branch)
}
