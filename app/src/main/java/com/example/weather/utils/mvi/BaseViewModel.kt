package com.example.weather.utils.mvi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.weather.App

abstract class BaseViewModel<Action, ViewState, ResultAction>(
    app: Application,
    initState: ViewState
) : AndroidViewModel(app) {

    val context = getApplication<App>()
    /**
     * Переопределить, чтобы установить первое действие для инициализации данных
     */
    open var firsAction: Action? = null

    private val nextAction = MutableLiveData<Action>()

    val viewState = LiveDataDelegate<ViewState>(::initData)

    /**
     * Метод будет вызван один раз после того, как будет проинициализирован BaseViewModel и на viewState
     * первый раз подпишутся. Метод используется для первой загрузки данных. Полезен при использовании
     * Navigation Component, когда при возвращении на предыдущий фрагмент он пересоздаётся.
     */

    private fun initData() {
        firsAction?.let { setNextAction(it) }
    }

    /** (1)
     * view вызывает этот метод на viewModel и передаеёт новое состояние: viewModel.setNextAction(action: Action)
     */
    fun setNextAction(action: Action) {
        nextAction.postValue(action)
    }

    /** (2)
     * Активити подписана на viewState
     * После изменения nextAction(1) срабатывает switchMap и вызывается метод handleNewAction
     * который обрабатывает action оправленный из view. По результатам handleNewAction() возвращается
     * LiveData с типом Result, после чего срабатывает map() и вызывается reduceNewViewState() который
     * обновляет значение internalViewState, из-за чего обновляется viewState:LiveDate на который подписана activity
     */
    init {
        initViewState(initState)
    }

    private fun initViewState(initState: ViewState) {
        val trigger = Transformations.switchMap(nextAction) { action -> handleNewAction(action) }
        val reducer = Transformations.map(trigger) { result ->
            reduceNewViewState(viewState.value ?: initState, result)
        }
        viewState.initSource(reducer)
    }

    /** (3)
     * Принимает новый action, отправленный из активити, обрабатывает его и возращает resultAction
     */
    protected abstract fun handleNewAction(action: Action): LiveData<ResultAction>

    /** (4)
     * Выставляет новое значение internalViewState в зависимости от результата handleNewAction
     */
    protected abstract fun reduceNewViewState(
        currentViewState: ViewState,
        result: ResultAction
    ): ViewState
}