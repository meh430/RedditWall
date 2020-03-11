package com.mehul.redditwall

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ProgressBar
import com.bumptech.glide.Glide
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
import java.util.concurrent.ExecutionException

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
                            AFTER = MainActivity.AFTER_HOT
                        }
                        MainActivity.NEW -> {
                            MODIFIER = "/new"
                            AFTER = MainActivity.AFTER_NEW
                        }
                        MainActivity.TOP -> {
                            MODIFIER = "/top"
                            AFTER = MainActivity.AFTER_TOP
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
                                     images: ArrayList<BitURL>, load: ProgressBar?, first: Boolean): Boolean {
            var loaded = false
            withContext(Dispatchers.Default) {
                val scale = (context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                !!.getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2
                val displayMetrics = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                val width = displayMetrics.widthPixels
                val height = displayMetrics.heightPixels
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
                        val preview = data.getJSONObject("preview")
                        val image = preview.getJSONArray("images").getJSONObject(0)
                        var gif: JSONObject? = null
                        var isImage = true
                        val canLoadGif = context.getSharedPreferences(MainActivity.SharedPrefFile,
                                Context.MODE_PRIVATE).getBoolean(SettingsActivity.LOAD_GIF, true)
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

                        try {
                            val url = source.getString("url").replace("amp;".toRegex(), "")
                            if (isImage) {
                                var bitmap: Bitmap? = null
                                withContext(Dispatchers.IO) {
                                    bitmap = Glide.with(context).asBitmap().load(url).override(width / scale, height / 4).centerCrop().submit().get()
                                }

                                withContext(Dispatchers.Main) {
                                    images.add(BitURL(bitmap, url))
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    images.add(BitURL(null, url))
                                }
                            }

                            /*withContext(Dispatchers.Main) {
                                //set adapter list
                                if(first) {
                                    load?.visibility = View.GONE
                                }
                                adapter?.notifyDataSetChanged()
                            }*/
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } catch (e: ExecutionException) {
                            e.printStackTrace()
                        }

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                loaded = images.isNotEmpty()
            }

            return loaded
        }
    }
}