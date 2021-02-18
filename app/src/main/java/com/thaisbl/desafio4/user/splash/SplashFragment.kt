package com.thaisbl.desafio4.user.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.thaisbl.desafio4.NavGraphDirections
import com.thaisbl.desafio4.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var splashViewModel: SplashViewModel
    private lateinit var binding: FragmentSplashBinding

    private fun initObservables() {
        splashViewModel.isUserSignedIn()
        splashViewModel.onIsUserSignedInSuccess.observe(viewLifecycleOwner, { isSignedIn ->
            if (isSignedIn) {
                val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                findNavController().navigate(action)
            } else {
                val action = NavGraphDirections.actionGlobalLoginFragment()
                findNavController().navigate(action)
            }
        })
        splashViewModel.onIsUserSignedInFailure.observe(viewLifecycleOwner, { error ->
            Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        initObservables()
    }

}