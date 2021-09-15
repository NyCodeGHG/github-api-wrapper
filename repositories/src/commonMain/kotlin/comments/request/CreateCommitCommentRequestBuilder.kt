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

package dev.nycode.github.repositories.comments.request

import kotlinx.serialization.Serializable

/**
 * Request Builder for the [Create a commit comment](https://docs.github.com/en/rest/reference/repos#create-a-commit-comment) endpoint.
 *
 * @property body The contents of the commit.
 * @property path Relative path of the file to comment on.
 * @property position Line index in the diff to comment on.
 * @property line Line number in the file to comment on.
 */
@Serializable
public class CreateCommitCommentRequestBuilder internal constructor(
    public val body: String,
    public var path: String? = null,
    public var position: Int? = null,
    @Deprecated(
        message = "Use position parameter instead. Line number in the file to comment on.",
        replaceWith = ReplaceWith("position")
    )
    public var line: Int? = null
)
