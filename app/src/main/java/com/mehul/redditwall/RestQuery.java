package com.mehul.redditwall;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;

import com.squareup.picasso.Picasso;

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

//opens up https connection to get json data and return as a string
public class RestQuery {
    private String BASE = "https://www.reddit.com/r/";
    private String END = "/.json";
    private String QUERY;
    private Context context;
    public RestQuery(String q, Context con) {
        QUERY = q;
        context = con;
    }

    public String getQueryJson(boolean first) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString = "";

        try {

            StringBuilder queryBuild = new StringBuilder(BASE);
            if (first) {
                queryBuild.append(QUERY);
                queryBuild.append(END);
            } else {
                queryBuild.append(QUERY);
                queryBuild.append(END);
                queryBuild.append("?after=");
                queryBuild.append(MainActivity.AFTER);
            }


            URL requestURL = new URL(queryBuild.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
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

    public ArrayList<BitURL> getImages(String jsonResult) {
        ArrayList<BitURL> ret = new ArrayList<>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        try {
            JSONObject json = new JSONObject(jsonResult);
            json = json.getJSONObject("data");
            MainActivity.AFTER = json.getString("after");
            JSONArray childrenArr = json.getJSONArray("children");

            for (int i = 0; i < childrenArr.length(); i++) {
                JSONObject curr = childrenArr.getJSONObject(i);
                JSONObject data = curr.getJSONObject("data");
                if (!data.has("preview")) {
                    continue;
                }
                JSONObject preview = data.getJSONObject("preview");

                JSONObject source = preview.getJSONArray("images").getJSONObject(0).getJSONObject("source");
                try {
                    String url = source.getString("url").replaceAll("amp;", "");
                    Bitmap bitmap = Picasso.get().load(url).resize(width / 2, 500).centerCrop().get();
                    ret.add(new BitURL(bitmap, url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
