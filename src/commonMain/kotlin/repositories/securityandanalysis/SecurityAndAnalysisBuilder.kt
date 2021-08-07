package de.nycode.github.repositories.securityandanalysis

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class SecurityAndAnalysisBuilder(
    @SerialName("advanced_security")
    private var _advancedSecurity: SecurityAndAnalysisValue? = null,
    @SerialName("secret_scanning")
    private var _secretScanning: SecurityAndAnalysisValue? = null
) {
    public var advancedSecurity: Boolean?
        set(value) {
            _advancedSecurity = if (value != null) {
                SecurityAndAnalysisValue(FancyBoolean.valueOf(value))
            } else {
                null
            }
        }
        get() = _advancedSecurity?.status?.booleanValue

    public var secretScanning: Boolean?
        set(value) {
            _advancedSecurity = if (value != null) {
                SecurityAndAnalysisValue(FancyBoolean.valueOf(value))
            } else {
                null
            }
        }
        get() = _advancedSecurity?.status?.booleanValue
}