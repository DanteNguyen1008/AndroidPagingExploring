package com.cat.domain.interfaces.interactor

import com.cat.domain.DataWrapper
import com.cat.domain.interfaces.states.InitializationState

interface IInitializationInteractor {
    suspend fun initialize() : DataWrapper<Any?, InitializationState>
}