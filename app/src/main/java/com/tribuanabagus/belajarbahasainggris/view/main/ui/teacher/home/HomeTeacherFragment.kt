package com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentHomeTeacherBinding
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsData
import com.tribuanabagus.belajarbahasainggris.view.auth.AuthActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.profile.UserProfileActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.home.adapter.HomeSliderAdapter

class HomeTeacherFragment : Fragment() {
    private var _binding: FragmentHomeTeacherBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeSliderAdapter: HomeSliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeTeacherBinding.inflate(inflater, container, false)
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
            tvRoleName.text = user.roleName
            Glide.with(requireContext())
                .load(ApiConfig.URL_IMAGES + user.gambar)
                .error(R.drawable.no_profile_images)
                .into(imgUser)
        }

        prepareClick()
    }

    private fun prepareSlider() {
        homeSliderAdapter = HomeSliderAdapter()
        homeSliderAdapter.setData(UtilsData.dataSlider)
        binding.sliderView.apply {
            setSliderAdapter(homeSliderAdapter)
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            startAutoCycle()
        }
    }

    private fun prepareClick() {
        with(binding) {
            menuKelolaSoal.setOnClickListener {
                val toCategoryMenu =
                    HomeTeacherFragmentDirections.actionHomeTeacherFragmentToCategoryMenuTeacherFragment()
                        .apply {
                            isFromKelolaSoal = true
                        }
                findNavController().navigate(toCategoryMenu)
            }
            menuNilaiSiswa.setOnClickListener {
                findNavController().navigate(R.id.action_homeTeacherFragment_to_studentsFragment)
            }

            imgUser.setOnClickListener {
                startActivity(Intent(requireActivity(), UserProfileActivity::class.java))
            }

            btnExit.setOnClickListener { showAlertDialog() }
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

    override fun onResume() {
        super.onResume()
        val user = UserPreference(requireContext()).getUser()
        with(binding) {
            tvName.text = user.nama
            tvRoleName.text = user.roleName
            Glide.with(requireContext())
                .load(ApiConfig.URL_IMAGES + user.gambar)
                .error(R.drawable.no_profile_images)
                .into(imgUser)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}