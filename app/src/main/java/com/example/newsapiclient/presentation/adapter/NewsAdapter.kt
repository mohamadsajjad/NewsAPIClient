package com.example.newsapiclient.presentation.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapiclient.R
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.databinding.NewsListItemBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)
    private var clickEvent: ((Article) -> Unit)? = null

    fun setClickEvent(clickEvent: (Article) -> Unit) {
        this.clickEvent = clickEvent
    }

    inner class MyViewHolder(val binding: NewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            NewsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.binding.tvTitle.text = article.title
        holder.binding.tvDescription.text = article.description
        holder.binding.tvPublishedAt.text = article.publishedAt
        holder.binding.tvSource.text = article.source?.name
        Glide.with(holder.binding.ivArticleImage.context)
            .load(article.urlToImage)
            .error(R.drawable.ic_close_24)
            .into(holder.binding.ivArticleImage)
        holder.binding.root.setOnClickListener {
            clickEvent?.let { it1 -> it1(article) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}