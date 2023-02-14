package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.nilai.kategori

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentCategoryScoreStudentBinding

class CategoryScoreStudentFragment : Fragment() {

    private var _binding: FragmentCategoryScoreStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryScoreStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        with(binding.layoutMenu) {
            btnHafalan.setOnClickListener {
                val toHafalan =
                    CategoryScoreStudentFragmentDirections.actionCategoryScoreStudentFragmentToHafalanFragment()
                        .apply {
                            isFromNilai = true
                        }
                findNavController().navigate(toHafalan)
            }

            btnPercakapan.setOnClickListener {
                val toPercakapan =
                    CategoryScoreStudentFragmentDirections.actionCategoryScoreStudentFragmentToPercakapanFragment()
                        .apply {
                            isFromNilai = true
                        }
                findNavController().navigate(toPercakapan)
            }

            btnPermainan.setOnClickListener { }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}