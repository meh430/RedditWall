package com.mehul.redditwall.savedsub;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehul.redditwall.MainActivity;
import com.mehul.redditwall.R;
import com.mehul.redditwall.SettingsActivity;

import java.util.List;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubViewHolder> {
    private final LayoutInflater inflater;
    private List<SubSaved> subs;
    private Context con;

    public SubAdapter(Context context) {
        con = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.sub_saved_item, parent, false);
        return new SubViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubAdapter.SubViewHolder holder, int position) {
        if (subs != null) {
            final SubSaved current = subs.get(position);
            holder.bindTo(current);
            holder.itemView.setLongClickable(true);
            holder.itemView.setClickable(true);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder defaultConfirm = new AlertDialog.Builder(con);
                    defaultConfirm.setTitle("Set as default?");
                    defaultConfirm.setMessage("Are you sure you want " + current.getSubName() + " to be your default subreddit?");
                    defaultConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences preferences = con.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefEdit = preferences.edit();
                            prefEdit.putString(SettingsActivity.DEFAULT, current.getSubName());
                            prefEdit.apply();
                            Toast.makeText(con, "Set " + current.getSubName() + " as default", Toast.LENGTH_SHORT).show();
                        }
                    });
                    defaultConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(con, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                    defaultConfirm.show();
                    return true;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent launchMain = new Intent(con, MainActivity.class);
                    launchMain.putExtra(MainActivity.SAVED, current.getSubName());
                    ClipboardManager clipboard = (ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", current.getSubName());
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(con, "Saved to clipboard", Toast.LENGTH_SHORT).show();
                    con.startActivity(launchMain);
                }
            });
        }
    }

    public void setSubs(List<SubSaved> subs) {
        this.subs = subs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (subs != null)
            return subs.size();
        else return 0;
    }

    public SubSaved getSubAtPosition(int position) {
        return subs.get(position);
    }

    class SubViewHolder extends RecyclerView.ViewHolder {
        private TextView subTv, dateTv;

        SubViewHolder(View itemView) {
            super(itemView);
            subTv = itemView.findViewById(R.id.sub_name);
            dateTv = itemView.findViewById(R.id.sub_date);
        }

        void bindTo(SubSaved saved) {
            String date = saved.getSubDate();
            String name = saved.getSubName();

            subTv.setText(name);
            dateTv.setText("Saved on " + date);
        }
    }
}
