package de.nycode.github.repositories.securityandanalysis

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class SecurityAndAnalysisValue(public val status: FancyBoolean)
