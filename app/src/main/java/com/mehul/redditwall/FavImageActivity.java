package com.mehul.redditwall;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private FavAdapter adapter;
    private FavViewModel favViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_image);
        favViewModel = new ViewModelProvider(this).get(FavViewModel.class);
        RecyclerView recycler = findViewById(R.id.fav_scroll);
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
                        favViewModel.deleteFavImage(saved);
                        adapter.notifyDataSetChanged();
                    }
                });
        helper.attachToRecyclerView(recycler);
        favViewModel = new ViewModelProvider(this).get(FavViewModel.class);

        favViewModel.getAllFav().observe(this, new Observer<List<FavImage>>() {
            @Override
            public void onChanged(List<FavImage> favs) {
                adapter.setFavs(favs);
                findViewById(R.id.fav_empty).setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

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
        } else if (item.getItemId() == R.id.clear_list) {
            AlertDialog.Builder confirmSubs = new AlertDialog.Builder(this);
            confirmSubs.setTitle("Are you sure?");
            confirmSubs.setMessage("Do you want to clear your favorites?");
            confirmSubs.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    favViewModel.deleteAll();
                    Toast.makeText(FavImageActivity.this, "Deleted favorite images", Toast.LENGTH_SHORT).show();
                }
            });

            confirmSubs.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(FavImageActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            confirmSubs.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clear_menu, menu);
        return true;
    }
}
