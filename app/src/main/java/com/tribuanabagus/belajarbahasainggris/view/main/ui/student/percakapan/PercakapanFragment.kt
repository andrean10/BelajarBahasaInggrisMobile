package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.percakapan

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
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentPercakapanBinding
import com.tribuanabagus.belajarbahasainggris.model.percakapan.ResultsPercakapan
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.percakapan.adapter.ItemPercakapanAdapter
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.percakapan.viewmodel.PercakapanViewModel
import www.sanju.motiontoast.MotionToast

class PercakapanFragment : Fragment() {

    private var _binding: FragmentPercakapanBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PercakapanViewModel>()
    private lateinit var percakapanAdapter: ItemPercakapanAdapter
    private lateinit var mainActivity: StudentActivity

    private var isFromNilai = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentPercakapanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareToolbar()

        isFromNilai = PercakapanFragmentArgs.fromBundle(arguments as Bundle).isFromNilai
        prepareView()
    }

    private fun prepareView() {
        loader(true)

        percakapanAdapter = ItemPercakapanAdapter()
        with(binding.rvPercakapan) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
            adapter = percakapanAdapter
        }

        percakapanAdapter.setOnItemClickCallBack(object :
            ItemPercakapanAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: ResultsPercakapan) {
                if (isFromNilai) {
                    moveToIsiPercakapanNilai(data)
                } else {
                    moveToIsiPercakapan(data)
                }
            }

            override fun onItemLongClicked(data: ResultsPercakapan) {
            }
        })

        observePercakapan()
    }

    private fun observePercakapan() {
        viewModel.getPercakapan().observe(viewLifecycleOwner) { response ->
            loader(false)
            if (response != null) {
                val results = response.results
                if (results != null) {
                    percakapanAdapter.setData(results)
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

    private fun moveToIsiPercakapan(data: ResultsPercakapan) {
        val toDaftarIsiPercakapan =
            PercakapanFragmentDirections.actionPercakapanFragmentToDaftarIsiPercakapanFragment(data)
        findNavController().navigate(toDaftarIsiPercakapan)
    }

    private fun moveToIsiPercakapanNilai(data: ResultsPercakapan) {
        val toDaftarIsiPercakapanNilai =
            PercakapanFragmentDirections.actionPercakapanFragmentToNilaiFragment().apply {
                dataPercakapan = data
            }
        findNavController().navigate(toDaftarIsiPercakapanNilai)
    }

    private fun loader(state: Boolean) {
        with(binding) {
            pbLoading.visibility = if (state) View.VISIBLE else View.GONE
            layoutRv.visibility = if (state) View.GONE else View.VISIBLE
        }
    }

    private fun prepareToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
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
