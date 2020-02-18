package com.mehul.redditwall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//TODO: Have a list of saved subreddits
//TODO: ability to download images
//TODO: sort by new or hot? spinner
//TODO: load images on the fly
public class MainActivity extends AppCompatActivity {
    private static int y;
    public static String AFTER = "";
    private String queryString;
    private EditText search;
    private ArrayList<BitURL> images;
    private ImageAdapter adapter;
    private RecyclerView imageScroll;
    private ProgressBar loading;
    private ProgressBar bottomLoading;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loading = findViewById(R.id.loading);
        info = findViewById(R.id.info);
        bottomLoading = findViewById(R.id.progressBar);
        search = findViewById(R.id.search);
        imageScroll = findViewById(R.id.imageScroll);
        imageScroll.setLayoutManager(new GridLayoutManager(this, 2));
        images = new ArrayList<>();
        adapter = new ImageAdapter(this, images);
        imageScroll.setAdapter(adapter);
        final Context c = this;
        imageScroll.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                y = dy;
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    new LoadMoreImages(c).execute(search.getText().toString().length() == 0 ? "mobilewallpapers" : search.getText().toString());
                }

                /*if(RecyclerView.SCROLL_STATE_DRAGGING==newState){
                    if(y<=0){
                        toolbar.setVisibility(View.VISIBLE);
                    }
                    else{
                        y=0;
                        toolbar.setVisibility(View.GONE);
                    }
                }*/
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void startSearch(View view) {
        loading.setVisibility(View.VISIBLE);
        info.setText("LOADING...");
        info.setVisibility(View.VISIBLE);
        images.clear();
        adapter.notifyDataSetChanged();
        queryString = search.getText().toString();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {
            info.setText("LOADING...");
            new LoadImages(this).execute(queryString);
        } else {
            if (queryString.length() == 0) {
                new LoadImages(this).execute("mobilewallpapers");
            } else {
                info.setText("No Network");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saved_subs) {
            //go to the next activity here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoadImages extends AsyncTask<String, Void, ArrayList<BitURL>> {
        Context context;

        LoadImages(Context con) {
            context = con;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
            info.setText("LOADING...");
        }

        @Override
        protected ArrayList<BitURL> doInBackground(String... strings) {
            RestQuery rq = new RestQuery(strings[0], context);
            return rq.getImages(rq.getQueryJson(true));
        }

        @Override
        protected void onPostExecute(ArrayList<BitURL> result) {
            super.onPostExecute(result);
            images.addAll(result);
            adapter.notifyDataSetChanged();
            info.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
        }
    }

    private class LoadMoreImages extends AsyncTask<String, Void, ArrayList<BitURL>> {
        Context context;

        LoadMoreImages(Context con) {
            context = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bottomLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<BitURL> doInBackground(String... strings) {
            RestQuery rq = new RestQuery(strings[0], context);
            return rq.getImages(rq.getQueryJson(false));
        }

        @Override
        protected void onPostExecute(ArrayList<BitURL> result) {
            super.onPostExecute(result);
            images.addAll(result);
            adapter.notifyDataSetChanged();
            bottomLoading.setVisibility(View.GONE);
        }
    }
}
