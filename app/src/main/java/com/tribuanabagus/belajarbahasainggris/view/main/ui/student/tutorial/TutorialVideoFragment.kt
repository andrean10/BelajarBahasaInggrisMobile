package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentTutorialVideoBinding
import com.tribuanabagus.belajarbahasainggris.local_db.VideoPembelajaran
import com.tribuanabagus.belajarbahasainggris.utils.UtilsData.dataTutorialVideo
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial.adapter.ItemTutorialVideoAdapter

class TutorialVideoFragment : Fragment() {

    private var _binding: FragmentTutorialVideoBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemsTutorialVideoAdapter: ItemTutorialVideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTutorialVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { view ->
            findNavController().navigateUp()
        }

        prepareView()
    }

    private fun prepareView() {
        itemsTutorialVideoAdapter = ItemTutorialVideoAdapter()
        itemsTutorialVideoAdapter.setData(dataTutorialVideo)
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
            override fun onItemClicked(data: VideoPembelajaran) {
                moveToPlayVideo(data)
            }
        })
    }

    private fun moveToPlayVideo(data: VideoPembelajaran) {
        val toPlayVideo =
            TutorialVideoFragmentDirections.actionTutorialVideoFragmentToPlayVideoFragment(data)
        findNavController().navigate(toPlayVideo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}