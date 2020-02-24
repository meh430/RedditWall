package com.mehul.redditwall;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class WallActivity extends AppCompatActivity {
    public static final String WALL_URL = "WALLURL", GIF = "GIF";
    private ImageView wallPreview;
    private boolean isGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        wallPreview = findViewById(R.id.wall_holder);
        Intent incoming = getIntent();
        String imgUrl = incoming.getStringExtra(WALL_URL);
        isGif = incoming.getBooleanExtra(GIF, false);
        SharedPreferences preferences = getSharedPreferences(MainActivity.SharedPrefFile, MODE_PRIVATE);

        DisplayMetrics disp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disp);
        int width = preferences.getInt(SettingsActivity.IMG_WIDTH, disp.widthPixels);
        int height = preferences.getInt(SettingsActivity.IMG_HEIGHT, disp.heightPixels);
        if (isGif) {
            Glide.with(this).asGif().load(imgUrl).override(width, height).centerCrop().into(wallPreview);
        } else {
            Picasso.get().load(imgUrl).resize(width, height).centerCrop().into(wallPreview);
        }
    }

    public void setWallpaper(View view) {
        if (isGif) {
            Toast.makeText(this, "GIF support is coming soon", Toast.LENGTH_SHORT).show();
            return;
        }
        final Context con = this;
        final Toast temp = Toast.makeText(this, "Setting wallpaper...", Toast.LENGTH_LONG);
        final WallpaperManager wall = (WallpaperManager) this.getApplicationContext().getSystemService(Context.WALLPAPER_SERVICE);
        final Bitmap bitmap = ((BitmapDrawable) wallPreview.getDrawable()).getBitmap();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Where?")
                .setItems(R.array.location_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int index) {
                        temp.show();
                        switch (index) {
                            case 0:
                                try {
                                    assert wall != null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        wall.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                                    } else {
                                        wall.setBitmap(bitmap);
                                    }
                                    Toast.makeText(con, "successfully changed wallpaper", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e("MainActivity", "Error setting wallpaper");
                                    Toast.makeText(con, "failed to set wallpaper", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                try {
                                    assert wall != null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        wall.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                                    } else {
                                        wall.setBitmap(bitmap);
                                    }
                                    Toast.makeText(con, "successfully changed wallpaper", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e("MainActivity", "Error setting wallpaper");
                                    Toast.makeText(con, "failed to set wallpaper", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                try {
                                    assert wall != null;
                                    wall.setBitmap(bitmap);
                                    Toast.makeText(con, "successfully changed wallpaper", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e("MainActivity", "Error setting wallpaper");
                                    Toast.makeText(con, "failed to set wallpaper", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });
        builder.create().show();
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
