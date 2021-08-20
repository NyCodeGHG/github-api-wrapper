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
import de.nycode.github.model.SimpleUser
import de.nycode.github.model.Team
import de.nycode.github.preview.ApiPreview
import de.nycode.github.preview.Previews
import de.nycode.github.preview.preview
import de.nycode.github.repositories.RepositoriesAPI
import de.nycode.github.repositories.branches.model.AccessRestrictions
import de.nycode.github.repositories.branches.model.App
import de.nycode.github.repositories.branches.request.*
import de.nycode.github.repositories.model.*
import de.nycode.github.request.*
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

/**
 * Access APIs related to repository branches.
 */
public val RepositoriesAPI.branches: RepositoryBranchesAPI
    get() = RepositoryBranchesAPI(gitHubClient)

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
        requiredPullRequestReviews: PullRequestReviewsBuilder?,
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

    /**
     * Gets admin branch protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-admin-branch-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getAdminBranchProtection(
        owner: String,
        repo: String,
        branch: String
    ): UrlEnabledValue =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection", "enforce_admins")

    /**
     * Sets admin branch protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#set-admin-branch-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun setAdminBranchProtection(
        owner: String,
        repo: String,
        branch: String
    ): UrlEnabledValue =
        gitHubClient.post("repos", owner, repo, "branches", branch, "protection", "enforce_admins")

    /**
     * Deletes admin branch protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#set-admin-branch-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun deleteAdminBranchProtection(
        owner: String,
        repo: String,
        branch: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "branches", branch, "protection", "enforce_admins")

    /**
     * Gets the pull request review protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-pull-request-review-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return the branches pull request protection
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getPullRequestReviewProtection(
        owner: String,
        repo: String,
        branch: String
    ): PullRequestReviewProtection =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection", "required_pull_request_reviews")

    /**
     * Updates the pull request review protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#update-pull-request-review-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param builder builder for updating the review protection
     * @return the branches pull request protection
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun updatePullRequestReviewProtection(
        owner: String,
        repo: String,
        branch: String,
        builder: EditPullRequestReviewsBuilder.() -> Unit
    ): PullRequestReviewProtection =
        gitHubClient.patch("repos", owner, repo, "branches", branch, "protection", "required_pull_request_reviews") {
            request {
                contentType(ContentType.Application.Json)
                body = EditPullRequestReviewsBuilder().apply(builder)
            }
        }

    /**
     * Deletes the pull request review protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#delete-pull-request-review-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun deletePullRequestReviewProtection(
        owner: String,
        repo: String,
        branch: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "branches", branch, "protection", "required_pull_request_reviews")

    /**
     * Gets the commit signature protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * When authenticated with admin or owner permissions to the repository,
     * you can use this endpoint to check whether a branch requires signed commits.
     * An enabled status of true indicates you must sign commits on this branch.
     * For more information, see [Signing commits with GPG](https://help.github.com/articles/signing-commits-with-gpg) in GitHub Help.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-commit-signature-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return an [UrlEnabledValue] containing the request url and the value
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    @ApiPreview
    public suspend fun getCommitSignatureProtection(
        owner: String,
        repo: String,
        branch: String
    ): UrlEnabledValue =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection", "required_signatures") {
            request {
                preview(Previews.ZzzaxPreview)
            }
        }

    /**
     * Creates a commit signature protection for the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * When authenticated with admin or owner permissions to the repository,
     * you can use this endpoint to require signed commits on a branch.
     * You must enable branch protection to require signed commits.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#create-commit-signature-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return an [UrlEnabledValue] containing the request url and the value
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    @ApiPreview
    public suspend fun createCommitSignatureProtection(
        owner: String,
        repo: String,
        branch: String
    ): UrlEnabledValue =
        gitHubClient.post("repos", owner, repo, "branches", branch, "protection", "required_signatures") {
            request {
                preview(Previews.ZzzaxPreview)
            }
        }

    /**
     * Deletes commit signature protection for the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * When authenticated with admin or owner permissions to the repository,
     * you can use this endpoint to disable required signed commits on a branch.
     * You must enable branch protection to require signed commits.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#delete-commit-signature-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    @ApiPreview
    public suspend fun deleteCommitSignatureProtection(
        owner: String,
        repo: String,
        branch: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "branches", branch, "protection", "required_signatures") {
            request {
                preview(Previews.ZzzaxPreview)
            }
        }

    /**
     * Gets the status checks protection of the specified branch of the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-status-checks-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return the status checks protection of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getStatusChecksProtection(
        owner: String,
        repo: String,
        branch: String
    ): StatusChecks =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection", "required_status_checks")

    /**
     * Updates the status checks protection of the specified branch of the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#update-status-checks-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return the status checks protection of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun updateStatusCheckProtection(
        owner: String,
        repo: String,
        branch: String,
        builder: StatusChecksBuilder.() -> Unit
    ): StatusChecks =
        gitHubClient.patch("repos", owner, repo, "branches", branch, "protection", "required_status_checks") {
            request {
                contentType(ContentType.Application.Json)
                body = StatusChecksBuilder().apply(builder)
            }
        }

    /**
     * Removes the status checks protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#remove-status-check-protection).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun removeStatusCheckProtection(
        owner: String,
        repo: String,
        branch: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "branches", branch, "protection", "required_status_checks")

    /**
     * Gets all status check contexts of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-all-status-check-contexts).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return [List] containing all check contexts
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getStatusCheckContexts(
        owner: String,
        repo: String,
        branch: String
    ): List<String> =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection", "required_status_checks", "contexts")

    /**
     * Adds a status check context to the status check protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#add-status-check-contexts).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param contexts the list of contexts to add
     * @return [List] containing all check contexts
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun addStatusCheckContexts(
        owner: String,
        repo: String,
        branch: String,
        contexts: List<String>
    ): List<String> =
        gitHubClient.post(
            "repos",
            owner,
            repo,
            "branches",
            branch,
            "protection",
            "required_status_checks",
            "contexts"
        ) {
            request {
                contentType(ContentType.Application.Json)
                body = StatusCheckContextsRequestBuilder(contexts)
            }
        }

    /**
     * Sets all status check contexts of the status check protection of the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#set-status-check-contexts).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param contexts the list of contexts to set to
     * @return [List] containing all check contexts
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun setStatusCheckContexts(
        owner: String,
        repo: String,
        branch: String,
        contexts: List<String>
    ): List<String> =
        gitHubClient.put("repos", owner, repo, "branches", branch, "protection", "required_status_checks", "contexts") {
            request {
                contentType(ContentType.Application.Json)
                body = StatusCheckContextsRequestBuilder(contexts)
            }
        }

    /**
     * Removes status check contexts from the specified branch in the specified repository.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#remove-status-check-contexts).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param contexts the list of contexts to remove
     * @return [List] containing all check contexts
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun removeStatusCheckContexts(
        owner: String,
        repo: String,
        branch: String,
        contexts: List<String>
    ): List<String> =
        gitHubClient.delete(
            "repos",
            owner,
            repo,
            "branches",
            branch,
            "protection",
            "required_status_checks",
            "contexts"
        ) {
            request {
                contentType(ContentType.Application.Json)
                body = StatusCheckContextsRequestBuilder(contexts)
            }
        }

    /**
     * Lists who has access to this protected branch.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Note: Users, apps, and teams restrictions are only available for organization-owned repositories.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return the access restriction of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getAccessRestrictions(
        owner: String,
        repo: String,
        branch: String
    ): AccessRestrictions =
        gitHubClient.get("repos", owner, repo, "branches", branch)

    /**
     * Disables the ability to restrict who can push to this branch.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Note: Users, apps, and teams restrictions are only available for organization-owned repositories.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#delete-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun deleteAccessRestrictions(
        owner: String,
        repo: String,
        branch: String
    ): Unit =
        gitHubClient.delete("repos", owner, repo, "branches", branch)

    /**
     * Lists the GitHub Apps that have push access to this branch.
     * Only installed GitHub Apps with write access to the `contents`
     * permission can be added as authorized actors on a protected branch.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-apps-with-access-to-the-protected-branch).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return [List] of [App]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getAppsWithAccessToProtectedBranch(
        owner: String,
        repo: String,
        branch: String
    ): List<App> =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection", "restrictions", "apps")

    /**
     * Grants the specified apps push access for this branch.
     * Only installed GitHub Apps with write access to the `contents` permission can be added as authorized actors on a protected branch.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#add-app-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param apps The GitHub Apps that have push access to this branch. Use the app's slug. Note: The list of users, apps, and teams in total is limited to 100 items.
     * @return [List] of [App]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun addAppAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        apps: List<String>
    ): List<App> =
        gitHubClient.post("repos", owner, repo, "branches", branch, "protection", "restrictions", "apps") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("apps" to apps)
            }
        }

    /**
     * Replaces the list of apps that have push access to this branch.
     * This removes all apps that previously had push access and grants push access to the new list of apps.
     * Only installed GitHub Apps with write access to the contents permission can be added as authorized actors on a protected branch.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#set-app-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param apps The GitHub Apps that have push access to this branch. Use the app's slug. Note: The list of users, apps, and teams in total is limited to 100 items.
     * @return [List] of [App]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun setAppAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        apps: List<String>
    ): List<App> =
        gitHubClient.put("repos", owner, repo, "branches", branch, "protection", "restrictions", "apps") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("apps" to apps)
            }
        }

    /**
     * Removes the ability of an app to push to this branch.
     * Only installed GitHub Apps with write access to the `contents` permission can be added as authorized actors on a protected branch.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#remove-app-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param apps The GitHub Apps that have push access to this branch. Use the app's slug. Note: The list of users, apps, and teams in total is limited to 100 items.
     * @return [List] of [App]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun removeAppAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        apps: List<String>
    ): List<App> =
        gitHubClient.delete("repos", owner, repo, "branches", branch, "protection", "restrictions", "apps") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("apps" to apps)
            }
        }

    /**
     * Lists the teams who have push access to this branch. The list includes child teams.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-teams-with-access-to-the-protected-branch).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return [List] of [Team]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getTeamsWithAccessToProtectedBranch(
        owner: String,
        repo: String,
        branch: String
    ): List<Team> =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection", "restrictions", "teams")

    /**
     * Grants the specified teams push access for this branch. You can also give push access to child teams.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#add-team-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param teams the teams to add
     * @return [List] of [Team]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun addTeamAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        teams: List<String>
    ): List<Team> =
        gitHubClient.post("repos", owner, repo, "branches", branch, "protection", "restrictions", "teams") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("teams" to teams)
            }
        }

    /**
     * Replaces the list of teams that have push access to this branch.
     * This removes all teams that previously had push access and grants push access to the new list of teams.
     * Team restrictions include child teams.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#set-team-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param teams the teams to set
     * @return [List] of [Team]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun setTeamAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        teams: List<String>
    ): List<Team> =
        gitHubClient.put("repos", owner, repo, "branches", branch, "protection", "restrictions", "teams") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("teams" to teams)
            }
        }

    /**
     * Removes the ability of a team to push to this branch.
     * You can also remove push access for child teams.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#remove-team-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param teams the teams to remove
     * @return [List] of [Team]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun removeTeamAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        teams: List<String>
    ): List<Team> =
        gitHubClient.delete("repos", owner, repo, "branches", branch, "protection", "restrictions", "teams") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("teams" to teams)
            }
        }

    /**
     * Lists the people who have push access to this branch.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#get-users-with-access-to-the-protected-branch).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @return [List] of [SimpleUser]s
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun getUsersWithAccessToProtectedBranch(
        owner: String,
        repo: String,
        branch: String
    ): List<SimpleUser> =
        gitHubClient.get("repos", owner, repo, "branches", branch, "protection", "restrictions", "users")

    /**
     * Grants the specified people push access for this branch.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#add-user-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param users the users to grant push access
     * @return [SimpleUser]s which are having push access
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun addUserAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        users: List<String>
    ): List<SimpleUser> =
        gitHubClient.post("repos", owner, repo, "branches", branch, "protection", "restrictions", "users") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("users" to users)
            }
        }

    /**
     * Replaces the list of people that have push access to this branch.
     * This removes all people that previously had push access and grants push access to the new list of people.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#set-user-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param users the users to replace previous users with push access with
     * @return [SimpleUser]s which are having push access
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun setUserAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        users: List<String>
    ): List<SimpleUser> =
        gitHubClient.put("repos", owner, repo, "branches", branch, "protection", "restrictions", "users") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("users" to users)
            }
        }

    /**
     * Removes the ability of a user to push to this branch.
     * This removes all people that previously had push access and grants push access to the new list of people.
     * Protected branches are available in public repositories with GitHub Free and GitHub Free for organizations,
     * and in public and private repositories with GitHub Pro, GitHub Team, GitHub Enterprise Cloud, and GitHub Enterprise Server.
     * For more information, see [GitHub's products](https://help.github.com/github/getting-started-with-github/githubs-products) in the GitHub Help documentation.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#remove-user-access-restrictions).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param users the users from whose push access will be revoked
     * @return [SimpleUser]s which are having push access
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun removeUserAccessRestrictions(
        owner: String,
        repo: String,
        branch: String,
        users: List<String>
    ): List<SimpleUser> =
        gitHubClient.delete("repos", owner, repo, "branches", branch, "protection", "restrictions", "users") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("users" to users)
            }
        }

    /**
     * Renames a branch in a repository.
     * Note: Although the API responds immediately,
     * the branch rename process might take some extra time to complete in the background.
     * You won't be able to push to the old branch name while the rename process is in progress.
     * For more information, see "[Renaming a branch](https://docs.github.com/github/administering-a-repository/renaming-a-branch)".
     *
     * The permissions required to use this endpoint depends on whether you are renaming the default branch.
     *
     * To rename a non-default branch:
     * - Users must have push access.
     * - GitHub Apps must have the `contents:write` repository permission.
     * To rename the default branch:
     * - Users must have admin or owner permissions.
     * - GitHub Apps must have the `administration:write` repository permission.
     *
     * Represents [this endpoint](https://docs.github.com/en/rest/reference/repos#rename-a-branch).
     *
     * @param owner the owner of the repository
     * @param repo the name of the repo
     * @param branch the name of the branch
     * @param newName the new name of the branch
     * @return the targeted branch
     * @throws de.nycode.github.request.GitHubRequestException when the request fails
     */
    public suspend fun renameBranch(
        owner: String,
        repo: String,
        branch: String,
        newName: String
    ): BranchWithProtection =
        gitHubClient.post("repos", owner, repo, "branches", branch, "rename") {
            request {
                contentType(ContentType.Application.Json)
                body = mapOf("new_name" to newName)
            }
        }
}
