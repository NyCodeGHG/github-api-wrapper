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

package dev.nycode.github.repositories.deploykeys

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.repositories.RepositoriesAPI
import dev.nycode.github.repositories.deploykeys.model.DeployKey
import dev.nycode.github.request.simplePaginatedGet
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

public val RepositoriesAPI.deployKeys: RepositoryDeployKeysAPI
    get() = RepositoryDeployKeysAPI(gitHubClient)

/**
 * Provides APIs regarding repository deploy keys.
 */
@JvmInline
public value class RepositoryDeployKeysAPI(@PublishedApi internal val gitHubClient: GitHubClientImpl) {
    /**
     * Lists all deploys keys of a repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-deploy-keys).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return a [Flow] of [DeployKey]s.
     */
    public fun listDeployKeys(
        owner: String,
        repo: String,
        perPage: Int? = null
    ): Flow<DeployKey> = gitHubClient.simplePaginatedGet("repos", owner, repo, "keys") {
        if (perPage != null) {
            this.perPage = perPage
        }
    }
}
