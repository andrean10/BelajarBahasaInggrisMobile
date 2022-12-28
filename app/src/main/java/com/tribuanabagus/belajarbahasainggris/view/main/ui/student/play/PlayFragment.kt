package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.play

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentPlayBinding

class PlayFragment : Fragment(),View.OnClickListener {

    companion object {
        fun newInstance() = PlayFragment()
    }
    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        with(binding){
            cardGuessWord.setOnClickListener(this@PlayFragment)
            cardFindPairWords.setOnClickListener(this@PlayFragment)
            btnBack.setOnClickListener(this@PlayFragment)
        }
    }

    override fun onClick(view: View?) {
        with(binding){
            if(view?.id == R.id.btn_back){
                findNavController().navigateUp()
                Log.e("btn back","clicked")
            }
            when(view){
                cardGuessWord -> findNavController().navigate(R.id.action_playFragment_to_guessFragment)
                cardFindPairWords -> findNavController().navigate(R.id.action_playFragment_to_pairFragment)
//                R.id.btn_back -> findNavController().navigateUp()
            }
        }
    }

}