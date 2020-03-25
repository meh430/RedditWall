package com.mehul.redditwall.rest

import android.content.Context
import android.util.Log
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.activities.WallActivity
import com.mehul.redditwall.objects.BitURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//opens up https connection to get json data and return as a string
@Suppress("LocalVariableName")
internal class RestQuery(private val QUERY: String, private val context: Context?) {
    private var sort: Int = 0

    suspend fun getQueryJson(): String {
        var jsonString = ""
        withContext(Dispatchers.IO) {
            var urlConnection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val MODIFIER: String
                val AFTER: String
                //https://www.reddit.com/r/memes/top/.json?t=all
                val preferences = context?.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                sort = preferences!!.getInt(SettingsActivity.SORT_METHOD, MainActivity.HOT)
                when (sort) {
                    MainActivity.HOT -> {
                        MODIFIER = "/hot"
                        AFTER = WallActivity.AFTER_HOT_WALL ?: ""
                    }
                    MainActivity.NEW -> {
                        MODIFIER = "/new"
                        AFTER = WallActivity.AFTER_NEW_WALL ?: ""
                    }
                    MainActivity.TOP -> {
                        MODIFIER = "/top"
                        AFTER = WallActivity.AFTER_TOP_WALL ?: ""
                    }
                    else -> {
                        AFTER = ""
                        MODIFIER = ""
                    }
                }
                val BASE = "https://www.reddit.com/r/"
                val queryBuild = StringBuilder(BASE)
                val END = "/.json"
                queryBuild.append(QUERY)
                queryBuild.append(MODIFIER)
                queryBuild.append(END)
                if (MODIFIER.contains("top")) {
                    queryBuild.append("?t=all&&after=")
                } else {
                    queryBuild.append("?after=")
                }
                queryBuild.append(AFTER)

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

    suspend fun getImages(jsonResult: String): ArrayList<BitURL> {
        val ret = ArrayList<BitURL>()
        withContext(Dispatchers.Default) {
            try {
                var json = JSONObject(jsonResult)
                json = json.getJSONObject("data")
                when (sort) {
                    MainActivity.HOT -> WallActivity.AFTER_HOT_WALL = json.getString("after")
                    MainActivity.NEW -> WallActivity.AFTER_NEW_WALL = json.getString("after")
                    MainActivity.TOP -> WallActivity.AFTER_TOP_WALL = json.getString("after")
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
                    val canLoadGif = context?.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                            ?.getBoolean(SettingsActivity.LOAD_GIF, true)
                    if (canLoadGif!!) {
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
                    val temp = BitURL(null, url, "https://www.reddit.com$postLink")
                    temp.setGif(!isImage)
                    ret.add(temp)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        Log.e("fin", "finished job")
        return ret
    }
}
