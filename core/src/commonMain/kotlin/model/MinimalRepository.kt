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
public data class MinimalRepository(
    val id: Int,
    @SerialName("node_id")
    val nodeId: String,
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("private")
    val isPrivate: Boolean,
    val owner: SimpleUser,
    @SerialName("html_url")
    val htmlUrl: String,
    val description: String?,
    @SerialName("fork")
    val isFork: Boolean,
    val url: String,
    @SerialName("forks_url")
    val forksUrl: String,
    @SerialName("keys_url")
    val keysUrl: String,
    @SerialName("collaborators_url")
    val collaboratorsUrl: String,
    @SerialName("teams_url")
    val teamsUrl: String,
    @SerialName("hooks_url")
    val hooksUrl: String,
    @SerialName("issue_events_url")
    val issueEventsUrl: String,
    @SerialName("events_url")
    val eventsUrl: String,
    @SerialName("assignees_url")
    val assigneesUrl: String,
    @SerialName("branches_url")
    val branchesUrl: String,
    @SerialName("tags_url")
    val tagsUrl: String,
    @SerialName("blobs_url")
    val blobsUrl: String,
    @SerialName("git_tags_url")
    val gitTagsUrl: String,
    @SerialName("git_refs_url")
    val gitRefsUrl: String,
    @SerialName("trees_url")
    val treesUrl: String,
    @SerialName("statuses_url")
    val statusesUrl: String,
    @SerialName("languages_url")
    val languagesUrl: String,
    @SerialName("stargazers_url")
    val stargazersUrl: String,
    @SerialName("contributors_url")
    val contributorsUrl: String,
    @SerialName("subscribers_url")
    val subscribersUrl: String,
    @SerialName("subscription_url")
    val subscriptionUrl: String,
    @SerialName("commits_url")
    val commitsUrl: String,
    @SerialName("git_commits_url")
    val gitCommitsUrl: String,
    @SerialName("comments_url")
    val commentsUrl: String,
    @SerialName("issue_comment_url")
    val issueCommentUrl: String,
    @SerialName("contents_url")
    val contentsUrl: String,
    @SerialName("compare_url")
    val compareUrl: String,
    @SerialName("merges_url")
    val mergesUrl: String,
    @SerialName("archive_url")
    val archiveUrl: String,
    @SerialName("downloads_url")
    val downloadsUrl: String,
    @SerialName("issues_url")
    val issuesUrl: String,
    @SerialName("pulls_url")
    val pullsUrl: String,
    @SerialName("milestones_url")
    val milestonesUrl: String,
    @SerialName("notifications_url")
    val notificationsUrl: String,
    @SerialName("labels_url")
    val labelsUrl: String,
    @SerialName("releases_url")
    val releasesUrl: String,
    @SerialName("deployments_url")
    val deploymentsUrl: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
    @SerialName("pushed_at")
    val pushedAt: Instant,
    @SerialName("git_url")
    val gitUrl: String,
    @SerialName("ssh_url")
    val sshUrl: String,
    @SerialName("clone_url")
    val cloneUrl: String,
    @SerialName("svn_url")
    val svnUrl: String,
    val homepage: String?,
    val size: Int,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    val language: String?,
    @SerialName("has_issues")
    val hasIssues: Boolean,
    @SerialName("has_projects")
    val hasProjects: Boolean,
    @SerialName("has_downloads")
    val hasDownloads: Boolean,
    @SerialName("has_wiki")
    val hasWiki: Boolean,
    @SerialName("has_pages")
    val hasPages: Boolean,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("mirror_url")
    val mirrorUrl: String?,
    @SerialName("archived")
    val isArchived: Boolean,
    @SerialName("disabled")
    val isDisabled: Boolean,
    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    val license: SimpleLicense?,
    val forks: Int,
    @SerialName("open_issues")
    val openIssues: Int,
    val watchers: Int,
    @SerialName("default_branch")
    val defaultBranch: String,
    val permissions: RepositoryPermissions
)
