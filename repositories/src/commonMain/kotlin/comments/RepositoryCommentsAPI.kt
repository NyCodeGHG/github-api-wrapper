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

package dev.nycode.github.repositories.comments

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.repositories.comments.model.CommitComment
import dev.nycode.github.request.simplePaginatedGet
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

@JvmInline
public value class RepositoryCommentsAPIWrapper internal constructor(private val gitHubClientImpl: GitHubClientImpl) {

    /**
     * Access repository comments in raw mode.
     *
     * [View comments API modes](https://docs.github.com/en/rest/reference/repos#comments)
     */
    public val raw: RepositoryCommentsAPI
        get() = RepositoryCommentsAPI(gitHubClientImpl to CommentMediaType.RAW)

    /**
     * Access repository comments in text mode.
     *
     * [View comments API modes](https://docs.github.com/en/rest/reference/repos#comments)
     */
    public val text: RepositoryCommentsAPI
        get() = RepositoryCommentsAPI(gitHubClientImpl to CommentMediaType.TEXT)

    /**
     * Access repository comments in html mode.
     *
     * [View comments API modes](https://docs.github.com/en/rest/reference/repos#comments)
     */
    public val html: RepositoryCommentsAPI
        get() = RepositoryCommentsAPI(gitHubClientImpl to CommentMediaType.HTML)

    /**
     * Access repository comments in full mode.
     *
     * [View comments API modes](https://docs.github.com/en/rest/reference/repos#comments)
     */
    public val full: RepositoryCommentsAPI
        get() = RepositoryCommentsAPI(gitHubClientImpl to CommentMediaType.FULL)
}

private fun HttpRequestBuilder.mediaType(mediaType: CommentMediaType) {
    header(HttpHeaders.ContentType, mediaType.header)
}

/**
 * Provides APIs regarding repository comments.
 */
@JvmInline
public value class RepositoryCommentsAPI internal constructor(
    private val gitHubClientAndMediaType: Pair<GitHubClientImpl, CommentMediaType>
) {
    private val gitHubClient: GitHubClientImpl
        get() = gitHubClientAndMediaType.first

    private val mediaType: CommentMediaType
        get() = gitHubClientAndMediaType.second

    /**
     * Lists commit comments for a repository.
     * Comments are ordered by ascending ID.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-commit-comments-for-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param perPage how many items per pagination page
     */
    public fun listRepositoryCommitComments(
        owner: String,
        repo: String,
        perPage: Int? = null
    ): Flow<CommitComment> = gitHubClient.simplePaginatedGet("repos", owner, repo, "comments") {
        if (perPage != null) {
            this.perPage = perPage
        }
        request {
            mediaType(mediaType)
        }
    }
}
