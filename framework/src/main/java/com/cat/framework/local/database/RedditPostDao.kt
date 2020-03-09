package com.cat.framework.local.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cat.framework.local.entities.RedditPost

@Dao
interface RedditPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRedditPosts(posts: List<RedditPost>)

    @Query("SELECT * FROM reddit_post WHERE subreddit = :subName ORDER BY indexInResponse")
    fun getRedditPostsBySub(subName : String) : DataSource.Factory<Int, RedditPost>

    @Query("DELETE FROM reddit_post WHERE subreddit = :subName")
    fun deleteRedditPostsBySub(subName: String)
}