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

package dev.nycode.github.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SimplePullRequest(
    val url: String,
    val id: Int,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("diff_url")
    val diffUrl: String,
    @SerialName("patch_url")
    val patchUrl: String,
    @SerialName("issue_url")
    val issueUrl: String,
    @SerialName("commits_url")
    val commitsUrl: String,
    @SerialName("review_comments_url")
    val reviewCommentsUrl: String,
    @SerialName("review_comment_url")
    val reviewCommentUrl: String,
    @SerialName("comments_url")
    val commentsUrl: String,
    @SerialName("statuses_url")
    val statusesUrl: String,
    val number: Int,
    val state: String,
    @SerialName("locked")
    val isLocked: Boolean,
    val title: String,
    val user: SimpleUser?,
    val body: String?,
    val labels: List<SimpleLabel>?,
    val milestone: SimpleMilestone?,
    val activeLockReason: String?,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
    @SerialName("closed_at")
    val closedAt: Instant?,
    @SerialName("merged_at")
    val mergedAt: Instant?,
    @SerialName("merge_commit_sha")
    val mergeCommitSha: String?,
    val assignee: SimpleUser?,
    val assignees: List<SimpleUser>?,
    @SerialName("requested_reviewers")
    val requestedReviewers: List<SimpleUser>?,
    @SerialName("requested_teams")
    val requestedTeams: List<SimpleTeam>?,
    val head: PullRequestRef,
    val base: PullRequestRef,
    val _links: SimplePullRequestLinks,
    val authorAssociation: AuthorAssociation,
    val autoMerge: AutoMerge?,
    @SerialName("draft")
    val isDraft: Boolean
)
