package com.mehul.redditwall.rest

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.adapters.ImageAdapter
import com.mehul.redditwall.objects.BitURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//TODO: get post link to view thread in app
@Suppress("LocalVariableName", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
internal class QueryRequest {
    companion object {
        private var sort = 0

        @InternalCoroutinesApi
        suspend fun getQueryJson(QUERY: String, first: Boolean, context: Context): String? {
            var jsonString = ""
            withContext(Dispatchers.IO) {
                var urlConnection: HttpURLConnection? = null
                var reader: BufferedReader? = null

                try {
                    val MODIFIER: String
                    val AFTER: String
                    //https://www.reddit.com/r/memes/top/.json?t=all
                    val preferences = context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                    sort = preferences!!.getInt(SettingsActivity.SORT_METHOD, MainActivity.HOT)
                    when (sort) {
                        MainActivity.HOT -> {
                            MODIFIER = "/hot"
                            AFTER = MainActivity.AFTER_HOT ?: ""
                        }
                        MainActivity.NEW -> {
                            MODIFIER = "/new"
                            AFTER = MainActivity.AFTER_NEW ?: ""
                        }
                        MainActivity.TOP -> {
                            MODIFIER = "/top"
                            AFTER = MainActivity.AFTER_TOP ?: ""
                        }
                        else -> {
                            AFTER = ""
                            MODIFIER = ""
                        }
                    }
                    val BASE = "https://www.reddit.com/r/"
                    val queryBuild = StringBuilder(BASE)
                    val END = "/.json"
                    if (first) {
                        queryBuild.append(QUERY)
                        queryBuild.append(MODIFIER)
                        queryBuild.append(END)
                        if (MODIFIER.contains("top")) {
                            queryBuild.append("?t=all")
                        }
                    } else {
                        queryBuild.append(QUERY)
                        queryBuild.append(MODIFIER)
                        queryBuild.append(END)
                        if (MODIFIER.contains("top")) {
                            queryBuild.append("?t=all&&after=")
                        } else {
                            queryBuild.append("?after=")
                        }
                        queryBuild.append(AFTER)
                    }

                    Log.e("URL", queryBuild.toString())

                    val requestURL = URL(queryBuild.toString())

                    urlConnection = requestURL.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.connect()

                    val inputStream = urlConnection.inputStream
                    reader = BufferedReader(InputStreamReader(inputStream))
                    val builder = StringBuilder()

                    var line: String? = reader.readLine()
                    while (line != null) {
                        if (!isActive) {
                            break
                        }
                        builder.append(line)
                        builder.append("\n")
                        line = reader.readLine()
                    }

                    if (!isActive) {
                        jsonString = ""
                    }

                    if (builder.isEmpty()) {
                        jsonString = ""
                    }

                    jsonString = builder.toString()

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    urlConnection?.disconnect()
                    if (reader != null) {
                        try {
                            reader.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
            }

            Log.d("JSON", jsonString)
            return jsonString
        }

        @InternalCoroutinesApi
        suspend fun loadImgsFromJSON(jsonResult: String?, adapter: ImageAdapter?, context: Context,
                                     images: ArrayList<BitURL>, load: ProgressBar?, first: Boolean) {
            withContext(Dispatchers.Default) {
                try {
                    var json = JSONObject(jsonResult)
                    json = json.getJSONObject("data")
                    when (sort) {
                        MainActivity.HOT -> MainActivity.AFTER_HOT = json.getString("after")
                        MainActivity.NEW -> MainActivity.AFTER_NEW = json.getString("after")
                        MainActivity.TOP -> MainActivity.AFTER_TOP = json.getString("after")
                    }
                    val childrenArr = json.getJSONArray("children")

                    for (i in 0 until childrenArr.length()) {
                        if (!isActive) {
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
                        var gif: JSONObject? = null
                        var isImage = true
                        val canLoadGif = context.getSharedPreferences(MainActivity.SharedPrefFile,
                                Context.MODE_PRIVATE).getBoolean(SettingsActivity.LOAD_GIF, true)
                        val lowRes = context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE).getBoolean(SettingsActivity.PREVIEW_RES, false)
                        if (canLoadGif) {
                            if (image.has("variants") && image.getJSONObject("variants").has("gif")) {
                                isImage = false
                                gif = image.getJSONObject("variants").getJSONObject("gif")
                            }
                        }

                        val source: JSONObject
                        source = if (isImage) {
                            image.getJSONObject("source")
                        } else {
                            gif!!.getJSONObject("source")
                        }

                        val url = source.getString("url").replace("amp;".toRegex(), "")
                        var bitmap: Bitmap? = null
                        var previewUrl = ""
                        if (isImage) {
                            val resolutions = image.getJSONArray("resolutions")
                            previewUrl = if (lowRes) {
                                resolutions.getJSONObject(0).getString("url").replace("amp;".toRegex(), "")
                            } else {
                                resolutions.getJSONObject(resolutions.length() - 1).getString("url").replace("amp;".toRegex(), "")
                            }
                        }

                        /*withContext(Dispatchers.IO) {
                            try {
                                if (isImage && i % 4 == 0) {
                                    bitmap = AppUtils.getGridImageBitmap(context, url)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                bitmap = null
                            }
                        }*/

                        withContext(Dispatchers.Main) {
                            val temp = BitURL(bitmap, url, "https://www.reddit.com$postLink", previewUrl)
                            temp.setGif(!isImage)
                            images.add(temp)
                            if (first && i % 4 == 0 && i != 0) {
                                load?.visibility = View.GONE
                            } else if (!first && i % 4 == 0 && i != 0) {
                                load?.visibility = View.INVISIBLE
                            }

                            if (i % 4 == 0 && i != 0) {
                                adapter?.notifyDataSetChanged()
                            }
                            //Log.e("ADDING", "$i")
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }
}