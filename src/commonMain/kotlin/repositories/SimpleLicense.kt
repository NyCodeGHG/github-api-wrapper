package de.nycode.github.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SimpleLicense(
    val key: String,
    val name: String,
    val url: String?,
    @SerialName("spdx_id")
    val spdxId: String?,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("html_url")
    val htmlUrl: String? = null
)
