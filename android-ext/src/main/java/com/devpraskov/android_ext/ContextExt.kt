package com.devpraskov.android_ext

import android.app.*
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/**
 * Extension method to provide simpler access to {@link ContextCompat#getColor(int)}.
 */
fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

/**
 * Extension method to find a device width in pixels
 */
inline val Context.displayWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * Extension method to find a device height in pixels
 */
inline val Context.displayHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * Extension method to get displayMetrics in Context.displayMetricks
 */
inline val Context.displayMetricks: DisplayMetrics
    get() = resources.displayMetrics

/**
 * Extension method to get LayoutInflater
 */
inline val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)

/**
 * Extension method to get a new Intent for an Activity class
 */
inline fun <reified T : Any> Context.intent() = Intent(this, T::class.java)

/**
 * Create an intent for [T] and apply a lambda on it
 */
inline fun <reified T : Any> Context.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}

/**
 * Extension method to startActivity for Context.
 */
inline fun <reified T : Activity> Context?.startActivity() =
    this?.startActivity(Intent(this, T::class.java))

/**
 * Extension method to start Service for Context.
 */
inline fun <reified T : Service> Context?.startService() =
    this?.startService(Intent(this, T::class.java))


/**
 * Extension method to show toast for Context.
 */
fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }

/**
 * Extension method to show toast for Context.
 */
fun Context?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, textId, duration).show() }

/**
 * Extension method to Get Integer resource for Context.
 */
fun Context.getInteger(@IntegerRes id: Int) = resources.getInteger(id)

/**
 * Extension method to Get Boolean resource for Context.
 */
fun Context.getBoolean(@BoolRes id: Int) = resources.getBoolean(id)

/**
 * Extension method to Get Color for resource for Context.
 */
fun Context.getColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

/**
 * Extension method to Get Drawable for resource for Context.
 */
fun Context.getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/**
 * InflateLayout
 */
fun Context.inflateLayout(
    @LayoutRes layoutId: Int, parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)

/**
 * Extension method to get inputManager for Context.
 */
inline val Context.inputManager: InputMethodManager?
    get() = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
/**
 * Extension method to get notificationManager for Context.
 */
inline val Context.notificationManager: NotificationManager?
    get() = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
/**
 * Extension method to get keyguardManager for Context.
 */
inline val Context.keyguardManager: KeyguardManager?
    get() = getSystemService(KEYGUARD_SERVICE) as? KeyguardManager

/**
 * Extension method to get alarmManager for Context.
 */
inline val Context.alarmManager: AlarmManager?
    get() = getSystemService(ALARM_SERVICE) as? AlarmManager

/**
 * Extension method to show notification for Context.
 */
inline fun Context.notification(body: NotificationCompat.Builder.() -> Unit): Notification {
    val builder = NotificationCompat.Builder(this) //todo модифицировать легаси
    builder.body()
    return builder.build()
}

/**
 * Extension method to provide quicker access to the [LayoutInflater] from [Context].
 */
fun Context.getLayoutInflater() =
    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
