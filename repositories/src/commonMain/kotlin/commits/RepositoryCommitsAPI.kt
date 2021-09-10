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
import dev.nycode.github.repositories.commits.request.ListCommitsRequestBuilder
import dev.nycode.github.repositories.model.CommitData
import dev.nycode.github.request.simplePaginatedGet
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
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
        gitHubClient.simplePaginatedGet<CommitData>("repos", owner, repo, "commits") {
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

}
