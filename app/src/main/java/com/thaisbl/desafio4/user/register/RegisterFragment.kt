package com.thaisbl.desafio4.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.thaisbl.desafio4.NavGraphDirections
import com.thaisbl.desafio4.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        initComponents()
        setUpObservables()
    }

    private fun initComponents() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val repeatPassword = binding.etRepeatPassword.text.toString()

            registerViewModel.registerUser(name, email, password, repeatPassword)
        }
    }

    private fun setUpObservables() {
        registerViewModel.onRegisterUserSuccess.observe(viewLifecycleOwner, {
            Toast.makeText(context, "User Created", Toast.LENGTH_SHORT).show()
            val action = NavGraphDirections.actionGlobalLoginFragment(binding.etEmail.text.toString())
            findNavController().navigate(action)
        })
        registerViewModel.onRegisterUserFailure.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
        })
    }

}