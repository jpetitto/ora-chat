package com.johnpetitto.orachat.ui.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import com.johnpetitto.orachat.ui.InputValidator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterFragment extends Fragment implements RegisterView {
    @BindView(R.id.register_name_layout) TextInputLayout nameLayout;
    @BindView(R.id.register_name) EditText name;
    @BindView(R.id.register_email_layout) TextInputLayout emailLayout;
    @BindView(R.id.register_email) EditText email;
    @BindView(R.id.register_password_layout) TextInputLayout passwordLayout;
    @BindView(R.id.register_password) EditText password;
    @BindView(R.id.register_confirm_layout) TextInputLayout confirmLayout;
    @BindView(R.id.register_confirm) EditText confirm;
    @BindView(R.id.register_button) Button button;
    private Unbinder unbinder;

    private AccountAccessActivity activity;
    private RegisterPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        activity = (AccountAccessActivity) getActivity();
        presenter = new RegisterPresenter(this, activity.getUserModel());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.register_button)
    public void register(View view) {
        boolean validated = InputValidator.validateInputs(
                InputValidator.nameValidator(nameLayout),
                InputValidator.emailValidator(emailLayout),
                InputValidator.passwordValidator(passwordLayout),
                new InputValidator(confirmLayout, getString(R.string.confirm_error)) {
                    @Override
                    public boolean validate(String input) {
                        return StringUtils.getTrimmedInput(password).equals(input);
                    }
                }
        );

        if (validated) {
            presenter.register(
                    StringUtils.getTrimmedInput(name),
                    StringUtils.getTrimmedInput(email),
                    StringUtils.getTrimmedInput(password),
                    StringUtils.getTrimmedInput(confirm)
            );
        }
    }

    @Override
    public void showLoading(boolean show) {
        activity.showProgressBar(show);
    }

    @Override
    public void showRegistrationFailure() {
        Toast.makeText(getContext(), R.string.registration_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMain() {
        activity.startMainActivity();
    }
}
