package com.cat.framework.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cat.framework.local.entities.RedditPost

@Database(
    entities = [RedditPost::class],
    version = 1,
    exportSchema = false
)
abstract class RedditDatabase : RoomDatabase() {
    companion object {
        fun create(context: Context) =
            Room.databaseBuilder(context, RedditDatabase::class.java, "reddit.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    abstract fun redditPostDao(): RedditPostDao
}