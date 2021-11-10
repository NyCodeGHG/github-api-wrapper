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

package dev.nycode.github.repositories.contents.request

import dev.nycode.github.model.GitUserOptionalDate
import kotlinx.datetime.Instant

public class CreateFileContentsRequestBuilder(
    public val message: String,
    public val content: String,
    public var branch: String? = null,
    public var committer: GitUserOptionalDate? = null,
    public var author: GitUserOptionalDate? = null
) {
    /**
     * Sets the committer of this builder instance
     */
    public fun committer(name: String, email: String, date: Instant? = null) {
        this.committer = GitUserOptionalDate(name, email, date)
    }

    /**
     * Sets the author of this builder instance
     */
    public fun author(name: String, email: String, date: Instant? = null) {
        this.author = GitUserOptionalDate(name, email, date)
    }
}
