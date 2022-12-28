package com.tribuanabagus.belajarbahasainggris.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {

    private var _binding: FragmentLoadingDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG = LoadingDialogFragment::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupWidthToMatchParent()
    }

//    private fun DialogFragment.setupWidthToMatchParent() {
//        dialog?.window?.setLayout(
//            ViewGroup,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}