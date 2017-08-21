package com.johnpetitto.orachat.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.StringUtils;
import com.johnpetitto.orachat.ui.AccountAccessActivity;
import com.johnpetitto.validator.ValidatingTextInputLayout;
import com.johnpetitto.validator.Validators;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends Fragment implements LoginView {
    @BindView(R.id.login_email_layout) ValidatingTextInputLayout emailLayout;
    @BindView(R.id.login_email) EditText email;
    @BindView(R.id.login_password_layout) ValidatingTextInputLayout passwordLayout;
    @BindView(R.id.login_password) EditText password;
    @BindView(R.id.login_button) Button button;
    private Unbinder unbinder;

    private AccountAccessActivity activity;
    private LoginPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        activity = (AccountAccessActivity) getActivity();
        presenter = new LoginPresenter(this, activity.getUserModel());

        passwordLayout.setValidator(Validators.minimum(6, true));

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
        if (Validators.validate(emailLayout, passwordLayout)) {
            presenter.login(
                    StringUtils.getTrimmedInput(email),
                    StringUtils.getTrimmedInput(password)
            );
        }
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
}
