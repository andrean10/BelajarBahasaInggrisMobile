package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.home

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
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentHomeStudentBinding
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig.Companion.URL_IMAGES
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsData.dataSlider
import com.tribuanabagus.belajarbahasainggris.view.auth.AuthActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.profile.UserProfileActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.home.adapter.HomeSliderAdapter

class HomeStudentFragment : Fragment() {

    private var _binding: FragmentHomeStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeSliderAdapter: HomeSliderAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var mainActivity: StudentActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentHomeStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        userPreference = UserPreference(requireContext())
        val user = userPreference.getUser()

        prepareSlider()
        with(binding) {
            tvName.text = user.nama
            tvRoleName.text = user.roleName
            Glide.with(requireContext())
                .load(URL_IMAGES + user.gambar)
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
            menuHafalan.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_students_to_hafalanFragment)
            }
            menuPercakapan.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_students_to_percakapanFragment)
            }
            menuPermainan.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_to_playFragment)
            }
            menuProfile.setOnClickListener {
                startActivity(Intent(requireActivity(), UserProfileActivity::class.java))
            }
            menuNilai.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_students_to_categoryScoreStudentFragment)
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
                requireActivity().finish()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        val user = userPreference.getUser()

        with(binding) {
            tvName.text = user.nama
            tvRoleName.text = user.roleName
            Glide.with(requireContext())
                .load(URL_IMAGES + user.gambar)
                .error(R.drawable.no_profile_images)
                .into(imgUser)
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.mediaPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        mainActivity.mediaPlayer.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}