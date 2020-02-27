package com.mehul.redditwall;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//TODO: manage threads properly
public class RecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);
        ProgressBar loading = findViewById(R.id.rec_loading);
        RecyclerView recycler = findViewById(R.id.rec_scroll);
        TextView error = findViewById(R.id.rec_empty);
        error.setVisibility(View.INVISIBLE);
        ArrayList<Recommendation> recs = new ArrayList<>();
        RecAdapter adapter = new RecAdapter(this, recs);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        LoadRecTask task = new LoadRecTask(loading, recs, adapter, error);
        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class LoadRecTask extends AsyncTask<Void, Void, ArrayList<Recommendation>> {
        WeakReference<ProgressBar> load;
        WeakReference<ArrayList<Recommendation>> rList;
        WeakReference<RecAdapter> adapt;
        WeakReference<TextView> err;

        LoadRecTask(ProgressBar l, ArrayList<Recommendation> r, RecAdapter a, TextView e) {
            load = new WeakReference<>(l);
            rList = new WeakReference<>(r);
            adapt = new WeakReference<>(a);
            err = new WeakReference<>(e);
        }

        @Override
        protected void onPreExecute() {
            err.get().setVisibility(View.GONE);
            load.get().setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Recommendation> doInBackground(Void... voids) {
            return RestRecs.parseJSON(RestRecs.getRecsJSON());
        }

        @Override
        protected void onPostExecute(ArrayList<Recommendation> res) {
            load.get().setVisibility(View.GONE);
            if (res.size() != 0) {
                rList.get().addAll(res);
                Collections.sort(rList.get(), new Comparator<Recommendation>() {
                    @Override
                    public int compare(Recommendation o1, Recommendation o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                adapt.get().notifyDataSetChanged();
            } else {
                err.get().setVisibility(View.VISIBLE);
            }
        }
    }
}
