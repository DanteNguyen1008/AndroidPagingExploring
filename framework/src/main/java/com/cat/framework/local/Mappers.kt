package com.cat.framework.local

import com.cat.framework.local.entities.RedditPost

fun RedditPost.toDomain() = com.cat.domain.entity.RedditPost(
    name = this.name,
    subreddit = this.subreddit,
    author = this.author,
    created = this.created,
    num_comments = this.num_comments,
    score = this.score,
    thumbnail = this.thumbnail,
    title = this.title,
    url = this.url
).also {
    it.indexInResponse = this.indexInResponse
}

fun com.cat.domain.entity.RedditPost.toData(subName : String) = RedditPost(
    name = this.name,
    subreddit = subName,
    author = this.author,
    created = this.created,
    num_comments = this.num_comments,
    score = this.score,
    thumbnail = this.thumbnail,
    title = this.title,
    url = this.url
).also {
    it.indexInResponse = this.indexInResponse
}