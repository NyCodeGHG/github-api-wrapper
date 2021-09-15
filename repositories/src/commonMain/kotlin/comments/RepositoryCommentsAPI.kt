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
import dev.nycode.github.request.delete
import dev.nycode.github.request.get
import dev.nycode.github.request.patch
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

    /**
     * Deletes a specific commit comment
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#delete-a-commit-comment).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param commentId the id of the commit comment
     */
    public suspend fun deleteCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): Unit = gitHubClientImpl.deleteCommitComment(owner, repo, commentId)
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

    /**
     * Updates a specific commit comment.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#update-a-commit-comment).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param commentId the id of the commit comment
     * @param body the new body
     * @return the updated comment
     */
    public suspend fun updateCommitComment(
        owner: String,
        repo: String,
        commentId: String,
        body: String
    ): T

    /**
     * Lists comments for a specific commit.
     */
    public fun listCommitComments(
        owner: String,
        repo: String,
        commitSha: String,
        perPage: Int? = null
    ): Flow<T>
}

private inline fun <reified T> GitHubClientImpl.listRepositoryCommitComments(
    owner: String,
    repo: String,
    perPage: Int? = null,
    mediaType: CommentMediaType
): Flow<T> = simplePaginatedGet("repos", owner, repo, "comments") {
    if (perPage != null) {
        this.perPage = perPage
    }
    request {
        mediaType(mediaType)
    }
}

private suspend inline fun <reified T> GitHubClientImpl.getCommitComment(
    owner: String,
    repo: String,
    commentId: String,
    mediaType: CommentMediaType
): T = get("repos", owner, repo, "comments", commentId) {
    request {
        mediaType(mediaType)
    }
}

private suspend inline fun <reified T> GitHubClientImpl.updateCommitComment(
    owner: String,
    repo: String,
    commentId: String,
    body: String,
    mediaType: CommentMediaType
): T = patch("repos", owner, repo, "comments", commentId) {
    request {
        mediaType(mediaType)
        this.body = body
    }
}

private suspend inline fun <reified T> GitHubClientImpl.deleteCommitComment(
    owner: String,
    repo: String,
    commentId: String
): T = delete("repos", owner, repo, "comments", commentId)

private inline fun <reified T> GitHubClientImpl.listCommitComments(
    owner: String,
    repo: String,
    commitSha: String,
    perPage: Int?,
    mediaType: CommentMediaType
): Flow<T> = simplePaginatedGet("repos", owner, repo, "commits", commitSha, "comments") {
    if (perPage != null) {
        this.perPage = perPage
    }
    request {
        mediaType(mediaType)
    }
}

@JvmInline
internal value class FullRepositoryCommentsAPI internal constructor(
    private val gitHubClient: GitHubClientImpl
) : RepositoryCommentsAPI<FullCommitComment> {

    override val mediaType: CommentMediaType
        get() = CommentMediaType.FULL

    override fun listRepositoryCommitComments(
        owner: String,
        repo: String,
        perPage: Int?
    ): Flow<FullCommitComment> = gitHubClient.listRepositoryCommitComments(owner, repo, perPage, mediaType)

    override suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): FullCommitComment = gitHubClient.getCommitComment(owner, repo, commentId, mediaType)

    override suspend fun updateCommitComment(
        owner: String,
        repo: String,
        commentId: String,
        body: String
    ): FullCommitComment = gitHubClient.updateCommitComment(owner, repo, commentId, body, mediaType)

    override fun listCommitComments(
        owner: String,
        repo: String,
        commitSha: String,
        perPage: Int?
    ): Flow<FullCommitComment> = gitHubClient.listCommitComments(owner, repo, commitSha, perPage, mediaType)
}

@JvmInline
internal value class HtmlRepositoryCommentsAPI internal constructor(
    private val gitHubClient: GitHubClientImpl
) : RepositoryCommentsAPI<HtmlCommitComment> {

    override val mediaType: CommentMediaType
        get() = CommentMediaType.HTML

    override fun listRepositoryCommitComments(
        owner: String,
        repo: String,
        perPage: Int?
    ): Flow<HtmlCommitComment> = gitHubClient.listRepositoryCommitComments(owner, repo, perPage, mediaType)

    override suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): HtmlCommitComment = gitHubClient.getCommitComment(owner, repo, commentId, mediaType)

    override suspend fun updateCommitComment(
        owner: String,
        repo: String,
        commentId: String,
        body: String
    ): HtmlCommitComment = gitHubClient.updateCommitComment(owner, repo, commentId, body, mediaType)

    override fun listCommitComments(
        owner: String,
        repo: String,
        commitSha: String,
        perPage: Int?
    ): Flow<HtmlCommitComment> = gitHubClient.listCommitComments(owner, repo, commitSha, perPage, mediaType)
}

@JvmInline
internal value class TextRepositoryCommentsAPI internal constructor(
    private val gitHubClient: GitHubClientImpl
) : RepositoryCommentsAPI<TextCommitComment> {

    override val mediaType: CommentMediaType
        get() = CommentMediaType.TEXT

    override fun listRepositoryCommitComments(
        owner: String,
        repo: String,
        perPage: Int?
    ): Flow<TextCommitComment> = gitHubClient.listRepositoryCommitComments(owner, repo, perPage, mediaType)

    override suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): TextCommitComment = gitHubClient.getCommitComment(owner, repo, commentId, mediaType)

    override suspend fun updateCommitComment(
        owner: String,
        repo: String,
        commentId: String,
        body: String
    ): TextCommitComment = gitHubClient.updateCommitComment(owner, repo, commentId, body, mediaType)

    override fun listCommitComments(
        owner: String,
        repo: String,
        commitSha: String,
        perPage: Int?
    ): Flow<TextCommitComment> = gitHubClient.listCommitComments(owner, repo, commitSha, perPage, mediaType)
}

@JvmInline
internal value class RawRepositoryCommentsAPI internal constructor(
    private val gitHubClient: GitHubClientImpl
) : RepositoryCommentsAPI<RawCommitComment> {

    override val mediaType: CommentMediaType
        get() = CommentMediaType.RAW

    override fun listRepositoryCommitComments(
        owner: String,
        repo: String,
        perPage: Int?
    ): Flow<RawCommitComment> = gitHubClient.listRepositoryCommitComments(owner, repo, perPage, mediaType)

    override suspend fun getCommitComment(
        owner: String,
        repo: String,
        commentId: String
    ): RawCommitComment = gitHubClient.getCommitComment(owner, repo, commentId, mediaType)

    override suspend fun updateCommitComment(
        owner: String,
        repo: String,
        commentId: String,
        body: String
    ): RawCommitComment = gitHubClient.updateCommitComment(owner, repo, commentId, body, mediaType)

    override fun listCommitComments(
        owner: String,
        repo: String,
        commitSha: String,
        perPage: Int?
    ): Flow<RawCommitComment> = gitHubClient.listCommitComments(owner, repo, commitSha, perPage, mediaType)
}
