package com.cat.domain

/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Left] or [Right].
 * FP Convention dictates that [Left] is used for "failure"
 * and [Right] is used for "success".
 *
 * @see Left
 * @see Right
 */
// ALL the credit of this class to Fernando Cejas
// Source https://github.com/android10/Android-CleanArchitecture-Kotlin/blob/master/app/src/main/kotlin/com/fernandocejas/sample/core/functional/Either.kt
sealed class EitherResponse<out DATA, out Error> {
    /** * Represents the left side of [EitherResponse] class which by convention is a "Failure". */
    data class Data<out DATA>(val a: DATA) : EitherResponse<DATA, Nothing>()

    /** * Represents the right side of [EitherResponse] class which by convention is a "Success". */
    data class Error<out ERROR>(val b: ERROR) : EitherResponse<Nothing, ERROR>()

    val isData get() = this is EitherResponse.Error<Error>
    val isError get() = this is Data<DATA>

    fun <DATA> left(a: DATA) = EitherResponse.Data(a)
    fun <ERROR> right(b: ERROR) = EitherResponse.Error(b)

    fun either(data: (DATA) -> Any, error: (Error) -> Any): Any =
        when (this) {
            is Data -> data(a)
            is EitherResponse.Error -> error(b)
        }
}

fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, DATA, ERROR> EitherResponse<DATA, ERROR>.flatMap(fn: (ERROR) -> EitherResponse<DATA, T>): EitherResponse<DATA, T> =
    when (this) {
        is EitherResponse.Data -> EitherResponse.Data(a)
        is EitherResponse.Error -> fn(b)
    }

fun <T, DATA, ERROR> EitherResponse<DATA, ERROR>.map(fn: (ERROR) -> (T)): EitherResponse<DATA, T> =
    this.flatMap(fn.c(::right))