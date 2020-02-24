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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.mehul.redditwall.savedsub.SubViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

//TODO: use three lists instead of one, don't clear them then
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SharedPrefFile = "com.mehul.redditwall", SAVED = "SAVED";
    private static final int NEW = 0, HOT = 1, TOP = 2;
    public static String AFTER_NEW = "", AFTER_HOT = "", AFTER_TOP = "";
    private String queryString, defaultLoad;
    private EditText search;
    private ArrayList<BitURL> hotImages, topImages, newImages;
    private ImageAdapter adapter;
    private RecyclerView imageScroll;
    private ProgressBar loading;
    private ProgressBar bottomLoading;
    private TextView info;
    private LoadImages imageTask, scrollImageTask;
    private Chip hotChip, newChip, topChip;
    private int currentSort;

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
        hotImages = new ArrayList<>();
        newImages = new ArrayList<>();
        topImages = new ArrayList<>();

        subViewModel = new ViewModelProvider(this).get(SubViewModel.class);

        SharedPreferences preferences = getSharedPreferences(SharedPrefFile, MODE_PRIVATE);
        defaultLoad = preferences.getString(SettingsActivity.DEFAULT, "mobilewallpaper");

        int sortSelected = preferences.getInt(SettingsActivity.SORT_METHOD, R.id.sort_hot);
        switch (sortSelected) {
            case R.id.sort_hot:
                adapter = new ImageAdapter(this, hotImages);
                currentSort = HOT;
                hotChip.setChipBackgroundColorResource(R.color.chip);
                hotChip.setTextColor(Color.WHITE);
                break;
            case R.id.sort_new:
                adapter = new ImageAdapter(this, newImages);
                currentSort = NEW;
                newChip.setChipBackgroundColorResource(R.color.chip);
                newChip.setTextColor(Color.WHITE);
                break;
            default:
                topImages = new ArrayList<>();
                currentSort = TOP;
                adapter = new ImageAdapter(this, topImages);
                topChip.setChipBackgroundColorResource(R.color.chip);
                topChip.setTextColor(Color.WHITE);
                break;
        }

        imageScroll.setAdapter(adapter);


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

            imageTask = getTask(true);//new LoadImages(this, loading, info, images, adapter, true);
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

                if (scrollImageTask == null) {
                    cancelThreads();

                    scrollImageTask = getTask(false);//new LoadImages(c, bottomLoading, info, images, adapter, false);
                    scrollImageTask.execute(queryString == null || queryString.length() == 0 ? defaultLoad : queryString);
                } else if (scrollImageTask.getStatus() != AsyncTask.Status.RUNNING) {
                    cancelThreads();

                    scrollImageTask = getTask(false);//new LoadImages(c, bottomLoading, info, images, adapter, false);
                    scrollImageTask.execute(queryString == null || queryString.length() == 0 ? defaultLoad : queryString);
                }
            }
        });
    }

    public LoadImages getTask(boolean first) {
        if (first) {
            if (currentSort == NEW) {
                return new LoadImages(this, loading, info, newImages, adapter, true);
            } else if (currentSort == HOT) {
                return new LoadImages(this, loading, info, hotImages, adapter, true);
            } else {
                return new LoadImages(this, loading, info, topImages, adapter, true);
            }
        } else {
            if (currentSort == NEW) {
                return new LoadImages(this, bottomLoading, info, newImages, adapter, false);
            } else if (currentSort == HOT) {
                return new LoadImages(this, bottomLoading, info, hotImages, adapter, false);
            } else {
                return new LoadImages(this, bottomLoading, info, topImages, adapter, false);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("STOP", "STOP");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("DESTROY", "DESTROY");
        cancelThreads();
    }

    @SuppressLint("SetTextI18n")
    public void startSearch(View view) {
        if (imageTask != null && imageTask.first && imageTask.getStatus() == AsyncTask.Status.RUNNING) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
            return;
        }
        cancelThreads();
        queryString = "";
        loading.setVisibility(View.VISIBLE);
        info.setVisibility(View.INVISIBLE);
        switch (currentSort) {
            case NEW:
                newImages.clear();
                break;
            case HOT:
                hotImages.clear();
                break;
            case TOP:
                topImages.clear();
                break;

        }
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
            imageTask = getTask(true);//new LoadImages(this, loading, info, images, adapter, true);
            imageTask.execute(queryString);
        } else {
            if (queryString.length() == 0) {
                imageTask = getTask(true);//new LoadImages(this, loading, info, images, adapter, true);
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

        if (scrollImageTask != null) {
            bottomLoading.setVisibility(View.GONE);
            scrollImageTask.cancel(true);
        }
    }

    public void runQuery() {
        //cancelThreads();
        imageTask = getTask(true);//new LoadImages(this, loading, info, images, adapter, true);
        imageTask.execute((queryString == null || queryString.length() == 1 || queryString.equalsIgnoreCase("")) ? defaultLoad : queryString);
        Log.e("BRUH", queryString + ", " + defaultLoad);
    }

    @Override
    public void onClick(View view) {
        String temp;
        switch (currentSort) {
            case NEW:
                temp = AFTER_NEW;
                break;
            case HOT:
                temp = AFTER_HOT;
                break;
            case TOP:
                temp = AFTER_TOP;
                break;
            default:
                temp = null;
                break;
        }
        if (imageTask != null && imageTask.first && imageTask.getStatus() == AsyncTask.Status.RUNNING && temp == null) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
            return;
        }

        if ((imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING) || (scrollImageTask != null && scrollImageTask.getStatus() == AsyncTask.Status.RUNNING)) {
            //Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            cancelThreads();
            //return;
        }
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor prefEdit = getSharedPreferences(SharedPrefFile, MODE_PRIVATE).edit();
        hotChip.setTextColor(Color.BLACK);
        newChip.setTextColor(Color.BLACK);
        topChip.setTextColor(Color.BLACK);
        if (view.equals(hotChip)) {
            currentSort = HOT;
            Log.e("CLICk", "CLICKED HOT");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, R.id.sort_hot);
            hotChip.setChipBackgroundColorResource(R.color.chip);
            hotChip.setTextColor(Color.WHITE);
            newChip.setChipBackgroundColorResource(R.color.white);
            topChip.setChipBackgroundColorResource(R.color.white);
            prefEdit.apply();
            if (hotImages.size() > 0) {
                adapter.setList(hotImages);
            } else {
                adapter.setList(hotImages);
                runQuery();
            }
        } else if (view.equals(newChip)) {
            currentSort = NEW;
            Log.e("CLICk", "CLICKED NEW");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, R.id.sort_new);
            hotChip.setChipBackgroundColorResource(R.color.white);
            newChip.setChipBackgroundColorResource(R.color.chip);
            newChip.setTextColor(Color.WHITE);
            topChip.setChipBackgroundColorResource(R.color.white);
            prefEdit.apply();
            if (newImages.size() > 0) {
                adapter.setList(newImages);
            } else {
                adapter.setList(newImages);
                runQuery();
            }
        } else if (view.equals(topChip)) {
            currentSort = TOP;
            Log.e("CLICk", "CLICKED TOP");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, R.id.sort_top);
            hotChip.setChipBackgroundColorResource(R.color.white);
            newChip.setChipBackgroundColorResource(R.color.white);
            topChip.setChipBackgroundColorResource(R.color.chip);
            topChip.setTextColor(Color.WHITE);
            prefEdit.apply();
            if (topImages.size() > 0) {
                adapter.setList(topImages);
            } else {
                adapter.setList(topImages);
                runQuery();
            }
        }
    }

    private static class LoadImages extends AsyncTask<String, Void, Void> {
        WeakReference<Context> context;
        WeakReference<ProgressBar> bLoad;
        WeakReference<TextView> inf;
        WeakReference<ArrayList<BitURL>> imgs;
        WeakReference<ImageAdapter> adapt;
        boolean first;

        LoadImages(Context con, ProgressBar bLoad, TextView inf, ArrayList<BitURL> imgs, ImageAdapter adapt, boolean first) {
            this.context = new WeakReference<>(con);
            this.bLoad = new WeakReference<>(bLoad);
            this.inf = new WeakReference<>(inf);
            this.imgs = new WeakReference<>(imgs);
            this.adapt = new WeakReference<>(adapt);
            this.first = first;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bLoad.get().setVisibility(View.VISIBLE);
            inf.get().setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (isCancelled()) {
                //imgs.clear();
                return null;
            }
            RestQuery rq = new RestQuery(strings[0], context.get(), imgs.get(), adapt.get(), bLoad.get(), this);
            rq.getImages(rq.getQueryJson(first));
            return null;
            //return rq.getImages(rq.getQueryJson(false));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                //imgs.get().clear();
                adapt.get().notifyDataSetChanged();
                return;
            }
            //images.addAll(result);
            adapt.get().notifyDataSetChanged();
            bLoad.get().setVisibility(View.GONE);

            if (!first && imgs.get().size() == 0) {
                inf.get().setVisibility(View.VISIBLE);
                inf.get().setText("Subreddit does not exist or it has no images");
            }
        }
    }
}
