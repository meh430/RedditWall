package com.mehul.redditwall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.material.chip.Chip;
import com.mehul.redditwall.savedsub.SubViewModel;

import java.util.ArrayList;

//TODO: Have a list of saved subreddits
//TODO: ability to download images
//TODO: sort by new or hot? spinner
//TODO: load images on the fly
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    private Chip hotChip, newChip, topChip;

    //viewmodels
    public static SubViewModel subViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hotChip = findViewById(R.id.hot_chip);
        newChip = findViewById(R.id.new_chip);
        topChip = findViewById(R.id.top_chip);
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

        int sortSelected = preferences.getInt(SettingsActivity.SORT_METHOD, R.id.sort_hot);
        switch (sortSelected) {
            case R.id.sort_hot:
                hotChip.setChipBackgroundColorResource(R.color.chip);
                hotChip.setTextColor(Color.WHITE);
                break;
            case R.id.sort_new:
                newChip.setChipBackgroundColorResource(R.color.chip);
                newChip.setTextColor(Color.WHITE);
                break;
            case R.id.sort_top:
                topChip.setChipBackgroundColorResource(R.color.chip);
                topChip.setTextColor(Color.WHITE);
                break;
        }

        hotChip.setOnClickListener(this);
        newChip.setOnClickListener(this);
        topChip.setOnClickListener(this);

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
                if (imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING) {
                    return;
                }

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

    public void runQuery() {
        //cancelThreads();
        imageTask = new LoadImages(this);
        imageTask.execute((queryString == null || queryString.length() == 1 || queryString.equalsIgnoreCase("")) ? defaultLoad : queryString);
        Log.e("BRUH", queryString + ", " + defaultLoad);
    }

    @Override
    public void onClick(View view) {
        if ((imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING)
                || (moreImageTask != null && moreImageTask.getStatus() == AsyncTask.Status.RUNNING)) {
            //Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            cancelThreads();
            //return;
        }
        images.clear();
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor prefEdit = getSharedPreferences(SharedPrefFile, MODE_PRIVATE).edit();
        hotChip.setTextColor(Color.BLACK);
        newChip.setTextColor(Color.BLACK);
        topChip.setTextColor(Color.BLACK);
        if (view.equals(hotChip)) {
            Log.e("CLICk", "CLICKED HOT");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, R.id.sort_hot);
            hotChip.setChipBackgroundColorResource(R.color.chip);
            hotChip.setTextColor(Color.WHITE);
            newChip.setChipBackgroundColorResource(R.color.white);
            topChip.setChipBackgroundColorResource(R.color.white);
            prefEdit.apply();

            runQuery();
        } else if (view.equals(newChip)) {
            Log.e("CLICk", "CLICKED NEW");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, R.id.sort_new);
            hotChip.setChipBackgroundColorResource(R.color.white);
            newChip.setChipBackgroundColorResource(R.color.chip);
            newChip.setTextColor(Color.WHITE);
            topChip.setChipBackgroundColorResource(R.color.white);
            prefEdit.apply();

            runQuery();
        } else if (view.equals(topChip)) {
            Log.e("CLICk", "CLICKED TOP");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, R.id.sort_top);
            hotChip.setChipBackgroundColorResource(R.color.white);
            newChip.setChipBackgroundColorResource(R.color.white);
            topChip.setChipBackgroundColorResource(R.color.chip);
            topChip.setTextColor(Color.WHITE);
            prefEdit.apply();

            runQuery();
        }
    }

    private class LoadImages extends AsyncTask<String, Void, Void> {
        Context context;

        LoadImages(Context con) {
            context = con;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            info.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (isCancelled()) {
                images.clear();
                return null;
            }
            RestQuery rq = new RestQuery(strings[0], context, images, adapter, loading, this);
            rq.getImages(rq.getQueryJson(true));
            //return rq.getImages(rq.getQueryJson(true));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //images.addAll(result);
            if (isCancelled()) {
                images.clear();
                adapter.notifyDataSetChanged();
                return;
            }
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
            if (images.size() == 0) {
                Log.e("BRUH", queryString + ", " + defaultLoad);
                info.setVisibility(View.VISIBLE);
                info.setText("Subreddit does not exist or it has no images");
            }
        }
    }

    private class LoadMoreImages extends AsyncTask<String, Void, Void> {
        Context context;

        LoadMoreImages(Context con) {
            context = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bottomLoading.setVisibility(View.VISIBLE);
            info.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (isCancelled()) {
                images.clear();
                return null;
            }
            RestQuery rq = new RestQuery(strings[0], context, images, adapter, bottomLoading, this);
            rq.getImages(rq.getQueryJson(false));
            return null;
            //return rq.getImages(rq.getQueryJson(false));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                images.clear();
                adapter.notifyDataSetChanged();
                return;
            }
            //images.addAll(result);
            adapter.notifyDataSetChanged();
            bottomLoading.setVisibility(View.GONE);
        }
    }
}
