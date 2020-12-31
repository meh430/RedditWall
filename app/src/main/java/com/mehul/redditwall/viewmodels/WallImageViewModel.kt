package com.mehul.redditwall.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehul.redditwall.objects.WallImage
import com.mehul.redditwall.rest.RedditApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class WallImageViewModel : ViewModel() {
    var wallImages = MutableLiveData<ArrayList<WallImage>>()
    var isLoading = MutableLiveData<Boolean>()
    var currentSort = 0
    var query = ""
    var after = ""

    init {
        wallImages.value = ArrayList()
        isLoading.value = false
    }

    @InternalCoroutinesApi
    fun getNextImages() {
        isLoading.value = true
        wallImages.value = wallImages.value

        viewModelScope.launch {
            RedditApi.loadImages(query, currentSort, after) { wallImage, nextAfter, isLast ->
                if (isLast) {
                    isLoading.postValue(false)
                    wallImages.postValue(wallImages.value)
                    if (wallImage.imgUrl.isEmpty()) {
                        return@loadImages
                    }
                }

                wallImages.value?.add(wallImage)
                wallImages.postValue(wallImages.value)
                after = nextAfter
            }
        }
    }

    @InternalCoroutinesApi
    fun setQAndSort(q: String, sort: Int) {
        query = q
        currentSort = sort
        wallImages.value?.clear()
        wallImages.value = wallImages.value
        after = ""
        getNextImages()
    }
}