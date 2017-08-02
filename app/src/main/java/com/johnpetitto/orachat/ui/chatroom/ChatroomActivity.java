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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.johnpetitto.orachat.OraChatApplication;
import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.StringUtils;
import com.johnpetitto.orachat.data.chat.ChatMessage;
import com.johnpetitto.orachat.data.chat.ChatModel;
import com.johnpetitto.orachat.ui.SimpleTextWatcher;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatroomActivity extends AppCompatActivity implements ChatroomView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.chatroom_toolbar) Toolbar toolbar;
    @BindView(R.id.chatroom_content) LinearLayout content;
    @BindView(R.id.chatroom_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.chatroom_progress_bar) ProgressBar progressBar;
    @BindView(R.id.chatroom_send_message) EditText sendMessage;
    @BindView(R.id.chatroom_send_button) ImageView sendButton;

    @BindDimen(R.dimen.dialog_margin) int dialogMargin;

    private ChatroomPresenter presenter;
    private ChatroomAdapter adapter;

    private boolean newChat;

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
        sendMessage.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(String text) {
                sendButton.setEnabled(text.trim().length() > 0);
            }
        });

        ChatModel model = ((OraChatApplication) getApplication()).getChatModel();
        presenter = new ChatroomPresenter(this, model);

        if (intent.hasExtra("chat_id")) {
            long chatId = intent.getLongExtra("chat_id", 0);
            presenter.getInitialMessages(chatId);
        } else {
            newChat = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        content.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.generic_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInitialMessages(List<ChatMessage> messages) {
        for (ChatMessage message : messages) {
            showNewMessage(message);
        }
    }

    @Override
    public void showNewMessage(ChatMessage message) {
        adapter.addMessage(message);
        recyclerView.scrollToPosition(0);
    }

    @OnClick(R.id.chatroom_send_button)
    public void sendMessageClick(View view) {
        String message = StringUtils.getTrimmedInput(sendMessage);

        if (newChat) {
            presenter.createNewChat(toolbar.getTitle().toString(), message);
            newChat = false;
        } else {
            presenter.sendMessage(message);
        }

        sendMessage.setText(null); // clear previous entry
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
                    String chatName = StringUtils.getTrimmedInput(editChatName);
                    toolbar.setTitle(chatName);

                    // only update name with server if chat has already been created
                    if (!newChat) {
                        presenter.updateChatName(chatName);
                    }
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
