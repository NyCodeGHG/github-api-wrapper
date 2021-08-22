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

package de.nycode.github.repositories.collaborators

import de.nycode.github.GitHubClient
import de.nycode.github.repositories.model.Collaborator
import de.nycode.github.request.GitHubRequestException
import de.nycode.github.request.simplePaginatedGet
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

/**
 * Provides APIs regarding repository collaborators.
 */
@JvmInline
public value class RepositoryCollaboratorsAPI(private val gitHubClient: GitHubClient) {
    /**
     * Lists all collaborators of the specified repository.
     * For organization-owned repositories,
     * the list of collaborators includes outside collaborators,
     * organization members that are direct collaborators,
     * organization members with access through team memberships,
     * organization members with access through default organization permissions,
     * and organization owners.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-repository-collaborators).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param perPage results per page
     * @return A [Flow] of the repositories [Collaborator]s
     * @throws GitHubRequestException when the request fails
     */
    public fun listRepositoryCollaborators(
        owner: String,
        repo: String,
        perPage: Int? = null
    ): Flow<Collaborator> =
        gitHubClient.simplePaginatedGet("repos", owner, repo, "collaborators") {
            if (perPage != null) {
                this.perPage = perPage
            }
        }
}
