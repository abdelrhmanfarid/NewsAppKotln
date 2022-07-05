package com.farid.newsapp.mvvmnewsapp.repository

import com.farid.newsapp.mvvmnewsapp.api.RetrofitInstance
import com.farid.newsapp.mvvmnewsapp.dp.ArticleDatabase
import com.farid.newsapp.mvvmnewsapp.models.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)


    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)





    suspend fun upsert(article: Article) = db.getArticleDoa().upsert(article)
    fun getSavedArticles() = db.getArticleDoa().getAllArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDoa().deleteArticle(article)

}