package com.cat.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.cat.domain.DataWrapper
import com.cat.domain.entity.PagingPosts
import com.cat.domain.entity.RedditPost
import com.cat.domain.entity.State
import com.cat.domain.entity.request.LoadPostRequest
import com.cat.domain.interfaces.interactor.IPostInteractor
import com.cat.domain.interfaces.states.LoadPostState
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import org.koin.core.inject

class ListViewModel : ViewModel(), KoinComponent {
    private val postInteractor: IPostInteractor by inject()

    val subNameTriggerLiveData: MutableLiveData<String> = MutableLiveData()

    private val getPostLiveData: LiveData<PagingPosts> =
        Transformations.map(subNameTriggerLiveData) { subName ->

            val getPostResult: DataWrapper<PagingPosts?, LoadPostState> = postInteractor.loadPosts(
                LoadPostRequest(
                    subName = subName,
                    coroutine = Dispatchers.IO
                )
            )

            when (getPostResult.getState()) {
                LoadPostState.SUCCESS -> getPostResult.getData()
                else -> null
            }
        }

    val postsLiveData: LiveData<PagedList<RedditPost>> =
        Transformations.switchMap(getPostLiveData) {
            it?.data
        }

    val refreshState: LiveData<State> =
        Transformations.switchMap(getPostLiveData) {
            it?.refreshState
        }

    val loadMoreState: LiveData<State> =
        Transformations.switchMap(getPostLiveData) {
            it?.loadMoreState
        }

    fun onLoad() {
        subNameTriggerLiveData.value = "gaming"
    }
}
