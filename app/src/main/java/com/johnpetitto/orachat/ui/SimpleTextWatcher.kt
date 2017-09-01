package com.johnpetitto.orachat.ui

import android.text.Editable
import android.text.TextWatcher

abstract class SimpleTextWatcher: TextWatcher {
    abstract fun onTextChanged(text: String)

    override final fun afterTextChanged(p0: Editable?) {}
    override final fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override final fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChanged(p0.toString())
    }
}
