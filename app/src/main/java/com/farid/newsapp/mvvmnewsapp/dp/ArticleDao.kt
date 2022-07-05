package com.farid.newsapp.mvvmnewsapp.dp

import androidx.lifecycle.LiveData
import androidx.room.*
import com.farid.newsapp.mvvmnewsapp.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM Article")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}