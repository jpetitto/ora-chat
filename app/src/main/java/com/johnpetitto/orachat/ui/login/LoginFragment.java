package com.johnpetitto.orachat.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.johnpetitto.orachat.ui.AccountAccessActivity;
import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.ui.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends Fragment implements LoginView {
    @BindView(R.id.login_email) EditText email;
    @BindView(R.id.login_password) EditText password;
    @BindView(R.id.login_button) Button button;
    private Unbinder unbinder;

    private AccountAccessActivity activity;
    private LoginPresenter presenter;

    private TextWatcher loginTextWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(String text) {
            boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(getInputEmail()).matches();
            boolean validPassword = getInputPassword().length() > 0;
            button.setEnabled(validEmail && validPassword);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        activity = (AccountAccessActivity) getActivity();
        presenter = new LoginPresenter(this, activity.getUserModel());

        email.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.login_button)
    public void login(View view) {
        presenter.login(getInputEmail(), getInputPassword());
    }

    @Override
    public void showLoading(boolean show) {
        activity.showProgressBar(show);
    }

    @Override
    public void showLoginFailure() {
        Toast.makeText(getContext(), R.string.login_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMain() {
        activity.startMainActivity();
    }

    private String getInputEmail() {
        return email.getText().toString().trim();
    }

    private String getInputPassword() {
        return password.getText().toString();
    }
}
