package com.johnpetitto.orachat.ui.chats

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnpetitto.orachat.R
import com.johnpetitto.orachat.data.chat.Chat
import com.johnpetitto.orachat.getTimeAgo
import kotlinx.android.synthetic.main.list_header_chat.view.*
import kotlinx.android.synthetic.main.list_item_chat.view.*

private const val HEADER_TYPE = 0
private const val ITEM_TYPE = 1

class ChatsAdapter(
        private val listener: OnChatClickListener,
        private var chatData: MutableList<Any> = mutableListOf()):
        RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    interface OnChatClickListener {
        fun onChatClick(chat: Chat)
    }

    override fun getItemCount() = chatData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        return when (viewType) {
            HEADER_TYPE -> VHHeader(inflater.inflate(R.layout.list_header_chat, parent, false))
            else -> VHItem(inflater.inflate(R.layout.list_item_chat, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder!!.itemViewType) {
            HEADER_TYPE -> (holder as VHHeader).bind(chatData[position] as String)
            ITEM_TYPE -> (holder as VHItem).bind(chatData[position] as Chat)
        }
    }

    override fun getItemViewType(position: Int) = when(chatData[position]) {
        is String -> HEADER_TYPE
        else -> ITEM_TYPE
    }

    fun setChatData(chatData: List<Any>) {
        this.chatData.clear()
        this.chatData.addAll(chatData)
        notifyDataSetChanged()
    }

    fun addChatData(chatData: List<Any>) {
        val start = this.chatData.size
        this.chatData.addAll(chatData)
        notifyItemRangeChanged(start, chatData.size)
    }

    inner class VHHeader(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(header: String) {
            itemView.headerLabel.text = if (header == "Today") {
                itemView.context.getString(R.string.today)
            } else {
                header
            }
        }
    }

    inner class VHItem(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(chat: Chat) {
            val ctx = itemView.context

            val ownerName = chat.users.first().name
            val chatName = ctx.getString(R.string.chat_item_name, chat.name, ownerName)
            itemView.chatName.text = chatName

            val lastMessage = chat.lastChatMessage
            val lastUserName = lastMessage.user.name
            val timeAgo = getTimeAgo(ctx, lastMessage.createdAt)
            val lastUpdate = ctx.getString(R.string.chat_message_timestamp, lastUserName, timeAgo)
            itemView.lastUpdate.text = lastUpdate
            itemView.lastMessage.text = lastMessage.message

            itemView.setOnClickListener { listener.onChatClick(chatData[adapterPosition] as Chat) }
        }
    }
}
