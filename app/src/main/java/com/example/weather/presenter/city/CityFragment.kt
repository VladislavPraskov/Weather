package com.example.weather.presenter.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.devpraskov.android_ext.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.fragment_city.*
import com.example.weather.R
import com.example.weather.models.CityUI


class CityFragment : Fragment(R.layout.fragment_city) {


    private val viewModel: CityViewModel by viewModel()
    val adapter = CityAdapter(::onClick)

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
            val cityList = state.data.getIfNotBeenHandled() ?: return@Observer
            adapter.submitList(cityList)
        })
    }

    private fun onClick(city: CityUI) {
        viewModel.setNextAction(CityAction.SaveCity(city))
    }

    private fun initListeners() {
        recycler_view?.viewTreeObserver?.addOnScrollChangedListener {
            header?.isSelected = recycler_view?.canScrollVertically(-1) ?: false
        }
        editText?.doAfterTextChanged { if (!it?.toString().isNullOrEmpty()) clearIcon.hideAnimateAlpha() }
        editText?.debounceAfterTextChanged(coroutineScope = lifecycle.coroutineScope) { q ->
            if (q.isNotEmpty()) {
                viewModel.setNextAction(CityAction.LoadByQuery(q.trim()))
            }
        }
        clearIcon?.onClick {
            editText?.setText("")
            clearIcon.hide()
        }

    }
}
