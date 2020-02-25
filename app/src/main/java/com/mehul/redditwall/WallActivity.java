package com.mehul.redditwall;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class WallActivity extends AppCompatActivity {
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager notifyManager;
    public static final String WALL_URL = "WALLURL", GIF = "GIF";
    private ImageView wallPreview;
    private boolean isGif;
    private int WRITE = 1231;
    private String fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        wallPreview = findViewById(R.id.wall_holder);
        Intent incoming = getIntent();
        String imgUrl = incoming.getStringExtra(WALL_URL);
        isGif = incoming.getBooleanExtra(GIF, false);
        SharedPreferences preferences = getSharedPreferences(MainActivity.SharedPrefFile, MODE_PRIVATE);
        createNotificationChannel();
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

    public void downloadImage(View view) {
        if (isGif) {
            Toast.makeText(this, "GIF support is coming soon", Toast.LENGTH_SHORT).show();
            return;
        }

        //ask for storage permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE);
        } else {
            saveImage();
        }
    }

    public void saveImage() {
        Bitmap bitmap = ((BitmapDrawable) wallPreview.getDrawable()).getBitmap();
        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RedditWalls");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        Log.e("BRUH", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            sendNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1231) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(this, "Cannot download, please grant permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendNotification() {

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        //send notification through the channel in the manager
        notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        updateNotification();
    }

    public void updateNotification() {
        Bitmap bitmap = ((BitmapDrawable) wallPreview.getDrawable()).getBitmap();
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .setBigContentTitle("Notification Updated!"));
        notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    public void cancelNotification() {
        notifyManager.cancel(NOTIFICATION_ID);
    }

    //creating a channel and putting it in the manager
    public void createNotificationChannel() {
        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //checking to see if the build version is greater than that of oreo to implement notification channels
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Download Notification", NotificationManager.IMPORTANCE_HIGH);

            //configuring notification settings when sent
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification for Download");
            notifyManager.createNotificationChannel(notificationChannel);
        }
    }

    //creating a notification using a builder
    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent();
        notificationIntent.setAction(Intent.ACTION_VIEW);
        notificationIntent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().toString() + "/RedditWalls/" + fname), "image/*");
        //flag indicating that if the described PendingIntent already exists, then keep it but replace its extra data with what is in this new Intent
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID);
        notifyBuilder.setContentIntent(notificationPendingIntent).setAutoCancel(false).setContentTitle("You've been notified!")
                .setContentText("View the Image!").setSmallIcon(R.drawable.ic_download).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }
}
