package com.johnpetitto.orachat

import android.widget.EditText

val EditText.trimmedText: String
    get() = text.toString().trim()
