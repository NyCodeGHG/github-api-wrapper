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
