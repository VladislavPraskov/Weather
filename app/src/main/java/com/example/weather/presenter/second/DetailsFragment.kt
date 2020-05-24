package com.example.weather.presenter.second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.weather.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(R.layout.fragment_details) {

    companion object {
        val TAG = DetailsFragment::class.java.name
    }

    private val viewModel: DetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
    }

    private fun initViews() {

    }

    private fun initObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->

        })
    }

    private fun initListeners() {

    }
}
