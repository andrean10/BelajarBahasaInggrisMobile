package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.percakapan.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentDaftarIsiPercakapanBinding
import com.tribuanabagus.belajarbahasainggris.model.percakapan.ResultsPercakapan
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.REQUEST_SUCCESS_CREATE
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TAG
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.nilai.viewmodel.NilaiStudentViewModel
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.percakapan.adapter.ItemIsiPercakapanAdapter
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.soal.viewmodel.SoalViewModel
import www.sanju.motiontoast.MotionToast

class DaftarIsiPercakapanFragment : Fragment() {

    private var _binding: FragmentDaftarIsiPercakapanBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SoalViewModel>()
    private val viewModelNilai by viewModels<NilaiStudentViewModel>()
    private lateinit var isiPercakapanAdapter: ItemIsiPercakapanAdapter
    private lateinit var mainActivity: StudentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentDaftarIsiPercakapanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = DaftarIsiPercakapanFragmentArgs.fromBundle(arguments as Bundle).data
        setToolbarTitle(data.title.toString())
        prepareView(data)
    }

    private fun prepareView(results: ResultsPercakapan) {
        isiPercakapanAdapter = ItemIsiPercakapanAdapter(requireActivity())
        with(binding.rvIsiPercakapan) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
            adapter = isiPercakapanAdapter
        }

        isiPercakapanAdapter.setOnItemClickCallBack(object :
            ItemIsiPercakapanAdapter.OnItemClickCallBack {
            override fun onItemClicked(type: Int, params: HashMap<String, Any>) {
                observeStoreNilaiPercakapan(params)
            }
        })

        val queryParams = hashMapOf<String, Any>(
            "id" to (results.id ?: 0),
            "tipe_soal" to "percakapan"
        )

        observeIsiPercakapan(queryParams)
    }

    private fun observeStoreNilaiPercakapan(params: HashMap<String, Any>) {
        Log.d(TAG, "observeStoreNilaiPercakapan: Params = $params")
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

    private fun observeIsiPercakapan(queryParams: HashMap<String, Any>) {
        viewModel.getSoal(queryParams).observe(viewLifecycleOwner) { response ->
            loader(false)
            Log.d(TAG, "observeIsiPercakapan: $response")
            if (response != null) {
                val results = response.results
                if (results != null) {
                    isiPercakapanAdapter.setData(results)
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

    private fun loader(state: Boolean) {
        with(binding) {
            pbLoading.visibility = if (state) View.VISIBLE else View.GONE
            layoutRv.visibility = if (state) View.GONE else View.VISIBLE
        }
    }

    private fun setToolbarTitle(title: String) {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        if ((activity as AppCompatActivity?)!!.supportActionBar != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = title
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.mediaPlayer.pause()
    }

    override fun onStop() {
        super.onStop()
        mainActivity.mediaPlayer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isiPercakapanAdapter.apply {
            releaseAudio(true)
            releaseTTS()
        }
        _binding = null
    }
}