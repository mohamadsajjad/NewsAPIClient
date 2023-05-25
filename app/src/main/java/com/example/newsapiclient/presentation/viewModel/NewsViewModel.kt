package com.example.newsapiclient.presentation.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.enums.SearchInType
import com.example.newsapiclient.data.model.enums.SearchSortType
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.domain.usecase.GetNewsHeadlinesUseCase
import com.example.newsapiclient.domain.usecase.GetSearchedNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class NewsViewModel(
    private val app: Application,
    private val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase
) : AndroidViewModel(app) {

    val newsHeadlines = MutableLiveData<Resource<APIResponse>>()
    val searchedNews = MutableLiveData<Resource<APIResponse>>()

    fun getNewsHeadlines(
        country: String,
        category: String,
        page: Int
    ) = viewModelScope
        .launch(Dispatchers.IO) {
            newsHeadlines.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = getNewsHeadlinesUseCase.execute(country, category, page)
                    newsHeadlines.postValue(apiResult)
                } else {
                    newsHeadlines.postValue(Resource.Error("Internet is not available"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                newsHeadlines.postValue(Resource.Error(e.message.toString()))
            }

        }

    fun getSearchedNews(
        keyword: String,
        searchIn: SearchInType = SearchInType.TITLE,
        sortBy: SearchSortType = SearchSortType.RELEVANCY,
        page: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        searchedNews.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable(app)){
                val apiResult = getSearchedNewsUseCase.execute(keyword,searchIn,sortBy,page)
                searchedNews.postValue(apiResult)
            }else {
                searchedNews.postValue(Resource.Error("Internet is not available"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            searchedNews.postValue(Resource.Error(e.message.toString()))
        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        return true
        var result = false
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
        return result
    }
}