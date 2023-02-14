package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentHafalanBinding
import com.tribuanabagus.belajarbahasainggris.model.hafalan.ResultsHafalan
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.adapter.ItemHafalanAdapter
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.viewmodel.HafalanViewModel
import www.sanju.motiontoast.MotionToast

class HafalanFragment : Fragment() {

    private var _binding: FragmentHafalanBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HafalanViewModel>()
    private lateinit var hafalanAdapter: ItemHafalanAdapter
    private lateinit var mainActivity: StudentActivity

    private var isFromNilai = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentHafalanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        prepareToolbar()

        isFromNilai = HafalanFragmentArgs.fromBundle(arguments as Bundle).isFromNilai
        prepareView()
    }

    private fun prepareView() {
        loader(true)

        hafalanAdapter = ItemHafalanAdapter()
        with(binding.rvHafalan) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
            adapter = hafalanAdapter
        }

        hafalanAdapter.setOnItemClickCallBack(object : ItemHafalanAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: ResultsHafalan) {
                if (isFromNilai) {
                    moveToIsiHafalanNilai(data)
                } else {
                    moveToIsiHafalan(data)
                }
            }

            override fun onItemLongClicked(data: ResultsHafalan) {
            }
        })

        observeHafalan()
    }

    private fun observeHafalan() {
        viewModel.getHafalan().observe(viewLifecycleOwner) { response ->
            loader(false)
            if (response != null) {
                val results = response.results
                if (results != null) {
                    hafalanAdapter.setData(results)
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

    private fun moveToIsiHafalan(data: ResultsHafalan) {
        val toDaftarIsiHafalan =
            HafalanFragmentDirections.actionHafalanFragmentToDaftarIsiHafalanFragment(data)
        findNavController().navigate(toDaftarIsiHafalan)
    }

    private fun moveToIsiHafalanNilai(data: ResultsHafalan) {
        val toDaftarIsiHafalanNilai =
            HafalanFragmentDirections.actionHafalanFragmentToNilaiFragment().apply {
                dataHafalan = data
            }
        findNavController().navigate(toDaftarIsiHafalanNilai)
    }

    private fun loader(state: Boolean) {
        with(binding) {
            pbLoading.visibility = if (state) View.VISIBLE else View.GONE
            layoutRv.visibility = if (state) View.GONE else View.VISIBLE
        }
    }

    private fun prepareToolbar() {
//        setToolbar()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) {
                    findNavController().navigateUp()
                }
                return false
            }
        })
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
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
