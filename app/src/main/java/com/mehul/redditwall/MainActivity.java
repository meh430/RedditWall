package com.mehul.redditwall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText search;
    private ArrayList<Bitmap> images;
    private ImageAdapter adapter;
    private RecyclerView imageScroll;
    private TextView loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading = findViewById(R.id.loading);
        search = findViewById(R.id.search);
        imageScroll = findViewById(R.id.imageScroll);
        imageScroll.setLayoutManager(new GridLayoutManager(this, 2));
        images = new ArrayList<>();
        adapter = new ImageAdapter(this, images);
        imageScroll.setAdapter(adapter);
    }

    public void startSearch(View view) {
        loading.setVisibility(View.VISIBLE);
        loading.setText("LOADING...");
        images.clear();
        String queryString = search.getText().toString();

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {
            loading.setText("LOADING...");
            new LoadImages(this).execute(queryString);
        } else {
            if (queryString.length() == 0) {
                //loading.setText("Enter a query");
                new LoadImages(this).execute("mobilewallpapers");
            } else {
                loading.setText("No Network");
            }
        }
    }

    private class LoadImages extends AsyncTask<String, Void, ArrayList<Bitmap>> {
        Context context;

        LoadImages(Context con) {
            context = con;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setText("LOADING...");
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(String... strings) {
            RestQuery rq = new RestQuery(strings[0], context);
            return rq.getImages(rq.getQueryJson());
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> result) {
            super.onPostExecute(result);
            images.addAll(result);
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
        }
    }
}
