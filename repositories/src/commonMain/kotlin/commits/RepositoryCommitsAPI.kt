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

package dev.nycode.github.repositories.commits

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.model.SimpleCommit
import dev.nycode.github.model.SimplePullRequest
import dev.nycode.github.preview.ApiPreview
import dev.nycode.github.preview.Previews
import dev.nycode.github.preview.preview
import dev.nycode.github.repositories.commits.model.CommitComparison
import dev.nycode.github.repositories.commits.request.ListCommitsRequestBuilder
import dev.nycode.github.repositories.model.Commit
import dev.nycode.github.repositories.model.CommitData
import dev.nycode.github.repositories.model.ShortBranch
import dev.nycode.github.request.*
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.toList
import kotlin.jvm.JvmInline

/**
 * Provides APIs regarding repository commits.
 */
@JvmInline
public value class RepositoryCommitsAPI internal constructor(private val gitHubClient: GitHubClientImpl) {

    /**
     * Lists commits of a repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-commits).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param builder builder for further specification of request filter options.
     */
    public fun listCommits(
        owner: String,
        repo: String,
        builder: ListCommitsRequestBuilder.() -> Unit = {}
    ): Flow<CommitData> =
        gitHubClient.simplePaginatedGet("repos", owner, repo, "commits") {
            val requestBuilder = ListCommitsRequestBuilder().apply(builder)
            requestBuilder.perPage?.let {
                perPage = it
            }
            request {
                parameter("sha", requestBuilder.sha)
                parameter("path", requestBuilder.path)
                parameter("author", requestBuilder.author)
                parameter("since", requestBuilder.since)
                parameter("until", requestBuilder.until)
            }
        }

    /**
     * Returns all branches where the given commit SHA is the HEAD, or latest commit for the branch.
     * Note: This API is in preview. It could change anytime.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-branches-for-head-commit).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param commitSha the commit to get the branches for
     */
    @ApiPreview
    public suspend fun listBranchesForHeadCommit(
        owner: String,
        repo: String,
        commitSha: String
    ): List<ShortBranch> =
        gitHubClient.get("repos", owner, repo, "commits", commitSha, "branches-where-head") {
            request {
                preview(Previews.GrootPreview)
            }
        }

    /**
     * Lists the merged pull request that introduced the commit to the repository.
     * If the commit is not present in the default branch,
     * additionally returns open pull requests associated with the commit.
     * The results may include open and closed pull requests.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-pull-requests-associated-with-a-commit).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param commitSha the commit to get the pull requests for
     * @param perPage how many entries should be requested in pagination per page
     */
    @ApiPreview
    public fun listPullRequestsAssociatedWithCommit(
        owner: String,
        repo: String,
        commitSha: String,
        perPage: Int? = null
    ): Flow<SimplePullRequest> =
        gitHubClient.simplePaginatedGet("repos", owner, repo, "commits", commitSha, "pulls") {
            if (perPage != null) {
                this.perPage = perPage
            }
            request {
                preview(Previews.GrootPreview)
            }
        }

    /**
     * Returns the contents of a single commit reference.
     * You must have read access for the repository to use this endpoint.
     * Note: If there are more than 300 files in the commit diff, the response will include pagination link headers for the remaining files,
     * up to a limit of 3000 files.
     * Each page contains the static commit information,
     * and the only changes are to the file listing.
     * Note: This endpoint only returns file modification data for up to 300 files.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-a-commit).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param commitSha the hash of the commit
     */
    public suspend fun getCommit(
        owner: String,
        repo: String,
        commitSha: String
    ): CommitData =
        gitHubClient.get("repos", owner, repo, "commits", commitSha)

    /**
     * Compares two commits.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#compare-two-commits).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     */
    public suspend fun compareCommits(
        owner: String,
        repo: String,
        basehead: String
    ): CommitComparison =
        gitHubClient.get("repos", owner, repo, "commits", "compare", basehead)

}
