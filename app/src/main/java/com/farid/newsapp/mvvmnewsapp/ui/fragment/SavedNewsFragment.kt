package com.farid.newsapp.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farid.newsapp.R
import com.farid.newsapp.databinding.FragmentSavedNewsBinding

import com.farid.newsapp.mvvmnewsapp.adapters.NewsAdapter
import com.farid.newsapp.mvvmnewsapp.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment:Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    lateinit var _binding: FragmentSavedNewsBinding
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()


        newsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                R.id.action_savedNewsFragment2_to_articleNewsFragment2,
                bundle
            )
        }

        val itemTouchHelper = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position  = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view!!,"Successfully deleted article ",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }


        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles->
            newsAdapter.differ.submitList(articles)

        })
        return binding.root
    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
}