package com.johnpetitto.orachat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends Fragment {
    @BindView(R.id.login_email) EditText email;
    @BindView(R.id.login_password) EditText password;
    @BindView(R.id.login_button) Button button;
    private Unbinder unbinder;

    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        userModel = ((OraChatApplication) getContext().getApplicationContext()).getUserModel();
        return rootView;
    }

    @OnClick(R.id.login_button)
    public void login(View view) {
        userModel.loginUser(email.getText().toString(), password.getText().toString()).subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
