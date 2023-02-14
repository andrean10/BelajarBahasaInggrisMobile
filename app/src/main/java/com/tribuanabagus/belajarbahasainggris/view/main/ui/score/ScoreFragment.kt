package com.tribuanabagus.belajarbahasainggris.view.main.ui.score

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
import com.tribuanabagus.belajarbahasainggris.adapter.MaterialStudyScoreAdapter
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentScoreBinding
import com.tribuanabagus.belajarbahasainggris.model.matery.MateryStudy
import com.tribuanabagus.belajarbahasainggris.model.student.StudentScore
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.main.ui.score.viewmodel.ScoreViewModelOld
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_AZ
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_KONSONAN
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_VOKAL
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_MEMBACA
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.material_study.MaterialStudyViewModel
import www.sanju.motiontoast.MotionToast

class ScoreFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel by viewModels<ScoreViewModelOld>()
    private val viewModel2 by viewModels<MaterialStudyViewModel>()
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var materyStudyScoreAdapter: MaterialStudyScoreAdapter
    private lateinit var args: com.tribuanabagus.belajarbahasainggris.view.main.ui.score.ScoreFragmentArgs
    private var idStudent = 0

    private var listMateryStudy = ArrayList<MateryStudy>()
    private val listScore = ArrayList<StudentScore>()
    private var isMatery = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args =
            com.tribuanabagus.belajarbahasainggris.view.main.ui.score.ScoreFragmentArgs.fromBundle(
                arguments as Bundle
            )
        idStudent = if (args.idSiswa == 0) {
            UserPreference(requireContext()).getUser().id ?: 0
        } else {
            args.idSiswa
        }

        val tipeMateri = args.tipeMateriScore
        prepareView(tipeMateri, idStudent)
    }

    private fun prepareView(tipeMateri: Int, idStudent: Int) {
        with(binding.score){
            when(tipeMateri){
                TIPE_HURUF_AZ -> observeStudentScores(TIPE_HURUF_AZ,idStudent)
                TIPE_HURUF_KONSONAN -> observeStudentScores(tipeMateri,idStudent)
                TIPE_HURUF_VOKAL -> {
                    observeMaterialStudy(TIPE_HURUF_VOKAL)
                    isMatery = true
                }
                TIPE_MEMBACA -> observeStudentScores(TIPE_MEMBACA,idStudent)
            }

            //adapter score
            materyStudyScoreAdapter = MaterialStudyScoreAdapter()
            with(rvScore){
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                this.adapter = materyStudyScoreAdapter
            }
            //search view
            searchview.clearFocus()
            searchview.setOnQueryTextListener(this@ScoreFragment)
            //hide back and title when searchview clicked
            searchview.setOnSearchClickListener{
                btnBack.visibility = View.GONE
                tvScore.visibility = View.GONE
            }
            //show back and title when searchview clicked
            searchview.setOnCloseListener(object: SearchView.OnCloseListener{
                override fun onClose(): Boolean {
                    btnBack.visibility = View.VISIBLE
                    tvScore.visibility = View.VISIBLE
                    return false //karna aku ingin fungsi close berjalan seperti biasa
                }
            })

            tvScore.text = args.namaTipeScore
            materyStudyScoreAdapter.setOnItemClickCallBack(object : MaterialStudyScoreAdapter.OnItemClickCallBack {
                override fun onItemClicked(materyStudy: MateryStudy) {
                    observeStudentScores(materyStudy.tipeMateri,idStudent,true)
                }
            })
            btnBack.setOnClickListener{findNavController().navigateUp()}
        }
    }

    private fun observeStudentScores(tipeMateri: Int,idSiswa: Int,isVocal: Boolean = false) {
        with(binding.score){
            viewModel.studentScores(tipeMateri,idSiswa).observe(viewLifecycleOwner, { response ->
                pbScore.visibility = View.GONE
                if (response.data != null) {
                    if(!response.data.isEmpty()){
                        if (response.code == 200) {
                            val result = response.data
                            materyStudyScoreAdapter.setVokal(isVocal)
                            materyStudyScoreAdapter.setData(result)
                            if(tipeMateri == TIPE_HURUF_AZ || tipeMateri == TIPE_HURUF_KONSONAN) materyStudyScoreAdapter.setLetter(true)

                            //for filter
                            listScore.addAll(result)
                        } else {
                            dataNotFound()
                        }
                    }else{
                        dataNotFound()
                    }
                } else {
                    dataNotFound()
                }
            })
        }
    }

    private fun observeMaterialStudy(materyId: Int) {
        with(binding.score){
            viewModel2.materialStudy(materyId).observe(viewLifecycleOwner, { response ->
                pbScore.visibility = View.GONE
                if (response.data != null) {
                    if(!response.data.isEmpty()){
                        if (response.code == 200) {
                            val result = response.data
                            materyStudyScoreAdapter.setData(result)

                            //for filter
                            listMateryStudy.addAll(result)
                        } else {
                            dataNotFound()
                        }
                    }else{
                        dataNotFound()
                    }
                } else {
                    dataNotFound()
                }
            })
        }
    }

    private fun dataNotFound() {
        with(binding.score) {
            val layoutEmpty = layoutEmpty.root
            layoutEmpty.visibility = View.VISIBLE
        }
    }

    private fun filterList(text: String?) {
        val filteredlist = ArrayList<Any>()

        if(isMatery){
            for (item in listMateryStudy) {
                if (item.teksMateri.uppercase().contains(text!!.uppercase())) {
                    filteredlist.add(item)
                }
            }
        }else{
            for (item in listScore) {
                if (item.namaMateri!!.uppercase().contains(text!!.uppercase())) {
                    filteredlist.add(item)
                }
            }
        }

        if (filteredlist.isEmpty()) {
            Toast.makeText(requireActivity(),"Tidak ada data ditemukan",Toast.LENGTH_SHORT).show()
        } else {
            materyStudyScoreAdapter.setFilteredList(filteredlist)
        }
    }

    private fun filterListScore(text: String?) {
        val filteredlist = ArrayList<Any>()


        if (filteredlist.isEmpty()) {
            showMessage(
                requireActivity(),
                UtilsCode.TITLE_ERROR,
                "Tidak ada data ditemukan",
                style = MotionToast.TOAST_ERROR
            )
        } else {
            materyStudyScoreAdapter.setFilteredList(filteredlist)
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