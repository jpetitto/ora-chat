package com.johnpetitto.orachat.ui.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.TimeUtils;
import com.johnpetitto.orachat.data.chat.ChatMessage;
import com.johnpetitto.orachat.data.user.User;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatroomActivity extends AppCompatActivity {
    @BindView(R.id.chatroom_toolbar) Toolbar toolbar;
    @BindView(R.id.chatroom_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.chatroom_progress_bar) ProgressBar progressBar;
    @BindView(R.id.chatroom_send_message) EditText sendMessageInput;
    @BindView(R.id.chatroom_send_button) ImageView sendButton;

    private ChatroomAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        toolbar.setTitle(intent.getStringExtra("chat_name"));
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        adapter = new ChatroomAdapter();
        recyclerView.setAdapter(adapter);

        sendMessageInput.setOnEditorActionListener((textView, action, keyEvent) -> {
            sendMessage();
            return true;
        });
    }

    @OnClick(R.id.chatroom_send_button)
    public void sendMessageClick(View view) {
        sendMessage();
    }

    private void sendMessage() {
        // TODO: make server call
        String message = sendMessageInput.getText().toString();
        String createdAt = TimeUtils.dateFormatter.format(new Date());
        User user = new User(0, "John", "john@orainteractive.com");
        ChatMessage chatMessage = new ChatMessage(0, 0, 0, message, createdAt, user);

        sendMessageInput.setText(null); // clear previous entry

        adapter.addMessage(chatMessage);
        recyclerView.scrollToPosition(0);
    }
}
