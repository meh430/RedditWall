package com.mehul.redditwall;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.mehul.redditwall.favorites.FavAdapter;
import com.mehul.redditwall.favorites.FavImage;
import com.mehul.redditwall.favorites.FavViewModel;

import java.util.List;

public class FavImageActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private FavAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_image);
        recycler = findViewById(R.id.fav_scroll);
        adapter = new FavAdapter(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        FavImage saved = adapter.getFavAtPosition(position);
                        MainActivity.favViewModel.deleteFavImage(saved);
                        adapter.notifyDataSetChanged();
                    }
                });
        helper.attachToRecyclerView(recycler);
        MainActivity.favViewModel = new ViewModelProvider(this).get(FavViewModel.class);

        MainActivity.favViewModel.getAllFav().observe(this, new Observer<List<FavImage>>() {
            @Override
            public void onChanged(List<FavImage> favs) {
                adapter.setFavs(favs);
                if (adapter.getItemCount() == 0) {
                    findViewById(R.id.fav_empty).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.fav_empty).setVisibility(View.GONE);
                }
            }
        });

        if (adapter.getItemCount() == 0) {
            findViewById(R.id.fav_empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.fav_empty).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
