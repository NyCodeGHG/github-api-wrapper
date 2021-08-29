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

package dev.nycode.github.repositories.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class CreateRepositoryForAuthenticatedUserRequestBuilder(
    public val name: String,
    public var description: String? = null,
    public var homepage: String? = null,
    public var private: Boolean? = null,
    @SerialName("has_issues")
    public var hasIssues: Boolean? = null,
    @SerialName("has_projects")
    public var hasProjects: Boolean? = null,
    @SerialName("has_wiki")
    public var hasWiki: Boolean? = null,
    @SerialName("team_id")
    public var teamId: Int? = null,
    @SerialName("auto_init")
    public var autoInit: Boolean? = null,
    @SerialName("gitignore_template")
    public var gitignoreTemplate: Boolean? = null,
    @SerialName("license_template")
    public var licenseTemplate: Boolean? = null,
    @SerialName("allow_squash_merge")
    public var allowSquashMerge: Boolean? = null,
    @SerialName("allow_merge_commit")
    public var allowMergeCommit: Boolean? = null,
    @SerialName("allow_rebase_merge")
    public var allowRebaseMerge: Boolean? = null,
    @SerialName("allow_auto_merge")
    public var allowAutoMerge: Boolean? = null,
    @SerialName("delete_branch_on_merge")
    public var deleteBranchOnMerge: Boolean? = null,
    @SerialName("has_downloads")
    public var hasDownloads: Boolean? = null,
    @SerialName("is_template")
    public var isTemplate: Boolean? = null
)
