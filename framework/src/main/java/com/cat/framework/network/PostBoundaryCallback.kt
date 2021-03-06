package com.cat.framework.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.cat.domain.entity.RedditPost
import com.cat.domain.entity.State
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class PostBoundaryCallback(
    val subName: String,
    val coroutineContext: CoroutineContext,
    val networkPageSize: Int,
    val handleResponse: (String, List<RedditPost>) -> Unit,
    val onZeroLoad: suspend (String, Int) -> List<RedditPost>?,
    val onLoadMore: suspend (String, String, Int) -> List<RedditPost>?
) : PagedList.BoundaryCallback<RedditPost>() {
    val loadMoreState: MutableLiveData<State> = MutableLiveData()

    override fun onZeroItemsLoaded() {
        runBlocking(this.coroutineContext) {
            this@PostBoundaryCallback.loadMoreState.postValue(State.LOADING)
            val newPost: List<RedditPost>? = this@PostBoundaryCallback.onZeroLoad(
                subName,
                networkPageSize
            )

            if (newPost == null) {
                this@PostBoundaryCallback
                    .loadMoreState
                    .postValue(State.error("Refreshing reddit posts by subname ${this@PostBoundaryCallback.subName} failed!"))
                return@runBlocking
            }

            if (newPost.isEmpty()) {
                this@PostBoundaryCallback.loadMoreState.postValue(State.EMPTY)
                return@runBlocking
            }

            this@PostBoundaryCallback.handleResponse(subName, newPost)
            this@PostBoundaryCallback.loadMoreState.postValue(State.LOADED)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: RedditPost) {
        runBlocking(this.coroutineContext) {
            this@PostBoundaryCallback.loadMoreState.postValue(State.LOADING)
            val morePosts: List<RedditPost>? = this@PostBoundaryCallback.onLoadMore(
                subName,
                itemAtEnd.name,
                networkPageSize
            )

            if (morePosts == null) {
                this@PostBoundaryCallback
                    .loadMoreState
                    .postValue(State.error("Loading more reddit posts by subname ${this@PostBoundaryCallback.subName} failed!"))
                return@runBlocking
            }

            if (morePosts.isEmpty()) {
                this@PostBoundaryCallback.loadMoreState.postValue(State.EMPTY)
                return@runBlocking
            }

            this@PostBoundaryCallback.handleResponse(subName, morePosts)
            this@PostBoundaryCallback.loadMoreState.postValue(State.LOADED)
        }
    }
}