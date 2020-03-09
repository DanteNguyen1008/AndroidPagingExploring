package com.cat.framework.network.entities

import com.cat.framework.network.RedditApi

data class ListingData(
    val children: List<RedditChildrenResponse>,
    val after: String?,
    val before: String?
)
