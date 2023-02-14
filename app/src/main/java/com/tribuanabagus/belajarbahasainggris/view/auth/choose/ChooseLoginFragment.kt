package com.tribuanabagus.belajarbahasainggris.view.auth.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentChooseLoginBinding

class ChooseLoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentChooseLoginBinding

    companion object {
        const val ROLE_ADMIN = 1
        const val ROLE_STUDENT = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnAdmin.setOnClickListener(this@ChooseLoginFragment)
            btnStudent.setOnClickListener(this@ChooseLoginFragment)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_admin -> {
                val toLogin =
                    ChooseLoginFragmentDirections.actionChooseLoginFragmentToLoginFragment(
                        ROLE_ADMIN
                    )
                findNavController().navigate(toLogin)
            }
            R.id.btn_student -> {
                val toLogin =
                    ChooseLoginFragmentDirections.actionChooseLoginFragmentToLoginFragment(
                        ROLE_STUDENT
                    )
                findNavController().navigate(toLogin)
            }
        }
    }
}