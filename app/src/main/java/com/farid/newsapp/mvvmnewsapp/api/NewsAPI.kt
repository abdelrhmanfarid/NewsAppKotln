package com.farid.newsapp.mvvmnewsapp.api

import com.farid.newsapp.mvvmnewsapp.models.NewsResponse
import com.farid.newsapp.util.Constants.Companion.API_KEY

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String="us",
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ):Response<NewsResponse>

// this method for searching in all the news
    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("query")
        searchQuery:String,
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ):Response<NewsResponse>
}