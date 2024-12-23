package com.example.applenews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class NewsAdapter(private var articles: List<Article>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textAuthor: TextView = itemView.findViewById(R.id.textAuthor)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textPublished: TextView = itemView.findViewById(R.id.textPublished)
        val textContent: TextView = itemView.findViewById(R.id.textContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.textAuthor.text = articles[position].author
        holder.textTitle.text = articles[position].title
        holder.textPublished.text = articles[position].publishedAt
        holder.textContent.text = articles[position].content
    }

    override fun getItemCount(): Int {
        Timber.d("Number of articles: ${articles.size}")
        return articles.size
    }
    fun filterList(filteredArticles: List<Article>) {
        articles = filteredArticles
        notifyDataSetChanged()
    }
}