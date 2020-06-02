package com.example.weather.utils.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

//The behavior is similar to https://developer.android.com/reference/android/support/v4/widget/ContentLoadingProgressBar?hl=ru
class SwipeRefreshWithDelay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    private var isRefreshingFlag = false
    private var delayHandler = Handler()
    private var lastCloseTime = 0L
    private val currentTimeProvider
        get() = System.currentTimeMillis()
    private val canRefreshImmediately
        get() = currentTimeProvider - lastCloseTime > DELAY_TIME_AFTER_CLOSE && isRefreshingFlag

    private val delayRunnable = Runnable {
        super.setRefreshing(isRefreshingFlag)
        delayHandler.removeMessages(0)
        if (!isRefreshingFlag) lastCloseTime = currentTimeProvider
    }

    override fun setRefreshing(refreshing: Boolean) {
        isRefreshingFlag = refreshing
        when {
            canRefreshImmediately -> postNew(0)
            isRefreshingFlag -> postNew(DELAY_TIME_OPEN)
            else -> postNew(DELAY_TIME_CLOSE)
        }
    }

    private fun postNew(delay: Long) {
        if (!delayHandler.hasMessages(0)) {
            // delay * 2 - гарантия, что message(0) , будет жив на момент запуска delayRunnable
            delayHandler.sendEmptyMessageDelayed(0, delay * 2)
            delayHandler.postDelayed(delayRunnable, delay)
        }
    }

    override fun setOnRefreshListener(listener: OnRefreshListener?) {
        val refresh = OnRefreshListener {
            isRefreshingFlag = true
            delayHandler.removeMessages(0)
            delayHandler.removeCallbacks(delayRunnable)
            postNew(DELAY_TIME_OPEN)
            listener?.onRefresh()
        }
        super.setOnRefreshListener(refresh)
    }

    companion object {
        const val DELAY_TIME_CLOSE: Long = 1000
        const val DELAY_TIME_OPEN: Long = 1000
        const val DELAY_TIME_AFTER_CLOSE: Long = 700

    }
}