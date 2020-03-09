package com.cat.framework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.cat.domain.entity.PagingPosts
import com.cat.domain.entity.RedditPost
import com.cat.domain.entity.State
import com.cat.domain.entity.request.LoadPostRequest
import com.cat.domain.interfaces.repository.IDataSource
import com.cat.framework.network.PostBoundaryCallback
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

class DataSource : IDataSource, KoinComponent {

    private val diskDataSource: IDataSource.IDiskDataSource by inject()
    private val networkDataSource: IDataSource.INetworkDataSource by inject()

    override fun loadPostBySubName(loadPostRequest: LoadPostRequest): PagingPosts {
        val boundaryCallback: PostBoundaryCallback = PostBoundaryCallback(
            subName = loadPostRequest.subName,
            coroutineContext = loadPostRequest.coroutine,
            networkPageSize = PAGE_SIZE,
            handleResponse = this::insertToLocalDatabase,
            onLoadMore = this::loadMorePosts,
            onZeroLoad = this::loadFromZero
        )

        val refreshTrigger: MutableLiveData<Unit> = MutableLiveData<Unit>()
        val refreshStateLiveData: LiveData<State> = Transformations.switchMap(refreshTrigger) {
            this@DataSource.getPostBySubName(loadPostRequest)
        }

        val postLiveData: LiveData<PagedList<RedditPost>> = LivePagedListBuilder(
            this.diskDataSource.loadPostsFromSubName(loadPostRequest.subName),
            PagedList.Config
                .Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(15)
                .build()
        ).setBoundaryCallback(boundaryCallback).build()

        // Building Paging DataSource
        return PagingPosts(
            data = postLiveData,
            loadMoreState = boundaryCallback.loadMoreState,
            refresh = {
                // Trigger the refresh live data
                refreshTrigger.value = null
            },
            refreshState = refreshStateLiveData
        )
    }

    private suspend fun loadFromZero(subName: String, loadSize: Int) =
        this@DataSource.networkDataSource.loadPostsFromSubName(
            subName,
            loadSize

        )

    private suspend fun loadMorePosts(
        subName: String,
        after: String,
        loadSize: Int
    ) = this.networkDataSource.loadMorePostsFromSubName(subName, after, loadSize)

    private fun getPostBySubName(loadPostRequest: LoadPostRequest): LiveData<State> {
        // This LiveData emit states of get post by sub name process
        val getPostBySubNameState: MutableLiveData<State> = MutableLiveData()
        // launch coroutine blocking
        runBlocking(loadPostRequest.coroutine) {
            getPostBySubNameState.postValue(State.LOADING)
            // TODO get more accurate error
            val newPosts: List<RedditPost>? =
                this@DataSource.networkDataSource.loadPostsFromSubName(
                    loadPostRequest.subName,
                    PAGE_SIZE
                )

            if (newPosts == null) {
                getPostBySubNameState.postValue(State.error("Refreshing reddit posts by subname ${loadPostRequest.subName} failed!"))
                return@runBlocking
            }

            if (newPosts.isEmpty()) {
                getPostBySubNameState.postValue(State.EMPTY)
                return@runBlocking
            }

            // 1st, Delete all posts belong to the requested sub name
            this@DataSource.deletePostBySubNameInLocalDatabase(loadPostRequest.subName)
            // 2nd, insert the new post with the requested sub name
            this@DataSource.insertToLocalDatabase(loadPostRequest.subName, newPosts)
            getPostBySubNameState.postValue(State.LOADED)
        }

        return getPostBySubNameState
    }

    private fun deletePostBySubNameInLocalDatabase(subName: String) {

    }

    private fun insertToLocalDatabase(subName: String, newRedditPosts: List<RedditPost>) {
        this.diskDataSource.insertRedditPost(subName = subName, newPosts = newRedditPosts)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}