package com.johnpetitto.orachat;

import android.widget.EditText;

public final class StringUtils {
    private StringUtils() {}

    public static String getTrimmedInput(EditText editText) {
        return editText.getText().toString().trim();
    }
}
