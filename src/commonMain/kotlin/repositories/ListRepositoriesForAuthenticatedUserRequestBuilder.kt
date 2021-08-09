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

package de.nycode.github.repositories

import de.nycode.github.request.RepositorySort
import de.nycode.github.request.SortDirection
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class ListRepositoriesForAuthenticatedUserRequestBuilder(
    public var visibility: Visibility? = null,
    public val affiliation: MutableList<Affiliation>? = null,
    public var type: Type? = null,
    public var sort: RepositorySort? = null,
    public var direction: SortDirection? = null,
    public var page: Int? = null,
    public var perPage: Int? = null,
    public var since: Instant? = null,
    public var before: Instant? = null
) {
    @Serializable
    public enum class Type {
        @SerialName("all")
        ALL,

        @SerialName("owner")
        OWNER,

        @SerialName("public")
        PUBLIC,

        @SerialName("private")
        PRIVATE,

        @SerialName("member")
        MEMBER
    }
}
