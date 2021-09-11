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

package dev.nycode.github.repositories.commits.model

import dev.nycode.github.repositories.model.CommitData
import dev.nycode.github.repositories.model.CommitFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CommitComparison(
    val url: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("permalink_url")
    val permalinkUrl: String,
    @SerialName("diff_url")
    val diffUrl: String,
    @SerialName("patch_url")
    val patchUrl: String,
    @SerialName("base_commit")
    val baseCommit: CommitData,
    @SerialName("merge_base_commit")
    val mergeBaseCommit: CommitData,
    val status: CommitStatus,
    @SerialName("ahead_by")
    val aheadBy: Int,
    @SerialName("behind_by")
    val behindBy: Int,
    @SerialName("total_commits")
    val totalCommits: Int,
    val commits: List<CommitData>,
    val files: List<CommitFile>? = null
)
