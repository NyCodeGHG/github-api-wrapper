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

package dev.nycode.github.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

/**
 * Represents a color as a hex value.
 */
@Serializable(with = Color.Serializer::class)
@JvmInline
public value class Color(public val hex: Int) {

    /**
     * Creates a Color from a hex int in a String.
     */
    public constructor(value: String) : this(value.toInt(16))

    /**
     * Gets the red part of the color.
     */
    public val red: Int
        get() = hex and 0xFF0000 shr 16

    /**
     * Gets the green part of the color.
     */
    public val green: Int
        get() = hex and 0xFF00 shr 8

    /**
     * Gets the blue part of the color.
     */
    public val blue: Int
        get() = hex and 0xFF

    override fun toString(): String = hex.toString(16)

    internal object Serializer : KSerializer<Color> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): Color {
            return Color(decoder.decodeString().toInt(16))
        }

        override fun serialize(encoder: Encoder, value: Color) {
            encoder.encodeString(value.hex.toString(16))
        }
    }
}
