package com.johnpetitto.orachat.ui.account

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.johnpetitto.orachat.*
import com.johnpetitto.orachat.ui.AccountAccessActivity
import com.johnpetitto.orachat.ui.SimpleTextWatcher
import com.johnpetitto.validator.Validators
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment: Fragment(), AccountView {
    private lateinit var presenter: AccountPresenter
    private  var originalName: String = ""
    private  var originalEmail: String = ""

    private val updateTextWatcher = object : SimpleTextWatcher() {
        override fun onTextChanged(text: String) {
            update.isEnabled = name.string != originalName || email.string != originalEmail
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        name.addTextChangedListener(updateTextWatcher)
        email.addTextChangedListener(updateTextWatcher)
        nameLayout.setValidator(Validators.minimum(1, true))

        update.setOnClickListener {
            if (Validators.validate(nameLayout, emailLayout)) {
                presenter.updateCurrentUser(name.trimmedText, email.trimmedText)
            }
        }

        val model = (context.applicationContext as OraChatApplication).userModel
        presenter = AccountPresenter(this, model)
        presenter.getCurrentUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity.setTitle(R.string.account)
        inflater!!.inflate(R.menu.account, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        presenter.logout()
        activity.startActivity(AccountAccessActivity::class.java, true)
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading(show: Boolean) {
        progress.show(show)
        content.show(!show)
    }

    override fun showError() {
        Toast.makeText(context, R.string.generic_error, Toast.LENGTH_SHORT).show()
    }

    override fun populateAccountInfo(name: String, email: String) {
        name.let {
            originalName = it
            this.name.setText(it)
        }

        email.let {
            originalEmail = it
            this.email.setText(it)
        }
    }
}
