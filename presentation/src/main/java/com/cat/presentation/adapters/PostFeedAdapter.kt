package com.cat.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cat.domain.entity.RedditPost
import com.cat.presentation.R
import com.facebook.drawee.view.SimpleDraweeView


class PostFeedAdapter : PagedListAdapter<RedditPost, PostFeedAdapter.ViewHolder>(DIFFER) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.onBind(getItem(position))

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        private val tvScore : AppCompatTextView = v.findViewById(R.id.tv_score)
        private val tvAuthor : AppCompatTextView = v.findViewById(R.id.tv_author)
        private val sdvThumbnail : SimpleDraweeView = v.findViewById(R.id.sdv_thumbnail)
        private val tvTitle : AppCompatTextView = v.findViewById(R.id.tv_title)

        fun onBind(item: RedditPost?) {
            if (item == null) return
            tvScore.text = item.score.toString()
            tvAuthor.text = String.format(v.context.resources.getString(R.string.created_by), item.author)
            tvTitle.text = item.title

            if (item.thumbnail.isNullOrEmpty() || item.thumbnail == "self") {
                sdvThumbnail.visibility = View.GONE
            } else {
                sdvThumbnail.visibility = View.VISIBLE
                val uri: Uri = Uri.parse(item.thumbnail)
                sdvThumbnail.setImageURI(uri)
            }
        }
    }

    companion object {
        val DIFFER = object : DiffUtil.ItemCallback<RedditPost>() {
            override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean =
                oldItem.name == newItem.name
        }
    }
}