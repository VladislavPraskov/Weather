package com.devpraskov.android_ext

import android.widget.TextView

//For MVI. Set text only if it's changed
var TextView.newText: String?
    get() = this.text.toString()
    set(value) {
        when (newText) {
            value -> return // no changes
            null -> this.text = ""
            else -> this.text = value
        }
    }