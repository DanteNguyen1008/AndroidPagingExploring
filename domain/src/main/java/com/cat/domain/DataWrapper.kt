package com.cat.domain

import java.lang.Exception

data class DataWrapper<DATA, STATE>(private val data: DATA, private val state : STATE) {
    fun getData() = this.data
    fun getState() = this.state

    var exception : Exception? = null

    fun hasException() = exception != null
}