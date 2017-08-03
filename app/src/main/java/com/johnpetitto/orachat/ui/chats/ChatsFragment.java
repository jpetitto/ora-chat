package com.johnpetitto.orachat.ui.chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.johnpetitto.orachat.OraChatApplication;
import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.data.chat.Chat;
import com.johnpetitto.orachat.data.chat.ChatModel;
import com.johnpetitto.orachat.ui.PagingScrollListener;
import com.johnpetitto.orachat.ui.chatroom.ChatroomActivity;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatsFragment extends Fragment implements ChatsView,
        FloatingSearchView.OnQueryChangeListener, ChatsAdapter.OnChatClickListener,
        PagingScrollListener.OnPageListener {

    @BindView(R.id.chats_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.chats_search_view) FloatingSearchView searchView;
    @BindView(R.id.chats_progress_bar) ProgressBar progressBar;

    @BindDimen(R.dimen.dialog_margin) int dialogMargin;

    private Unbinder unbinder;

    private ChatsPresenter presenter;
    private ChatsAdapter adapter;
    private PagingScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        ChatModel model = ((OraChatApplication) getContext().getApplicationContext()).getChatModel();
        presenter = new ChatsPresenter(this, model);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new ChatsAdapter(this));
        recyclerView.addOnScrollListener(scrollListener = new PagingScrollListener(this));

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
        Intent intent = new Intent(getActivity(), ChatroomActivity.class);
        intent.putExtra("chat_name", getString(R.string.new_chat));
        startActivity(intent);
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
        adapter.setChatsGroupedByDate(chatsGroupedByDate);
    }

    @Override
    public void showMoreChatsByDate(List<Object> chatsGroupedByDate) {
        adapter.addChatsGroupedByDate(chatsGroupedByDate);
    }

    @Override
    public void pageLoaded() {
        scrollListener.loadingComplete();
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        presenter.searchChatsByName(newQuery); // TODO: throttle
    }

    @Override
    public void onChatClick(Chat chat) {
        Intent intent = new Intent(getActivity(), ChatroomActivity.class);
        intent.putExtra("chat_id", chat.getId());
        intent.putExtra("chat_name", chat.getName());
        startActivity(intent);
    }

    @Override
    public void loadPage() {
        presenter.loadMoreChats();
    }
}
