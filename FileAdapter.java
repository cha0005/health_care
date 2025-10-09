package com.nibm.medlink_healthcare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private Context context;
    private ArrayList<FileModel> fileList;

    public FileAdapter(Context context, ArrayList<FileModel> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileModel file = fileList.get(position);
        holder.textFileName.setText(file.getName());

        // Open button
        holder.btnOpen.setOnClickListener(v -> {
            try {
                Uri fileUri = Uri.parse(file.getUri());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(fileUri, "*/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(intent, "Open with"));
            } catch (Exception e) {
                Toast.makeText(context, "Cannot open file", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete button
        holder.btnDelete.setOnClickListener(v -> {
            fileList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fileList.size());
            Toast.makeText(context, "File deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView textFileName;
        Button btnOpen, btnDelete;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            textFileName = itemView.findViewById(R.id.textFileName);
            btnOpen = itemView.findViewById(R.id.btnOpen);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
