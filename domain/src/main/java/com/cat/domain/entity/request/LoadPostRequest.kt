package com.cat.domain.entity.request

import kotlin.coroutines.CoroutineContext

data class LoadPostRequest(val subName : String, val coroutine : CoroutineContext)