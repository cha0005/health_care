package com.nibm.medlink_healthcare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RecordModel> records;

    public RecordsAdapter(Context context, ArrayList<RecordModel> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordModel record = records.get(position);

        holder.txtName.setText(record.getName());

        // Try decoding Base64 if it's an image
        try {
            byte[] bytes = Base64.decode(record.getData(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                holder.imgPreview.setImageBitmap(bitmap);
            } else {
                holder.imgPreview.setImageResource(R.drawable.ic_file); // fallback
            }
        } catch (Exception e) {
            holder.imgPreview.setImageResource(R.drawable.ic_file);
        }

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked: " + record.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgPreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtRecordName);
            imgPreview = itemView.findViewById(R.id.imgRecordPreview);
        }
    }
}
