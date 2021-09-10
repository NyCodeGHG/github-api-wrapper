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

import dev.nycode.github.model.Color
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
internal class ColorTest {

    @Test
    fun parseColor() {
        val color = Color(0x2476d4)
        assertEquals(36, color.red)
        assertEquals(118, color.green)
        assertEquals(212, color.blue)
    }

    @Test
    fun serializeColor() {
        val color = Color(0x8328eb)
        val serialized = Json.encodeToString(color)
        assertEquals(""""8328eb"""", serialized)
    }

    @Test
    fun deserializeColor() {
        val serializedColor = """"8328eb""""
        assertEquals(Color(0x8328eb), Json.decodeFromString(serializedColor))
    }

}
