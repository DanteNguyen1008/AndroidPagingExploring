package com.cat.domain.usecase

import com.cat.domain.DataWrapper
import com.cat.domain.entity.PagingPosts
import com.cat.domain.entity.request.LoadPostRequest
import com.cat.domain.interfaces.interactor.IPostInteractor
import com.cat.domain.interfaces.repository.IDataSource
import com.cat.domain.interfaces.states.LoadPostState
import org.koin.core.KoinComponent
import org.koin.core.inject

class PostInteractor : IPostInteractor, KoinComponent {

    private val dataSource: IDataSource by inject()

    override fun loadPosts(loadPostRequest: LoadPostRequest): DataWrapper<PagingPosts?, LoadPostState> {
        return try {
            DataWrapper(
                this.dataSource.loadPostBySubName(loadPostRequest = loadPostRequest),
                LoadPostState.SUCCESS
            )
        } catch (ex: Exception) {
            DataWrapper(null as PagingPosts?, LoadPostState.FAILED).also {
                it.exception =
                    ex
            }
        }
    }
}