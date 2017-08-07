package com.johnpetitto.orachat.ui.chatroom;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.johnpetitto.orachat.ui.PagingScrollListener;
import com.johnpetitto.orachat.ui.SimpleTextWatcher;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatroomActivity extends AppCompatActivity implements ChatroomView,
        Toolbar.OnMenuItemClickListener, PagingScrollListener.OnPageListener,
        ChatroomAdapter.OnMessageSelectListener, ActionMode.Callback {

    @BindView(R.id.chatroom_toolbar) Toolbar toolbar;
    @BindView(R.id.chatroom_content) LinearLayout content;
    @BindView(R.id.chatroom_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.chatroom_progress_bar) ProgressBar progressBar;
    @BindView(R.id.chatroom_send_message) EditText sendMessage;
    @BindView(R.id.chatroom_send_button) ImageView sendButton;

    @BindDimen(R.dimen.dialog_margin) int dialogMargin;
    @BindDimen(R.dimen.chat_bubble_normal_padding) int bubbleNormalPadding;
    @BindDimen(R.dimen.chat_bubble_start_padding) int bubbleStartPadding;
    @BindDimen(R.dimen.chat_bubble_end_padding) int bubbleEndPadding;

    private ChatroomPresenter presenter;
    private ChatroomAdapter adapter;
    private PagingScrollListener scrollListener;

    private boolean newChat;
    private ActionMode actionMode;
    private ChatMessage activeMessage;

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
        adapter = new ChatroomAdapter(bubbleNormalPadding, bubbleStartPadding, bubbleEndPadding, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(scrollListener = new PagingScrollListener(this));

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
        adapter.setMessages(messages);
    }

    @Override
    public void showNewMessage(ChatMessage message) {
        adapter.addMessage(message);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void showMoreMessages(List<ChatMessage> messages) {
        adapter.addMessages(messages);
    }

    @Override
    public void pageLoaded() {
        scrollListener.loadingComplete();
    }

    @Override
    public void loadPage() {
        presenter.getMoreMessages();
    }

    @OnClick(R.id.chatroom_send_button)
    public void sendMessageClick(View view) {
        String message = StringUtils.getTrimmedInput(sendMessage);

        adapter.addUserMessage(message); // for demo purposes

        if (newChat) {
            presenter.createNewChat(toolbar.getTitle().toString(), message);
            newChat = false;
        } else {
            presenter.sendMessage(message);
        }

        sendMessage.setText(null); // clear previous entry
    }

    @Override
    public void onMessageSelect(ChatMessage message) {
        activeMessage = message;

        // only start action mode if it's not already visible
        if (actionMode == null) {
            actionMode = startActionMode(this);
        }
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

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_action_chatroom, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.share:
                shareMessage();
                break;
            case R.id.copy:
                copyMessage();
                break;
            case R.id.delete:
                showDeleteMessageDialog();
                break;
        }

        actionMode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        adapter.deselectMessage();
    }

    private void shareMessage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, activeMessage.getMessage());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    private void copyMessage() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, activeMessage.getMessage());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.copy_complete, Toast.LENGTH_SHORT).show();
    }

    private void showDeleteMessageDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(R.string.delete_message)
                .setPositiveButton(R.string.delete, (dialogInterface, i) -> adapter.removeMessage(activeMessage))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
