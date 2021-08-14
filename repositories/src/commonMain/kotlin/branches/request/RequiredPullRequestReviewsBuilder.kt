package de.nycode.github.repositories.branches.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class RequiredPullRequestReviewsBuilder(
    @SerialName("dismissal_restrictions ")
    public var dismissalRestrictions: DismissalRestrictionsBuilder = DismissalRestrictionsBuilder(),
    @SerialName("dismiss_stale_reviews")
    public var dismissStaleReviews: Boolean,
    @SerialName("require_code_owner_reviews")
    public var requireCodeOwnerReviews: Boolean,
    @SerialName("required_approving_review_count")
    public var requiredApprovingReviewCount: Int
)
