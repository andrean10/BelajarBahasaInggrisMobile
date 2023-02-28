package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentDaftarIsiHafalanBinding
import com.tribuanabagus.belajarbahasainggris.model.hafalan.ResultsHafalan
import com.tribuanabagus.belajarbahasainggris.model.soal.ResultsSoal
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.REQUEST_SUCCESS_CREATE
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TAG
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.adapter.ItemIsiHafalanAdapter
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.nilai.viewmodel.NilaiStudentViewModel
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.soal.viewmodel.SoalViewModel
import www.sanju.motiontoast.MotionToast

class DaftarIsiHafalanFragment : Fragment() {

    private var _binding: FragmentDaftarIsiHafalanBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SoalViewModel>()
    private val viewModelNilai by viewModels<NilaiStudentViewModel>()
    private lateinit var isiHafalanAdapter: ItemIsiHafalanAdapter
    private lateinit var mainActivity: StudentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentDaftarIsiHafalanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = DaftarIsiHafalanFragmentArgs.fromBundle(arguments as Bundle).data
        prepareToolbar(data.title.toString())
        prepareView(data)
    }

    private fun prepareView(results: ResultsHafalan) {
        isiHafalanAdapter = ItemIsiHafalanAdapter(requireActivity())
        with(binding.rvIsiHafalan) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
            adapter = isiHafalanAdapter
        }

        isiHafalanAdapter.setOnItemClickCallBack(object :
            ItemIsiHafalanAdapter.OnItemClickCallBack {
            override fun onItemClicked(type: Int, params: HashMap<String, Any>) {
                observeStoreNilaiHafalan(params)
            }

            override fun onItemLongClicked(data: ResultsSoal) {
            }
        })

        val queryParams = hashMapOf<String, Any>(
            "id" to (results.id ?: 0),
            "tipe_soal" to "hafalan"
        )

        observeIsiHafalan(queryParams)
    }

    private fun observeStoreNilaiHafalan(params: HashMap<String, Any>) {
        Log.d(TAG, "observeStoreNilaiHafalan: Params = $params")
        viewModelNilai.storeNilaiBelajar(params).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.status == REQUEST_SUCCESS_CREATE) {
                    showMessage(
                        requireActivity(), "Berhasil",
                        response.message,
                        MotionToast.TOAST_SUCCESS
                    )
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

    private fun observeIsiHafalan(queryParams: HashMap<String, Any>) {
        viewModel.getSoal(queryParams).observe(viewLifecycleOwner) { response ->
            loader(false)
            if (response != null) {
                val results = response.results
                if (results != null) {
                    isiHafalanAdapter.setData(results)
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

    private fun moveToDetailHafalan(data: ResultsSoal) {
//        findNavController().navigate(R.id.action_daftarIsiHafalanFragment_to_questionFragment)
    }

    private fun loader(state: Boolean) {
        with(binding) {
            pbLoading.visibility = if (state) View.VISIBLE else View.GONE
            layoutRv.visibility = if (state) View.GONE else View.VISIBLE
        }
    }

//    private fun setToolbarTitle(title: String) {
//        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
//        if ((activity as AppCompatActivity?)!!.supportActionBar != null) {
//            (activity as AppCompatActivity?)!!.supportActionBar!!.title = title
//        }
//    }

    private fun prepareToolbar(title: String) {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            setTitle(title)
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.mediaPlayer.pause()
    }

    override fun onStop() {
        super.onStop()
//        mainActivity.mediaPlayer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isiHafalanAdapter.apply {
            releaseAudio(true)
            releaseTTS()
        }
        _binding = null
    }
}