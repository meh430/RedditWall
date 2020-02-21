package com.mehul.redditwall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mehul.redditwall.savedsub.SubViewModel;

import java.util.ArrayList;

//TODO: Have a list of saved subreddits
//TODO: ability to download images
//TODO: sort by new or hot? spinner
//TODO: load images on the fly
public class MainActivity extends AppCompatActivity {
    public static final String SharedPrefFile = "com.mehul.redditwall", SAVED = "SAVED";
    public static String AFTER = "";
    private String queryString, defaultLoad;
    private EditText search;
    private ArrayList<BitURL> images;
    private ImageAdapter adapter;
    private RecyclerView imageScroll;
    private ProgressBar loading;
    private ProgressBar bottomLoading;
    private TextView info;
    private LoadImages imageTask;
    private LoadMoreImages moreImageTask;

    //viewmodels
    public static SubViewModel subViewModel;

    @SuppressLint("SetTextI18n")
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

        subViewModel = new ViewModelProvider(this).get(SubViewModel.class);

        SharedPreferences preferences = getSharedPreferences(SharedPrefFile, MODE_PRIVATE);
        defaultLoad = preferences.getString(SettingsActivity.DEFAULT, "mobilewallpaper");

        Intent savedIntent = getIntent();
        defaultLoad = savedIntent.getStringExtra(SAVED);

        defaultLoad = defaultLoad == null ? preferences.getString(SettingsActivity.DEFAULT, "mobilewallpaper") : defaultLoad;

        search.setHint(defaultLoad);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            //get string from sharedpref
            imageTask = new LoadImages(this);
            imageTask.execute(defaultLoad);
        } else {
            info.setVisibility(View.VISIBLE);
            info.setText("No Network");
        }

        final Context c = this;
        imageScroll.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {

                    if (moreImageTask == null) {
                        cancelThreads();

                        moreImageTask = new LoadMoreImages(c);
                        moreImageTask.execute(queryString == null || queryString.length() == 0 ? defaultLoad : queryString);
                    } else if (moreImageTask.getStatus() != AsyncTask.Status.RUNNING) {
                        cancelThreads();

                        moreImageTask = new LoadMoreImages(c);
                        moreImageTask.execute(queryString == null || queryString.length() == 0 ? defaultLoad : queryString);
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelThreads();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelThreads();
    }

    @SuppressLint("SetTextI18n")
    public void startSearch(View view) {
        cancelThreads();
        queryString = "";
        loading.setVisibility(View.VISIBLE);
        info.setVisibility(View.INVISIBLE);
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
            imageTask = new LoadImages(this);
            imageTask.execute(queryString);
        } else {
            if (queryString.length() == 0) {
                imageTask = new LoadImages(this);
                imageTask.execute(defaultLoad);
            } else {
                info.setVisibility(View.VISIBLE);
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
            Intent launchSaved = new Intent(this, SavedActivity.class);
            startActivity(launchSaved);
            return true;
        } else if (id == R.id.settings) {
            Intent launchSettings = new Intent(this, SettingsActivity.class);
            startActivity(launchSettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelThreads() {

        if (imageTask != null) {
            loading.setVisibility(View.GONE);
            imageTask.cancel(true);
        }

        if (moreImageTask != null) {
            bottomLoading.setVisibility(View.GONE);
            moreImageTask.cancel(true);
        }
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
