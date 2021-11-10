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

@file:Suppress("NAME_SHADOWING")

package dev.nycode.github.repositories.contents

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.repositories.RepositoriesAPI
import dev.nycode.github.repositories.contents.model.ArchiveFormat
import dev.nycode.github.repositories.contents.model.FileCommit
import dev.nycode.github.repositories.contents.model.RepositoryContent
import dev.nycode.github.repositories.contents.request.CreateFileContentsRequestBuilder
import dev.nycode.github.repositories.contents.request.DeleteFileContentsRequestBuilder
import dev.nycode.github.repositories.contents.request.DownloadRepositoryArchiveRequestBuilder
import dev.nycode.github.repositories.contents.request.GetRepositoryContentRequestBuilder
import dev.nycode.github.repositories.contents.request.GetRepositoryReadMeRequestBuilder
import dev.nycode.github.repositories.contents.request.ReadMeRequest
import dev.nycode.github.repositories.contents.request.UpdateFileContentsRequestBuilder
import dev.nycode.github.request.delete
import dev.nycode.github.request.get
import dev.nycode.github.request.put
import io.ktor.client.request.accept
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmInline

/**
 * Provides APIs regarding repository contents.
 */
public val RepositoriesAPI.contents: RepositoryContentsAPI
    get() = RepositoryContentsAPI(gitHubClient)

/**
 * Provides APIs regarding repository contents.
 */
@JvmInline
public value class RepositoryContentsAPI(@PublishedApi internal val gitHubClient: GitHubClientImpl) {
    /**
     * Gets a directory, file, symlink or submodule from a GitHub repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-repository-content).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param path the path of the content in the repo. Leave it empty to get the contents of the root directory.
     * @param builder builder for specifying optional parameters
     * @return the repository content
     */
    public suspend inline fun getRepositoryContent(
        owner: String,
        repo: String,
        path: String,
        builder: GetRepositoryContentRequestBuilder.() -> Unit = {}
    ): RepositoryContent {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return gitHubClient.get("repos", owner, repo, "contents", path) {
            val (ref) = GetRepositoryContentRequestBuilder().apply(builder)
            request {
                parameter("ref", ref)
                accept(ContentType("application", "vnd.github.v3.object"))
            }
        }
    }

    /**
     * Creates a new file in a GitHub repository.
     * Internally this speaks to the `Create or update file contents` endpoint,
     * but it's implemented as 2 different functions because so we are able to have strongly typed requests.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#create-or-update-file-contents).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param path the path of the file to create
     * @param message the commit message
     * @param content the new content in Base64 encoding
     * @param builder builder for additional optional parameters
     */
    public suspend inline fun createFileContents(
        owner: String,
        repo: String,
        path: String,
        message: String,
        content: String,
        builder: CreateFileContentsRequestBuilder.() -> Unit = {}
    ): FileCommit {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        val builder = CreateFileContentsRequestBuilder(message, content).apply(builder)
        return gitHubClient.put("repos", owner, repo, "contents", path) {
            request {
                contentType(ContentType.Application.Json)
                body = builder
            }
        }
    }

    /**
     * Updates a file in a GitHub repository.
     * Internally this speaks to the `Create or update file contents` endpoint,
     * but it's implemented as 2 different functions because so we are able to have strongly typed requests.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#create-or-update-file-contents).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param path the path of the file to update
     * @param message the commit message
     * @param content the new content in Base64 encoding
     * @param sha the blob SHA of the file being replaced
     * @param builder builder for additional optional parameters
     */
    public suspend inline fun updateFileContents(
        owner: String,
        repo: String,
        path: String,
        message: String,
        content: String,
        sha: String,
        builder: UpdateFileContentsRequestBuilder.() -> Unit = {}
    ): FileCommit {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        val builder = UpdateFileContentsRequestBuilder(message, content, sha).apply(builder)
        return gitHubClient.put("repos", owner, repo, "contents", path) {
            request {
                contentType(ContentType.Application.Json)
                body = builder
            }
        }
    }

    /**
     * Deletes a file in a GitHub repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#delete-a-file).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param path the path of the file to delete
     * @param message the commit message
     * @param sha the blob SHA of the file being replaced
     * @param builder builder for additional optional parameters
     */
    public suspend inline fun deleteFile(
        owner: String,
        repo: String,
        path: String,
        message: String,
        sha: String,
        builder: DeleteFileContentsRequestBuilder.() -> Unit = {}
    ): FileCommit {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        val builder = DeleteFileContentsRequestBuilder(message, sha).apply(builder)
        return gitHubClient.delete("repos", owner, repo, "contents", path) {
            request {
                contentType(ContentType.Application.Json)
                body = builder
            }
        }
    }

    /**
     * Gets the preferred README for a repository.
     *
     * The actual request happens when you call a function on the returned object.
     * You can choose between different formats like [ReadMeRequest.raw], [ReadMeRequest.html] and [ReadMeRequest.regular].
     *
     * Represents [this](https://docs.github.com/en/rest/reference/repos#get-a-repository-readme) and [this](https://docs.github.com/en/rest/reference/repos#get-a-repository-readme-for-a-directory) endpoint.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param builder builder for specifying optional parameters e.g. the path
     * @return a wrapper object around the request.
     */
    public inline fun getRepositoryReadMe(
        owner: String,
        repo: String,
        builder: GetRepositoryReadMeRequestBuilder.() -> Unit = {}
    ): ReadMeRequest {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        val (ref, path) = GetRepositoryReadMeRequestBuilder().apply(builder)
        return ReadMeRequest(gitHubClient, owner, repo, path, ref)
    }

    /**
     * Downloads the repos archive as a [ByteReadChannel].
     * You could write this to a file or work with it in-memory.
     *
     * Represents [this](https://docs.github.com/en/rest/reference/repos#download-a-repository-archive-tar) and [this](https://docs.github.com/en/rest/reference/repos#download-a-repository-archive-zip) endpoint.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param format the archive type to download
     * @param builder builder for specifying optional parameters e.g. the reference
     * @return the data in a [ByteReadChannel]
     */
    public suspend inline fun downloadRepositoryArchive(
        owner: String,
        repo: String,
        format: ArchiveFormat,
        builder: DownloadRepositoryArchiveRequestBuilder.() -> Unit = {}
    ): ByteReadChannel {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        val format = "${format.name.lowercase()}ball"
        val (ref) = DownloadRepositoryArchiveRequestBuilder().apply(builder)
        return gitHubClient.get("repos", owner, repo, format, ref)
    }
}
