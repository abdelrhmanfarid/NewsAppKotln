package com.farid.newsapp.mvvmnewsapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.farid.newsapp.R
import com.farid.newsapp.databinding.FragmentSignInBinding
import com.farid.newsapp.mvvmnewsapp.ui.NewsActivity
import com.farid.newsapp.mvvmnewsapp.ui.RegistrationActivity

class SignInFragment : Fragment() {

    lateinit var viewModel:RegistrationViewModel
    lateinit var _binding: FragmentSignInBinding
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        viewModel = (activity as RegistrationActivity).viewModel


        binding.loginButton.setOnClickListener{
            var email:String = binding.emailEt.text.toString().trim()
            var password:String = binding.passET.text.toString().trim()
            viewModel.login(email,password)
        }
        viewModel.isSuccess.observe(viewLifecycleOwner){
            if (it){
                requireActivity().finish()
                startActivity(Intent(requireContext(),NewsActivity::class.java))
            }
        }

        binding.signUpTextView.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction().replace(R.id.host,SignUpFragment()).commit()
        }
        return binding.root
    }

}