package com.mehul.redditwall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final ArrayList<BitURL> images;
    private LayoutInflater inflater;

    ImageAdapter(Context context, ArrayList<BitURL> list) {
        inflater = LayoutInflater.from(context);
        this.images = list;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.card_image, parent, false);
        return new ImageViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        BitURL current = images.get(position);
        holder.image.setImageBitmap(current.getImg());
    }

    @Override
    public int getItemCount() {
        return images.size();
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
