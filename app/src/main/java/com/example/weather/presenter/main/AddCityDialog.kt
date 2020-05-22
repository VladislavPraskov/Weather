package com.example.weather.presenter.main

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.devpraskov.android_ext.inflater
import com.devpraskov.android_ext.onClick
import com.example.weather.R
import kotlinx.android.synthetic.main.add_city_dialog.*


class AddCityDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setView(requireActivity().inflater.inflate(R.layout.add_city_dialog, null))
        val alert = builder.create()
        positiveButton?.onClick {
            val intent = Intent()
            intent.putExtra(CITY_EXTRA_KEY, editText?.text?.toString()?.trim() ?: "")
            targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, intent)
        }
        return alert
    }

    companion object {
        const val ADD_CITY_REQUEST_CODE = 2344
        const val CITY_EXTRA_KEY = "city_extra_key"
    }
}