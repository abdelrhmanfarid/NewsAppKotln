package com.farid.newsapp.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import com.farid.newsapp.mvvmnewsapp.ui.fragment.NewsViewModel
import com.farid.newsapp.R
import com.farid.newsapp.databinding.ActivityNewsBinding
import com.farid.newsapp.mvvmnewsapp.dp.ArticleDatabase
import com.farid.newsapp.mvvmnewsapp.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        //Log.i("Internet", viewModel.hasInternetConnection(this).toString())


        // binding.bottomNavigationView.setupWithNavController(binding.newsNavHostFragment.findNavController())
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        val navController = findNavController(R.id.bottomNavigationView)
//         bottomNavigationView.setupWithNavController(navController)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)


    }
}