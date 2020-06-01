package com.example.weather.presenter.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
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
        recycler_view?.apply {
            setHasFixedSize(true)
            adapter = this@CityFragment.adapter
        }
        adapter.submitList(getMockCity())
    }

    fun getMockCity() =
        listOf(
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow"),
            CityUI("Moscow")
        )


    private fun initObservers() {
        viewModel.firsAction = CityAction.LoadCityFromDB
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->

        })
    }

    private fun onClick(city: CityUI) {

    }

    private fun initListeners() {
        recycler_view?.viewTreeObserver?.addOnScrollChangedListener {
            header?.isSelected = recycler_view?.canScrollVertically(-1) ?: false
        }
    }
}
