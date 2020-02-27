package com.mehul.redditwall.favorites;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mehul.redditwall.MainActivity;
import com.mehul.redditwall.R;
import com.mehul.redditwall.SettingsActivity;
import com.mehul.redditwall.WallActivity;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {
    private final LayoutInflater inflater;
    private List<FavImage> favs;
    private Context con;
    private int width, height, scale;

    public FavAdapter(Context context) {
        con = context;
        inflater = LayoutInflater.from(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        scale = (context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE).
                getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.card_image, parent, false);
        return new FavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavAdapter.FavViewHolder holder, int position) {
        if (favs != null) {
            final FavImage current = favs.get(position);
            holder.bindTo(current);
            holder.itemView.setLongClickable(true);
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //launch wall activity
                    Intent wallIntent = new Intent(con, WallActivity.class);
                    wallIntent.putExtra(WallActivity.WALL_URL, current.getFavUrl());
                    wallIntent.putExtra(WallActivity.GIF, current.isGif());
                    con.startActivity(wallIntent);
                }
            });
        }
    }

    public void setFavs(List<FavImage> favs) {
        this.favs = favs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (favs != null)
            return favs.size();
        else return 0;
    }

    public FavImage getFavAtPosition(int position) {
        return favs.get(position);
    }

    class FavViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        FavViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image_holder);
        }

        void bindTo(FavImage saved) {
            String url = saved.getFavUrl();
            if (saved.isGif()) {
                Glide.with(con).asGif().load(url).override(width / scale, height / 4).centerCrop().into(img);
            } else {
                Glide.with(con).load(url).override(width / 2, height / 4).centerCrop().into(img);
            }
        }
    }
}
