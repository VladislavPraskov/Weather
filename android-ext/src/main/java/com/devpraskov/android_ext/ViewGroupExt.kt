package com.devpraskov.android_ext

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.ChipGroup

/**
 * Extension method to inflate layout for ViewGroup.
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}


/**
 * Extension method to get views by tag for ViewGroup.
 */
fun ViewGroup.getViewsByTag(tag: String): ArrayList<View> {
    val views = ArrayList<View>()
    val childCount = childCount
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            views.addAll(child.getViewsByTag(tag))
        }

        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            views.add(child)
        }

    }
    return views
}

/**
 * Extension method to remove views by tag ViewGroup.
 */
fun ViewGroup.removeViewsByTag(tag: String) {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.removeViewsByTag(tag)
        }

        if (child.tag == tag) {
            removeView(child)
        }
    }
}


/**
 * Extension method to get list objects attached to every chip as tag
 */
fun <T : Any> ChipGroup?.getAttachedObject(): List<T>? {
    return this?.children?.mapNotNull { it.tag as? T }
}

fun ViewGroup.setBackgroundShapeColor(colorId: Int? = null, colorStr: String? = null) {
    if (colorId != null)
        this.background.setTint(getColor(colorId))
    else if (colorStr != null)
        this.background.setTint(Color.parseColor(colorStr))

}