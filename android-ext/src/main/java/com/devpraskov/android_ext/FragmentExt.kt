package com.devpraskov.android_ext

import android.content.Context
import android.os.Build
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.getStringOrNull(@StringRes res: Int?): String? {
    res ?: return null
    return getString(res)
}

fun Fragment.getColor(@ColorRes id: Int) = ContextCompat.getColor(requireContext(), id)


fun Fragment.statusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = this.requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor =  color
    }
}

fun Fragment.hideSoftKeyboard() {
    if (requireActivity().currentFocus != null) {
        val inputMethodManager = requireActivity().getSystemService(Context
            .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}