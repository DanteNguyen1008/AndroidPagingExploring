package com.cat.domain.usecase

import android.content.Context
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
    private val context : Context by inject()

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

    override fun currentSubName(): String {
        val sharedPref = this.context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(PRE_SUB_NAME_NAME, DEFAULT_SUB_NAME) ?: DEFAULT_SUB_NAME
    }

    override fun setSubName(subName : String) {
        val sharedPref = this.context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE)
        sharedPref.edit()?.putString(PRE_SUB_NAME_NAME, subName)?.apply()
    }

    companion object {
        private const val DEFAULT_SUB_NAME = "gaming"
        private const val PRE_NAME = "REDDIT_HEX"
        private const val PRE_SUB_NAME_NAME = "SUB_NAME"
    }
}