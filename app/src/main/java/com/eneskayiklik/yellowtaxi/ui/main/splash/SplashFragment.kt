package com.eneskayiklik.yellowtaxi.ui.main.splash

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eneskayiklik.yellowtaxi.R
import com.eneskayiklik.yellowtaxi.databinding.FragmentSplashBinding
import com.eneskayiklik.yellowtaxi.ui.base.BaseFragment
import com.eneskayiklik.yellowtaxi.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SplashFragment: BaseFragment<FragmentSplashBinding>() {
   private val viewModel by activityViewModels<MainViewModel>()
    private var canNavigate = false
    override val layoutId: Int
        get() = R.layout.fragment_splash

    override fun initialize() {
        collectData()
        startPostDelayed()
    }

    private fun startPostDelayed() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (canNavigate) navigateToMain() else canNavigate = true
        }, 500L)
    }

    private fun collectData() {
        lifecycleScope.launchWhenResumed {
            viewModel.tripDataFlow.collectLatest {
                if (canNavigate && it.isNotEmpty()) navigateToMain()
                if (!canNavigate && it.isNotEmpty()) canNavigate = true
            }
        }
    }

    private fun navigateToMain() {
        findNavController().navigate(R.id.action_splashFragment_to_listFragment)
    }
}