package com.example.weather.utils.mvi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class LiveDataDelegate<ViewState>(private val firstAction: () -> Unit) : LiveData<ViewState>() {

    private lateinit var viewState: LiveData<ViewState>
    /**
     * Переменная становится false после того как на viewState подпишутся первый раз после создания ViewModel
     */
    private var isFirstSubscription = true

    fun initSource(liveData: LiveData<ViewState>) {
        viewState = liveData
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in ViewState>) {
        launchFirstAction()
        viewState.observe(owner, observer)
    }

    override fun observeForever(observer: Observer<in ViewState>) {
        launchFirstAction()
        viewState.observeForever(observer)
    }

    private fun launchFirstAction() {
        if (!isFirstSubscription) return
        firstAction.invoke()
        isFirstSubscription = false
    }
}