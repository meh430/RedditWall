package com.mehul.redditwall.savedsub;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehul.redditwall.MainActivity;
import com.mehul.redditwall.R;

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
    public void onBindViewHolder(@NonNull SubAdapter.SubViewHolder holder, int position) {
        if (subs != null) {
            final SubSaved current = subs.get(position);
            holder.bindTo(current);
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

    public void setTimeScores(List<SubSaved> subs) {
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
