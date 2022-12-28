package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentHomeStudentBinding
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig.Companion.URL_IMAGE
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsData.dataSlider
import com.tribuanabagus.belajarbahasainggris.view.auth.AuthActivity
import com.tribuanabagus.belajarbahasainggris.view.dialog.LoadingDialogFragment.Companion.TAG
import com.tribuanabagus.belajarbahasainggris.view.main.ui.profile.UserProfileActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.home.adapter.HomeSliderAdapter
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.home.viewmodel.HomeStudentViewModel

class HomeStudentFragment : Fragment() {

    private var _binding: FragmentHomeStudentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeStudentViewModel>()
    private lateinit var homeSliderAdapter: HomeSliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        val user = UserPreference(requireContext()).getUser()
        prepareSlider()
        with(binding) {
            tvName.text = user.nama
            Glide.with(requireContext())
                .load(URL_IMAGE + user.gambar)
                .error(R.drawable.no_profile_images)
                .into(imgUser)
        }

        prepareClick()
    }

    private fun prepareSlider() {
        homeSliderAdapter = HomeSliderAdapter()
        homeSliderAdapter.setData(dataSlider)

        binding.sliderView.apply {
            setSliderAdapter(homeSliderAdapter)
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            startAutoCycle()
        }
    }

    private fun prepareClick() {
        with(binding) {
            menuVideoPembelajaran.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_students_to_tutorialVideoFragment)
            }
//                cardMenu1 -> {
//                    findNavController().navigate(R.id.action_navigation_home_to_studyFragment)
//                }
//                cardMenu2 -> {
//                    findNavController().navigate(R.id.action_navigation_home_to_playFragment)
//                }
//                cardMenu3 -> {
//                    val toKategoriNilaiSiswa = HomeStudentFragmentDirections.actionNavigationHomeToCategoryScoreFragment()
//                    findNavController().navigate(toKategoriNilaiSiswa)
//                }
            menuProfile.setOnClickListener {
                startActivity(Intent(requireActivity(), UserProfileActivity::class.java))
            }
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setTitle(getString(R.string.log_out))
            .setMessage(getString(R.string.message_log_out))
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                // clear all preferences
                UserPreference(requireActivity()).apply {
                    removeUser()
                }
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
            }
            .setNegativeButton("Tidak") { dialog, i ->
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}