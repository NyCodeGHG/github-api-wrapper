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
public data class SimpleMilestone(
    val url: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("lablels_url")
    val labelsUrl: String,
    val id: Int,
    @SerialName("node_id")
    val nodeId: String,
    val number: Int,
    val state: MilestoneState,
    val title: String,
    val description: String?,
    val creator: SimpleUser?,
    @SerialName("open_issues")
    val openIssues: Int,
    @SerialName("closed_issues")
    val closedIssues: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
    @SerialName("closed_at")
    val closedAt: Instant?,
    @SerialName("due_on")
    val dueOn: Instant?
)
