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

package dev.nycode.github.repositories.contents

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.repositories.RepositoriesAPI
import dev.nycode.github.repositories.contents.model.RepositoryContent
import dev.nycode.github.repositories.contents.request.GetRepositoryContentRequestBuilder
import dev.nycode.github.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
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
                header(HttpHeaders.Accept, "application/vnd.github.v3.object")
            }
        }
    }
}
