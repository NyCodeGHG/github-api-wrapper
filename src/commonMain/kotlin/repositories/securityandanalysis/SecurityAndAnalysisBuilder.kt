/*
 *    Copyright 2021 NyCode
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
