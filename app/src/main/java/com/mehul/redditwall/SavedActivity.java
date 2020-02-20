package com.mehul.redditwall;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class SavedActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private SubAdapter adapter;
    private EditText saveText;
    private SubViewModel subViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        recycler = findViewById(R.id.saveScroll);
        saveText = findViewById(R.id.search);
        subViewModel = new ViewModelProvider(this).get(SubViewModel.class);
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
                        subViewModel.deleteSavedSub(saved);
                    }
                });
        helper.attachToRecyclerView(recycler);

        subViewModel.getAllSubs().observe(this, new Observer<List<SubSaved>>() {
            @Override
            public void onChanged(List<SubSaved> subSaveds) {
                adapter.setTimeScores(subSaveds);
            }
        });
    }

    public void saveSub(View view) {
        String saveVal = saveText.getText().toString();
        if (saveVal.length() == 1) {
            Toast.makeText(this, "Please enter something to save", Toast.LENGTH_SHORT).show();
            return;
        } else {
            saveText.setText("");
        }

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        subViewModel.insert(new SubSaved((int) (Math.random() * 10000) + 1, saveVal, new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).format(new Date())));
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}
