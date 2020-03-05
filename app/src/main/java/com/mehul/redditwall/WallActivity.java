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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mehul.redditwall.favorites.FavImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WallActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager notifyManager;
    public static final String WALL_URL = "WALLURL", GIF = "GIF", LIST = "LIST", INDEX = "INDEX", FROM_MAIN = "MAIN";
    private ImageView wallPreview;
    private boolean isGif, fromMain;
    private int WRITE = 1231, index, width, height;
    private String fname, imgUrl;
    private ArrayList<BitURL> imageList;
    private List<FavImage> favImages;
    private GestureDetector detector;
    private LoadImages task;
    private SharedPreferences preferences;
    private Drawable filledStar, openStar;
    private Menu starred;

    public static String listToJson(ArrayList<BitURL> imgs, List<FavImage> favs) {
        if (imgs != null) {
            Log.e("IMGS", new Gson().toJson(imgs));
            return new Gson().toJson(imgs);
        } else {
            Log.e("FAVS", new Gson().toJson(favs));
            return new Gson().toJson(favs);
        }
    }

    public static ArrayList<BitURL> jsonToList(String json) {
        ArrayList<BitURL> ret = new ArrayList<>();
        try {
            JSONArray list = new JSONArray(json);
            for (int i = 0; i < list.length(); i++) {
                JSONObject curr = list.getJSONObject(i);
                boolean gif = false;
                if (curr.getBoolean("gif")) {
                    gif = true;
                }
                BitURL temp = new BitURL(null, curr.getString("url"));
                temp.setGif(gif);
                ret.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static List<FavImage> jsonFavToList(String json) {
        List<FavImage> ret = new ArrayList<>();
        Log.e("JSON", ret.toString());
        try {
            JSONArray list = new JSONArray(json);
            for (int i = 0; i < list.length(); i++) {
                JSONObject curr = list.getJSONObject(i);
                Log.e("JSON", curr.toString());
                boolean gif = false;
                if (curr.getBoolean("gif")) {
                    gif = true;
                }
                FavImage temp = new FavImage((int) (Math.random() * 10000) + 1, curr.getString("url"), gif);
                ret.add(temp);
            }
        } catch (JSONException e) {
            Log.e("JSON", e.toString());
            e.printStackTrace();
        }
        return (List<FavImage>) ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        wallPreview = findViewById(R.id.wall_holder);
        detector = new GestureDetector(this, this);
        Intent incoming = getIntent();
        fromMain = incoming.getBooleanExtra(FROM_MAIN, true);
        String jsonList = incoming.getStringExtra(LIST);
        if (jsonList != null) {
            if (fromMain) {
                imageList = jsonToList(jsonList);
            } else {
                favImages = jsonFavToList(jsonList);
            }
        }
        index = incoming.getIntExtra(INDEX, 0);
        imgUrl = incoming.getStringExtra(WALL_URL);
        isGif = incoming.getBooleanExtra(GIF, false);
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, MODE_PRIVATE);
        createNotificationChannel();
        DisplayMetrics disp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disp);
        width = preferences.getInt(SettingsActivity.Companion.getIMG_WIDTH(), disp.widthPixels);
        height = preferences.getInt(SettingsActivity.Companion.getIMG_HEIGHT(), disp.heightPixels);
        filledStar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_black);
        openStar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_open);

        if (isGif) {
            Glide.with(this).asGif().load(imgUrl).override(width, height).centerCrop().into(wallPreview);
        } else {
            Glide.with(this).load(imgUrl).override(width, height).centerCrop().into(wallPreview);
            //Picasso.get().load(imgUrl).resize(width, height).centerCrop().into(wallPreview);
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
        } else if (item.getItemId() == R.id.fav_image) {
            for (FavImage img : MainActivity.favViewModel.getFavList()) {
                if (imgUrl.equalsIgnoreCase(img.getFavUrl())) {
                    item.setIcon(openStar);
                    MainActivity.favViewModel.deleteFavImage(img);
                    Toast.makeText(this, "Unfavorited", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            item.setIcon(filledStar);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            MainActivity.favViewModel.insert(new FavImage((int) (Math.random() * 10000) + 1, imgUrl, isGif));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wall_menu, menu);
        for (FavImage img : MainActivity.favViewModel.getFavList()) {
            if (imgUrl.equalsIgnoreCase(img.getFavUrl())) {
                menu.getItem(0).setIcon(filledStar);
                return true;
            }
        }

        menu.getItem(0).setIcon(openStar);
        starred = menu;
        return true;
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
        fname = new SimpleDateFormat("MM-dd-yyyy 'at' hh-mm-ss", Locale.CANADA).format(new Date())
                .replaceAll(" ", "") + ".jpg";
        File file = new File(myDir, fname);
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
        if (requestCode == WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                .setBigContentTitle("Finished Downloading!"));
        notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    /*public void cancelNotification() {
        notifyManager.cancel(NOTIFICATION_ID);
    }*/

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
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID);
        notifyBuilder.setContentIntent(notificationPendingIntent).setAutoCancel(true).setContentTitle("You've been notified!")
                .setContentText("View the Image!").setSmallIcon(R.drawable.ic_download).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (task != null)
            task.cancel(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void swipedRight() {
        Log.e("R", "Right");
        if (index - 1 >= 0) {
            index--;
            if (fromMain) {
                BitURL curr = imageList.get(index);
                imgUrl = curr.getUrl();
                isGif = curr.hasGif();
            } else {
                FavImage curr = favImages.get(index);
                imgUrl = curr.getFavUrl();
                isGif = curr.isGif();
            }

            if (isGif) {
                Glide.with(this).asGif().load(imgUrl).override(width, height).centerCrop().into(wallPreview);
            } else {
                Glide.with(this).load(imgUrl).override(width, height).centerCrop().into(wallPreview);
            }

            if (fromMain) {
                for (FavImage img : MainActivity.favViewModel.getFavList()) {
                    if (imgUrl.equalsIgnoreCase(img.getFavUrl())) {
                        starred.getItem(0).setIcon(filledStar);
                        return;
                    }
                }

                starred.getItem(0).setIcon(openStar);
            } else {
                starred.getItem(0).setIcon(filledStar);
            }
        } else {
            Toast.makeText(this, "Reached the end", Toast.LENGTH_SHORT).show();
        }
    }

    public void swipedLeft() {
        Log.e("L", "LEFT");
        boolean inBound = fromMain ? (index + 1) < imageList.size() : (index + 1) < favImages.size();
        if (inBound) {
            index++;
            if (fromMain) {
                BitURL curr = imageList.get(index);
                imgUrl = curr.getUrl();
                isGif = curr.hasGif();
            } else {
                FavImage curr = favImages.get(index);
                imgUrl = curr.getFavUrl();
                isGif = curr.isGif();
            }

            if (isGif) {
                Glide.with(this).asGif().load(imgUrl).override(width, height).centerCrop().into(wallPreview);
            } else {
                Glide.with(this).load(imgUrl).override(width, height).centerCrop().into(wallPreview);
            }

            if (fromMain) {
                for (FavImage img : MainActivity.favViewModel.getFavList()) {
                    if (imgUrl.equalsIgnoreCase(img.getFavUrl())) {
                        starred.getItem(0).setIcon(filledStar);
                        return;
                    }
                }

                starred.getItem(0).setIcon(openStar);
            } else {
                starred.getItem(0).setIcon(filledStar);
            }
        } else if ((task == null || task.getStatus() != AsyncTask.Status.RUNNING) && fromMain) {
            Toast.makeText(this, "Reached the end", Toast.LENGTH_SHORT).show();
            if (task != null) task.cancel(true);
            task = new LoadImages(this, (ProgressBar) findViewById(R.id.load_more), imageList);
            task.execute(preferences.getString(MainActivity.QUERY,
                    preferences.getString(SettingsActivity.Companion.getDEFAULT(), "mobilewallpaper")));
        } else if (!fromMain) {
            Toast.makeText(this, "Reached the end", Toast.LENGTH_SHORT).show();
        } else if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
        }
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
                        swipedRight();
                    else
                        swipedLeft();
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
        WeakReference<ProgressBar> load;
        WeakReference<ArrayList<BitURL>> imgs;

        LoadImages(Context con, ProgressBar load, ArrayList<BitURL> imgs) {
            this.context = new WeakReference<>(con);
            this.load = new WeakReference<>(load);
            this.imgs = new WeakReference<>(imgs);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.get().setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            RestQuery rq = new RestQuery(strings[0], context.get(), imgs.get(),
                    null, load.get(), this);
            rq.getImages(rq.getQueryJson(false));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                return;
            }
            load.get().setVisibility(View.GONE);
        }
    }
}
