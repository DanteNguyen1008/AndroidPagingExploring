package com.cat.framework.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cat.framework.local.entities.RedditPost.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RedditPost(
    @PrimaryKey
    val name: String,
    val title: String,
    val score: Int,
    val author: String,
    val subreddit: String,
    val num_comments: Int,
    val created: Long,
    val thumbnail: String?,
    val url: String?
) {
    // to be consistent w/ changing backend order, we need to keep a data like this
    var indexInResponse: Int = -1

    companion object {
        const val TABLE_NAME = "reddit_post"
    }
}