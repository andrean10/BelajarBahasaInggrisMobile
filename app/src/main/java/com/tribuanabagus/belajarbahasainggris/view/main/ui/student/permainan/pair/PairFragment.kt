package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.permainan.pair

import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.adapter.ImagePairQAdapter
import com.tribuanabagus.belajarbahasainggris.adapter.TextPairQAdapter
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentPairBinding
import com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan.ResultsTemukanPasangan
import com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan.jawaban_pasangan.ResultsJawabanPasangan
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TITLE_WARNING
import com.tribuanabagus.belajarbahasainggris.utils.miliSecondToTimer
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.ResultFragment.Companion.PAIR_TYPE
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.permainan.pair.viewmodel.PairViewModel
import www.sanju.motiontoast.MotionToast

class PairFragment : Fragment() {
    private lateinit var binding: FragmentPairBinding

    private var selectedImage: ImageView? = null
    private var listSelectedImg = ArrayList<ImageView>()
    private var listSelectedText = ArrayList<TextView>()
    private var listSelectedPair = ArrayList<Pair<ImageView, String>>()
    private var listPair = ArrayList<ResultsTemukanPasangan>()
    private var listGuess = ArrayList<ResultsJawabanPasangan>()
    private var listText = ArrayList<String>()
    private var selectedPair: Pair<ImageView, String>? = null

    private var isTextBeingSelected = false
    private var countDeletedLines = 0

    private var x1 = 0f
    private var y1 = 0f

    private var score: Int = 0
    private var index = 0
    private var nextQ = 1
    private var totalQ = 0
    private var indexProgress = 1

    private var indexPair = 1
    private var totalPairs = 0

    private var countTotalPairs = 0

    private lateinit var countDownTimer: CountDownTimer
    private var timeLeft: Long = 120000 //180000 = 3minute per question

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var imgAdapter: ImagePairQAdapter
    private lateinit var textAdapter: TextPairQAdapter

    private val viewModel by viewModels<PairViewModel>()
    private lateinit var mainActivity: StudentActivity

    private val TAG = PairFragment::class.java.simpleName

    companion object {
        private const val CLICKED_TEXT = 100
        private const val CLICKED_IMAGE = 200
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        binding = FragmentPairBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        with(binding) {
            loader(true)
            //hapus baris jika ada
            drawView.clearLines()
            observePair()
        }
    }

    private fun prepareQuestion() {
        with(binding) {
            val pairQ = listPair[index]
            totalPairs = pairQ.jawabanPasangan?.size ?: 0

            if (totalPairs == 0) {
                if (index != listPair.size - 1 && index < listPair.size) {
                    nextPairQ()
                } else {
                    val studentId = UserPreference(requireActivity()).getUser().id

                    if (listPair.size == 1) {
                        dataNotFound(isPairsEmpty = true)
                    } else {
                        moveToResult()
                    }
                }
            } else {
                countTotalPairs += totalPairs

                //prepare audio
                mediaPlayer = MediaPlayer()
                val urlAudio = ApiConfig.URL_FILES + pairQ.suara
                prepareMediaPlayer(urlAudio)

                val (listImg, listText) = prepareDataQ(pairQ)

                //init adapter
                imgAdapter = ImagePairQAdapter(requireActivity(), 0, listImg)
                textAdapter = TextPairQAdapter(requireActivity(), 0, listText)

                lvImg.adapter = imgAdapter
                lvText.adapter = textAdapter

                lvImg.isScrollContainer = false
                lvText.isScrollContainer = false

                imgAdapter.setOnImageClickCallback(object : ImagePairQAdapter.OnImageClickCallback {
                    override fun onImageClicked(view: ImageView, imgPoint: ImageView) {
                        if (selectedImage != view) {
                            val isImageHadSelected = findImage(listSelectedPair, view)
                            if (!isImageHadSelected) {
                                selectedImage = view
                                drawStartLine(imgPoint)
                            } else {
                                showMessage(
                                    requireActivity(),
                                    TITLE_WARNING,
                                    "pilihlah gambar yang belum dipasangkan",
                                    MotionToast.TOAST_WARNING
                                )
                            }
                        } else {
                            showMessage(
                                requireActivity(),
                                TITLE_WARNING,
                                "gambar sudah dipilih, pilihlah pasangan katanya",
                                MotionToast.TOAST_WARNING
                            )
                        }
                    }
                })
                textAdapter.setOnTextClickCallback(object : TextPairQAdapter.OnTextClickCallback {
                    override fun onTextClicked(
                        textView: TextView,
                        view: ImageView,
                        position: Int
                    ) {
                        Log.d(TAG, "posisi text $position")
//                        Log.d(TAG,"isTextBeingSelected: ${isTextBeingSelected.toString()}")
                        val isTextViewHadSelected = findText(listSelectedPair, textView)

                        if (!isTextViewHadSelected && !isTextBeingSelected) {
                            val stateDraw = drawDestLine(view)
                            Log.d(TAG, "state draw dest line: $stateDraw")
                            if (stateDraw) {
                                checkPair(textView.text.toString())

                                //change the text point to remover line
                                view.setImageResource(R.drawable.ic_red_close)
                                view.setBackgroundColor(Color.BLACK)
                            }
                        } else {
                            if (selectedImage == null) {
                                //change the remover line to text point
                                view.setImageResource(R.drawable.bg_shape_button_blue)
                                view.setBackgroundColor(Color.TRANSPARENT)

                                //remove the line
                                removeLine(view)

                                //remove text from selected text list
                                val text = textView.text.toString().trim()
                                val filteredPairs = listSelectedPair.filterNot {
                                    it.second == text
                                }
                                listSelectedPair.clear()
                                listSelectedPair.addAll(filteredPairs)
                            } else {
                                showMessage(
                                    requireActivity(),
                                    TITLE_WARNING,
                                    "pilih kata yg belum dipasangkan",
                                    MotionToast.TOAST_WARNING
                                )
                            }

                        }
                    }

                })
                //seekbar
                seekBar.setOnTouchListener { _, _ -> true }
                seekBar.max = 0
                seekBar.max = listPair.size
                seekBar.progress = indexProgress
                tvCorrectValue.text = "Benar\n${score}"

                //automatically play sounds and timer
                playAudioVoice()
                startTimer()

                //btn sound
                btnSound.setOnClickListener { playAudioVoice() }

                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                drawView.setDensitiyDpi(displayMetrics.densityDpi)
            }
        }
    }

    private fun nextPairQ() {
        reset()
        index++
        indexProgress++
        prepareQuestion()
    }

    //prepare list gambar n kata n shuffle it
    private fun prepareDataQ(result: ResultsTemukanPasangan): Pair<List<ResultsJawabanPasangan>, List<String>> {
        //for data image
        val pairsItem = result.jawabanPasangan
        val shuffledPairs = pairsItem!!.shuffled()

        //for data text
        val listText = ArrayList<String>()
        for ((idx, value) in pairsItem.withIndex()) {
            listText.add(value.kata!!)
        }
        val shuffledText = listText.shuffled()

        return Pair(shuffledPairs, shuffledText)
    }

    private fun drawStartLine(view: ImageView) {
        val coordinates = IntArray(2)
        view.getLocationOnScreen(coordinates)
        val x1 = coordinates[0].toFloat()
        val y1 = coordinates[1].toFloat()

        binding.drawView.addSourcePoint(x1, y1)
        isTextBeingSelected = false

        Log.d(TAG, "img point position x1:${x1},y1:${y1}")
    }

    private fun drawDestLine(view: ImageView): Boolean {
        if (selectedImage != null) {
            val coordinates = IntArray(2)
            view.getLocationInWindow(coordinates)
            val x2 = coordinates[0].toFloat()
            val y2 = coordinates[1].toFloat()
            binding.drawView.addDestinationPoint(x2, y2)

            isTextBeingSelected = true

            Log.d(TAG, "text point position x2:${x2},y2:${y2}")

            return true
        } else {
            showMessage(
                requireActivity(),
                TITLE_WARNING,
                "pilih dulu gambar yang ingin dipasangkan",
                MotionToast.TOAST_WARNING
            )
            return false
        }
    }

    private fun removeLine(view: ImageView) {
        isTextBeingSelected = false
        indexPair--

        val coordinates = IntArray(2)
        view.getLocationInWindow(coordinates)
        val x2 = coordinates[0].toFloat()
        val y2 = coordinates[1].toFloat()
        binding.drawView.removeLine(x2, y2)

        Log.d(TAG, "removeline() called")

    }

    private fun checkPair(text: String) {
        val imgPair = selectedImage!!.contentDescription.trim()
        val textPair = text.trim()
        if (textPair == imgPair) {
            score++
        }
        //save pair
        selectedPair = Pair(selectedImage!!, text)
        listSelectedPair.add(selectedPair!!)

        selectedImage = null //update state image to null after finished using

        //reset after all pairing finished and move index PairQ
        if (indexPair == totalPairs) {
            //cek dulu ada ndak soal selanjutnya
            if (index != listPair.size - 1 && index < listPair.size) {
                loader(true)
                //delay 1 second
                Handler(Looper.getMainLooper()).postDelayed({
                    loader(false)
                    nextPairQ()
                }, 1000)
            } else {
                loader(true)
                reset()
                moveToResult()
            }
        } else {
            indexPair++
            Log.d(TAG, "content description img : ${imgPair}")
            Log.d(TAG, "skor saat ini : ${score}")
        }
        Log.d(TAG, "jumlah gambar yg terpilih ${listSelectedPair.size}")
    }

    private fun observePair() {
        viewModel.getTemukanPasangan().observe(viewLifecycleOwner) { response ->
            loader(false)
            if (response != null) {
                if (response.status == 200) {
                    val result = response.results
                    if (result != null) {
                        listPair.addAll(result)
                        prepareQuestion()
                    } else {
                        dataNotFound()
                    }
                } else {
                    dataNotFound()
                }
            } else {
                showMessage(
                    requireActivity(),
                    getString(R.string.failed_title),
                    getString(R.string.failed_description),
                    MotionToast.TOAST_ERROR
                )
            }
        }
    }

    private fun moveToResult() {
        val toResult = PairFragmentDirections.actionPairFragmentToResultFragment().apply {
            totalQuestion = countTotalPairs
            correctNumber = score
            type = PAIR_TYPE
        }
        Handler(Looper.getMainLooper()).postDelayed({
            loader(false)
            findNavController().navigate(toResult)
        }, 200)//0.2s
    }

    private fun findImage(list: List<Pair<ImageView, String>>, view: View): Boolean {
        return list.filter { it.first == view }.isNotEmpty()
    }

    private fun findText(list: List<Pair<ImageView, String>>, view: TextView): Boolean {
        return list.filter { it.second == view.text.toString().trim() }.isNotEmpty()
    }

    private fun cancelTimer() {
        if (countDownTimer != null) countDownTimer.cancel()
    }

    private fun startTimer() {
        with(binding) {
            countDownTimer = object : CountDownTimer(timeLeft, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    tvTimer.text = miliSecondToTimer(millisUntilFinished)
                }

                override fun onFinish() {
                    showMessage(
                        requireActivity(),
                        "Waktu Habis",
                        "Sayang sekali kesempatanmu untuk menjawab habis",
                        MotionToast.TOAST_WARNING
                    )
                    reset()
                    index++
                    indexProgress++
                    prepareQuestion()
                }
            }.start()
        }
    }

    private fun playAudioVoice() {
        mediaPlayer?.start()
    }

    private fun reset() {
        //clear all selected condition
        listSelectedImg.clear()
        listSelectedText.clear()
        binding.drawView.clearLines()

        //reset index
        indexPair = 1

        //clear audio & timer
        releaseAudio()
        cancelTimer()
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

    private fun dataNotFound(isPairsEmpty: Boolean = false) {
        with(binding) {
            if (isPairsEmpty) {
                layoutEmpty.tvEmptyMessage.text = "Data pasangan tidak ditemukan"
            }
            val layoutEmpty = layoutEmpty.root
            layoutEmpty.visibility = View.VISIBLE
            imgAlarm.visibility = View.GONE
            tvTimer.visibility = View.GONE
            btnSound.visibility = View.GONE
            imgRight.visibility = View.GONE
            imgRight.visibility = View.GONE
            tvCorrectValue.visibility = View.GONE
        }
    }

    private fun loader(state: Boolean) {
        with(binding) {
            if (state) {
                pbLoader.visibility = View.VISIBLE
            } else {
                pbLoader.visibility = android.view.View.GONE
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
        if (countDownTimer != null) cancelTimer()
        if (mainActivity != null) mainActivity.mediaPlayer.start()
    }
}