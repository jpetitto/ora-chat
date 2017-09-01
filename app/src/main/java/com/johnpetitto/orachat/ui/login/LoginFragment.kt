package com.johnpetitto.orachat.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.johnpetitto.orachat.R
import com.johnpetitto.orachat.trimmedText
import com.johnpetitto.orachat.ui.AccountAccessActivity
import com.johnpetitto.validator.Validators
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: Fragment(), LoginView {
    private lateinit var activity: AccountAccessActivity
    private lateinit var presenter: LoginPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as AccountAccessActivity
        presenter = LoginPresenter(this, activity.userModel)

        passwordLayout.setValidator(Validators.minimum(6, true))

        login.setOnClickListener {
            if (Validators.validate(emailLayout, passwordLayout)) {
                presenter.login(email.trimmedText, password.trimmedText)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    override fun showLoading(show: Boolean) = activity.showProgressBar(show)

    override fun showLoginFailure() {
        Toast.makeText(context, R.string.login_error, Toast.LENGTH_LONG).show()
    }

    override fun navigateToMain() = activity.startMainActivity()
}
