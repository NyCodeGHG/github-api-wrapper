package de.nycode.github.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class UserType {
    @SerialName("User")
    USER,

    @SerialName("Organization")
    ORGANIZATION
}
