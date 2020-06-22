package com.julio.runningtranckerapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.julio.runningtranckerapp.R
import com.julio.runningtranckerapp.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.statistics_fragment) {
    private val viewModel : StatisticsViewModel by viewModels()
}