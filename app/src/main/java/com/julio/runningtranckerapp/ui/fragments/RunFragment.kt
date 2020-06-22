package com.julio.runningtranckerapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.julio.runningtranckerapp.R
import com.julio.runningtranckerapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.run_fragment) {

    private val viewModel : MainViewModel by viewModels()
}