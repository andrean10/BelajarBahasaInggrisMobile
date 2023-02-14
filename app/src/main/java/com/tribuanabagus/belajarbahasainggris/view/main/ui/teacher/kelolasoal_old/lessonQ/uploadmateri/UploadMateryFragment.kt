package com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.kelolasoal_old.lessonQ.uploadmateri

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentUploadMateryBinding
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import www.sanju.motiontoast.MotionToast

class UploadMateryFragment : Fragment(), View.OnClickListener {

    private val viewModel by viewModels<UploadMateryViewModel>()
    private var _binding: FragmentUploadMateryBinding? = null
    private val binding get() = _binding!!
    private lateinit var args: UploadMateryFragmentArgs
    private var tipeMateri = 0
    private var idMateri = 0
    private var teksMateri = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadMateryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = UploadMateryFragmentArgs.fromBundle(arguments as Bundle)
        tipeMateri = args.tipeMateriData
        idMateri = args.idData
        teksMateri = args.teksMateri
        prepareView(tipeMateri)
    }

    private fun prepareView(tipeMateri: Int) {
        with(binding){
            btnSimpan.setOnClickListener(this@UploadMateryFragment)
            btnBack.setOnClickListener(this@UploadMateryFragment)

            if(idMateri != 0){
                edtTeksJawaban.setText(teksMateri ?: "")
            }
        }
    }

    override fun onClick(view: View?) {
        with(binding){
            when(view){
                btnSimpan -> storeMatery()
                btnBack -> findNavController().navigateUp()
                else -> {}
            }
        }
    }

    private fun storeMatery() {
        with(binding){
            val materyParams = hashMapOf<String,Any>()
            materyParams["id"] = idMateri
            materyParams["tipe_materi"] = tipeMateri
            materyParams["teks_materi"] = edtTeksJawaban.text.toString()

            viewModel.storeMateryStudy(materyParams).observe(viewLifecycleOwner, { response ->
                loader(false)
                if (response != null) {
                    if (response.code == 200) {
                        showMessage(
                            requireActivity(),
                            UtilsCode.TITLE_SUCESS,
                            response.message ?: "",
                            style = MotionToast.TOAST_SUCCESS
                        )
                        findNavController().navigateUp()
                    } else {
                        showMessage(
                            requireActivity(),
                            UtilsCode.TITLE_ERROR,
                            response.message ?: "",
                            style = MotionToast.TOAST_ERROR
                        )
                    }
                } else {
                    showMessage(
                        requireActivity(),
                        UtilsCode.TITLE_ERROR,
                        style = MotionToast.TOAST_ERROR
                    )
                }
            })
        }
    }

    private fun loader(state: Boolean) {
        with(binding) {
            if (state) {
                pbMatery.visibility = View.VISIBLE
            } else {
                pbMatery.visibility = View.GONE
            }
        }
    }
}