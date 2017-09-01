package com.johnpetitto.orachat.ui.chatroom

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.johnpetitto.orachat.R
import com.johnpetitto.orachat.data.chat.ChatMessage
import com.johnpetitto.orachat.data.user.User
import com.johnpetitto.orachat.dateFormatter
import com.johnpetitto.orachat.getTimeAgo
import kotlinx.android.synthetic.main.list_item_chat_message.view.*
import java.util.*

private const val USER_ID = 0L // for demo purposes

class ChatroomAdapter(
        private val normalPadding: Int,
        private val startPadding: Int,
        private val endPadding: Int,
        val onMessageSelect: (message: ChatMessage) -> Unit):
        RecyclerView.Adapter<ChatroomAdapter.VHItem>()
{
    private val messages: MutableList<ChatMessage> = mutableListOf()
    private var selectedMessage: ChatMessage? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHItem {
        val inflater = LayoutInflater.from(parent!!.context)
        val itemView = inflater.inflate(R.layout.list_item_chat_message, parent, false)
        return VHItem(itemView)
    }

    override fun onBindViewHolder(holder: VHItem?, position: Int) {
        holder!!.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    fun setMessages(messages: List<ChatMessage>) {
        this.messages.clear()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }

    fun addMessage(message: ChatMessage) {
        messages.add(0, message)
        notifyItemInserted(0)
    }

    fun addMessages(messages: List<ChatMessage>) {
        val start = this.messages.size
        this.messages.addAll(messages)
        notifyItemRangeInserted(start, messages.size)
    }

    // for demo purposes since API returns static data
    fun addUserMessage(message: String) {
        val createdAt = dateFormatter.format(Date())
        val chatMessage = ChatMessage(USER_ID, message, createdAt, User("You"))
        addMessage(chatMessage)
    }

    fun removeMessage(message: ChatMessage) {
        val index = messages.indexOf(message)
        messages.remove(message)
        notifyItemRemoved(index)
    }

    fun deselectMessage() {
        val index = messages.indexOf(selectedMessage)
        selectedMessage = null
        notifyItemChanged(index)
    }

    fun setSelectedMessage(message: ChatMessage) {
        // message may already be selected
        if (selectedMessage == message) return

        val previousSelectedMessage = selectedMessage
        selectedMessage = message
        notifyItemChanged(messages.indexOf(message))

        // update previously selected message if there was one
        if (previousSelectedMessage != null) {
            notifyItemChanged(messages.indexOf(previousSelectedMessage))
        }
    }

    inner class VHItem(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(chatMessage: ChatMessage) {
            val ctx = itemView.context
            itemView.message.text = chatMessage.message

            val userName = chatMessage.user.name
            val timeAgo = getTimeAgo(ctx, chatMessage.createdAt)
            val timestamp = ctx.getString(R.string.chat_message_timestamp, userName, timeAgo)
            itemView.timestamp.text = timestamp

            (itemView as LinearLayout).let {
                // switch bubble direction for user message
                if (chatMessage.id == USER_ID) {
                    itemView.gravity = Gravity.END
                    itemView.message.setPaddingRelative(normalPadding, normalPadding, endPadding, normalPadding)
                    itemView.message.setBackgroundResource(R.drawable.color_state_chat_bubble_right)
                } else {
                    itemView.gravity = Gravity.START
                    itemView.message.setPaddingRelative(startPadding, normalPadding, normalPadding, normalPadding)
                    itemView.message.setBackgroundResource(R.drawable.color_state_chat_bubble_left)
                }
            }

            itemView.message.isSelected = chatMessage == selectedMessage

            itemView.setOnLongClickListener {
                setSelectedMessage(chatMessage)
                onMessageSelect(chatMessage)
                true
            }
        }
    }
}
