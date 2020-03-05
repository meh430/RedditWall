package com.mehul.redditwall

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object RestRecs {
    private val ENDPOINT = "https://reddtwalls-8176.restdb.io/rest/recommendations"

    val recsJSON: String?
        get() {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            var jsonString = ""
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

                    content.append(line)
                    content.append("\n")
                    line = reader.readLine()
                }

                if (content.isEmpty()) {
                    return null
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
            Log.e("RECC", jsonString)
            return jsonString
        }

    fun parseJSON(json: String): ArrayList<Recommendation> {
        val ret = ArrayList<Recommendation>()
        try {
            val reccList = JSONArray(json)
            for (i in 0 until reccList.length()) {
                val curr = reccList.getJSONObject(i)
                ret.add(Recommendation(curr.getString("Name"),
                        curr.getString("Description"),
                        curr.getString("Url").replace("amp;".toRegex(), "")))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return ret
    }
}
