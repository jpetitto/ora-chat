package com.johnpetitto.orachat.ui.chats;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.johnpetitto.orachat.OraChatApplication;
import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.data.chat.ChatModel;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatsFragment extends Fragment implements ChatsView, FloatingSearchView.OnQueryChangeListener {
    @BindView(R.id.chats_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.chats_search_view) FloatingSearchView searchView;
    @BindView(R.id.chats_progress_bar) ProgressBar progressBar;

    @BindDimen(R.dimen.dialog_margin) int dialogMargin;

    private Unbinder unbinder;

    private ChatsPresenter presenter;
    private ChatsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        ChatModel model = ((OraChatApplication) getContext().getApplicationContext()).getChatModel();
        presenter = new ChatsPresenter(this, model);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatsAdapter();
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryChangeListener(this);

        presenter.getAllChatsByDate();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.chats_new)
    public void newChat(View view) {
        EditText chatName = new EditText(getActivity());
        chatName.setInputType(InputType.TYPE_CLASS_TEXT);

        // wrap in FrameLayout with margins so EditText aligns with other dialog elements
        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(dialogMargin);
        params.setMarginEnd(dialogMargin);
        chatName.setLayoutParams(params);
        container.addView(chatName);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_chat)
                .setView(container)
                .setPositiveButton(R.string.create, (dialogInterface, i) ->
                        Toast.makeText(getContext(), chatName.getText().toString(), Toast.LENGTH_SHORT).show())
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        Button createButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        createButton.setEnabled(false);

        chatName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                createButton.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        chatName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (createButton.isEnabled()) {
                createButton.callOnClick();
            }
            return true;
        });
    }

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.generic_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayChatsByDate(List<Object> chatsGroupedByDate) {
        adapter.setData(chatsGroupedByDate);
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        // TODO throttle
        presenter.searchChatsByName(newQuery);
    }
}
