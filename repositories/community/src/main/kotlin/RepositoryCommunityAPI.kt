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

package dev.nycode.github.repositories.community

import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.repositories.community.model.CommunityProfile
import dev.nycode.github.request.get
import kotlin.jvm.JvmInline

/**
 * Provides APIs regarding community.
 */
@JvmInline
public value class RepositoryCommunityAPI(private val gitHubClient: GitHubClientImpl) {

    /**
     * Gets all community profile metrics including an overall health score,
     * repository description, the presence of documentation, detected code of conduct,
     * detected license, and the presence of ISSUE_TEMPLATE, PULL_REQUEST_TEMPLATE, README, and CONTRIBUTING files.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-community-profile-metrics).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @return the repository's community profile
     */
    public suspend fun getCommunityProfileMetrics(
        owner: String,
        repo: String
    ): CommunityProfile = gitHubClient.get("repos", owner, repo, "community", "profile")

}
