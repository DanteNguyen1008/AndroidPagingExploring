package com.cat.framework.network

import com.cat.domain.entity.RedditPost
import com.cat.domain.interfaces.repository.IDataSource
import com.cat.framework.network.entities.ListingResponse

class RedditNetworkDataSource : IDataSource.INetworkDataSource {
    private val redditAPI: RedditApi by lazy { RedditApi.create() }

    override suspend fun loadPostsFromSubName(subName: String, loadSize: Int): List<RedditPost>? {
        val result: ListingResponse = redditAPI.getTop(subreddit = subName, limit = loadSize)
        return result.data.children.map {
            it.data.toDomain()
        }
    }

    override suspend fun loadMorePostsFromSubName(
        subName: String,
        after: String,
        loadSize: Int
    ): List<RedditPost>? {
        val result: ListingResponse =
            redditAPI.getTopAfter(subreddit = subName, after = after, limit = loadSize)
        return result.data.children.map {
            it.data.toDomain()
        }
    }
}