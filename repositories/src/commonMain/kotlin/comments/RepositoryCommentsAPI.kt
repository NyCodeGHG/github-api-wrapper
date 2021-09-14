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
import dev.nycode.github.repositories.comments.model.*
import dev.nycode.github.request.get
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
    public val raw: RepositoryCommentsAPI<RawCommitComment>
        get() = RawRepositoryCommentsAPI(gitHubClientImpl)

    /**
     * Access repository comments in text mode.
     *
     * [View comments API modes](https://docs.github.com/en/rest/reference/repos#comments)
     */
    public val text: RepositoryCommentsAPI<TextCommitComment>
        get() = TextRepositoryCommentsAPI(gitHubClientImpl)

    /**
     * Access repository comments in html mode.
     *
     * [View comments API modes](https://docs.github.com/en/rest/reference/repos#comments)
     */
    public val html: RepositoryCommentsAPI<HtmlCommitComment>
        get() = HtmlRepositoryCommentsAPI(gitHubClientImpl)

    /**
     * Access repository comments in full mode.
     *
     * [View comments API modes](https://docs.github.com/en/rest/reference/repos#comments)
     */
    public val full: RepositoryCommentsAPI<FullCommitComment>
        get() = FullRepositoryCommentsAPI(gitHubClientImpl)
}

internal fun HttpRequestBuilder.mediaType(mediaType: CommentMediaType) {
    header(HttpHeaders.ContentType, mediaType.header)
}

/**
 * Provides APIs regarding repository comments.
 */
public sealed interface RepositoryCommentsAPI<T : CommitComment> {

    /**
     * The selected media type for this comment api instance
     */
    public val mediaType: CommentMediaType

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
        perPage: Int?
    ): Flow<T>

    /**
     * Gets a specific commit comment.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-a-commit-comment).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param commentId the id of the commit comment
     */
    public suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): T
}

@JvmInline
internal value class FullRepositoryCommentsAPI internal constructor(
    private val gitHubClient: GitHubClientImpl
) : RepositoryCommentsAPI<FullCommitComment> {

    override val mediaType: CommentMediaType
        get() = CommentMediaType.FULL

    override fun listCommitComments(
        owner: String,
        repo: String,
        perPage: Int?
    ): Flow<FullCommitComment> = gitHubClient.simplePaginatedGet("repos", owner, repo, "comments") {
        if (perPage != null) {
            this.perPage = perPage
        }
        request {
            mediaType(mediaType)
        }
    }

    override suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): FullCommitComment = gitHubClient.get("repos", owner, repo, "comments", commentId) {
        request {
            mediaType(mediaType)
        }
    }
}

@JvmInline
internal value class HtmlRepositoryCommentsAPI internal constructor(
    private val gitHubClient: GitHubClientImpl
) : RepositoryCommentsAPI<HtmlCommitComment> {

    override val mediaType: CommentMediaType
        get() = CommentMediaType.HTML

    override fun listCommitComments(
        owner: String,
        repo: String,
        perPage: Int?
    ): Flow<HtmlCommitComment> = gitHubClient.simplePaginatedGet("repos", owner, repo, "comments") {
        if (perPage != null) {
            this.perPage = perPage
        }
        request {
            mediaType(mediaType)
        }
    }

    override suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): HtmlCommitComment = gitHubClient.get("repos", owner, repo, "comments", commentId) {
        request {
            mediaType(mediaType)
        }
    }
}

@JvmInline
internal value class TextRepositoryCommentsAPI internal constructor(
    private val gitHubClient: GitHubClientImpl
) : RepositoryCommentsAPI<TextCommitComment> {

    override val mediaType: CommentMediaType
        get() = CommentMediaType.TEXT

    override fun listCommitComments(
        owner: String,
        repo: String,
        perPage: Int?
    ): Flow<TextCommitComment> = gitHubClient.simplePaginatedGet("repos", owner, repo, "comments") {
        if (perPage != null) {
            this.perPage = perPage
        }
        request {
            mediaType(mediaType)
        }
    }

    override suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): TextCommitComment = gitHubClient.get("repos", owner, repo, "comments", commentId) {
        request {
            mediaType(mediaType)
        }
    }
}

@JvmInline
internal value class RawRepositoryCommentsAPI internal constructor(
    private val gitHubClient: GitHubClientImpl
) : RepositoryCommentsAPI<RawCommitComment> {

    override val mediaType: CommentMediaType
        get() = CommentMediaType.RAW

    override fun listCommitComments(
        owner: String,
        repo: String,
        perPage: Int?
    ): Flow<RawCommitComment> = gitHubClient.simplePaginatedGet("repos", owner, repo, "comments") {
        if (perPage != null) {
            this.perPage = perPage
        }
        request {
            mediaType(mediaType)
        }
    }

    override suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): RawCommitComment = gitHubClient.get("repos", owner, repo, "comments", commentId) {
        request {
            mediaType(mediaType)
        }
    }
}
