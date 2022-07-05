package com.farid.newsapp.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.farid.newsapp.mvvmnewsapp.adapters.NewsAdapter
import com.farid.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.farid.newsapp.util.Resource
import com.farid.newsapp.R
import com.farid.newsapp.databinding.FragmentBreakingNewsBinding
import com.farid.newsapp.mvvmnewsapp.ui.NewsActivity

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    lateinit var _binding: FragmentBreakingNewsBinding
    private val binding get() = _binding!!

    val TAG = "BreakingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)


        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleNewsFragment2,
                bundle
            )
        }

        viewModel.breakingNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        // notify the recycler if that is the last page or not
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages

                        if(isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {
                        Log.e("error",it)
                    }
                }

                is Resource.Loading ->{
                    showProgressBar()
                }
            }

        })

        return binding.root
    }




    var isLoading = false
    var isLastPage = false
    var isScrolling= false

    val scrollListener = object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling =true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            //calculations to know if we scrolled to the bottom of the recycler or not
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLastPage && !isLoading
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >=0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling=false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }


    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar .visibility = View.VISIBLE
        isLoading=true
    }

}






//    private fun setupRecyclerView() {
//        newsAdapter = NewsAdapter()
//       view!!.findViewById<RecyclerView>(R.id.rvBreakingNews) .apply {
//            adapter = newsAdapter
//            layoutManager = LinearLayoutManager(activity)
//        }
//    }
