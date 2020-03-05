package com.mehul.redditwall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ReccViewHolder> {
    private ArrayList<Recommendation> recs;
    private LayoutInflater inflater;
    private Context context;
    private int width, height;

    RecAdapter(Context context, ArrayList<Recommendation> list) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        inflater = LayoutInflater.from(context);
        this.recs = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecAdapter.ReccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.card_rec, parent, false);
        return new RecAdapter.ReccViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapter.ReccViewHolder holder, int position) {
        if (recs != null) {
            final Recommendation current = recs.get(position);
            Glide.with(context).load(current.getUrl()).override(width, height / 4).centerCrop().into(holder.image);
            holder.nameTv.setText(current.getName());
            holder.descTv.setText(current.getDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent launchMain = new Intent(context, MainActivity.class);
                    launchMain.putExtra(MainActivity.SAVED, current.getName());
                    launchMain.putExtra(MainActivity.OVERRIDE, true);
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", current.getName());
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Saved to clipboard", Toast.LENGTH_SHORT).show();
                    context.startActivity(launchMain);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder defaultConfirm = new AlertDialog.Builder(context);
                    defaultConfirm.setTitle("Set as default?");
                    defaultConfirm.setMessage("Are you sure you want " + current.getName() + " to be your default subreddit?");
                    defaultConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences preferences = context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefEdit = preferences.edit();
                            prefEdit.putString(SettingsActivity.Companion.getDEFAULT(), current.getName());
                            prefEdit.apply();
                            Toast.makeText(context, "Set " + current.getName() + " as default", Toast.LENGTH_SHORT).show();
                        }
                    });
                    defaultConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                    defaultConfirm.show();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recs.size();
    }


    class ReccViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView nameTv;
        TextView descTv;

        ReccViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.rec_image);
            nameTv = view.findViewById(R.id.rec_name);
            descTv = view.findViewById(R.id.rec_desc);
        }
    }
}
