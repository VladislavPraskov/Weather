package com.example.weather.presenter.second

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weather.R
import kotlinx.android.synthetic.main.details_view.view.*

class DetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    var titleText: String? = ""
        set(value) {
            field = value; title?.text = field
        }

    var dataText: String? = ""
        set(value) {
            field = value; data?.text = field
        }

    var iconRes: Int? = null
        set(value) {
            field = value; iconRes?.let { icon.setImageResource(it) }
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.details_view, this, true)
        val a = context.obtainStyledAttributes(attrs, R.styleable.DetailsView, defStyle, 0)
        titleText = a.getString(R.styleable.DetailsView_titleText)
        titleText = a.getString(R.styleable.DetailsView_titleText)

        if (a.hasValue(R.styleable.DetailsView_icon)) {
            val icon = a.getResourceId(R.styleable.DetailsView_icon, 0)
            iconRes = if (icon != 0) icon else null
        }
        a.recycle()
    }
}
