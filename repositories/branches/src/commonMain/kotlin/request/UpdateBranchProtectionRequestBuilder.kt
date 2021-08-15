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

package de.nycode.github.repositories.branches.request

import de.nycode.github.repositories.model.SimpleStatusChecks
import kotlinx.serialization.SerialName
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public class UpdateBranchProtectionRequestBuilder internal constructor(
    @SerialName("required_status_checks")
    public var requiredStatusChecks: SimpleStatusChecks?,
    @SerialName("enforce_admins")
    public var enforceAdmins: Boolean?,
    @SerialName("required_pull_request_rewviews")
    internal var requiredPullRequestReviews: RequiredPullRequestReviewsBuilder?,
    internal var restrictions: RestrictionsBuilder?,
    @SerialName("required_linear_history")
    public var requiredLinearHistory: Boolean,
    @SerialName("allow_force_pushes")
    public var allowForcePushes: Boolean? = null,
    @SerialName("allow_deletions")
    public var allowDeletions: Boolean,
    @SerialName("required_conversation_resolution")
    public var requiredConversationResolution: Boolean
) {
    public fun requiredPullRequestReviews(
        dismissStaleReviews: Boolean,
        requireCodeOwnerReviews: Boolean,
        requiredApprovingReviewCount: Int,
        builder: RequiredPullRequestReviewsBuilder.() -> Unit
    ) {
        contract {
            callsInPlace(builder, InvocationKind.AT_MOST_ONCE)
        }
        require(requiredApprovingReviewCount in IntRange(1, 6)) { "requiredApprovingReviewCount must be in 1..6" }
        requiredPullRequestReviews = RequiredPullRequestReviewsBuilder(
            dismissStaleReviews = dismissStaleReviews,
            requireCodeOwnerReviews = requireCodeOwnerReviews,
            requiredApprovingReviewCount = requiredApprovingReviewCount
        ).apply(builder)
    }

    public fun restrictions(builder: RestrictionsBuilder.() -> Unit) {
        restrictions = RestrictionsBuilder(mutableListOf(), mutableListOf(), mutableListOf()).apply(builder)
    }
}
