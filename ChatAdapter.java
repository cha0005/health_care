package com.nibm.medlink_healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;

    private final Context context;
    private final List<ChatMessage> messages;
    private final LayoutInflater inflater;

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isUser() ? TYPE_USER : TYPE_BOT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage msg = getItem(position);
        int type = getItemViewType(position);

        if (convertView == null) {
            if (type == TYPE_USER) {
                convertView = inflater.inflate(R.layout.item_chat_user, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.item_chat_bot, parent, false);
            }
        }

        TextView tv = convertView.findViewById(R.id.text_message);
        tv.setText(msg.getMessage());
        return convertView;
    }
}
