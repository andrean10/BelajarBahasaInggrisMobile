package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.adapter

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
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ItemIsiHafalanBinding
import com.tribuanabagus.belajarbahasainggris.model.soal.ResultsSoal
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.*
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TAG
import www.sanju.motiontoast.MotionToast
import java.util.*

class ItemIsiHafalanAdapter(private val fragmentActivity: FragmentActivity) :
    RecyclerView.Adapter<ItemIsiHafalanAdapter.ItemIsiHafalanViewHolder>(),
    TextToSpeech.OnInitListener {

    private var _binding: ItemIsiHafalanBinding? = null
    private val binding get() = _binding!!

    private var userPreference: UserPreference = UserPreference(fragmentActivity)
    private var mediaPlayer: MediaPlayer? = null
    private val speechRecognizer: SpeechRecognizer =
        SpeechRecognizer.createSpeechRecognizer(fragmentActivity)
    private val tts: TextToSpeech = TextToSpeech(fragmentActivity, this)

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val data = arrayListOf<ResultsSoal>()

    fun setData(listData: List<ResultsSoal>?) {
        if (listData == null) return
        data.clear()
        data.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d(TAG, "onInit: Engines =  ${tts.engines}")
            Log.d(TAG, "onInit: Voice = ${tts.voice}")
            Log.d(TAG, "onInit: Banyak Suara = ${tts.voices}")
            tts.setSpeechRate(0.6f)
            val lang = tts.setLanguage(Locale.ENGLISH)


            if (lang == TextToSpeech.LANG_MISSING_DATA
                || lang == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e(TAG, "This Language is not supported");
            }
        } else {
            Log.e(TAG, "onInit: Init TTS gagal");
        }
    }

    private fun speakOut(value: String) {
        tts.speak(value, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemIsiHafalanViewHolder {
        _binding =
            ItemIsiHafalanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemIsiHafalanViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemIsiHafalanViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ItemIsiHafalanViewHolder(private val binding: ItemIsiHafalanBinding) :
        RecyclerView.ViewHolder(binding.root), RecognitionListener {

        private var answerText = ""
        private var idSoal = 0

        fun bind(data: ResultsSoal) {
            itemView.setOnLongClickListener {
                onItemClickCallBack?.onItemLongClicked(data)
                true
            }

            var expand = false

            with(binding) {
                tvEnIsiHafalan.text = data.title
                tvIdIsiHafalan.text = data.subtitle

                answerText =
                    if (data.title != null) data.title.removePunctuation().lowercase() else ""
                idSoal = data.id ?: 0

                with(layoutJawabSoal) {
                    tvTitle.text = data.title
                    tvSubtitle.text = data.subtitle
                    btnPlaySound.setOnClickListener {
//                        loaderSound(true)
                        speakOut(data.title.toString())

//                        if (!checkPlayMediaPlayer()) {
//                            val url = ApiConfig.URL_FILES + data.sounds
//                            prepareMediaPlayer(url)
//                        }
                    }
                    btnSpeech.setOnClickListener {
//                        mediaPlayer?.stop()
                        openSpeechRecord()
                    }
                }

                itemView.setOnClickListener {
                    expand = !expand
                    isExpand(expand)
                }
            }
        }

        private fun loaderSound(state: Boolean) {
            with(binding.layoutJawabSoal) {
                btnPlaySound.alpha = if (state) 0.6f else 1f
                pbSound.visibility = if (state) View.VISIBLE else View.GONE
            }
        }

        private fun isExpand(state: Boolean) {
            with(binding) {
                if (state) {
                    TransitionManager.beginDelayedTransition(
                        layoutJawabSoal.cdJawabSoal,
                        AutoTransition()
                    )
                    layoutJawabSoal.cdJawabSoal.visibility = View.VISIBLE
                    icArrow.setImageResource(R.drawable.ic_arrow_down)
//                    checkPlayMediaPlayer()
                } else {
                    layoutJawabSoal.cdJawabSoal.visibility = View.GONE
                    icArrow.setImageResource(R.drawable.ic_arrow_right)
                    checkPlayMediaPlayer()
                }
            }
        }

        private fun checkPlayMediaPlayer(): Boolean {
            Log.d(TAG, "checkPlayMediaPlayer: ${mediaPlayer?.isPlaying}")
            if (mediaPlayer?.isPlaying == true) {
                releaseAudio(true)
                return true
            }
            return false
        }

        private fun prepareMediaPlayer(urlAudio: String) {
            mediaPlayer = MediaPlayer()

            val attribute = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            try {
                mediaPlayer?.setAudioAttributes(attribute)
                mediaPlayer?.setDataSource(urlAudio) // URL music file
                mediaPlayer?.prepare()
                playAudioVoice()
            } catch (e: Exception) {
                Log.e(TAG, "prepareMediaPlayer: ${e.message}")
            }
        }

        private fun playAudioVoice() {
            mediaPlayer?.start()
            loaderSound(false)
        }

        private fun pauseAudioVoice() {
            mediaPlayer?.pause()
        }

        private fun openSpeechRecord() {
            checkPermissionAudioRecord()
            speechRecognizer.setRecognitionListener(this)
            // Get the Intent action
            val recIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).also {
                it.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                it.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH
                )
            }
            //start listening
            speechRecognizer.startListening(recIntent)
        }

        private fun calculateScore(recognizedText: String) {
            //dg metode levenstein;ad di helper
//        loader(true)
            Log.d(TAG, "calculateScore: Answer Text = $answerText")
            val distance = getLevenshteinDistance(recognizedText, answerText)
            val rightWrongWord = countRightWrongWord(answerText, recognizedText)
            val similarity = findSimilarity(recognizedText, answerText)
            val score = (similarity * 100).toInt()
            val rightWord = rightWrongWord["righWord"] ?: 0
            val wrongWord = rightWrongWord["wrongWord"] ?: 0
            //set ke view
            with(binding.layoutJawabSoal) {
                tvVoiceResult.text = "Disebut : $recognizedText"
                tvCorrectResult.text = "Benar Huruf: $rightWord"
                tvWrongResult.text = "Salah Huruf: $wrongWord"
                tvDistanceResult.text = "Hasil Rumus: $distance"
                tvResultScore.text = score.toString()
                tvMessageResultScore.text = kesimpulanHasil(score)
            }
            releaseAudio(false) //lepas audio lama

            //simpan atau update nilai skornya
            storeOrUpdate(UtilsCode.STORE, score)
        }

        private fun storeOrUpdate(type: Int, score: Int) {
            val params: HashMap<String, Any> = hashMapOf(
                "id_siswa" to (userPreference.getUser().id ?: 0),
                "id_soal" to idSoal,
                "nilai" to score
            )
            onItemClickCallBack?.onItemClicked(type, params)
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

        override fun onReadyForSpeech(params: Bundle?) {
            binding.layoutJawabSoal.cardOnReadySpeech.visibility = android.view.View.VISIBLE
        }

        override fun onBeginningOfSpeech() {
        }

        override fun onRmsChanged(rmsdB: Float) {
        }

        override fun onBufferReceived(buffer: ByteArray?) {
        }

        override fun onEndOfSpeech() {
            binding.layoutJawabSoal.cardOnReadySpeech.visibility = View.INVISIBLE
        }

        override fun onError(error: Int) {
            val errorMessage = ErrorCodeSpeechRecognizer.getErrorCode(error)
            showMessage(
                fragmentActivity,
                fragmentActivity.getString(R.string.failed_title),
                errorMessage,
                MotionToast.TOAST_ERROR,
            )
        }

        override fun onResults(results: Bundle?) {
            val result = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val recognizedText = result!![0]
            calculateScore(recognizedText!!.lowercase(Locale.ENGLISH))

            var message = ""
            for (msg in message) {
                message += msg + "\n"
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
        }
    }

    fun releaseAudio(emptyMediaPlayer: Boolean) {
        mediaPlayer?.release()
        if (emptyMediaPlayer) mediaPlayer = null
    }

    fun releaseTTS() {
        tts.stop()
        tts.shutdown()
    }

    private fun checkPermissionAudioRecord() {
        if (ContextCompat.checkSelfPermission(
                fragmentActivity,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    fragmentActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    UtilsCode.REQUEST_CODE_AUDIO_RECORD
                )
            }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(type: Int, params: HashMap<String, Any>)
        fun onItemLongClicked(data: ResultsSoal)
    }
}
