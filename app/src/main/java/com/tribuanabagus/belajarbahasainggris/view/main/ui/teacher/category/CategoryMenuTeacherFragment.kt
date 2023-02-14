package com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentCategoryMenuTeacherBinding

class CategoryMenuTeacherFragment : Fragment() {

    private var _binding: FragmentCategoryMenuTeacherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryMenuTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CategoryMenuTeacherFragmentArgs.fromBundle(arguments as Bundle)
        val idSiswa = args.idSiswa
        val isFromKelolaSoal = args.isFromKelolaSoal
        prepareView(idSiswa, isFromKelolaSoal)
    }

    private fun prepareView(idSiswa: Int, isKelolaSoal: Boolean) {
        with(binding.layoutMenu) {
            btnHafalan.setOnClickListener {
                val toHafalan =
                    CategoryMenuTeacherFragmentDirections.actionCategoryMenuTeacherFragmentToHafalanTeacherFragment(
                        idSiswa
                    ).apply {
                        isFromKelolaSoal = isKelolaSoal
                    }
                findNavController().navigate(toHafalan)
            }

            btnPercakapan.setOnClickListener {
                val toPercakapan =
                    CategoryMenuTeacherFragmentDirections.actionCategoryMenuTeacherFragmentToPercakapanTeacherFragment(
                        idSiswa
                    ).apply {
                        isFromKelolaSoal = isKelolaSoal
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