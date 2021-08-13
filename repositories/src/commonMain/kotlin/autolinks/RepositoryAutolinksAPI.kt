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

package autolinks;

import de.nycode.github.GitHubClient
import de.nycode.github.repositories.model.Autolink
import de.nycode.github.request.simplePaginatedGet
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

/**
 * Access [Repository autolink endpoints](https://docs.github.com/en/rest/reference/repos#autolinks).
 */
@JvmInline
public value class RepositoryAutolinksAPI(private val gitHubClient: GitHubClient) {

    /**
     * Lists all autolinks configured for the given repository.
     * Information about autolinks are only available to repository administrators.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-all-autolinks-of-a-repository).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return [Flow] of [Autolink]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public fun listRepositoryAutolinks(
        owner: String,
        repo: String
    ): Flow<Autolink> =
        gitHubClient.simplePaginatedGet("repos", owner, repo, "autolinks")

}
