package com.johnpetitto.orachat.ui.chatroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.TimeUtils;
import com.johnpetitto.orachat.data.chat.ChatMessage;
import com.johnpetitto.orachat.data.user.User;
import com.johnpetitto.orachat.ui.SimpleTextWatcher;

import java.util.Date;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatroomActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.chatroom_toolbar) Toolbar toolbar;
    @BindView(R.id.chatroom_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.chatroom_progress_bar) ProgressBar progressBar;
    @BindView(R.id.chatroom_send_message) EditText sendMessageInput;
    @BindView(R.id.chatroom_send_button) ImageView sendButton;

    @BindDimen(R.dimen.dialog_margin) int dialogMargin;

    private ChatroomAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        toolbar.setTitle(intent.getStringExtra("chat_name"));
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        toolbar.inflateMenu(R.menu.menu_chatroom);
        toolbar.setOnMenuItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        adapter = new ChatroomAdapter();
        recyclerView.setAdapter(adapter);

        sendButton.setEnabled(false);
        sendMessageInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(String text) {
                sendButton.setEnabled(text.trim().length() > 0);
            }
        });
    }

    @OnClick(R.id.chatroom_send_button)
    public void sendMessageClick(View view) {
        sendMessage();
    }

    private void sendMessage() {
        // TODO: make server call
        String message = sendMessageInput.getText().toString().trim();
        String createdAt = TimeUtils.dateFormatter.format(new Date());
        User user = new User(0, "John", "john@orainteractive.com");
        ChatMessage chatMessage = new ChatMessage(0, 0, 0, message, createdAt, user);

        sendMessageInput.setText(null); // clear previous entry

        adapter.addMessage(chatMessage);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // show dialog for editing chat name
        EditText editChatName = new EditText(this);
        editChatName.setText(toolbar.getTitle());
        editChatName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editChatName.setSelection(editChatName.getText().length());

        // wrap in FrameLayout with margins so EditText aligns with other dialog elements
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(dialogMargin);
        params.setMarginEnd(dialogMargin);
        editChatName.setLayoutParams(params);
        container.addView(editChatName);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.edit_chat_name)
                .setView(container)
                .setPositiveButton(R.string.apply, (dialogInterface, i) -> {
                    toolbar.setTitle(editChatName.getText().toString().trim());
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        Button applyButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        applyButton.setEnabled(false);

        editChatName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(String text) {
                applyButton.setEnabled(text.trim().length() > 0);
            }
        });

        editChatName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (applyButton.isEnabled()) {
                applyButton.callOnClick();
            }
            return true;
        });

        return true;
    }
}
