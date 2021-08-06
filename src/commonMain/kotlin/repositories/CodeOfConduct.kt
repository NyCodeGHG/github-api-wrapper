package de.nycode.github.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeOfConduct(
    val key: String,
    val name: String,
    val url: String,
    val body: String,
    @SerialName("html_url")
    val htmlUrl: String?
)
