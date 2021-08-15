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
public data class BranchProtection(
    val url: String? = null,
    @SerialName("enabled")
    val isEnabled: Boolean? = null,
    @SerialName("required_status_checks")
    val requiredStatusChecks: StatusChecks? = null,
    @SerialName("enforce_admins")
    val enforceAdmins: UrlEnabledValue? = null,
    @SerialName("required_pull_request_reviews")
    val requiredPullRequestReviews: PullRequestReviews? = null,
    val restrictions: DismissalRestrictions? = null,
    @SerialName("required_linear_history")
    val requiredLinearHistory: WrappedBoolean? = null,
    @SerialName("allow_force_pushes")
    val allowForcePushes: WrappedBoolean? = null,
    @SerialName("allow_deletions")
    val allowDeletions: WrappedBoolean? = null,
    @SerialName("required_conversation_resolution")
    val requiredConversationResolution: WrappedBoolean? = null,
    val name: String? = null,
    @SerialName("protection_url")
    val protectionUrl: String? = null,
    @SerialName("required_signatures")
    val requiredSignatures: UrlEnabledValue? = null
)
