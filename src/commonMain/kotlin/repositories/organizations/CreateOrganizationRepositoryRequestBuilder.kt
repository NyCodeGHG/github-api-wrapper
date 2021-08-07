package de.nycode.github.repositories.organizations

import de.nycode.github.repositories.Visibility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
