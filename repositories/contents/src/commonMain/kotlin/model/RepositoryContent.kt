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

package dev.nycode.github.repositories.contents.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a directory, file, symlink or submodule in a GitHub repository.
 * Use the corresponding subclass.
 */
@Serializable
public sealed class RepositoryContent {
    public abstract val links: RepositoryContentLinks
    public abstract val gitUrl: String?
    public abstract val htmlUrl: String?
    public abstract val downloadUrl: String?
    public abstract val name: String
    public abstract val path: String
    public abstract val sha: String
    public abstract val size: Int
    public abstract val url: String
}

@Serializable
@SerialName("dir")
public class DirectoryRepositoryResponse(
    @SerialName("_links")
    override val links: RepositoryContentLinks,
    @SerialName("git_url")
    override val gitUrl: String?,
    @SerialName("html_url")
    override val htmlUrl: String?,
    @SerialName("download_url")
    override val downloadUrl: String?,
    override val name: String,
    override val path: String,
    override val sha: String,
    override val size: Int,
    override val url: String,
    public val entries: List<RepositoryContentValue>
) : RepositoryContent()

@Serializable
@SerialName("file")
public class FileRepositoryContent(
    @SerialName("_links")
    override val links: RepositoryContentLinks,
    @SerialName("git_url")
    override val gitUrl: String?,
    @SerialName("html_url")
    override val htmlUrl: String?,
    @SerialName("download_url")
    override val downloadUrl: String?,
    override val name: String,
    override val path: String,
    override val sha: String,
    override val size: Int,
    override val url: String,
    public val content: String,
    public val encoding: String
) : RepositoryContent()

@Serializable
@SerialName("symlink")
public class SymlinkRepositoryContent(
    @SerialName("_links")
    override val links: RepositoryContentLinks,
    @SerialName("git_url")
    override val gitUrl: String?,
    @SerialName("html_url")
    override val htmlUrl: String?,
    @SerialName("download_url")
    override val downloadUrl: String?,
    override val name: String,
    override val path: String,
    override val sha: String,
    override val size: Int,
    override val url: String,
    public val target: String
) : RepositoryContent()

@Serializable
@SerialName("submodule")
public class SubmoduleRepositoryContent(
    @SerialName("_links")
    override val links: RepositoryContentLinks,
    @SerialName("git_url")
    override val gitUrl: String?,
    @SerialName("html_url")
    override val htmlUrl: String?,
    @SerialName("download_url")
    override val downloadUrl: String?,
    override val name: String,
    override val path: String,
    override val sha: String,
    override val size: Int,
    override val url: String,
    @SerialName("submodule_git_url")
    public val submoduleGitUrl: String
) : RepositoryContent()

@Serializable
public sealed class RepositoryContentValue {
    public abstract val links: RepositoryContentLinks
    public abstract val gitUrl: String?
    public abstract val htmlUrl: String?
    public abstract val downloadUrl: String?
    public abstract val name: String
    public abstract val path: String
    public abstract val sha: String
    public abstract val size: Int
    public abstract val url: String
}

@Serializable
@SerialName("dir")
public class DirectoryRepositoryValue(
    @SerialName("_links")
    override val links: RepositoryContentLinks,
    @SerialName("git_url")
    override val gitUrl: String?,
    @SerialName("html_url")
    override val htmlUrl: String?,
    @SerialName("download_url")
    override val downloadUrl: String?,
    override val name: String,
    override val path: String,
    override val sha: String,
    override val size: Int,
    override val url: String
) : RepositoryContentValue()

@Serializable
@SerialName("file")
public class FileRepositoryContentValue(
    @SerialName("_links")
    override val links: RepositoryContentLinks,
    @SerialName("git_url")
    override val gitUrl: String?,
    @SerialName("html_url")
    override val htmlUrl: String?,
    @SerialName("download_url")
    override val downloadUrl: String?,
    override val name: String,
    override val path: String,
    override val sha: String,
    override val size: Int,
    override val url: String
) : RepositoryContentValue()

@Serializable
@SerialName("symlink")
public class SymlinkRepositoryContentValue(
    @SerialName("_links")
    override val links: RepositoryContentLinks,
    @SerialName("git_url")
    override val gitUrl: String?,
    @SerialName("html_url")
    override val htmlUrl: String?,
    @SerialName("download_url")
    override val downloadUrl: String?,
    override val name: String,
    override val path: String,
    override val sha: String,
    override val size: Int,
    override val url: String,
    public val target: String
) : RepositoryContentValue()

@Serializable
@SerialName("submodule")
public class SubmoduleRepositoryContentValue(
    @SerialName("_links")
    override val links: RepositoryContentLinks,
    @SerialName("git_url")
    override val gitUrl: String?,
    @SerialName("html_url")
    override val htmlUrl: String?,
    @SerialName("download_url")
    override val downloadUrl: String?,
    override val name: String,
    override val path: String,
    override val sha: String,
    override val size: Int,
    override val url: String,
    @SerialName("submodule_git_url")
    public val submoduleGitUrl: String
) : RepositoryContentValue()
