package com.johnpetitto.orachat.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.johnpetitto.orachat.OraChatApplication;
import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.data.user.UserModel;
import com.johnpetitto.orachat.ui.AccountAccessActivity;
import com.johnpetitto.orachat.ui.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AccountFragment extends Fragment implements AccountView {
    @BindView(R.id.account_content) LinearLayout content;
    @BindView(R.id.account_progress_bar) ProgressBar progressBar;
    @BindView(R.id.account_name) EditText name;
    @BindView(R.id.account_email) EditText email;
    @BindView(R.id.account_update) Button update;
    private Unbinder unbinder;

    private AccountPresenter presenter;

    private TextWatcher updateTextWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(String text) {
            update.setEnabled(getInputName().length() > 0 && getInputEmail().length() > 0);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        name.addTextChangedListener(updateTextWatcher);
        email.addTextChangedListener(updateTextWatcher);

        UserModel model = ((OraChatApplication) getContext().getApplicationContext()).getUserModel();
        presenter = new AccountPresenter(this, model);

        presenter.getCurrentUserInfo();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().setTitle(R.string.account);
        inflater.inflate(R.menu.menu_account, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.logout();

        startActivity(new Intent(getActivity(), AccountAccessActivity.class));
        getActivity().finish();

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.account_update)
    public void update(View view) {
        presenter.updateCurrentUser(name.getText().toString(), email.getText().toString());
    }

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        content.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.generic_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populateAccountInfo(String name, String email) {
        this.name.setText(name);
        this.email.setText(email);
    }

    private String getInputName() {
        return name.getText().toString().trim();
    }

    private String getInputEmail() {
        return email.getText().toString().trim();
    }
}
