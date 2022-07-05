package com.farid.newsapp.mvvmnewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.farid.newsapp.mvvmnewsapp.repository.NewsRepository
import com.farid.newsapp.mvvmnewsapp.ui.fragment.NewsViewModel

class NewsViewModelProviderFactory(
    val app :Application,
    val newsRepository: NewsRepository
    ):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository) as T
    }
}