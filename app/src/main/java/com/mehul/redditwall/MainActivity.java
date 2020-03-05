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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.mehul.redditwall.favorites.FavViewModel;
import com.mehul.redditwall.savedsub.SubViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {
    public static final String SharedPrefFile = "com.mehul.redditwall", SAVED = "SAVED", OVERRIDE = "OVERRIDE", QUERY = "QUERY";
    public static final int NEW = 0, HOT = 1, TOP = 2;
    public static String AFTER_NEW = "", AFTER_HOT = "", AFTER_TOP = "";
    private String queryString, defaultLoad;
    private EditText search;
    private ArrayList<BitURL> hotImages, topImages, newImages;
    private ImageAdapter adapter;
    private ProgressBar loading;
    private ProgressBar bottomLoading;
    private TextView info;
    private LoadImages imageTask, scrollImageTask;
    private Chip hotChip, newChip, topChip;
    private int currentSort;
    private SharedPreferences preferences;
    private GestureDetector detector;

    //viewmodels
    public static SubViewModel subViewModel;
    public static FavViewModel favViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favViewModel = new ViewModelProvider(this).get(FavViewModel.class);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hotChip = findViewById(R.id.hot_chip);
        newChip = findViewById(R.id.new_chip);
        topChip = findViewById(R.id.top_chip);
        loading = findViewById(R.id.loading);
        info = findViewById(R.id.info);
        bottomLoading = findViewById(R.id.progressBar);
        search = findViewById(R.id.search);
        RecyclerView imageScroll = findViewById(R.id.imageScroll);
        imageScroll.setLayoutManager(new GridLayoutManager(this, 2));
        hotImages = new ArrayList<>();
        newImages = new ArrayList<>();
        topImages = new ArrayList<>();
        detector = new GestureDetector(imageScroll.getContext(), this);

        subViewModel = new ViewModelProvider(this).get(SubViewModel.class);

        preferences = getSharedPreferences(SharedPrefFile, MODE_PRIVATE);
        defaultLoad = preferences.getString(SettingsActivity.DEFAULT, "mobilewallpaper");

        int sortSelected = preferences.getInt(SettingsActivity.SORT_METHOD, HOT);
        switch (sortSelected) {
            case HOT:
                adapter = new ImageAdapter(this, hotImages, imageTask, scrollImageTask);
                currentSort = HOT;
                hotChip.setChipBackgroundColorResource(R.color.chip);
                hotChip.setTextColor(Color.WHITE);
                break;
            case NEW:
                adapter = new ImageAdapter(this, newImages, imageTask, scrollImageTask);
                currentSort = NEW;
                newChip.setChipBackgroundColorResource(R.color.chip);
                newChip.setTextColor(Color.WHITE);
                break;
            default:
                topImages = new ArrayList<>();
                currentSort = TOP;
                adapter = new ImageAdapter(this, topImages, imageTask, scrollImageTask);
                topChip.setChipBackgroundColorResource(R.color.chip);
                topChip.setTextColor(Color.WHITE);
                break;
        }

        imageScroll.setAdapter(adapter);


        hotChip.setOnClickListener(this);
        newChip.setOnClickListener(this);
        topChip.setOnClickListener(this);

        Intent savedIntent = getIntent();

        if (savedIntent.getBooleanExtra(OVERRIDE, false)) {
            defaultLoad = savedIntent.getStringExtra(SAVED);
        } else {
            defaultLoad = preferences.getString(SettingsActivity.DEFAULT, "mobilewallpaper");
        }

        search.setHint(defaultLoad);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        preferences.edit().putString(QUERY, defaultLoad).apply();
        if (networkInfo != null && networkInfo.isConnected()) {
            imageTask = getTask(true);
            imageTask.execute(defaultLoad);
        } else {
            info.setVisibility(View.VISIBLE);
            info.setText("No Network");
        }

        imageScroll.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING) {
                    return;
                }

                if (scrollImageTask == null) {
                    cancelThreads();

                    scrollImageTask = getTask(false);
                    scrollImageTask.execute(queryString == null || queryString.length() == 0 ? defaultLoad : queryString);
                } else if (scrollImageTask.getStatus() != AsyncTask.Status.RUNNING) {
                    cancelThreads();

                    scrollImageTask = getTask(false);
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

    public void startSearch(View view) {
        if (imageTask != null && imageTask.first && imageTask.getStatus() == AsyncTask.Status.RUNNING) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
            return;
        }
        cancelThreads();
        queryString = "";
        loading.setVisibility(View.VISIBLE);
        info.setVisibility(View.INVISIBLE);
        newImages.clear();
        hotImages.clear();
        topImages.clear();
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
            imageTask = getTask(true);
            preferences.edit().putString(QUERY, defaultLoad).apply();
            imageTask.execute(queryString);
        } else {
            if (queryString.length() == 0) {
                imageTask = getTask(true);
                imageTask.execute(defaultLoad);
            } else {
                info.setVisibility(View.VISIBLE);
                info.setText("No Network");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //Launch activities from menu here
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saved_subs) {
            Intent launchSaved = new Intent(this, SavedActivity.class);
            startActivity(launchSaved);
            return true;
        } else if (id == R.id.settings) {
            Intent launchSettings = new Intent(this, SettingsActivity.class);
            startActivity(launchSettings);
            return true;
        } else if (id == R.id.fav_pics) {
            Intent launchFav = new Intent(this, FavImageActivity.class);
            startActivity(launchFav);
        } else if (id == R.id.recc_subs) {
            Intent launcRec = new Intent(this, RecActivity.class);
            startActivity(launcRec);
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
        imageTask = getTask(true);
        imageTask.execute((queryString == null || queryString.length() == 1 ||
                queryString.equalsIgnoreCase("")) ? defaultLoad : queryString);
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

        if ((imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING)
                || (scrollImageTask != null && scrollImageTask.getStatus() == AsyncTask.Status.RUNNING)) {
            cancelThreads();
        }
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor prefEdit = preferences.edit();
        hotChip.setTextColor(Color.BLACK);
        newChip.setTextColor(Color.BLACK);
        topChip.setTextColor(Color.BLACK);
        if (view.equals(hotChip)) {
            currentSort = HOT;
            Log.e("CLICk", "CLICKED HOT");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, HOT);
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
            prefEdit.putInt(SettingsActivity.SORT_METHOD, NEW);
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
            prefEdit.putInt(SettingsActivity.SORT_METHOD, TOP);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void switchSort() {
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

        if ((imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING)
                || (scrollImageTask != null && scrollImageTask.getStatus() == AsyncTask.Status.RUNNING)) {
            cancelThreads();
        }
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor prefEdit = preferences.edit();
        hotChip.setTextColor(Color.BLACK);
        newChip.setTextColor(Color.BLACK);
        topChip.setTextColor(Color.BLACK);
        if (currentSort == HOT) {
            Log.e("CLICk", "CLICKED HOT");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, HOT);
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
        } else if (currentSort == NEW) {
            Log.e("CLICk", "CLICKED NEW");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, NEW);
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
        } else if (currentSort == TOP) {
            Log.e("CLICk", "CLICKED TOP");
            prefEdit.putInt(SettingsActivity.SORT_METHOD, TOP);
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

    //NEW = 0, HOT = 1, TOP = 2;
    public void swipedRight() {
        Log.e("R", "Right");
        if (currentSort == NEW) {
            currentSort = TOP;
        } else {
            currentSort--;
        }

        //load stuff here
        switchSort();
    }

    public void swipedLeft() {
        Log.e("L", "LEFT");
        if (currentSort == TOP) {
            currentSort = NEW;
        } else {
            currentSort++;
        }

        //load stuff here
        switchSort();
    }

    @Override
    public boolean onFling(MotionEvent evt1, MotionEvent evt2, float vX, float vY) {
        boolean ret = false;
        try {
            float diffY = evt2.getY() - evt1.getY();
            float diffX = evt2.getX() - evt1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > 100 && Math.abs(vX) > 100) {
                    if (diffX > 0)
                        swipedLeft();
                    else
                        swipedRight();
                    ret = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
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
                return null;
            }
            RestQuery rq = new RestQuery(strings[0], context.get(), imgs.get(),
                    adapt.get(), bLoad.get(), this);
            rq.getImages(rq.getQueryJson(first));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                adapt.get().notifyDataSetChanged();
                return;
            }
            adapt.get().notifyDataSetChanged();
            bLoad.get().setVisibility(View.GONE);

            if (first && imgs.get().size() == 0) {
                inf.get().setVisibility(View.VISIBLE);
                inf.get().setText("Subreddit does not exist or it has no images");
            }
        }
    }
}