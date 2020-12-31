package com.mehul.redditwall.rest

import android.util.Log
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.objects.Subreddit
import com.mehul.redditwall.objects.WallImage
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject

class RedditApi {


    companion object {
        const val HOT = 0
        const val NEW = 1
        const val TOP_WEEK = 2
        const val TOP_YEAR = 3
        const val TOP_ALL = 4

        //https://www.reddit.com/r/uwaterloo/top.json?sort=top&t=all
        @InternalCoroutinesApi
        suspend fun loadImages(query: String, sort: Int, after: String = "", onUpdate: (WallImage, String, Boolean) -> Unit) {
            withContext(Dispatchers.Default) {
                try {
                    var endpoint = "https://www.reddit.com/r/$query"

                    endpoint += when (sort) {
                        HOT -> "/hot.json?sort=hot"
                        NEW -> "/new.json?sort=new"
                        TOP_WEEK -> "/top.json?sort=top&t=week"
                        TOP_YEAR -> "/top.json?sort=top&t=year"
                        TOP_ALL -> "/top.json?sort=top&t=all"
                        else -> "/top.json?sort=top&t=all"
                    }

                    if (after.isNotEmpty()) {
                        endpoint += "&after=$after"
                    }

                    Log.e("ENDPOINT", endpoint)

                    val jsonString = async { AppUtils.getJsonData(endpoint) }
                    var json = JSONObject(jsonString.await())
                    json = json.getJSONObject("data")
                    val nextAfter = json.getString("after")
                    val childrenArr = json.getJSONArray("children")
                    for (i in 0 until childrenArr.length()) {
                        if (!NonCancellable.isActive) {
                            break
                        }

                        val curr = childrenArr.getJSONObject(i)
                        val data = curr.getJSONObject("data")
                        if (!data.has("preview")) {
                            continue
                        }
                        val postLink = data.getString("permalink")
                        val preview = data.getJSONObject("preview")
                        val image = preview.getJSONArray("images").getJSONObject(0)
                        val source: JSONObject
                        source = image.getJSONObject("source")
                        val url = source.getString("url").replace("amp;".toRegex(), "")
                        val resolutions = image.getJSONArray("resolutions")
                        val previewUrl = resolutions.getJSONObject(0).getString("url").replace("amp;".toRegex(), "")
                        withContext(Dispatchers.Main) {
                            val currentImage = WallImage(imgUrl = url, postLink = "https://www.reddit.com$postLink", subName = query, previewUrl = previewUrl)
                            onUpdate(currentImage, nextAfter, false)
                        }
                    }
                    onUpdate(WallImage(), nextAfter, true)
                } catch (e: Exception) {
                    Log.e("Error", e.message ?: "")
                }
            }
        }

        suspend fun loadSearchedSubs(query: String, onFinished: (ArrayList<Subreddit>) -> Unit) {
            val temp = ArrayList<Subreddit>()
            withContext(Dispatchers.Default) {
                val endpoint = "https://www.reddit.com/api/search_reddit_names/.json?query=$query"
                val jsonString = async { AppUtils.getJsonData(endpoint) }
                try {
                    val results = JSONObject(jsonString.await())
                    val resultsList = results.getJSONArray("names")
                    for (i in 0..resultsList.length()) {
                        val curr = resultsList.getString(i)
                        val json = async { AppUtils.getSubInfo(curr) }
                        val result = json.await().getJSONObject("data")
                        val iconUrl = result.getString("icon_img")
                        val title = result.getString("display_name_prefixed")
                        val desc = result.getString("public_description")
                        val subs = result.getInt("subscribers")
                        val sub = Subreddit(subName = title, subDesc = desc, subscribers = subs, subIcon = iconUrl)
                        temp.add(sub)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            onFinished(temp)
        }
    }
}

/*        binding.search.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startSearch(binding.searchButton)
                return@OnEditorActionListener true
            }
            false
        })*/