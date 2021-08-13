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

package de.nycode.github.request

import io.ktor.client.request.HttpRequestBuilder

public typealias SimplePaginatedRequestBuilder<T> = PaginatedRequestBuilder<List<T>, T>

public open class PaginatedRequestBuilder<T, R>(
    public var perPage: Int = 20,
    private val requests: MutableList<HttpRequestBuilder.() -> Unit> = mutableListOf(),
    public var mapper: (T) -> List<R> = {
        @Suppress("UNCHECKED_CAST") // it's fine trust me ;)
        it as? List<R> ?: error("T has to be list for the default mapper")
    }
) {
    public operator fun component1(): Int = perPage
    public operator fun component2(): List<HttpRequestBuilder.() -> Unit> = requests
    public operator fun component3(): (T) -> List<R> = mapper

    public fun request(builder: HttpRequestBuilder.() -> Unit) {
        requests += builder
    }

    public fun mapper(mapper: (T) -> List<R>) {
        this.mapper = mapper
    }
}
