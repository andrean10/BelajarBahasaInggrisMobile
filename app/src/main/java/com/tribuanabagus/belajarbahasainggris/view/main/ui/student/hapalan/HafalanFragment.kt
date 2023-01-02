package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentHafalanBinding
import com.tribuanabagus.belajarbahasainggris.local_db.Hafalan
import com.tribuanabagus.belajarbahasainggris.utils.UtilsData.dataHafalan
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.adapter.ItemHafalanAdapter

class HafalanFragment : Fragment() {

    private var _binding: FragmentHafalanBinding? = null
    private val binding get() = _binding!!
    private lateinit var hafalanAdapter: ItemHafalanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHafalanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        hafalanAdapter = ItemHafalanAdapter()
        hafalanAdapter.setData(dataHafalan)
        with(binding.rvVideoPembelajaran) {
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
            override fun onItemClicked(data: Hafalan) {
                Toast.makeText(requireContext(), data.title, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}