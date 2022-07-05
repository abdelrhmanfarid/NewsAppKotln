package com.farid.newsapp.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

import com.farid.newsapp.R
import com.farid.newsapp.databinding.FragmentArticleBinding
import com.farid.newsapp.mvvmnewsapp.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {


    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()


    lateinit var _binding: FragmentArticleBinding
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }


        binding.fab.setOnClickListener{
            if(article.url==null){
                Toast.makeText(requireContext(),"null",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.saveArticle(article)
                Snackbar.make(view!!,"Article saved successfully",Snackbar.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }
}