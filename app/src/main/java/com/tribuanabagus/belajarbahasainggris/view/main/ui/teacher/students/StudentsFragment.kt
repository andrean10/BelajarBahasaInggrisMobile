package com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.students

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tribuanabagus.belajarbahasainggris.adapter.StudentAdapter
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentStudentsBinding
import com.tribuanabagus.belajarbahasainggris.model.users.ResultsUsers
import com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.students.viewmodel.StudentsViewModel

class StudentsFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<StudentsViewModel>()
    private lateinit var studentsAdapter: StudentAdapter
    private val listStudents = ArrayList<ResultsUsers>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        with(binding) {
            //adapter
            studentsAdapter = StudentAdapter()
            with(rvDataSiswa) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                this.adapter = studentsAdapter
            }

            //search view
            searchview.clearFocus()
            searchview.setOnQueryTextListener(this@StudentsFragment)
            //hide back and title when searchview clicked
            searchview.setOnSearchClickListener {
                btnBack.visibility = View.GONE
                tvTitle.visibility = View.GONE
            }
            //show back and title when searchview clicked
            searchview.setOnCloseListener {
                btnBack.visibility = View.VISIBLE
                tvTitle.visibility = View.VISIBLE
                false //karna aku ingin fungsi close berjalan seperti biasa
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            studentsAdapter.setOnItemClickCallBack(object : StudentAdapter.OnItemClickCallBack {
                override fun onItemClicked(data: ResultsUsers) {
                    moveToCategoryScore(data)
                }
            })
        }

        observeSiswa()
    }

    private fun moveToCategoryScore(data: ResultsUsers) {
        val toCategoryScore =
            StudentsFragmentDirections.actionStudentsFragmentToCategoryMenuTeacherFragment().apply {
                idSiswa = data.id ?: 0
            }
        findNavController().navigate(toCategoryScore)
    }

    private fun observeSiswa() {
        with(binding) {
            viewModel.siswa().observe(viewLifecycleOwner) { response ->
                pbLoading.visibility = View.GONE
                if (response != null) {
                    if (response.status == 200) {
                        val result = response.results
                        if (result != null) {
                            studentsAdapter.setData(result)
                            listStudents.clear()
                            listStudents.addAll(result)
                            dataNotFound(false)
                        } else {
                            dataNotFound()
                        }
                    } else {
                        dataNotFound()
                    }
                } else {
                    dataNotFound()
                }
            }
        }
    }

    private fun filterList(text: String?) {
        val filteredlist = ArrayList<ResultsUsers>()

        for (item in listStudents) {
            if (item.nama!!.uppercase().contains(text!!.uppercase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(requireActivity(), "Tidak ada data ditemukan", Toast.LENGTH_SHORT).show()
        } else {
            studentsAdapter.setFilteredList(filteredlist)
        }
    }

    private fun dataNotFound(state: Boolean = true) {
        with(binding) {
            val layoutEmpty = layoutEmpty.root
            if (state) {
                layoutEmpty.visibility = View.VISIBLE
            } else {
                layoutEmpty.visibility = View.GONE
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        filterList(newText)
        return true
    }
}