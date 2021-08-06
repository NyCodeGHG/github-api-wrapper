package de.nycode.github.repositories

import kotlinx.serialization.Serializable

@Serializable
public data class RepositoryPermissions(
    val admin: Boolean,
    val push: Boolean,
    val pull: Boolean,
    val maintain: Boolean? = null,
    val triage: Boolean? = null
)
