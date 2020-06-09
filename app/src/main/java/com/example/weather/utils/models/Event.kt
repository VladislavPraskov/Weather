package com.example.weather.utils.models

//https://github.com/mitchtabian/MVIExample/blob/33be93b487f23b650fa217c0250043d032fdd7e3/app/src/main/java/com/codingwithmitch/mviexample/util/Event.kt
/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
class Event<T>(private val content: T? = null) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getIfNotBeenHandled(): T? {
        return if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun getAnyway(): T? = content

    override fun toString(): String {
        return "Event(content=$content,hasBeenHandled=$hasBeenHandled)"
    }
}

fun <T> T?.toEvent(): Event<T?> {
    return Event(this)
}

