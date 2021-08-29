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
public class CreateRepositoryFromTemplateRequestBuilder(
    public val name: String,
    public var owner: String? = null,
    public var description: String? = null,
    @SerialName("include_all_branches")
    public var includeAllBranches: Boolean? = null,
    public var private: Boolean? = null
)
