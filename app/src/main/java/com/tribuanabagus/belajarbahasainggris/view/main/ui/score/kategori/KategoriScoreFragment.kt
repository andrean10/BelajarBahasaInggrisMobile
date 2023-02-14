package com.tribuanabagus.belajarbahasainggris.view.main.ui.score.kategori

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentKategoriScoreBinding
import com.tribuanabagus.belajarbahasainggris.local_db.User
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.ROLE_ADMIN
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.ROLE_SISWA
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.ResultFragment.Companion.GET_SCORE_GUESS_GAME
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.ResultFragment.Companion.GET_SCORE_PAIR_GAME
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_AZ
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_KONSONAN
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_VOKAL
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_MEMBACA


class KategoriScoreFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentKategoriScoreBinding? = null
    private val binding get() = _binding!!
    private var id_siswa = 0
    private lateinit var preference: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKategoriScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        with(binding) {
            cardBelajarHuruf.setOnClickListener(this@KategoriScoreFragment)
            cardBelajarKonsonan.setOnClickListener(this@KategoriScoreFragment)
            cardBelajarVokal.setOnClickListener(this@KategoriScoreFragment)
            cardBelajarMembaca.setOnClickListener(this@KategoriScoreFragment)
            cardBermainTebakKata.setOnClickListener(this@KategoriScoreFragment)
            cardBermainTemukanPasangan.setOnClickListener(this@KategoriScoreFragment)
            btnBack.setOnClickListener(this@KategoriScoreFragment)
        }
        preference = UserPreference(requireActivity()).getUser()
        when (preference.role) {
            ROLE_ADMIN -> {
                id_siswa = 0
//                    CategoryScoreFragmentArgs.fromBundle(arguments as Bundle).idSiswa
            }
            ROLE_SISWA -> id_siswa = preference.id!!
        }

    }

    override fun onClick(view: View?) {
        with(binding) {
            if (view?.id == R.id.btn_back) {
                findNavController().navigateUp()
            }
            when (preference.role) {
                ROLE_ADMIN -> {
//                    id_siswa = CategoryScoreFragmentArgs.fromBundle(arguments as Bundle).idSiswa
                    id_siswa = 0
                    when (view) {
                        cardBelajarHuruf -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment2_to_scoreFragment2,
                                bundleOf(
                                    "tipe_materi_score" to TIPE_HURUF_AZ,
                                    "nama_tipe_score" to "Nilai Huruf A-Z"
                                )
                            )
                        }
                        cardBelajarKonsonan -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment2_to_scoreFragment2,
                                bundleOf(
                                    "tipe_materi_score" to TIPE_HURUF_KONSONAN,
                                    "nama_tipe_score" to "NIlai Huruf Konsonan"
                                )
                            )
                        }
                        cardBelajarVokal -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment2_to_scoreFragment2,
                                bundleOf(
                                    "tipe_materi_score" to TIPE_HURUF_VOKAL,
                                    "nama_tipe_score" to "Nilai Huruf Vokal"
                                )
                            )
                        }
                        cardBelajarMembaca -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment2_to_scoreFragment2,
                                bundleOf(
                                    "tipe_materi_score" to TIPE_MEMBACA,
                                    "nama_tipe_score" to "Nilai Membaca Kosakata"
                                )
                            )
                        }
                        cardBermainTebakKata -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment2_to_resultFragment2,
                                bundleOf(
                                    "id_siswa" to id_siswa,
                                    "type" to GET_SCORE_GUESS_GAME
                                )
                            )
                        }
                        cardBermainTemukanPasangan -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment2_to_resultFragment2,
                                bundleOf(
                                    "id_siswa" to id_siswa,
                                    "type" to GET_SCORE_PAIR_GAME
                                )
                            )
                        }
                    }
                }
                ROLE_SISWA -> {
                    id_siswa = preference.id!!
                    when (view) {
                        cardBelajarHuruf -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment_to_scoreFragment,
                                bundleOf(
                                    "tipe_materi_score" to TIPE_HURUF_AZ,
                                    "nama_tipe_score" to "Nilai Huruf A-Z"
                                )
                            )
                        }
                        cardBelajarKonsonan -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment_to_scoreFragment,
                                bundleOf(
                                    "tipe_materi_score" to TIPE_HURUF_KONSONAN,
                                    "nama_tipe_score" to "NIlai Huruf Konsonan"
                                )
                            )
                        }
                        cardBelajarVokal -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment_to_scoreFragment,
                                bundleOf(
                                    "tipe_materi_score" to TIPE_HURUF_VOKAL,
                                    "nama_tipe_score" to "Nilai Huruf Vokal"
                                )
                            )
                        }
                        cardBelajarMembaca -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment_to_scoreFragment,
                                bundleOf(
                                    "tipe_materi_score" to TIPE_MEMBACA,
                                    "nama_tipe_score" to "Nilai Membaca Kosakata"
                                )
                            )
                        }
                        cardBermainTebakKata -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment_to_resultFragment,
                                bundleOf(
                                    "id_siswa" to id_siswa,
                                    "type" to GET_SCORE_GUESS_GAME
                                )
                            )
                        }
                        cardBermainTemukanPasangan -> {
                            findNavController().navigate(
                                R.id.action_categoryScoreFragment_to_resultFragment,
                                bundleOf(
                                    "id_siswa" to id_siswa,
                                    "type" to GET_SCORE_PAIR_GAME
                                )
                            )
                        }
                    }
                }
            }


        }
    }
}