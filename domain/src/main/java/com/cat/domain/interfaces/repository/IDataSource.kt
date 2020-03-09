package com.cat.domain.interfaces.repository

import androidx.paging.DataSource
import com.cat.domain.entity.PagingPosts
import com.cat.domain.entity.RedditPost
import com.cat.domain.entity.request.LoadPostRequest

interface IDataSource {
    fun loadPostBySubName(loadPostRequest : LoadPostRequest) : PagingPosts

    interface IDiskDataSource {
        fun loadPostsFromSubName(subName : String) : DataSource.Factory<Int, RedditPost>
        fun insertRedditPost(subName: String, newPosts: List<RedditPost>)
    }

    interface INetworkDataSource {
        suspend fun loadPostsFromSubName(subName : String, loadSize : Int) : List<RedditPost>?
        suspend fun loadMorePostsFromSubName(subName: String, after: String, loadSize: Int): List<RedditPost>?
    }
}