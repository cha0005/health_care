package com.nibm.medlink_healthcare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AIChatActivity extends AppCompatActivity {

    private ListView chatListView;
    private EditText inputMessage;
    private Button sendButton;

    private ChatAdapter chatAdapter;
    private final List<ChatMessage> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        chatListView = findViewById(R.id.chatListView);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);

        chatAdapter = new ChatAdapter(this, chatMessages);
        chatListView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> {
            String text = inputMessage.getText().toString().trim();
            if (text.isEmpty()) return;
            // add user message
            chatMessages.add(new ChatMessage(text, true));
            chatAdapter.notifyDataSetChanged();
            chatListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
            inputMessage.setText("");

            // send to "AI" (mock) in background
            sendToAiMock(text);
        });
    }

    private void sendToAiMock(String userMessage) {
        // Simulate background/network work and post a reply
        new Thread(() -> {
            try {
                Thread.sleep(600); // simulate latency
            } catch (InterruptedException ignored) { }

            final String botReply = generateMockReply(userMessage);

            runOnUiThread(() -> {
                chatMessages.add(new ChatMessage(botReply, false));
                chatAdapter.notifyDataSetChanged();
                chatListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
            });
        }).start();
    }

    // Simple mock: you will replace this with a real network/API call.
    private String generateMockReply(String input) {
        // small canned logic to feel "smart"
        if (input.toLowerCase().contains("hello") || input.toLowerCase().contains("hi")) {
            return "Hi! How can I help you today?";
        }
        if (input.endsWith("?")) {
            return "That's a good question — I don't have network AI enabled here, but I can help structure an answer.";
        }
        // echo fallback
        return "Echo: " + input;
    }

    // If you want to show errors to user quickly:
    private void showToast(String msg) {
        Toast.makeText(AIChatActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
