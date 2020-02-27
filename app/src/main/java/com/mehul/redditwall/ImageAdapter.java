package com.mehul.redditwall;

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

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<BitURL> images;
    private LayoutInflater inflater;
    private Context context;
    private int width, height;

    ImageAdapter(Context context, ArrayList<BitURL> list) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        inflater = LayoutInflater.from(context);
        this.images = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.card_image, parent, false);
        return new ImageViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        if (images != null) {
            final BitURL current = images.get(position);
            if (current.getImg() == null) {
                Glide.with(context).asGif().load(current.getUrl()).override(width / 2, height / 4).into(holder.image);
            } else {
                holder.image.setImageBitmap(current.getImg());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent wallIntent = new Intent(context, WallActivity.class);
                    wallIntent.putExtra(WallActivity.WALL_URL, current.getUrl());
                    wallIntent.putExtra(WallActivity.GIF, current.getImg() == null);
                    context.startActivity(wallIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    void setList(ArrayList<BitURL> list) {
        images = list;
        notifyDataSetChanged();
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final ImageAdapter adapter;

        ImageViewHolder(View view, ImageAdapter adapt) {
            super(view);

            image = view.findViewById(R.id.image_holder);
            this.adapter = adapt;
        }
    }
}
