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

package dev.nycode.github.repositories.model

import dev.nycode.github.model.SimpleUser
import dev.nycode.github.model.VerySimpleCommit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CommitData(
    val sha: String,
    @SerialName("node_id")
    val nodeId: String,
    val commit: Commit,
    val url: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("comments_url")
    val commentsUrl: String,
    val author: SimpleUser,
    val committer: SimpleUser,
    val parents: List<VerySimpleCommit>
)
