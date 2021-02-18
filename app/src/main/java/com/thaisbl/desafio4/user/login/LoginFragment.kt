package com.thaisbl.desafio4.user.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thaisbl.desafio4.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private val args: LoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        initComponents()
        setUpObservables()
    }


    private fun setUpObservables() {
        loginViewModel.onSignInUserSuccess.observe(viewLifecycleOwner, { userEmail ->
            if (binding.cbRemember.isChecked)
                loginViewModel.saveLoginCredentials(userEmail)
            else
                loginViewModel.deleteSavedLoginCredentials()

            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            findNavController().navigate(action)
        })
        loginViewModel.onSignInUserFailure.observe(viewLifecycleOwner, { error ->
            Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        })

        loginViewModel.onGetSavedLoginCredentialsSuccess.observe(viewLifecycleOwner, {
            binding.etEmail.setText(it)
        })
        loginViewModel.onGetSavedLoginCredentialsFailure.observe(viewLifecycleOwner, {
            Log.i("LoginFragmentFirebase", "getSavedLoginCredentials() [FAIL] Error: $it")
        })

        loginViewModel.onSaveLoginCredentialsSuccess.observe(viewLifecycleOwner, {
            Log.d("LoginFragmentFirebase", "deleteSavedLoginCredentials() [OK] Success")
        })
        loginViewModel.onSaveLoginCredentialsFailure.observe(viewLifecycleOwner, {
            Log.e("LoginFragmentFirebase", "saveLoginCredentials() [FAIL] Error: $it")
        })

        loginViewModel.onDeleteSavedLoginCredentialsSuccess.observe(viewLifecycleOwner, {
            Log.d("LoginFragmentFirebase", "deleteSavedLoginCredentials() [OK] Success")
        })
        loginViewModel.onDeleteSavedLoginCredentialsFailure.observe(viewLifecycleOwner, {
            Log.e("LoginFragmentFirebase", "deleteSavedLoginCredentials() [FAIL] Error: $it")
        })
    }

    private fun initComponents() {
        val userFromRegister = args.userFromRegister
        userFromRegister?.let {
            binding.etEmail.setText(it)
        } ?: loginViewModel.getSavedLoginCredentials()

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            loginViewModel.signInUser(email, password)
        }

        binding.btCreateAccount.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }
}