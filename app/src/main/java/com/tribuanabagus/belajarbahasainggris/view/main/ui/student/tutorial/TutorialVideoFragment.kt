package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentTutorialVideoBinding
import com.tribuanabagus.belajarbahasainggris.model.video_pembelajaran.ResultsVideoPembelajaran
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial.adapter.ItemTutorialVideoAdapter
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial.viewmodel.TutorialVideoViewModel
import www.sanju.motiontoast.MotionToast

class TutorialVideoFragment : Fragment() {

    private var _binding: FragmentTutorialVideoBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemsTutorialVideoAdapter: ItemTutorialVideoAdapter
    private val viewModel by viewModels<TutorialVideoViewModel>()
    private lateinit var mainActivity: StudentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentTutorialVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        prepareView()
    }

    private fun prepareView() {
        itemsTutorialVideoAdapter = ItemTutorialVideoAdapter()

        with(binding.rvVideoPembelajaran) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
            adapter = itemsTutorialVideoAdapter
        }

        itemsTutorialVideoAdapter.setOnItemClickCallBack(object :
            ItemTutorialVideoAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: ResultsVideoPembelajaran) {
                moveToPlayVideo(data)
            }
        })

        observeVideoPembelajaran()
    }

    private fun observeVideoPembelajaran() {
        viewModel.getVideoPembelajaran().observe(viewLifecycleOwner) { response ->
            if (response != null) {
                val results = response.results
                if (results != null) {
                    itemsTutorialVideoAdapter.setData(results)
                } else {
                    showMessage(
                        requireActivity(), getString(R.string.failed_title),
                        response.message,
                        MotionToast.TOAST_ERROR
                    )
                }
            } else {
                showMessage(
                    requireActivity(), getString(R.string.failed_title),
                    getString(R.string.failed_description),
                    MotionToast.TOAST_ERROR
                )
            }
        }
    }

    private fun moveToPlayVideo(data: ResultsVideoPembelajaran) {
        val toPlayVideo =
            TutorialVideoFragmentDirections.actionTutorialVideoFragmentToPlayVideoFragment(data)
        findNavController().navigate(toPlayVideo)
    }

    override fun onStart() {
        super.onStart()
        mainActivity.mediaPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        mainActivity.mediaPlayer.pause()
    }

//    private fun loader(state: Boolean) {
//        with(binding) {
//            if (state) {
//                pbLoading.visibility = View.VISIBLE
//                rvVideoPembelajaran.visibility = View.GONE
//            } else {
//                pbLoading.visibility = View.GONE
//                rvVideoPembelajaran.visibility = View.VISIBLE
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}