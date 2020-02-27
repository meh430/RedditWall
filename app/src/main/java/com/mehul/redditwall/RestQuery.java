package com.mehul.redditwall;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//opens up https connection to get json data and return as a string
class RestQuery {
    private String QUERY;
    private Context context;
    private ArrayList<BitURL> images;
    private ImageAdapter adapter;
    private ProgressBar progress;
    private AsyncTask<String, Void, Void> imageTask;
    private int sort;

    RestQuery(String q, Context con, ArrayList<BitURL> images, ImageAdapter adapter, ProgressBar progCircle,
              AsyncTask<String, Void, Void> imageTask) {
        QUERY = q;
        context = con;
        this.images = images;
        this.adapter = adapter;
        progress = progCircle;
        this.imageTask = imageTask;
    }

    String getQueryJson(boolean first) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString = "";

        try {
            String MODIFIER;
            String AFTER;
            //https://www.reddit.com/r/memes/top/.json?t=all
            SharedPreferences preferences = context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE);
            sort = preferences.getInt(SettingsActivity.SORT_METHOD, MainActivity.HOT);
            switch (sort) {
                case MainActivity.HOT:
                    MODIFIER = "/hot";
                    AFTER = MainActivity.AFTER_HOT;
                    break;
                case MainActivity.NEW:
                    MODIFIER = "/new";
                    AFTER = MainActivity.AFTER_NEW;
                    break;
                case MainActivity.TOP:
                    MODIFIER = "/top";
                    AFTER = MainActivity.AFTER_TOP;
                    break;
                default:
                    AFTER = "";
                    MODIFIER = "";
            }
            String BASE = "https://www.reddit.com/r/";
            StringBuilder queryBuild = new StringBuilder(BASE);
            String END = "/.json";
            if (first) {
                queryBuild.append(QUERY);
                queryBuild.append(MODIFIER);
                queryBuild.append(END);
                if (MODIFIER.contains("top")) {
                    queryBuild.append("?t=all");
                }
            } else {
                queryBuild.append(QUERY);
                queryBuild.append(MODIFIER);
                queryBuild.append(END);
                if (MODIFIER.contains("top")) {
                    queryBuild.append("?t=all&&after=");
                } else {
                    queryBuild.append("?after=");
                }
                queryBuild.append(AFTER);
            }

            Log.e("URL", queryBuild.toString());

            URL requestURL = new URL(queryBuild.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                if (imageTask.isCancelled()) {
                    break;
                }
                builder.append(line);
                builder.append("\n");
            }

            if (imageTask.isCancelled()) {
                return null;
            }

            if (builder.length() == 0) {
                return null;
            }

            jsonString = builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d("JSON", jsonString);
        return jsonString;
    }

    void getImages(String jsonResult) {
        int scale = (context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE).
                getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2;
        if (imageTask.isCancelled()) {
            return;
        }
        //TODO: change with lite mode
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        try {
            JSONObject json = new JSONObject(jsonResult);
            json = json.getJSONObject("data");
            switch (sort) {
                case MainActivity.HOT:
                    MainActivity.AFTER_HOT = json.getString("after");
                    break;
                case MainActivity.NEW:
                    MainActivity.AFTER_NEW = json.getString("after");
                    break;
                case MainActivity.TOP:
                    MainActivity.AFTER_TOP = json.getString("after");
                    break;
            }
            JSONArray childrenArr = json.getJSONArray("children");

            for (int i = 0; i < childrenArr.length(); i++) {
                if (imageTask.isCancelled()) {
                    return;
                }
                JSONObject curr = childrenArr.getJSONObject(i);
                JSONObject data = curr.getJSONObject("data");
                if (!data.has("preview")) {
                    continue;
                }
                JSONObject preview = data.getJSONObject("preview");
                JSONObject image = preview.getJSONArray("images").getJSONObject(0);
                JSONObject gif = null;
                boolean isImage = true;
                boolean canLoadGif = context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE).getBoolean(SettingsActivity.LOAD_GIF, true);
                if (canLoadGif) {
                    if (image.has("variants") && image.getJSONObject("variants").has("gif")) {
                        isImage = false;
                        gif = image.getJSONObject("variants").getJSONObject("gif");
                    }
                }

                JSONObject source;
                if (isImage) {
                    source = image.getJSONObject("source");
                } else {
                    source = gif.getJSONObject("source");
                }

                try {
                    String url = source.getString("url").replaceAll("amp;", "");
                    //TODO: replace with glide
                    if (isImage) {
                        Bitmap bitmap = Glide.with(context).asBitmap().load(url).override(width / scale, height / 4).centerCrop().submit().get();
                        //Bitmap bitmap = Picasso.get().load(url).resize(width / 12, height / 4).centerCrop().get();

                        images.add(new BitURL(bitmap, url));
                    } else {
                        //Bitmap bitmap = Glide.with(context).asBitmap().load(url).override(width / 2, 500).centerCrop().submit().get();
                        images.add(new BitURL(null, url));
                    }
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            progress.setVisibility(View.GONE);
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
