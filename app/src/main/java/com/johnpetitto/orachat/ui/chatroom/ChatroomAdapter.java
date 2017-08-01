package com.johnpetitto.orachat.ui.chatroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.TimeUtils;
import com.johnpetitto.orachat.data.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.VHItem> {
    private List<ChatMessage> messages = new ArrayList<>();

    @Override
    public VHItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_chatroom, parent, false);
        return new VHItem(itemView);
    }

    @Override
    public void onBindViewHolder(VHItem holder, int position) {
        Context context = holder.itemView.getContext();
        ChatMessage message = messages.get(position);

        holder.message.setText(message.getMessage());

        String userName = message.getUser().getName();
        String timeAgo = TimeUtils.getTimeAgo(context, message.getCreatedAt());
        String timestamp = context.getString(R.string.chat_message_timestamp, userName, timeAgo);
        holder.timestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage message) {
        messages.add(0, message);
        notifyItemInserted(0);
    }

    static class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.chatroom_message) TextView message;
        @BindView(R.id.chatroom_timestamp) TextView timestamp;

        public VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
