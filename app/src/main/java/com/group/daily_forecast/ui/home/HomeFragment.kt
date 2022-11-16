package com.group.daily_forecast.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.group.daily_forecast.R
import com.group.daily_forecast.databinding.FragmentHomeBinding
import com.group.daily_forecast.pojo.response.*
import com.group.daily_forecast.utils.*
import com.group.daily_forecast.utils.Validator.validate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var resultAdapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUp()
        observe()

    }

    private fun setUp() {

        binding.result.rvResults.adapter = resultAdapter

        binding.toolBar.search.setOnClickListener { verify() }

        binding.error.retry.setOnClickListener { verify() }

        binding.toolBar.etCity.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                verify()

                return@OnEditorActionListener true
            }

            false
        })
    }

    private fun verify() {

        if (isValidated())
            search(binding.toolBar.etCity.text.toString().trim())

    }

    private fun search(cityName: String) {
        viewModel.forecast(cityName)
    }

    private fun observe() {

        lifecycleScope.launchWhenStarted {
            viewModel.forecastStateFlow.collect {
                when (it) {
                    is NetworkState.Idle -> {
                        visIdleView()
                    }
                    is NetworkState.Loading -> {
                        visLoadingView()
                    }
                    is NetworkState.Error -> {
                        visErrorView(it.msg)
                    }
                    is NetworkState.Result<*> -> {
                        handleResult(it.response)
                    }
                }
            }
        }

    }

    private fun <T> handleResult(response: T) {

        visResultView()

        when (response) {
            is DailyForecastResponse -> {

                when (response.cod!!) {
                    Constants.Codes.SUCCESSES_CODE.toString() -> {
                        ui(response.list, true)
                    }
                    Constants.Codes.OFFLINE_CODE.toString() -> {
                        ui(response.list, false)
                    }
                }

            }
        }

    }

    private fun ui(data: List<DailyForecastData>?, isOnline: Boolean) {

        binding.result.offline.apply {
            visibility = if (isOnline)
                View.GONE
            else
                View.VISIBLE

        }


        resultAdapter.submitList(data)

    }

    private fun visResultView() {

        enableSearch(true)

        binding.apply {
            View.GONE.also { vis ->
                idle.root.visibility = vis
                error.root.visibility = vis
                loading.root.visibility = vis
            }
            View.VISIBLE.also { vis ->
                result.root.visibility = vis
            }
        }

    }

    private fun visLoadingView() {

        enableSearch(false)

        binding.apply {
            View.GONE.also { vis ->
                idle.root.visibility = vis
                error.root.visibility = vis
                result.root.visibility = vis
            }
            View.VISIBLE.also { vis ->
                loading.root.visibility = vis
            }
        }

    }

    private fun visIdleView() {

        enableSearch(true)

        binding.apply {
            View.GONE.also { vis ->
                result.root.visibility = vis
                error.root.visibility = vis
                loading.root.visibility = vis
            }
            View.VISIBLE.also { vis ->
                idle.root.visibility = vis
            }
        }

    }

    private fun visErrorView(message: String?) {

        enableSearch(true)

        binding.apply {

            View.GONE.also { vis ->
                idle.root.visibility = vis
                result.root.visibility = vis
                loading.root.visibility = vis
            }

            View.VISIBLE.also { vis ->
                error.apply {
                    root.visibility = vis

                    sorry.apply {
                        if (message.isNullOrEmpty())
                            visibility = View.GONE
                        else {
                            visibility = View.VISIBLE
                            text = message
                        }
                    }

                }
            }

        }


    }

    private fun enableSearch(enable: Boolean) {
        binding.toolBar.search.isEnabled = enable
    }

    private fun isValidated(): Boolean {

        var validated = true

        if (!binding.toolBar.etCity.validate())
            validated = false

        return validated
    }

}