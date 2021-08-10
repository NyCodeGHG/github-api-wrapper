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

package de.nycode.github.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Repository(
    public val id: Int,
    @SerialName("node_id")
    public val nodeId: String,
    public val name: String,
    @SerialName("full_name")
    public val fullName: String,
    @SerialName("private")
    public val isPrivate: Boolean,
    public val owner: SimpleUser,
    @SerialName("html_url")
    public val htmlUrl: String,
    public val description: String?,
    @SerialName("fork")
    public val isFork: Boolean,
    public val url: String,
    @SerialName("forks_url")
    public val forksUrl: String,
    @SerialName("keys_url")
    public val keysUrl: String,
    @SerialName("collaborators_url")
    public val collaboratorsUrl: String,
    @SerialName("teams_url")
    public val teamsUrl: String,
    @SerialName("hooks_url")
    public val hooksUrl: String,
    @SerialName("issue_events_url")
    public val issueEventsUrl: String,
    @SerialName("events_url")
    public val eventsUrl: String,
    @SerialName("assignees_url")
    public val assigneesUrl: String,
    @SerialName("branches_url")
    public val branchesUrl: String,
    @SerialName("tags_url")
    public val tagsUrl: String,
    @SerialName("blobs_url")
    public val blobsUrl: String,
    @SerialName("git_tags_url")
    public val gitTagsUrl: String,
    @SerialName("git_refs_url")
    public val gitRefsUrl: String,
    @SerialName("trees_url")
    public val treesUrl: String,
    @SerialName("statuses_url")
    public val statusesUrl: String,
    @SerialName("languages_url")
    public val languagesUrl: String,
    @SerialName("stargazers_url")
    public val stargazersUrl: String,
    @SerialName("contributors_url")
    public val contributorsUrl: String,
    @SerialName("subscribers_url")
    public val subscribersUrl: String,
    @SerialName("subscription_url")
    public val subscriptionUrl: String,
    @SerialName("commits_url")
    public val commitsUrl: String,
    @SerialName("git_commits_url")
    public val gitCommitsUrl: String,
    @SerialName("comments_url")
    public val commentsUrl: String,
    @SerialName("issue_comment_url")
    public val issueCommentUrl: String,
    @SerialName("contents_url")
    public val contentsUrl: String,
    @SerialName("compare_url")
    public val compareUrl: String,
    @SerialName("merges_url")
    public val mergesUrl: String,
    @SerialName("archive_url")
    public val archiveUrl: String,
    @SerialName("downloads_url")
    public val downloadsUrl: String,
    @SerialName("issues_url")
    public val issuesUrl: String,
    @SerialName("pulls_url")
    public val pullsUrl: String,
    @SerialName("milestones_url")
    public val milestonesUrl: String,
    @SerialName("notifications_url")
    public val notificationsUrl: String,
    @SerialName("labels_url")
    public val labelsUrl: String,
    @SerialName("releases_url")
    public val releasesUrl: String,
    @SerialName("deployments_url")
    public val deploymentsUrl: String,
    @SerialName("created_at")
    public val createdAt: Instant? = null,
    @SerialName("updated_at")
    public val updatedAt: Instant? = null,
    @SerialName("pushed_at")
    public val pushedAt: Instant? = null,
    @SerialName("git_url")
    public val gitUrl: String? = null,
    @SerialName("ssh_url")
    public val sshUrl: String? = null,
    @SerialName("clone_url")
    public val cloneUrl: String? = null,
    @SerialName("svn_url")
    public val svnUrl: String? = null,
    public val homepage: String? = null,
    public val size: Int? = null,
    @SerialName("stargazers_count")
    public val stargazersCount: Int? = null,
    @SerialName("watchers_count")
    public val watchersCount: Int? = null,
    public val language: String? = null,
    @SerialName("has_issues")
    public val hasIssues: Boolean? = null,
    @SerialName("has_projects")
    public val hasProjects: Boolean? = null,
    @SerialName("has_downloads")
    public val hasDownloads: Boolean? = null,
    @SerialName("has_wiki")
    public val hasWiki: Boolean? = null,
    @SerialName("has_pages")
    public val hasPages: Boolean? = null,
    @SerialName("forks_count")
    public val forksCount: Int? = null,
    @SerialName("mirror_url")
    public val mirrorUrl: String? = null,
    @SerialName("archived")
    public val isArchived: Boolean? = null,
    @SerialName("disabled")
    public val isDisabled: Boolean? = null,
    @SerialName("open_issues_count")
    public val openIssuesCount: Int? = null,
    public val license: SimpleLicense? = null,
    public val forks: Int? = null,
    @SerialName("open_issues")
    public val openIssues: Int? = null,
    public val watchers: Int? = null,
    @SerialName("default_branch")
    public val defaultBranch: String? = null,
    public val organization: SimpleUser? = null,
    public val parent: Repository? = null,
    public val source: Repository? = null,
    public val permissions: RepositoryPermissions? = null,
    @SerialName("temp_clone_token")
    public val tempCloneToken: String? = null,
    @SerialName("allow_squash_merge")
    public val allowSquashMerge: Boolean? = null,
    @SerialName("allow_merge_commit")
    public val allowMergeCommit: Boolean? = null,
    @SerialName("allow_rebase_merge")
    public val allowRebaseMerge: Boolean? = null,
    @SerialName("allow_auto_merge")
    public val allowAutoMerge: Boolean? = null,
    @SerialName("delete_branch_on_merge")
    public val deleteBranchOnMerge: Boolean? = null,
    @SerialName("network_count")
    public val networkCount: Int? = null,
    @SerialName("subscribers_count")
    public val subscribersCount: Int? = null
)
