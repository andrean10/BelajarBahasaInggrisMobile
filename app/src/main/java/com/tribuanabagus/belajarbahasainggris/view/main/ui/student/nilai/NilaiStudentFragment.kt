package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.nilai

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
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentNilaiStudentBinding
import com.tribuanabagus.belajarbahasainggris.model.hafalan.ResultsHafalan
import com.tribuanabagus.belajarbahasainggris.model.nilai.belajar.ResultsNilaiBelajar
import com.tribuanabagus.belajarbahasainggris.model.nilai.permainan.ResultsNilaiPermainan
import com.tribuanabagus.belajarbahasainggris.model.percakapan.ResultsPercakapan
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TAG
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.nilai.adapter.ItemNilaiAdapter
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.nilai.viewmodel.NilaiStudentViewModel
import www.sanju.motiontoast.MotionToast

class NilaiStudentFragment : Fragment() {

    private var _binding: FragmentNilaiStudentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NilaiStudentViewModel>()
    private lateinit var nilaiAdapter: ItemNilaiAdapter
    private lateinit var mainActivity: StudentActivity

    private var dataHafalan: ResultsHafalan? = null
    private var dataPercakapan: ResultsPercakapan? = null
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentNilaiStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = NilaiStudentFragmentArgs.fromBundle(arguments as Bundle)
        dataHafalan = args.dataHafalan
        dataPercakapan = args.dataPercakapan

        val title = when {
            dataHafalan != null -> "Nilai ${dataHafalan!!.title}"
            dataPercakapan != null -> "Nilai ${dataPercakapan!!.title}"
            else -> ""
        }

        userPreference = UserPreference(requireContext())

        prepareToolbar(title)
        prepareView()
    }

    private fun prepareToolbar(title: String) {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            setTitle(title)
        }
    }

    private fun prepareView() {
        loader(true)

        nilaiAdapter = ItemNilaiAdapter()
        with(binding.rvIsiHafalanNilai) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
            adapter = nilaiAdapter
        }

        nilaiAdapter.setOnItemClickCallBack(object : ItemNilaiAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: Any) {
                when (data) {
                    is ResultsNilaiBelajar -> {
                        Log.d(TAG, "onItemClicked: Nilai Belajar")
                    }
                    is ResultsNilaiPermainan -> {
                        Log.d(TAG, "onItemClicked: Nilai Permainan")
                    }
                }
            }
        })

        var tipeSoal = ""
        var idSoal = 0
        if (dataHafalan != null) {
            tipeSoal = "hafalan"
            idSoal = dataHafalan!!.id ?: 0
        } else if (dataPercakapan != null) {
            tipeSoal = "percakapan"
            idSoal = dataPercakapan!!.id ?: 0
        }

        val queryParams = hashMapOf<String, Any>(
            "id_siswa" to (userPreference.getUser().id ?: 0),
            "tipe_soal" to tipeSoal,
            "id_soal" to idSoal,
        )

        observeNilai(queryParams)
    }

    private fun observeNilai(queryParams: HashMap<String, Any>) {
        Log.d(TAG, "observeNilai: $queryParams")
        viewModel.getNilaiBelajar(queryParams).observe(viewLifecycleOwner) { response ->
            loader(false)
            if (response != null) {
                val results = response.results
                if (results != null) {
                    nilaiAdapter.setData(results)
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
