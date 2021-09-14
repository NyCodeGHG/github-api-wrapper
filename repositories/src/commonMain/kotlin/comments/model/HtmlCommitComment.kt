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

package dev.nycode.github.repositories.comments.model

import dev.nycode.github.model.AuthorAssociation
import dev.nycode.github.model.SimpleUser
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class HtmlCommitComment(
    @SerialName("body_html")
    public val bodyHtml: String,
    @SerialName("html_url")
    override val htmlUrl: String,
    override val url: String,
    override val id: Int,
    @SerialName("node_id")
    override val nodeId: String,
    override val path: String?,
    override val position: Int?,
    override val line: Int?,
    @SerialName("commit_id")
    override val commitId: String,
    override val user: SimpleUser?,
    @SerialName("created_at")
    override val createdAt: Instant,
    @SerialName("updated_at")
    override val updatedAt: Instant,
    @SerialName("author_association")
    override val authorAssociation: AuthorAssociation
) : CommitComment()
