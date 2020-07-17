package com.devpraskov.android_ext

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent.ACTION_UP
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun EditText?.enterClick(block: (String) -> Unit) {
    this?.setOnEditorActionListener { v, actionId, event ->
        if ((event == null || event.action == ACTION_UP) && actionId == EditorInfo.IME_ACTION_DONE) {
            block.invoke(this.text.toString())
            true
        } else false
    }
}

fun EditText?.debounceAfterTextChanged(
    coroutineScope: CoroutineScope,
    onTextChanged: (String) -> Unit
) {
    this?.addTextChangedListener(object : TextWatcher {
        var debouncePeriod: Long = 400
        private var searchJob: Job? = null
        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                text?.toString()?.let { nextText ->
                    delay(debouncePeriod)
                    onTextChanged.invoke(nextText)
                }
            }
        }
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    })
}