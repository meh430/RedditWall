package com.mehul.redditwall.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehul.redditwall.objects.Subreddit
import com.mehul.redditwall.rest.RedditApi
import kotlinx.coroutines.launch

class SubViewModel : ViewModel() {
    var subreddits = MutableLiveData<ArrayList<Subreddit>>()
    var isLoading = MutableLiveData<Boolean>()

    init {
        subreddits.value = ArrayList()
        isLoading.value = false
    }

    fun searchSubs(query: String) {
        subreddits.value?.clear()
        isLoading.value = true
        subreddits.value = subreddits.value

        viewModelScope.launch {
            RedditApi.loadSearchedSubs(query) {
                subreddits.value?.addAll(it)
                isLoading.postValue(false)
                subreddits.postValue(subreddits.value)
            }
        }
    }
}