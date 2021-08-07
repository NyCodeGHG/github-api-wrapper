package de.nycode.github.repositories.securityandanalysis

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class FancyBoolean(public val booleanValue: Boolean) {

    @SerialName("enabled")
    ENABLED(true),

    @SerialName("disabled")
    DISABLED(false);

    public companion object {
        public fun valueOf(value: Boolean): FancyBoolean {
            return values().first { it.booleanValue == value }
        }
    }
}