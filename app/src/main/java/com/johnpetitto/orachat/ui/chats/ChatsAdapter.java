package com.johnpetitto.orachat.ui.chats;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.TimeUtilsKt;
import com.johnpetitto.orachat.data.chat.Chat;
import com.johnpetitto.orachat.data.chat.ChatMessage;
import com.johnpetitto.orachat.data.user.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int ITEM_TYPE = 1;

    private List<Object> data = new ArrayList<>();
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatsAdapter(OnChatClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HEADER_TYPE:
                holder = new VHHeader(inflater.inflate(R.layout.list_header_chat, parent, false));
                break;
            case ITEM_TYPE:
                holder = new VHItem(inflater.inflate(R.layout.list_item_chat, parent, false));
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER_TYPE:
                String date = (String) data.get(position);
                TextView header = (TextView) holder.itemView;

                if (date.equals("Today")) {
                    header.setText(R.string.today);
                } else {
                    header.setText(date);
                }

                break;
            case ITEM_TYPE:
                Chat chat = (Chat) data.get(position);
                VHItem item = (VHItem) holder;
                Context context = item.itemView.getContext();

                User chatOwner = chat.getUsers().get(0); // TODO: is first user the owner?
                String formattedName = context.getString(R.string.chat_item_name, chat.getName(), chatOwner.getName());
                item.name.setText(formattedName);

                ChatMessage lastMessage = chat.getLastChatMessage();
                String lastUserName = lastMessage.getUser().getName();
                String timeAgo = TimeUtilsKt.getTimeAgo(context, lastMessage.getCreatedAt());
                String formattedLastUpdate = context.getString(R.string.chat_message_timestamp, lastUserName, timeAgo);
                item.lastUpdate.setText(formattedLastUpdate);

                item.lastMessage.setText(lastMessage.getMessage());

                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) instanceof String ? HEADER_TYPE : ITEM_TYPE;
    }

    public void setChatsGroupedByDate(List<Object> chatsGroupedByDate) {
        data = chatsGroupedByDate;
        notifyDataSetChanged();
    }

    public void addChatsGroupedByDate(List<Object> chatsGroupedByDate) {
        int start = data.size();
        data.addAll(chatsGroupedByDate);
        notifyItemRangeInserted(start, chatsGroupedByDate.size());
    }

    @SuppressWarnings("WeakerAccess")
    class VHHeader extends RecyclerView.ViewHolder {
        VHHeader(View itemView) {
            super(itemView);
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.chats_item_name) TextView name;
        @BindView(R.id.chats_item_last_update) TextView lastUpdate;
        @BindView(R.id.chats_item_last_message) TextView lastMessage;

        VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Chat chat = (Chat) data.get(getAdapterPosition());
                    listener.onChatClick(chat);
                }
            });
        }
    }
}
