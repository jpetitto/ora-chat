package com.johnpetitto.orachat

import android.app.Activity
import android.content.Intent
import android.widget.EditText

val EditText.trimmedText: String
    get() = text.toString().trim()

fun Activity.startActivity(clazz: Class<*>, finish: Boolean = false) {
    startActivity(Intent(this, clazz))
    if (finish) finish()
}
