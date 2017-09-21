package com.johnpetitto.orachat.ui.chatroom

import android.content.*
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.johnpetitto.orachat.OraChatApplication
import com.johnpetitto.orachat.R
import com.johnpetitto.orachat.data.chat.ChatMessage
import com.johnpetitto.orachat.show
import com.johnpetitto.orachat.trimmedText
import com.johnpetitto.orachat.ui.PagingScrollListener
import com.johnpetitto.orachat.ui.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_chatroom.*

class ChatroomActivity:
        AppCompatActivity(),
        ChatroomView,
        PagingScrollListener.OnPageListener,
        ActionMode.Callback
{
    private lateinit var presenter: ChatroomPresenter
    private lateinit var chatroomAdapter: ChatroomAdapter
    private lateinit var chatName: String
    private lateinit var activeMessage: ChatMessage
    private val scrollListener = PagingScrollListener(this)
    private var isNewChat = false
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        chatName = intent.getStringExtra("chat_name")

        toolbar.title = chatName
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.inflateMenu(R.menu.chatroom)
        toolbar.setOnMenuItemClickListener { showEditChatNameDialog(); true }

        val normalPadding = resources.getDimension(R.dimen.chat_bubble_normal_padding).toInt()
        val startPadding = resources.getDimension(R.dimen.chat_bubble_start_padding).toInt()
        val endPadding = resources.getDimension(R.dimen.chat_bubble_end_padding).toInt()

        chatroomAdapter = ChatroomAdapter(normalPadding, startPadding, endPadding) { message ->
            activeMessage = message

            // only start action mode of it's not already visible
            if (actionMode == null) {
                actionMode = startActionMode(this)
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            adapter = chatroomAdapter
            addOnScrollListener(scrollListener)
        }

        sendButton.isEnabled = false
        sendMessage.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(text: String) {
                sendButton.isEnabled = text.trim().isNotBlank()
            }
        })

        sendButton.setOnClickListener {
            val message = sendMessage.trimmedText
            chatroomAdapter.addUserMessage(message) // for demo purposes

            if (isNewChat) {
                presenter.createNewChat(toolbar.title.toString(), message)
                isNewChat = false
            } else {
                presenter.sendMessage(message)
            }

            sendMessage.text.clear()
        }

        val model = (application as OraChatApplication).chatModel
        presenter = ChatroomPresenter(this, model)

        if (intent.hasExtra("chat_id")) {
            val chatId = intent.getLongExtra("chat_id", 0L)
            presenter.getInitialMessages(chatId)
        } else {
            isNewChat = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showLoading(show: Boolean) {
        progress.show(show)
        content.show(!show)
    }

    override fun showError() {
        Toast.makeText(this, R.string.generic_error, Toast.LENGTH_SHORT).show()
    }

    override fun showInitialMessages(messages: List<ChatMessage>) {
        chatroomAdapter.setMessages(messages)
    }

    override fun showNewMessage(message: ChatMessage) {
        chatroomAdapter.addMessage(message)
        recyclerView.scrollToPosition(0)
    }

    override fun showMoreMessages(messages: List<ChatMessage>) {
        chatroomAdapter.addMessages(messages)
    }

    override fun pageLoaded() = scrollListener.loadingComplete()
    override fun loadPage() = presenter.getMoreMessages()

    override fun onActionItemClicked(actionMode: ActionMode?, menuItem: MenuItem?): Boolean {
        when (menuItem!!.itemId) {
            R.id.share -> shareMessage()
            R.id.copy -> copyMessage()
            R.id.delete -> showDeleteMessageDialog()
        }

        actionMode!!.finish()
        return true
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode!!.menuInflater.inflate(R.menu.action_chatroom, menu)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?) = false

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        this.actionMode = null
        chatroomAdapter.deselectMessage()
    }

    private fun shareMessage() {
        startActivity(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, activeMessage.message)
            type = "text/plain"
        }, getString(R.string.share)))
    }

    private fun copyMessage() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText(null, activeMessage.message)
        Toast.makeText(this, R.string.copy_complete, Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteMessageDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(R.string.delete_message)
                .setPositiveButton(R.string.delete, { _: DialogInterface, _: Int ->
                    chatroomAdapter.removeMessage(activeMessage)
                })
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    private fun showEditChatNameDialog() {
        // create margins so EditText aligns with other dialog elements
        val margin = resources.getDimension(R.dimen.dialog_margin).toInt()
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.marginStart = margin
        params.marginEnd = margin

        val editChatName = EditText(this)
        editChatName.setText(toolbar.title)
        editChatName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        editChatName.setSelection(editChatName.text.length)
        editChatName.layoutParams = params

        val container = FrameLayout(this)
        container.addView(editChatName)

        val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.edit_chat_name)
                .setView(container)
                .setPositiveButton(R.string.apply, { _: DialogInterface, _: Int ->
                    chatName = editChatName.trimmedText
                    toolbar.title = chatName

                    // only update name with server if chat has already been created
                    if (!isNewChat) {
                        presenter.updateChatName(chatName)
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show()

        val applyButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        applyButton.isEnabled = false

        editChatName.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(text: String) {
                applyButton.isEnabled = text.isNotEmpty() && text != chatName
            }
        })

        editChatName.setOnEditorActionListener { _, _, _ ->
            if (applyButton.isEnabled) {
                applyButton.callOnClick()
            }
            true
        }
    }
}
