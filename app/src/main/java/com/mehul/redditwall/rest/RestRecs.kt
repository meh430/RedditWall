package com.mehul.redditwall.rest

import android.util.Log
import com.mehul.redditwall.objects.Recommendation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object RestRecs {
    private const val ENDPOINT = "https://reddtwalls-8176.restdb.io/rest/recommendations"

    suspend fun getRecsJSON(): String {
        var jsonString = ""
        withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                connection = URL(ENDPOINT).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("x-apikey", "f6bccbe7982701d49352805d8c5f2b86c634c")
                connection.setRequestProperty("cache-control", "no-cache")
                connection.useCaches = false
                connection.connect()
                val inputStream = connection.inputStream
                reader = BufferedReader(InputStreamReader(inputStream))
                val content = StringBuilder()

                var line: String? = reader.readLine()
                while (line != null) {
                    if (!isActive) {
                        content.append("")
                        break
                    }
                    content.append(line)
                    content.append("\n")
                    line = reader.readLine()
                }

                if (content.isEmpty()) {
                    jsonString = ""
                }

                jsonString = content.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
        Log.e("RECC", jsonString)
        return jsonString
    }

    suspend fun parseJSON(json: String): ArrayList<Recommendation> {
        val ret = ArrayList<Recommendation>()
        withContext(Dispatchers.Default) {
            try {
                val reccList = JSONArray(json)
                for (i in 0 until reccList.length()) {
                    if (!isActive)
                        break
                    val curr = reccList.getJSONObject(i)
                    ret.add(Recommendation(curr.getString("Name"),
                            curr.getString("Description"),
                            curr.getString("Url").replace("amp;".toRegex(), "")))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return ret
    }
}
