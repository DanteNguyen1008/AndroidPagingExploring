package com.cat.framework.local

import com.cat.domain.entity.RedditPost
import com.cat.domain.interfaces.repository.IDataSource
import com.cat.framework.local.database.RedditDatabase
import org.koin.core.KoinComponent
import org.koin.core.inject

class DiskDataSource : IDataSource.IDiskDataSource, KoinComponent {

    private val database: RedditDatabase by inject()

    override fun loadPostsFromSubName(subName: String) =
        this.database.redditPostDao().getRedditPostsBySub(subName).map {
            it.toDomain()
        }

    override fun insertRedditPost(subName: String, newPosts: List<RedditPost>) {
        this.database.redditPostDao().addRedditPosts(newPosts.map { domainRedditPost ->
            domainRedditPost.toData(subName).also { dataRedditPost ->
                dataRedditPost.indexInResponse =
                    this.database.redditPostDao().getNextIndexInSub(subName = subName)
            }
        })
    }

    override fun deleteAllPostBySub(subName: String) {
        this.database.redditPostDao().deleteRedditPostsBySub(subName = subName)
    }
}