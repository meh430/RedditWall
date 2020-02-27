package com.mehul.redditwall;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    //pref keys
    public static final String SORT_METHOD = "SORTIMG", IMG_WIDTH = "WIDTH", IMG_HEIGHT = "HEIGHT", DEFAULT = "DEFAULT",
            LOAD_SCALE = "LOAD", LOAD_GIF = "LOADGIF";

    private EditText widthEdit, heightEdit, defaultEdit;
    private SeekBar scaleSeek;
    private TextView seekCount;
    private SharedPreferences preferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, MODE_PRIVATE);
        seekCount = findViewById(R.id.scale_count);
        scaleSeek = findViewById(R.id.scale_seek);
        widthEdit = findViewById(R.id.width_edit);
        heightEdit = findViewById(R.id.height_edit);
        defaultEdit = findViewById(R.id.default_edit);
        Switch gifSwitch = findViewById(R.id.gif_switch);
        gifSwitch.setChecked(preferences.getBoolean(LOAD_GIF, true));
        scaleSeek.setProgress(preferences.getInt(LOAD_SCALE, 0));
        seekCount.setText((scaleSeek.getProgress() + 1) * 2 + "X");
        gifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    preferences.edit().putBoolean(LOAD_GIF, true).apply();
                } else {
                    preferences.edit().putBoolean(LOAD_GIF, false).apply();
                }
            }
        });
        scaleSeek = findViewById(R.id.scale_seek);
        scaleSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 0) {
                    seekCount.setText(((i + 1) * 2) + "X");
                } else {
                    seekCount.setText("2X");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        DisplayMetrics disp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disp);
        int width = preferences.getInt(IMG_WIDTH, disp.widthPixels);
        int height = preferences.getInt(IMG_HEIGHT, disp.heightPixels);
        widthEdit.setText(width + "");
        heightEdit.setText(height + "");

        String defaultSub = preferences.getString(DEFAULT, "mobilewallpaper");
        defaultEdit.setText(defaultSub);
    }

    @Override
    public void onPause() {
        super.onPause();
        boolean valid = true;
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putInt(LOAD_SCALE, scaleSeek.getProgress());
        DisplayMetrics disp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disp);
        int width = disp.widthPixels;
        int height = disp.heightPixels;
        try {
            width = Integer.parseInt(widthEdit.getText().toString());
            height = Integer.parseInt(heightEdit.getText().toString());
        } catch (NumberFormatException e) {
            valid = false;
            Toast.makeText(this, "You didn't enter a valid number", Toast.LENGTH_SHORT).show();
        }

        if (valid) {
            preferenceEditor.putInt(IMG_WIDTH, width);
            preferenceEditor.putInt(IMG_HEIGHT, height);
            Toast.makeText(this, "Saved Settings", Toast.LENGTH_SHORT).show();
        }

        String defaultSub = defaultEdit.getText().toString();
        preferenceEditor.putString(DEFAULT, defaultSub);
        preferenceEditor.apply();
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
