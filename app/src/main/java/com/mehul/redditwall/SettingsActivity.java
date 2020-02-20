package com.mehul.redditwall;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//TODO: change download path
public class SettingsActivity extends AppCompatActivity {
    //pref keys
    public static final String SORT_METHOD = "SORTIMG", IMG_WIDTH = "WIDTH", IMG_HEIGHT = "HEIGHT", DEFAULT = "DEFAULT";

    private EditText widthEdit, heightEdit, defaultEdit;
    private RadioGroup sortMethod;
    private SharedPreferences preferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        widthEdit = findViewById(R.id.width_edit);
        heightEdit = findViewById(R.id.height_edit);
        defaultEdit = findViewById(R.id.default_edit);
        sortMethod = findViewById(R.id.sort_options);
        RadioButton sortNew = findViewById(R.id.sort_new);
        RadioButton sortHot = findViewById(R.id.sort_hot);
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, MODE_PRIVATE);

        int selectedSort = preferences.getInt(SORT_METHOD, R.id.sort_hot);
        switch (selectedSort) {
            case R.id.sort_hot:
                sortHot.setChecked(true);
                break;
            case R.id.sort_new:
                sortNew.setChecked(true);
                break;
        }
        DisplayMetrics disp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disp);
        int width = preferences.getInt(IMG_WIDTH, disp.widthPixels);
        int height = preferences.getInt(IMG_HEIGHT, disp.heightPixels);
        widthEdit.setText(width + "");
        heightEdit.setText(height + "");

        String defaultSub = preferences.getString(DEFAULT, "mobilewallpaper");
        defaultEdit.setText(defaultSub);
    }

    public void saveSettings(View view) {
        boolean valid = true;
        int selectedSort = sortMethod.getCheckedRadioButtonId();
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putInt(SORT_METHOD, selectedSort);
        DisplayMetrics disp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disp);
        int width = disp.widthPixels;
        int height = disp.heightPixels;
        try {
            width = Integer.parseInt(widthEdit.getText().toString());
            height = Integer.parseInt(heightEdit.getText().toString());
        } catch (NumberFormatException e) {
            valid = false;
            Toast.makeText(this, "Enter a valid number...", Toast.LENGTH_SHORT).show();
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
}
