package com.example.newsapiclient.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapiclient.R
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.databinding.FragmentNewsBinding
import com.example.newsapiclient.presentation.activity.MainActivity
import com.example.newsapiclient.presentation.adapter.NewsAdapter
import com.example.newsapiclient.presentation.viewModel.NewsViewModel


class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentNewsBinding
    private lateinit var adapter: NewsAdapter

    private var country = "us"
    private var category = "sport"
    private var page = 1
    private var isScrolling = false
    private var isLoading = false
    private var isLastPage = false
    private var pages = 0
    private var isSearching = false
    private var keyword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        adapter = (activity as MainActivity).adapter
        initRecyclerView()
        viewNewsList()
        setSearchView()
    }

    private fun viewNewsList() {
        viewModel.getNewsHeadlines(country, category, page)
        viewModel.newsHeadlines.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { apiResponse ->
                        val newListTempVar = if (page == 1)
                            apiResponse.articles
                        else
                            adapter.differ.currentList + apiResponse.articles
                        adapter.differ.submitList(newListTempVar)
                        pages = if (apiResponse.totalResults % 20 == 0) {
                            apiResponse.totalResults / 20
                        } else {
                            apiResponse.totalResults / 20 + 1
                        }
                        isLastPage = page == pages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { errorMessage ->
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun setSearchView() {
        binding.imgSearch.setOnClickListener {
            if (!isSearching){
                isSearching = true
                binding.edtSearch.apply {
                    visibility = View.VISIBLE
                    animate().alpha(1f).setDuration(400).withEndAction {
                        binding.imgSearch.setImageResource(R.drawable.ic_close_24)
                    }.start()
                }
            }else{
                isSearching = false
                binding.edtSearch.apply {
                    visibility = View.GONE
                    animate().alpha(0f).setDuration(400).withEndAction {
                        binding.imgSearch.setImageResource(R.drawable.ic_search_24)
                    }.start()
                }
                page =1
                viewModel.getNewsHeadlines(country,category,page)
            }
        }
        binding.edtSearch.setOnEditorActionListener(OnEditorActionListener { tv, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                page = 1
                keyword = tv.text.toString()
                viewModel.getSearchedNews(keyword, page = page)
                viewSearchList()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun viewSearchList() {
        viewModel.searchedNews.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { apiResponse ->
                        val newListTempVar = if (page == 1)
                            apiResponse.articles
                        else
                            adapter.differ.currentList + apiResponse.articles
                        adapter.differ.submitList(newListTempVar)
                        pages = if (apiResponse.totalResults % 20 == 0) {
                            apiResponse.totalResults / 20
                        } else {
                            apiResponse.totalResults / 20 + 1
                        }
                        isLastPage = page == pages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { errorMessage ->
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.RVNews.apply {
            adapter = this@NewsFragment.adapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.onScrollRVListener)
        }
        adapter.setClickEvent {

            val bundle = Bundle()
            bundle.putSerializable("selected_article",it)
            findNavController().navigate(
                R.id.action_newsFragment_to_infoFragment,
                bundle
            )
        }
    }

    private fun showProgressBar() {
        isLoading = true
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        isLoading = false
        binding.progressbar.visibility = View.GONE
    }

    private val onScrollRVListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = binding.RVNews.layoutManager as LinearLayoutManager
            val sizeOfTheCurrentList = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val topPosition = layoutManager.findFirstVisibleItemPosition()

            val hasReachedToEnd = topPosition + visibleItems >= sizeOfTheCurrentList
            val shouldPaginate = !isLoading && !isLastPage && hasReachedToEnd && isScrolling
            if (shouldPaginate) {
                page++
                if (isSearching)
                    viewModel.getSearchedNews(keyword, page = page)
                else
                    viewModel.getNewsHeadlines(country, category, page)
                isScrolling = false
            }
        }
    }

}