package com.mehul.redditwall

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import com.bumptech.glide.Glide
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class AppUtils {
    companion object {

        public fun getDimensions(con: Context): IntArray {
            val disp = DisplayMetrics()
            (con as Activity).windowManager.defaultDisplay.getMetrics(disp)
            return intArrayOf(disp.widthPixels, disp.heightPixels)
        }

        public fun getGridImageScale(context: Context): Int {
            return (context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                    .getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2
        }

        public fun getWallDimensions(context: Context): IntArray {
            val dimensions = getDimensions(context)
            val prefs = getPreferences(context)
            return intArrayOf(prefs.getInt(SettingsActivity.IMG_WIDTH, dimensions[0]),
                    prefs.getInt(SettingsActivity.IMG_HEIGHT, dimensions[1]))
        }

        public fun getGridImageBitmap(context: Context, url: String): Bitmap {
            val scale = getGridImageScale(context)
            val dimensions = getDimensions(context)
            return Glide.with(context).asBitmap()
                    .load(url).error(ColorDrawable(Color.GRAY)).placeholder(ColorDrawable(Color.GRAY))
                    .override(dimensions[0] / scale, dimensions[1] / 4).centerCrop().submit().get()
        }

        public fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        }

        public fun saveBitmap(bitmap: Bitmap, context: Context): String {
            val fName = (0..999999999).random().toString().replace(" ", "") + ".jpg"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val relativeLocation: String = Environment.DIRECTORY_PICTURES + File.separator + "RedditWalls"

                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fName)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)

                val resolver = context.contentResolver

                var stream: OutputStream? = null
                var uri: Uri? = null

                try {
                    val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    uri = resolver.insert(contentUri, contentValues)
                    if (uri == null) {
                        throw IOException("Failed to create new MediaStore record.")
                    }
                    Log.e("URI", uri.toString())
                    stream = resolver.openOutputStream(uri)
                    if (stream == null) {
                        throw IOException("Failed to get output stream.")
                    }
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                        throw IOException("Failed to save bitmap.")
                    }

                } catch (e: IOException) {
                    if (uri != null) {
                        resolver.delete(uri, null, null)
                    }
                    throw e
                } finally {
                    stream?.close()
                }
            } else {
                val root = Environment.getExternalStorageDirectory().toString()
                val myDir = File("$root/RedditWalls")
                myDir.mkdirs()
                val file = File(myDir, fName)
                if (file.exists())
                    file.delete()
                try {
                    val out = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                    out.close()
                    MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, file.name, file.name)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return fName
        }

        suspend fun getSubInfo(subName: String): JSONObject {
            var subInfo = JSONObject()
            withContext(Dispatchers.Default) {
                val endpoint = "https://www.reddit.com/r/$subName/about/.json"
                val jsonString = async { getJsonData(endpoint) }
                try {
                    subInfo = JSONObject(jsonString.await())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            return subInfo
        }

        suspend fun getJsonData(endpoint: String): String {
            var jsonString = ""
            withContext(Dispatchers.IO) {
                var urlConnection: HttpURLConnection? = null
                var reader: BufferedReader? = null
                try {
                    val requestURL = URL(endpoint)

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
            Log.e("JSON", jsonString)
            return jsonString
        }
    }
}