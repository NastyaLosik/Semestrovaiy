package com.example.applenews

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var articlesList: List<Article>
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        searchEditText = findViewById(R.id.edit)
        val recyclerView = findViewById<RecyclerView>(R.id.rView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(emptyList())
        recyclerView.adapter = newsAdapter

        loadArticles("https://newsapi.org/v2/everything?q=tesla&from=2024-11-23&sortBy=publishedAt&apiKey=aea293797bce41f6b4d4a55f24c02393")

        findViewById<Button>(R.id.search).setOnClickListener {
            val searchText = searchEditText.text.toString()
            Timber.d("Searching for: $searchText")

            if (searchText.isEmpty()) {
                newsAdapter.filterList(articlesList)
            } else {
                val filteredArticles = articlesList.filter { article ->
                    article.author != null && article.author.contains(searchText, ignoreCase = true)}
                newsAdapter.filterList(filteredArticles)
            }
        }
    }

    private fun loadArticles(url: String) {
        Thread {
            val request = Request.Builder().url(url).build()
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    Timber.d("JSON Response: $json")
                    if (json != null) {
                        try {
                            val articles = parseArticles(json)
                            runOnUiThread {
                                articlesList = articles
                                Timber.d("Number of articles: ${articles.size}")
                                newsAdapter.filterList(articlesList)
                            }
                        } catch (e: Exception) {
                            Timber.e(e, "Error parsing JSON")
                        }
                    }
                } else {
                    Timber.e("Failed to access file: ${response.message} - Code: ${response.code}")
                }
            } catch (e: IOException) {
                Timber.e(e, "Error loading articles")
            }
        }.start()
    }

    private fun parseArticles(json: String): List<Article> {
        val response = Gson().fromJson(json, NewsResponse::class.java)
        return response.articles
    }
}