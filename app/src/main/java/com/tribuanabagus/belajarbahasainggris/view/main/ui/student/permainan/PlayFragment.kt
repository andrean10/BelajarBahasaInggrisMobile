package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.permainan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentPlayBinding

class PlayFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        with(binding) {
            cardGuessThePicture.setOnClickListener(this@PlayFragment)
            cardFindAPartner.setOnClickListener(this@PlayFragment)
        }
    }

    override fun onClick(view: View?) {
        with(binding) {
            when (view) {
                cardGuessThePicture -> findNavController().navigate(R.id.action_playFragment_to_guessFragment)
                cardFindAPartner -> findNavController().navigate(R.id.action_playFragment_to_pairFragment)
            }
        }
    }


}