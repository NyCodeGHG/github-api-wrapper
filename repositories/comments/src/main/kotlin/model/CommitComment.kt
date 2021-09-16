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
public sealed class CommitComment {

    @SerialName("html_url")
    public abstract val htmlUrl: String
    public abstract val url: String
    public abstract val id: Int

    @SerialName("node_id")
    public abstract val nodeId: String
    public abstract val path: String?
    public abstract val position: Int?
    public abstract val line: Int?

    @SerialName("commit_id")
    public abstract val commitId: String
    public abstract val user: SimpleUser?

    @SerialName("created_at")
    public abstract val createdAt: Instant

    @SerialName("updated_at")
    public abstract val updatedAt: Instant

    @SerialName("author_association")
    public abstract val authorAssociation: AuthorAssociation

}
