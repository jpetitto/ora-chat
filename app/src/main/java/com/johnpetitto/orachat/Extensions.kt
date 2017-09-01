package com.johnpetitto.orachat

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.EditText

val EditText.string: String
    get() = text.toString()

val EditText.trimmedText: String
    get() = text.toString().trim()

fun Activity.startActivity(clazz: Class<*>, finish: Boolean = false) {
    startActivity(Intent(this, clazz))
    if (finish) finish()
}

fun View.show(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}
