package com.johnpetitto.orachat.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.johnpetitto.orachat.OraChatApplication;
import com.johnpetitto.orachat.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatsFragment extends Fragment implements ChatsView {
    @BindView(R.id.chats_recycler_view) RecyclerView recyclerView;
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
        Toast.makeText(getContext(), "Create new chat", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void displayChatsByDate(List<Object> chatsGroupedByDate) {
        adapter.setData(chatsGroupedByDate);
    }
}
