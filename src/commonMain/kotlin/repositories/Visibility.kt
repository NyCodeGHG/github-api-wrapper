package de.nycode.github.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the visibility of a [Repository].
 *
 */
@Serializable
public enum class Visibility {

    @SerialName("public")
    PUBLIC,

    @SerialName("private")
    PRIVATE,

    @SerialName("internal")
    INTERNAL

}
