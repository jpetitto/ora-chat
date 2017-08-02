package com.johnpetitto.orachat.ui.register;

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

public class RegisterFragment extends Fragment implements RegisterView {
    @BindView(R.id.register_name) EditText name;
    @BindView(R.id.register_email) EditText email;
    @BindView(R.id.register_password) EditText password;
    @BindView(R.id.register_confirm) EditText confirm;
    @BindView(R.id.register_button) Button button;
    private Unbinder unbinder;

    private AccountAccessActivity activity;
    private RegisterPresenter presenter;

    private TextWatcher registerTextWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(String text) {
            boolean validName = getInputName().length() > 0;
            boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(getInputEmail()).matches();

            String password = getInputPassword();
            boolean validPassword = password.length() > 0 && password.equals(getInputConfirm());

            button.setEnabled(validName && validEmail && validPassword);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        activity = (AccountAccessActivity) getActivity();
        presenter = new RegisterPresenter(this, activity.getUserModel());

        name.addTextChangedListener(registerTextWatcher);
        email.addTextChangedListener(registerTextWatcher);
        password.addTextChangedListener(registerTextWatcher);
        confirm.addTextChangedListener(registerTextWatcher);

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
        presenter.register(getInputName(), getInputEmail(), getInputPassword(), getInputConfirm());
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

    private String getInputName() {
        return name.getText().toString().trim();
    }

    private String getInputEmail() {
        return email.getText().toString().trim();
    }

    private String getInputPassword() {
        return password.getText().toString();
    }

    private String getInputConfirm() {
        return confirm.getText().toString();
    }
}
