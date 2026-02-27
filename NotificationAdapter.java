package com.nibm.medlink_healthcare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final ArrayList<Notification> notificationList;
    private final Context context;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());

    public NotificationAdapter(Context context, ArrayList<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.txtTitle.setText(notification.getTitle() != null ? notification.getTitle() : "No title");
        holder.txtMessage.setText(notification.getDescription() != null ? notification.getDescription() : "");

        Timestamp ts = notification.getScheduledTime();
        if (ts != null) {
            Date d = ts.toDate();
            holder.txtTime.setText(sdf.format(d));
        } else {
            holder.txtTime.setText("");
        }

        // completed status icon
        holder.imgTaskStatus.setVisibility(notification.isCompleted() ? View.VISIBLE : View.GONE);

        // ✅ open detail screen with Firestore document id
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationDetailActivity.class);
            intent.putExtra("id", notification.getId()); // Firestore document id
            intent.putExtra("title", notification.getTitle());
            intent.putExtra("description", notification.getDescription());
            intent.putExtra("scheduledTimeStr", holder.txtTime.getText().toString());
            intent.putExtra("completed", notification.isCompleted());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtMessage, txtTime;
        ImageView imgTaskStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtNotificationTitle);
            txtMessage = itemView.findViewById(R.id.txtNotificationMessage);
            txtTime = itemView.findViewById(R.id.txtNotificationTime);
            imgTaskStatus = itemView.findViewById(R.id.imgTaskStatus);
        }
    }
}
