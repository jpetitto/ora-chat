package com.johnpetitto.orachat.ui.chats

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.johnpetitto.orachat.OraChatApplication
import com.johnpetitto.orachat.R
import com.johnpetitto.orachat.show
import com.johnpetitto.orachat.ui.PagingScrollListener
import com.johnpetitto.orachat.ui.chatroom.ChatroomActivity
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment: Fragment(), ChatsView, PagingScrollListener.OnPageListener {
    private lateinit var presenter: ChatsPresenter
    private val scrollListener = PagingScrollListener(this)
    private val chatsAdapter = ChatsAdapter { chat ->
        startActivity(Intent(activity, ChatroomActivity::class.java).apply {
            putExtra("chat_id", chat.id)
            putExtra("chat_name", chat.name)
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatsAdapter
            addOnScrollListener(scrollListener)
        }

        search.setOnQueryChangeListener { _, newQuery -> presenter.searchChatsByName(newQuery) }

        newChat.setOnClickListener {
            startActivity(Intent(activity, ChatroomActivity::class.java).apply {
                putExtra("chat_name", getString(R.string.new_chat))
            })
        }

        val model = (context.applicationContext as OraChatApplication).chatModel
        presenter = ChatsPresenter(this, model)
        presenter.getAllChatsByDate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    override fun showLoading(show: Boolean) {
        progress.show(show)
        recyclerView.show(!show)
    }

    override fun showError() {
        Toast.makeText(context, R.string.generic_error, Toast.LENGTH_SHORT).show()
    }

    override fun displayChatData(chatData: List<Any>) = chatsAdapter.setChatData(chatData)
    override fun showMoreChatData(chatData: List<Any>) = chatsAdapter.addChatData(chatData)

    override fun pageLoaded() = scrollListener.loadingComplete()
    override fun loadPage() = presenter.loadMoreChats()
}
