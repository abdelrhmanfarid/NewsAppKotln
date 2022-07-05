package com.farid.newsapp.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.farid.newsapp.mvvmnewsapp.models.Article
import com.farid.newsapp.R

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)

    private val differCallBack = object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(findViewById(R.id.ivArticleImage))
            rootView.findViewById<TextView>(R.id.tvSource).text=article.source?.name
            rootView.findViewById<TextView>(R.id.tvTitle).text=article.title
            rootView.findViewById<TextView>(R.id.tvDescription).text=article.description
            rootView.findViewById<TextView>(R.id.tvPublishedAt).text=article.publishedAt

            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener:((Article) -> Unit)?=null

    fun setOnClickListener(listener:(Article) -> Unit){
        onItemClickListener = listener
    }
}