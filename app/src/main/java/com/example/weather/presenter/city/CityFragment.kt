package com.example.weather.presenter.city

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.view.postDelayed
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devpraskov.android_ext.*
import com.example.weather.R
import com.example.weather.models.CityUI
import com.example.weather.presenter.main.MainFragment
import kotlinx.android.synthetic.main.fragment_city.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CityFragment : Fragment(R.layout.fragment_city) {


    private val viewModel: CityViewModel by viewModel()
    private val adapter = CityAdapter(::onClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
    }

    private fun initViews() {
        with(swipeRefreshLayout) {
            isRefreshing = false
            isEnabled = false
        }

        recycler_view?.apply {
            setHasFixedSize(true)
            adapter = this@CityFragment.adapter
            val itemTouchHelper = CityItemTouchHelper(::delete)
            itemTouchHelper.attachToRecyclerView(this)
        }
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
            recycler_view.visibilityGone(!state.isNotFound)
            val cityList = state.data.getIfNotBeenHandled() ?: return@Observer
            adapter.submitList(cityList)
        })
    }

    private fun onClick(city: CityUI) {
        viewModel.setNextAction(CityAction.SaveCity(city))
        (targetFragment as? MainFragment)?.isUpdateCityNeeded = true            /***/
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
            if (q.isNotEmpty())
                viewModel.setNextAction(CityAction.LoadByQuery(q.trim()))
            else
                viewModel.setNextAction(CityAction.LoadCityFromDB)
        }

        clearIcon?.onClick {
            editText?.setText("")
            editText?.clearFocus()
            hideSoftKeyboard()
            clearIcon.hideAnimateAlpha()
        }

    }
}
