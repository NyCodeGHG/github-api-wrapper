package dev.nycode.github.repositories.model

import kotlinx.serialization.Serializable

/**
 * @property strict Require branches to be up-to-date before merging.
 * @property contexts The list of status checks to require in order to merge into this branch
 */
@Serializable
public data class SimpleStatusChecks(
    val strict: Boolean,
    val contexts: List<String>
)
