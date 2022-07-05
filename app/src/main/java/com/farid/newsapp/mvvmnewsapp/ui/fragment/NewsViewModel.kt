package com.farid.newsapp.mvvmnewsapp.ui.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farid.newsapp.mvvmnewsapp.NewsApplication
import com.farid.newsapp.mvvmnewsapp.models.Article
import com.farid.newsapp.mvvmnewsapp.models.NewsResponse
import com.farid.newsapp.mvvmnewsapp.repository.NewsRepository
import com.farid.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(var context: Context,val newsRepository: NewsRepository) : ViewModel() {

    val breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakinNewsResponse: NewsResponse? = null


    val searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNewsLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
                Log.i("breaking", response.toString())
            }else{
                breakingNewsLiveData.postValue(Resource.Error("No internet connection"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException -> breakingNewsLiveData.postValue(Resource.Error("Network failure"))
                else ->breakingNewsLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }

    }


    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsLiveData.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNewsLiveData.postValue(handleSearchNewsResponse(response))
        Log.i("searchNews", response.toString())
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakinNewsResponse == null) {
                    breakinNewsResponse = resultResponse
                } else {
                    val oldArticles = breakinNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakinNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }




    // check that the user is connected to the internet or not
     private fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork?:return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI)-> true
                capabilities.hasTransport(TRANSPORT_CELLULAR)-> true
                capabilities.hasTransport(TRANSPORT_ETHERNET)-> true
                else ->false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run{
                return when(type){
                    TYPE_WIFI->true
                    TRANSPORT_ETHERNET->true
                    TYPE_ETHERNET->true
                    else ->false
                }
            }
        }
        return false
    }



}