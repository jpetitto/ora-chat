package com.johnpetitto.orachat.ui;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class SimpleTextWatcher implements TextWatcher {
    public abstract void onTextChanged(String text);

    @Override
    public final void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public final void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        onTextChanged(charSequence.toString());
    }

    @Override
    public final void afterTextChanged(Editable editable) {}
}
