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

package de.nycode.github.repositories.organizations.request

import de.nycode.github.model.Visibility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Builder for configuring the [Create organization repository endpoint](https://docs.github.com/en/rest/reference/repos#create-an-organization-repository).
 *
 * @property name the name of the repository
 * @property description short description of the repository
 * @property homepage an url with more information about the repository
 * @property private if the new repo should be private. Default: false
 * @property visibility the visibility of the new repo
 * @property hasIssues if the new repo should have issues enabled. Default: true
 * @property hasProjects if the new repo should have projects enabled. Default: true
 * @property hasWiki if the new repo should have wiki enabled: Default: true
 * @property isTemplate if the new repo is a template: Default: false
 * @property teamId The id of the team that will be granted access to this repository. This is only valid when creating a repository in an organization
 * @property autoInit Pass true to create an initial commit with empty README
 * @property gitignoreTemplate Desired language or platform [.gitignore template](https://github.com/github/gitignore) to apply. Use the name of the template without the extension
 * @property licenseTemplate Choose an [open source license template](https://choosealicense.com/) that best suits your needs, and then use the [license keyword](https://help.github.com/articles/licensing-a-repository/#searching-github-by-license-type) as the [licenseTemplate] string. For example, "mit" or "mpl-2.0"
 * @property allowSquashMerge Either true to allow squash-merging pull requests, or false to prevent squash-merging. Default: true
 * @property allowMergeCommit Either true to allow merging pull requests with a merge commit, or false to prevent merging pull requests with merge commits. Default: true
 * @property allowRebaseMerge Either true to allow rebase-merging pull requests, or false to prevent rebase-merging. Default: true
 * @property allowAutoMerge Either true to allow auto-merge on pull requests, or false to disallow auto-merge. Default: false
 * @property deleteBranchOnMerge Either true to allow automatically deleting head branches when pull requests are merged, or false to prevent automatic deletion. Default: false
 */
@Serializable
public class CreateOrganizationRepositoryRequestBuilder(
    public val name: String,
    public var description: String? = null,
    public var homepage: String? = null,
    public var private: Boolean? = null,
    public var visibility: Visibility? = null,
    @SerialName("has_issues")
    public var hasIssues: Boolean? = null,
    @SerialName("has_projects")
    public var hasProjects: Boolean? = null,
    @SerialName("has_wiki")
    public var hasWiki: Boolean? = null,
    @SerialName("is_template")
    public var isTemplate: Boolean? = null,
    @SerialName("team_id")
    public var teamId: Int? = null,
    @SerialName("auto_init")
    public var autoInit: Boolean? = null,
    @SerialName("gitignore_template")
    public var gitignoreTemplate: String? = null,
    @SerialName("license_template")
    public var licenseTemplate: String? = null,
    @SerialName("allow_squash_merge")
    public var allowSquashMerge: Boolean? = null,
    @SerialName("allow_merge_commit")
    public var allowMergeCommit: Boolean? = null,
    @SerialName("allow_rebase_merge")
    public var allowRebaseMerge: Boolean? = null,
    @SerialName("allow_auto_merge")
    public var allowAutoMerge: Boolean? = null,
    @SerialName("delete_branch_on_merge")
    public var deleteBranchOnMerge: Boolean? = null
)
