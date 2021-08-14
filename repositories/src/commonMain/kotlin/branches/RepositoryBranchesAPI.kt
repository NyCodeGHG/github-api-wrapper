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

package de.nycode.github.repositories.branches

import de.nycode.github.GitHubClient
import de.nycode.github.branches.request.ListBranchesRequestBuilder
import de.nycode.github.repositories.branches.request.RequiredPullRequestReviewsBuilder
import de.nycode.github.repositories.branches.request.RestrictionsBuilder
import de.nycode.github.repositories.branches.request.UpdateBranchProtectionRequestBuilder
import de.nycode.github.repositories.model.BranchProtection
import de.nycode.github.repositories.model.BranchWithProtection
import de.nycode.github.repositories.model.ShortBranch
import de.nycode.github.repositories.model.SimpleStatusChecks
import de.nycode.github.request.delete
import de.nycode.github.request.get
import de.nycode.github.request.put
import de.nycode.github.request.simplePaginatedGet
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

/**
 * Access [Repository branches endpoints](https://docs.github.com/en/rest/reference/repos#branches).
 */
@JvmInline
public value class RepositoryBranchesAPI(private val gitHubClient: GitHubClient) {

    /**
     * Lists all branches of the specified repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#list-branches).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param builder builder for configuring the request
     * @return [Flow] of [ShortBranch]es
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public fun listBranches(
        owner: String,
        repo: String,
        builder: ListBranchesRequestBuilder.() -> Unit = {}
    ): Flow<ShortBranch> =
        gitHubClient.simplePaginatedGet("repos", owner, repo, "branches") {
            request {
                val (protected, perPage) = ListBranchesRequestBuilder().apply(builder)
                parameter("protected", protected)
                parameter("per_page", perPage)
            }
        }

    /**
     * Gets a specific branch of the specified repository.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-a-branch).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return the branch with its protection data
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getBranch(
        owner: String,
        repo: String,
        branch: String
    ): BranchWithProtection =
        gitHubClient.get("repos", owner, repo, "branches", branch)

    /**
     * Gets protection of a branch from the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-branch-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return the branch's protection information
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getBranchProtection(
        owner: String,
        repo: String,
        branch: String
    ): BranchProtection =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection")

    /**
     * Updates protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#update-branch-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param requiredStatusChecks Require status checks to pass before merging. Set to null to disable.
     * @param enforceAdmins Enforce all configured restrictions for administrators. Set to true to enforce required status checks for repository administrators. Set to null to disable.
     * @param requiredPullRequestReviews Require at least one approving review on a pull request, before merging. Set to null to disable.
     * @param restrictions Restrict who can push to the protected branch. User, app, and team restrictions are only available for organization-owned repositories. Set to null to disable.
     * @param requiredLinearHistory Enforces a linear commit Git history, which prevents anyone from pushing merge commits to a branch. Set to true to enforce a linear commit history. Set to false to disable a linear commit Git history. Your repository must allow squash merging or rebase merging before you can enable a linear commit history. Default: false. For more information, see ["Requiring a linear commit history"](https://help.github.com/github/administering-a-repository/requiring-a-linear-commit-history) in the GitHub Help documentation.
     * @param allowForcePushes Permits force pushes to the protected branch by anyone with write access to the repository. Set to true to allow force pushes. Set to false or null to block force pushes. Default: false. For more information, see ["Enabling force pushes to a protected branch"](https://help.github.com/en/github/administering-a-repository/enabling-force-pushes-to-a-protected-branch) in the GitHub Help documentation.
     * @param allowDeletions Allows deletion of the protected branch by anyone with write access to the repository. Set to false to prevent deletion of the protected branch. Default: false. For more information, see ["Enabling force pushes to a protected branch"](https://help.github.com/en/github/administering-a-repository/enabling-force-pushes-to-a-protected-branch) in the GitHub Help documentation.
     * @param requiredConversationResolution Requires all conversations on code to be resolved before a pull request can be merged into a branch that matches this rule. Set to false to disable. Default: false.
     * @param builder builder for configuring the request
     * @return the updated [BranchProtection]
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun updateBranchProtection(
        owner: String,
        repo: String,
        branch: String,
        requiredStatusChecks: SimpleStatusChecks,
        enforceAdmins: Boolean,
        requiredPullRequestReviews: RequiredPullRequestReviewsBuilder?,
        restrictions: RestrictionsBuilder?,
        requiredLinearHistory: Boolean,
        allowForcePushes: Boolean?,
        allowDeletions: Boolean,
        requiredConversationResolution: Boolean,
        builder: UpdateBranchProtectionRequestBuilder.() -> Unit = {}
    ): BranchProtection =
        gitHubClient.put("repos", owner, repo, "branches", branch, "protection") {
            request {
                contentType(ContentType.Application.Json)
                body = UpdateBranchProtectionRequestBuilder(
                    requiredStatusChecks,
                    enforceAdmins,
                    requiredPullRequestReviews,
                    restrictions,
                    requiredLinearHistory,
                    allowForcePushes,
                    allowDeletions,
                    requiredConversationResolution
                ).apply(builder)
            }
        }

    /**
     * Deletes protection of the specified branch of the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#delete-branch-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun deleteBranchProtection(
        owner: String,
        repo: String,
        branch: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "branches", branch, "protection")
}
