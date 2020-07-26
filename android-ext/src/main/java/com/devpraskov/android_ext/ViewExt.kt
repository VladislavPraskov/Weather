package com.devpraskov.android_ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


/**
 * Extension method to provide simpler access to {@link View#getResources()#getString(int)}.
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

/**
 * Extension method to remove the required boilerplate for running code after a view has been
 * inflated and measured.
 *
 * @author Antonio Leiva
 * @see <a href="https://antonioleiva.com/kotlin-ongloballayoutlistener/>Kotlin recipes: OnGlobalLayoutListener</a>
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

/**
 * Extension method to get ClickableSpan.
 * e.g.
 * val loginLink = getClickableSpan(context.getColorCompat(R.color.colorAccent), { })
 */
fun View.doOnLayout(onLayout: (View) -> Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View, left: Int, top: Int, right: Int, bottom: Int,
            oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
        ) {
            if (onLayout(view)) {
                view.removeOnLayoutChangeListener(this)
            }
        }
    })
}

/**
 * Extension method to provide quicker access to the [LayoutInflater] from a [View].
 */
//fun View.getLayoutInflater() = context.getLayoutInflater()

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}

/**
 * Extension method to update padding of view.
 *
 */
fun View.updatePadding(
    paddingStart: Int = getPaddingStart(),
    paddingTop: Int = getPaddingTop(),
    paddingEnd: Int = getPaddingEnd(),
    paddingBottom: Int = getPaddingBottom()
) {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
}

/**
 * Extension method to set View's left padding.
 */
fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)


/**
 * Extension method to set View's right padding.
 */
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)


/**
 * Extension method to set View's top padding.
 */
fun View.setPaddingTop(value: Int) =
    setPaddingRelative(paddingStart, value, paddingEnd, paddingBottom)


/**
 * Extension method to set View's bottom padding.
 */
fun View.setPaddingBottom(value: Int) =
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, value)


/**
 * Extension method to set View's start padding.
 */
fun View.setPaddingStart(value: Int) =
    setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)


/**
 * Extension method to set View's end padding.
 */
fun View.setPaddingEnd(value: Int) =
    setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)


/**
 * Extension method to set View's horizontal padding.
 */
fun View.setPaddingHorizontal(value: Int) =
    setPaddingRelative(value, paddingTop, value, paddingBottom)


/**
 * Extension method to set View's vertical padding.
 */
fun View.setPaddingVertical(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, value)


/**
 * Extension method to set View's height.
 */
fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}


/**
 * Extension method to set View's width.
 */
fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}


/**
 * Extension method to resize View with height & width.
 */
fun View.resize(width: Int, height: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
}


/**
 * Extension method to access the view's children as a list
 */
val ViewGroup.children: List<View>
    get() = (0 until childCount).map { getChildAt(it) }

/**
 * Set an onclick listener
 */
fun <T : View?> T?.onClick(block: (T?) -> Unit) {
    this?.setOnClickListener { block(it as T) }
}


/**
 * Extension method to set OnClickListener on a view.
 */
fun <T : View> T.longClick(block: (T?) -> Boolean) = setOnLongClickListener { block(it as? T?) }

/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}


/**
 * Show the view if [condition] returns true
 * (visibility = View.VISIBLE)
 */
inline fun View.showIf(condition: () -> Boolean): View {
    if (visibility != View.VISIBLE && condition()) {
        visibility = View.VISIBLE
    }
    return this
}


/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}


/**
 * Hide with alpha animation the view. (visibility = View.INVISIBLE)
 */
fun View.hideAnimateAlpha(duration: Long = 200): View {
    if (visibility == View.VISIBLE) {
        animate()
            .alpha(0f)
            .setDuration(duration)
            .withEndAction {
                visibility = View.GONE
            }
            .start()
    }
    return this
}

/**
 * Show with alpha animation the view. (visibility = View.INVISIBLE)
 */
fun View.showAnimateAlpha(duration: Long = 200): View {
    if (visibility in listOf(View.INVISIBLE, View.GONE)) {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(duration)
            .start()
    }
    return this
}


/**
 * Hide the view if [predicate] returns true
 * (visibility = View.INVISIBLE)
 */
inline fun View.hideIf(predicate: () -> Boolean): View {
    if (visibility != View.INVISIBLE && predicate()) {
        visibility = View.INVISIBLE
    }
    return this
}


/**
 * Remove the view (visibility = View.GONE)
 */
fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}


fun View?.visibility(isVisible: Boolean) {
    this?.apply { visibility = if (isVisible) View.VISIBLE else View.INVISIBLE }
}

fun View?.visibilityGone(isVisible: Boolean) {
    this?.apply { visibility = if (isVisible) View.VISIBLE else View.GONE }
}

/**
 * Toggle a view's visibility
 */
fun View.toggleVisibility(): View {
    visibility = if (visibility == View.VISIBLE) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
    return this
}

/**
 * Toggle a view's visibility/gone
 */
fun View.toggleRemove(): View {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
}

/**
 * Extension method to get a view as bitmap.
 */
fun View.getBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    canvas.save()
    return bmp
}

/**
 * Show a snackbar with [message]
 */
fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) = snack(message, length) {}

/**
 * Show a snackbar with [messageRes]
 */
fun View.snack(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG) =
    snack(messageRes, length) {}

/**
 * Show a snackbar with [message], execute [f] and show it
 */
inline fun View.snack(
    message: String,
    @BaseTransientBottomBar.Duration length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

/**
 * Show a snackbar with [messageRes], execute [f] and show it
 */
inline fun View.snack(
    @StringRes messageRes: Int,
    @BaseTransientBottomBar.Duration length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, messageRes, length)
    snack.f()
    snack.show()
}

/**
 * Find a parent of type [parentType], assuming it exists
 */
tailrec fun <T : View> View.findParent(parentType: Class<T>): T {
    return if (parent.javaClass == parentType) parent as T else (parent as View).findParent(
        parentType
    )
}

/**
 * Like findViewById but with type interference, assume the view exists
 */
inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById<T>(id)

/**
 *  Like findViewById but with type interference, or null if not found
 */
inline fun <reified T : View> View.findOptional(@IdRes id: Int): T? = findViewById(id) as? T

/**
 * Extension method underLine for TextView.
 */
fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}


/**
 * Extension method deleteLine for TextView.
 */
fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}


/**
 * Extension method bold for TextView.
 */
fun TextView.bold() {
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
}

/**
 * Extension method to set different color for substring TextView.
 */
fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, color)),
            start,
            start + substring.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannable
    } catch (e: Exception) {
        Log.d(
            "ViewExtensions",
            "exception in setColorOfSubstring, text=$text, substring=$substring",
            e
        )
    }
}

/**
 * Extension method to set font for TextView.
 */
fun TextView.font(font: String) {
    typeface = Typeface.createFromAsset(context.assets, "fonts/$font.ttf")
}

/**
 * get color from view
 */
fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)

/**
 * get color from view
 */
fun View.getColorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(context, id)

/**
 * get color from view
 */
var ImageView.color: Int
    get() = 0
    set(value) {
        this.imageTintList = ContextCompat.getColorStateList(context, value)
    }

/**
 * Extension method to set a drawable to the left of a TextView.
 */
fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

/**
 * Extension method to get value from EditText.
 */
val EditText.value
    get() = text.toString()

/**
 * Extension method to get value from EditText.
 */
fun View.setBackgroundColorTint(colorRes: Int) {
    this.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
}

/**
 * Extension method to get value from EditText.
 */
fun View.setBackgroundColorTint(colorRes: Int? = null, color: Int? = null) {
    this.backgroundTintList = when {
        colorRes != null -> ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
        color != null -> ColorStateList.valueOf(color)
        else -> this.backgroundTintList
    }

}

/**
 * Sometimes stateListAnimator has slow reaction on state "pressed", this extension accelerates start of animation
 */
fun View?.setPressedOnTouch() {
    this?.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> v.isPressed = true
            MotionEvent.ACTION_UP -> v.isPressed = false
        }
        false
    }
    
/**animate expand view height(gone -> visible). Parent view height should be wrap_content*/
fun View.expand(interpolator: TimeInterpolator? = null, duration: Long = 300) {
    this.apply {
        measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = measuredHeight
        val initialHeight = 1
        layoutParams.height = initialHeight
        visibility = View.VISIBLE
        val va = ValueAnimator.ofInt(initialHeight, targetHeight)
        va.addUpdateListener { animation ->
            layoutParams.height = (animation.animatedValue as Int)
            requestLayout()
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        va.duration = duration
        interpolator?.let { va.interpolator = interpolator }
        va.start()
    }
}

/**animate collapse view height(visible -> gone). Parent view height should be wrap_content*/
fun View.collapse(interpolator: TimeInterpolator? = null, duration: Long = 300) {
    this.apply {
        val initialHeight = measuredHeight
        val va = ValueAnimator.ofInt(initialHeight, 0)
        va.addUpdateListener { animation ->
            layoutParams.height = (animation.animatedValue as Int)
            requestLayout()
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        interpolator?.let { va.interpolator = interpolator }
        va.duration = duration
        va.start()
    }
}
}
