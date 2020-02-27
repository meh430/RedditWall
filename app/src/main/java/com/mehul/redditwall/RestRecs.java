package com.mehul.redditwall;

import android.util.Log;

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

public class RestRecs {
    private static final String ENDPOINT = "https://reddtwalls-8176.restdb.io/rest/recommendations";

    public static String getRecsJSON() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String jsonString = "";
        try {
            connection = (HttpURLConnection) new URL(ENDPOINT).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-apikey", "f6bccbe7982701d49352805d8c5f2b86c634c");
            connection.setRequestProperty("cache-control", "no-cache");
            connection.setUseCaches(false);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {

                content.append(line);
                content.append("\n");
            }

            if (content.length() == 0) {
                return null;
            }

            jsonString = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("RECC", jsonString);
        return jsonString;
    }

    public static ArrayList<Recommendation> parseJSON(String json) {
        ArrayList<Recommendation> ret = new ArrayList<>();
        try {
            JSONArray reccList = new JSONArray(json);
            for (int i = 0; i < reccList.length(); i++) {
                JSONObject curr = reccList.getJSONObject(i);
                ret.add(new Recommendation(curr.getString("Name"), curr.getString("Description")
                        , curr.getString("Url").replaceAll("amp;", "")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
