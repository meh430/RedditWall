package com.mehul.redditwall;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mehul.redditwall.savedsub.SubAdapter;
import com.mehul.redditwall.savedsub.SubSaved;
import com.mehul.redditwall.savedsub.SubViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SavedActivity extends AppCompatActivity {
    private SubAdapter adapter;
    private EditText saveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        RecyclerView recycler = findViewById(R.id.saveScroll);
        saveText = findViewById(R.id.search);
        adapter = new SubAdapter(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
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
                        SubSaved saved = adapter.getSubAtPosition(position);
                        MainActivity.subViewModel.deleteSavedSub(saved);
                        adapter.notifyDataSetChanged();
                    }
                });
        helper.attachToRecyclerView(recycler);
        MainActivity.subViewModel = new ViewModelProvider(this).get(SubViewModel.class);

        MainActivity.subViewModel.getAllSubs().observe(this, new Observer<List<SubSaved>>() {
            @Override
            public void onChanged(List<SubSaved> subSaveds) {
                adapter.setSubs(subSaveds);
                if (adapter.getItemCount() == 0) {
                    findViewById(R.id.sub_empty).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.sub_empty).setVisibility(View.GONE);
                }
            }
        });

        if (adapter.getItemCount() == 0) {
            findViewById(R.id.sub_empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.sub_empty).setVisibility(View.GONE);
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
            confirmSubs.setMessage("Do you want to clear your saved subreddits?");
            confirmSubs.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.subViewModel.deleteAll();
                    Toast.makeText(SavedActivity.this, "Deleted saved subs", Toast.LENGTH_SHORT).show();
                }
            });

            confirmSubs.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(SavedActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
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


    public void saveSub(View view) {
        String saveVal = saveText.getText().toString();
        if (saveVal.length() == 1) {
            Toast.makeText(this, "Please enter something to save", Toast.LENGTH_SHORT).show();
            return;
        } else {
            saveText.setText("");
            saveVal = saveVal.toLowerCase();
        }

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        for (SubSaved saved : Objects.requireNonNull(MainActivity.subViewModel.getAllSubs().getValue())) {
            if (saved.getSubName().equalsIgnoreCase(saveVal)) {
                Toast.makeText(this, saved.getSubName() + " has already been saved", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        MainActivity.subViewModel.insert(new SubSaved((int) (Math.random() * 10000) + 1, saveVal, new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).format(new Date())));
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}
