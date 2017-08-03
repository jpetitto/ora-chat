package com.johnpetitto.orachat.ui.chatroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.TimeUtils;
import com.johnpetitto.orachat.data.chat.ChatMessage;
import com.johnpetitto.orachat.data.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.VHItem> {
    private static final int USER_ID = 0; // for demo purposes

    private List<ChatMessage> messages = new ArrayList<>();

    private int normalPadding;
    private int startPadding;
    private int endPadding;

    public ChatroomAdapter(int normalPadding, int startPadding, int endPadding) {
        this.normalPadding = normalPadding;
        this.startPadding = startPadding;
        this.endPadding = endPadding;
    }

    @Override
    public VHItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_chat_message, parent, false);
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

        // switch bubble direction for user message
        if (message.getUserId() == USER_ID) {
            ((LinearLayout) holder.itemView).setGravity(Gravity.END);
            holder.message.setPaddingRelative(normalPadding, normalPadding, endPadding, normalPadding);
            holder.message.setBackgroundResource(R.drawable.chat_bubble_right);
        } else {
            ((LinearLayout) holder.itemView).setGravity(Gravity.START);
            holder.message.setPaddingRelative(startPadding, normalPadding, normalPadding, normalPadding);
            holder.message.setBackgroundResource(R.drawable.chat_bubble_left);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessage message) {
        messages.add(0, message);
        notifyItemInserted(0);
    }

    public void addMessages(List<ChatMessage> chatMessages) {
        int start = messages.size();
        messages.addAll(chatMessages);
        notifyItemRangeInserted(start, chatMessages.size());
    }

    // for demo purposes since API returns static data
    public void addUserMessage(String message) {
        String createdAt = TimeUtils.dateFormatter.format(new Date());
        ChatMessage chatMessage = new ChatMessage(USER_ID, message, createdAt, new User("You"));
        addMessage(chatMessage);
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
