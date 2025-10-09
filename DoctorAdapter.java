package com.nibm.medlink_healthcare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private final Context context;
    private final List<Doctor> doctors;

    public DoctorAdapter(Context context, List<Doctor> doctors) {
        this.context = context;
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor d = doctors.get(position);
        holder.name.setText(d.getName());
        holder.specialty.setText(d.getSpecialty());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DoctorDetailsActivity.class);
            intent.putExtra("id", d.getId());
            intent.putExtra("name", d.getName());
            intent.putExtra("specialty", d.getSpecialty());
            intent.putExtra("hospital", d.getHospital());
            intent.putExtra("contact", d.getContact());
            intent.putExtra("available", d.isAvailable());
            // if you want to open new activity from non-activity context, ensure context is Activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return doctors == null ? 0 : doctors.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, specialty;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemDoctorName);
            specialty = itemView.findViewById(R.id.itemDoctorSpecialty);
        }
    }
}
