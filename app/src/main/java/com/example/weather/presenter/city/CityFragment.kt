package com.example.weather.presenter.city

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.devpraskov.android_ext.*
import com.example.weather.R
import com.example.weather.models.city.CityUI
import com.example.weather.presenter.city.mvi.CityAction
import com.example.weather.presenter.main.MainFragment
import com.example.weather.utils.error.ErrorMVI
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import kotlinx.android.synthetic.main.fragment_city.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CityFragment : Fragment(R.layout.fragment_city) {

    companion object {
        const val COLOR_KEY = "color_key"
        fun create(color: Pair<Int, Int>): CityFragment {
            return CityFragment().apply {
                arguments = Bundle().apply { putSerializable(COLOR_KEY, color) }
            }
        }
    }

    lateinit var currentColor: Pair<Int, Int>
    private val viewModel: CityViewModel by viewModel()
    private val adapter = CityAdapter(::onClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
    }

    private fun initViews() {
        currentColor = arguments?.get(COLOR_KEY) as? Pair<Int, Int> ?: Pair(16507811, 8483167)
        with(swipeRefreshLayout) {
            isRefreshing = false
            isEnabled = false
        }
        adapter.color = currentColor.second
        recycler_view?.apply {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setHasFixedSize(true)
            adapter = this@CityFragment.adapter
            val itemTouchHelper = CityItemTouchHelper(::delete)
            itemTouchHelper.attachToRecyclerView(this)
        }
        header?.setBackgroundColor(currentColor.first)
        swipeRefreshLayout?.background = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(currentColor.first, currentColor.second)
        )
    }

    private fun delete(pos: Int) {
        val deletedCity = adapter.deleteItem(pos)
        viewModel.setNextAction(CityAction.DeleteCity(deletedCity))
    }

    private fun initObservers() {
        viewModel.firsAction = CityAction.LoadCityFromDB
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            swipeRefreshLayout?.isRefreshing = state.isLoading
            noResult.visibilityGone(state.isNotFound)
            showError(state.error.getIfNotBeenHandled())
            val cityList = state.data.getIfNotBeenHandled() ?: return@Observer
            adapter.submitList(cityList, state.isCache)
        })
    }

    private fun showError(error: ErrorMVI?) {
        error ?: return
        error.message ?: return
        Snackbar.make(root, error.message, LENGTH_LONG).show()
    }

    private fun onClick(city: CityUI) {
        viewModel.setNextAction(CityAction.SaveCity(city))
        (targetFragment as? MainFragment)?.isUpdateCityNeeded = true
        /***/
    }

    private fun initListeners() {
        recycler_view?.viewTreeObserver?.addOnScrollChangedListener {
            header?.isSelected = recycler_view?.canScrollVertically(-1) ?: false
        }
        editText?.doAfterTextChanged {
            if (!it?.toString().isNullOrEmpty()) clearIcon.showAnimateAlpha()
            else clearIcon.hideAnimateAlpha()
        }
        editText?.debounceAfterTextChanged(coroutineScope = lifecycle.coroutineScope) { q ->
            if (q.length > 1) viewModel.setNextAction(CityAction.LoadByQuery(q.trim()))
            else if (q.isEmpty()) viewModel.setNextAction(CityAction.LoadCityFromDB)
        }

        clearIcon?.onClick {
            editText?.setText("")
            editText?.clearFocus()
            hideSoftKeyboard()
        }

    }
}
