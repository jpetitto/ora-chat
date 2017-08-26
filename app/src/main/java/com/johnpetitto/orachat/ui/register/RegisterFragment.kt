package com.johnpetitto.orachat.ui.register

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
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment: Fragment(), RegisterView {
    private lateinit var activity: AccountAccessActivity
    private lateinit var presenter: RegisterPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as AccountAccessActivity
        presenter = RegisterPresenter(this, activity.userModel)

        nameLayout.setValidator(Validators.minimum(1, true))
        passwordLayout.setValidator(Validators.minimum(6, true))
        confirmLayout.setValidator { it == password.text.toString() }

        register.setOnClickListener {
            if (Validators.validate(nameLayout, emailLayout, passwordLayout, confirmLayout)) {
                presenter.register(name.trimmedText, email.trimmedText, password.trimmedText, confirm.trimmedText)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showLoading(show: Boolean) = activity.showProgressBar(show)

    override fun showRegistrationFailure() {
        Toast.makeText(context, R.string.registration_error, Toast.LENGTH_LONG).show()
    }

    override fun navigateToMain() = activity.startMainActivity()
}
