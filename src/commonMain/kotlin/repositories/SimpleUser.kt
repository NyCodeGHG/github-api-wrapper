package de.nycode.github.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents either a User or an Organization
 */
@Serializable
public data class SimpleUser(
    val name: String? = null,
    val email: String? = null,
    val login: String,
    val id: Int,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("gravatar_id")
    val gravatarId: String?,
    val url: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("followers_url")
    val followersUrl: String,
    @SerialName("following_url")
    val followingUrl: String,
    @SerialName("gists_url")
    val gistsUrl: String,
    @SerialName("starred_url")
    val starredUrl: String,
    @SerialName("subscriptions_url")
    val subscriptionsUrl: String,
    @SerialName("organizations_url")
    val organizationsUrl: String,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("events_url")
    val eventsUrl: String,
    @SerialName("received_events_url")
    val receivedEventsUrl: String,
    val type: UserType,
    @SerialName("site_admin")
    val isSiteAdmin: Boolean
)
