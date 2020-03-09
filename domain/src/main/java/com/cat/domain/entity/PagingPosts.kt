package com.cat.domain.entity

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagingPosts(
    val data : LiveData<PagedList<RedditPost>>,
    val refreshState : LiveData<State>,
    val loadMoreState : LiveData<State>,
    val refresh: () -> Unit
)

