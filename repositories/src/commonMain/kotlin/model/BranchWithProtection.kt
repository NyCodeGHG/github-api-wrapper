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

package de.nycode.github.repositories.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BranchWithProtection(
    val name: String,
    val commit: CommitData,
    val _links: Map<String, String>,
    @SerialName("protected")
    val isProtected: Boolean,
    val protection: BranchProtection,
    @SerialName("protection_url")
    val protectionUrl: String,
    val pattern: String? = null,
    @SerialName("required_approving_review_count")
    val requiredApprovingReviewCount: Int? = null
)
