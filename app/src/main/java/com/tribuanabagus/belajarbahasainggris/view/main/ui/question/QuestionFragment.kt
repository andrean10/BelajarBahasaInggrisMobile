package com.tribuanabagus.belajarbahasainggris.view.main.ui.question

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentQuestionBinding
import com.tribuanabagus.belajarbahasainggris.local_db.StudentScore
import com.tribuanabagus.belajarbahasainggris.model.questions.Question
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig.Companion.URL_SOUNDS
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.*
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.REQUEST_CODE_AUDIO_RECORD
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TITLE_ERROR
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TITLE_SUCESS
import com.tribuanabagus.belajarbahasainggris.view.main.ui.question.viewmodel.QuestionViewModel
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_AZ
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_KONSONAN
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_HURUF_VOKAL
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.study.StudyFragment.Companion.TIPE_MEMBACA
import www.sanju.motiontoast.MotionToast
import java.util.*

class QuestionFragment : Fragment(), View.OnClickListener, RecognitionListener {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: QuestionViewModel

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var speechRecognizer: SpeechRecognizer

    private val listSoal: ArrayList<Question> = arrayListOf()
    private val listSoalTerjawab: HashMap<Int, StudentScore> = hashMapOf()

//    private var nextQ = 1
//    private var totalQ = 0

    private var distance = 0
    private var rightWord = 0
    private var wrongWord = 0
    private var score = 0
    private var index = 0
    private var recognizedWord = ""
    private var isTheLastQ = false
    private var materyType = 0
    private var answerText = ""
    private var siswa = 0

    private lateinit var mainActivity: StudentActivity

    private val TAG = QuestionFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[
                QuestionViewModel::class.java]
        prepareView()
    }

    private fun prepareView() {
        val args = QuestionFragmentArgs.fromBundle(arguments as Bundle)
        siswa = UserPreference(requireContext()).getUser().id ?: 0
        val materyId = 1
        materyType = 4

        //set title
        var title = ""
        when (materyType) {
            TIPE_HURUF_AZ -> title = "Mengenal\nHuruf"
            TIPE_HURUF_KONSONAN -> title = "Konsonan"
            TIPE_HURUF_VOKAL -> title = "Huruf\nVokal"
            TIPE_MEMBACA -> title = "Mengenal\nKosakata"
        }
        binding.layoutQuestion.tvTitle.text = title

        //siapkan data soal
        when (materyType) {
            TIPE_HURUF_AZ -> getQuestionsByType(TIPE_HURUF_AZ)
            TIPE_HURUF_KONSONAN -> getQuestionsByType(TIPE_HURUF_KONSONAN)
            TIPE_HURUF_VOKAL, TIPE_MEMBACA -> getQuestions(materyId)
        }

    }

    private fun getQuestionsByType(materyTypeId: Int) {
        viewModel.questionsByType(materyTypeId).observe(viewLifecycleOwner) { response ->
            loader(false)
            if (response.data != null) {
                if (response.data.isNotEmpty()) {
                    if (response.code == 200) {
                        val results = response.data
                        listSoal.addAll(results)
                        showLayout()
                        prepareQuestions()
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

    private fun getQuestions(materyId: Int) {
        viewModel.questions(materyId).observe(viewLifecycleOwner) { response ->
            binding.pbLoading.visibility = View.GONE
            if (response.data != null) {
                if (!response.data.isEmpty()) {
                    if (response.code == 200) {
                        val results = response.data
                        listSoal.addAll(results)
                        showLayout()
                        prepareQuestions()
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

    private fun prepareQuestions() {
        if (mediaPlayer?.isPlaying == true) releaseAudio()

        prepareButtonPrevNext()
        with(binding) {
            with(layoutQuestion) {
                btnSpeech.setOnClickListener(this@QuestionFragment)
                btnPlaySound.setOnClickListener(this@QuestionFragment)
//                imgbNextq.setOnClickListener(this@QuestionFragment)
//                imgbPrevq.setOnClickListener(this@QuestionFragment)
            }
            with(layoutVocabQuestion) {
                btnSpeech.setOnClickListener(this@QuestionFragment)
            }
            val soal = listSoal[index]

            //Prepare Audio
            mediaPlayer = MediaPlayer()
            val urlAudio = URL_SOUNDS + soal.suara
            prepareMediaPlayer(urlAudio)
            playAudioVoice()

            //prepare text
            with(binding.layoutQuestion) {
//                answerText = soal.teksJawaban.lowercase()
//                tvKalimatTest.text = "Kalimat: ${answerText}"
            }
            with(binding.layoutVocabQuestion) {
                answerText = soal.teksJawaban.lowercase()
                tvKalimatTest.text = "Kalimat: ${answerText}"
            }

            //prepare view answered Question
            val soalTerjawab = listSoalTerjawab[index]
            if (soalTerjawab != null) {
                with(binding.layoutQuestion) {
                    tvVoiceResult.text = "Disebut : ${soalTerjawab.spokenWords}"
                    tvCorrectResult.text = "Benar Huruf : ${soalTerjawab.righWord}"
                    tvWrongResult.text = "Salah Huruf : ${soalTerjawab.wrongWord}"
                    tvDistanceResult.text = "Hasil Rumus : ${soalTerjawab.distance}"
                    tvResultScore.text = soalTerjawab.score.toString()
                    tvMessageResultScore.text = kesimpulanHasil(soalTerjawab.score)
                }
                with(binding.layoutVocabQuestion) {
                    tvVoiceResult.text = "Disebut : ${soalTerjawab.spokenWords}"
                    tvCorrectResult.text = "Benar Huruf : ${soalTerjawab.righWord}"
                    tvWrongResult.text = "Salah Huruf : ${soalTerjawab.wrongWord}"
                    tvDistanceResult.text = "Hasil Rumus : ${soalTerjawab.distance}"
                    tvResultScore.text = soalTerjawab.score.toString()
                    tvMessageResultScore.text = kesimpulanHasil(soalTerjawab.score)
                }
            } else {
                with(binding.layoutQuestion) {
                    tvVoiceResult.text = "Disebut : "
                    tvCorrectResult.text = "Benar Huruf : "
                    tvWrongResult.text = "Salah Huruf : "
                    tvDistanceResult.text = "Hasil Rumus : "
                    tvResultScore.text = ""
                    tvMessageResultScore.text = "-"
                }
                with(binding.layoutVocabQuestion) {
                    tvVoiceResult.text = "Yang disebut : ..."
                    tvCorrectResult.text = "Benar Huruf: ..."
                    tvWrongResult.text = "Salah Huruf: ..."
                    tvDistanceResult.text = "Hasil Rumus: ..}"
                    tvResultScore.text = ""
                    tvMessageResultScore.text = "-"
                }
            }
        }
    }

    private fun prepareButtonPrevNext() {
        val sizeQuestion = listSoal.size
        if (sizeQuestion != 1) {
            with(binding.layoutQuestion) {
//                when (index) {
//                    0 -> {
//                        imgbPrevq.visibility = View.GONE
//                        imgbNextq.visibility = View.VISIBLE
//                    }
//                    sizeQuestion - 1 -> {
//                        imgbPrevq.visibility = View.VISIBLE
//                        imgbNextq.visibility = View.VISIBLE
//                    }
//                    else -> {
//                        imgbPrevq.visibility = View.VISIBLE
//                        imgbNextq.visibility = View.VISIBLE
//                    }
//                }
            }
        }
    }

    override fun onClick(view: View?) {
        with(binding.layoutQuestion) {
            when (view?.id) {
//                R.id.imgb_nextq -> {
//                    ++index
//                    nextQuestion()
//                }
//                R.id.imgb_prevq -> {
//                    --index
//                    prevQuestion()
//                }
                R.id.btn_speech -> {
                    openSpeechRecord()
                }
                R.id.btn_speaker -> {
                    playAudioVoice()
                }
            }
        }
    }

    private fun prevQuestion() {
        prepareQuestions()
    }

    private fun nextQuestion() {
        if (index != listSoal.size || !(index >= listSoal.size)) {
            prepareQuestions()
        } else {
            //tutup halaman Question (SELESAI)
            findNavController().popBackStack()
            Log.i("tag", "indeks lebih besar dr size soal")
        }
    }

    private fun openSpeechRecord() {
        checkPermissionAudioRecord()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireActivity())
        speechRecognizer.setRecognitionListener(this)
        // Get the Intent action
        val recIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).also {
            it.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            it.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE, "en-US"
            )
        }
        //start listening
        speechRecognizer.startListening(recIntent)
    }

    private fun checkPermissionAudioRecord() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_CODE_AUDIO_RECORD
                )
            }
        }
    }

    private fun playAudioVoice() {
        mediaPlayer?.start()
    }

    private fun releaseAudio(emptyMediaPlayer: Boolean = true) {
        mediaPlayer?.release()
        if (emptyMediaPlayer) mediaPlayer = null
    }

    private fun prepareMediaPlayer(urlAudio: String) {
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        try {
            mediaPlayer?.setAudioAttributes(attribute)
            mediaPlayer?.setDataSource(urlAudio) // URL music file
            mediaPlayer?.prepare()
        } catch (e: Exception) {
            Log.e(TAG, "prepareMediaPlayer: ${e.message}")
        }
    }

    private fun loader(state: Boolean) {
        with(binding.layoutQuestion) {
            if (state) {
                pbLoader.visibility = View.VISIBLE
            } else {
                pbLoader.visibility = View.GONE
            }
        }
    }

    private fun calculateScore(recognizedText: String) {
        //dg metode levenstein;ad di helper
        loader(true)
        distance = getLevenshteinDistance(recognizedText, answerText)
        val rightWrongWord = countRightWrongWord(answerText, recognizedText)
        val similarity = findSimilarity(recognizedText, answerText)
        score = (similarity * 100).toInt()
        rightWord = rightWrongWord["righWord"] ?: 0
        wrongWord = rightWrongWord["wrongWord"] ?: 0
        recognizedWord = recognizedText
        //set ke view
        with(binding.layoutQuestion) {
            tvVoiceResult.text = "Yang disebut : ${recognizedWord}"
            tvCorrectResult.text = "Benar Huruf: ${rightWord}"
            tvWrongResult.text = "Salah Huruf: ${wrongWord}"
            tvDistanceResult.text = "Hasil Rumus: ${distance}"
            tvResultScore.text = score.toString()
            tvMessageResultScore.text = kesimpulanHasil(score)
        }
        with(binding.layoutVocabQuestion) {
            tvVoiceResult.text = "Yang disebut : ${recognizedWord}"
            tvCorrectResult.text = "Benar Huruf: ${rightWord}"
            tvWrongResult.text = "Salah Huruf: ${wrongWord}"
            tvDistanceResult.text = "Hasil Rumus: ${distance}"
            tvResultScore.text = score.toString()
            tvMessageResultScore.text = kesimpulanHasil(score)
        }
        releaseAudio() //lepas audio lama

        //simpan atau update nilai skornya
        storeOrUpdate(score)
    }

    private fun kesimpulanHasil(score: Int): String {
        var mString = "-"
        when (score) {
            in 0..30 -> {
                mString = "Ucapan Sangat Kurang Sempurna, silahkan coba lagi"
            }
            in 31..50 -> {
                mString = "Ucapan Kurang Sempurna, silahkan coba lagi"
            }
            in 56..70 -> {
                mString = "Ucapan Cukup Sempurna"
            }
            in 71..85 -> {
                mString = "Ucapan Sempurna"
            }
            in 86..100 -> {
                mString = "Ucapan Sangat Sempurna, Mantap!!!"
            }
        }
        return mString
    }

    private fun storeOrUpdate(score: Int) {
        // temporary
        listSoalTerjawab[index] = StudentScore(
            0,
            score,
            recognizedWord,
            rightWord,
            wrongWord,
            distance
        )

        //db server
        val studentScore = hashMapOf<String, Any>()
        studentScore["id_siswa"] = siswa
        studentScore["id_tipe_game"] = 0
        studentScore["id_soal"] = listSoal[index].id
        studentScore["nilai"] = score
        viewModel.storeScore(studentScore).observe(viewLifecycleOwner) { response ->
            loader(false)
            if (response.data != null) {
                if (response.code == 200) {
                    showMessage(
                        requireActivity(),
                        TITLE_SUCESS,
                        "Berhasil menyimpan nilai",
                        style = MotionToast.TOAST_SUCCESS
                    )
                } else {
                    showMessage(
                        requireActivity(),
                        TITLE_ERROR,
                        response.message ?: "",
                        style = MotionToast.TOAST_ERROR
                    )
                }
            } else {
                showMessage(
                    requireActivity(),
                    TITLE_ERROR,
                    "nilai gagal disimpan",
                    style = MotionToast.TOAST_ERROR
                )
            }
        }
    }

    private fun showLayout() {
        with(binding) {
            when (materyType) {
                TIPE_HURUF_AZ, TIPE_HURUF_KONSONAN, TIPE_HURUF_VOKAL -> {
                    val layoutQuestion = layoutQuestion.root
                    layoutQuestion.visibility = View.VISIBLE
                }
                TIPE_MEMBACA -> {
                    val layoutVocab = layoutVocabQuestion.root
                    layoutVocab.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        if (mainActivity != null) mainActivity.mediaPlayer.pause()
    }

    override fun onStop() {
        super.onStop()
        releaseAudio(emptyMediaPlayer = false)
        if (mainActivity != null) mainActivity.mediaPlayer.start()
    }

    private fun dataNotFound() {
        with(binding) {
            val layoutEmpty = layoutEmpty.root
            val layoutQuestion = layoutQuestion.root
            val layoutVocab = layoutVocabQuestion.root
            layoutEmpty.visibility = View.VISIBLE
            layoutQuestion.visibility = View.GONE
            layoutVocab.visibility = View.GONE
        }
    }

    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(p0: Float) {}
    override fun onBufferReceived(p0: ByteArray?) {}

    override fun onReadyForSpeech(p0: Bundle?) {
        with(binding.layoutQuestion) {
            cardOnReadySpeech.visibility = View.VISIBLE
        }
        with(binding.layoutVocabQuestion) {
            cardOnReadySpeech.visibility = View.VISIBLE
        }
        Log.d(TAG, "onreadyspeech")
    }

    override fun onEndOfSpeech() {
        with(binding.layoutQuestion) {
            cardOnReadySpeech.visibility = View.INVISIBLE
        }
        with(binding.layoutVocabQuestion) {
            cardOnReadySpeech.visibility = View.INVISIBLE
        }
        Log.d(TAG, "onendspeech")
    }

    override fun onError(errorCode: Int) {
        val errorMessage = ErrorCodeSpeechRecognizer.getErrorCode(errorCode)
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onResults(results: Bundle?) {
        val result = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val recognizedText = result!![0]
        calculateScore(recognizedText!!.lowercase(Locale.getDefault()))

        var message = ""
        for (msg in message) {
            message += msg + "\n"
        }

        Log.d(TAG, "hasil arr index 0: ${recognizedText}")
        Log.d(TAG, "hasil seluruh arr: ${message}")
    }

    override fun onPartialResults(p0: Bundle?) {}

    override fun onEvent(p0: Int, p1: Bundle?) {}
}
