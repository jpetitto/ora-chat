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

public class RegisterFragment extends Fragment {
    @BindView(R.id.register_name) EditText name;
    @BindView(R.id.register_email) EditText email;
    @BindView(R.id.register_password) EditText password;
    @BindView(R.id.register_confirm) EditText confirm;
    @BindView(R.id.register_button) Button button;
    private Unbinder unbinder;

    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        userModel = ((OraChatApplication) getContext().getApplicationContext()).getUserModel();
        return rootView;
    }

    @OnClick(R.id.register_button)
    public void register(View view) {
        userModel.createUser(
                name.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                confirm.getText().toString()
        ).subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
