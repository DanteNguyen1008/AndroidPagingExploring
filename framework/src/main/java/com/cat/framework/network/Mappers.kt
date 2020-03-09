package com.cat.framework.network

import com.cat.framework.network.entities.RedditPost

fun RedditPost.toDomain() = com.cat.domain.entity.RedditPost(
    this.name,
    this.title,
    this.score,
    this.author,
    this.subreddit,
    this.num_comments,
    this.created,
    this.thumbnail,
    this.url
).also { domainRedditPost ->
    domainRedditPost.indexInResponse = this.indexInResponse
}