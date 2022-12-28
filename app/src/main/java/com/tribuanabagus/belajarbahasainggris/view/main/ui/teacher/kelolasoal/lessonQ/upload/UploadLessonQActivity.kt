package com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.kelolasoal.lessonQ.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ActivityUploadLessonQBinding
import com.tribuanabagus.belajarbahasainggris.local_db.QuestionStudyClass
import com.tribuanabagus.belajarbahasainggris.model.questions.Question
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import com.tribuanabagus.belajarbahasainggris.network.UploadRequestBody
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TITLE_ERROR
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TITLE_SUCESS
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TITLE_WARNING
import com.tribuanabagus.belajarbahasainggris.utils.createPartFromString
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import www.sanju.motiontoast.MotionToast
import java.io.File


class UploadLessonQActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUploadLessonQBinding
    private val viewModel by viewModels<UploadLessonQViewModel>()
    private var listTeksJawaban =  ArrayList<String>()

    private var tipeMateriSoal = 0

    private var imagePath: String? = null
    private var audioPath: String? = null

    private var idQuestion = 0
    private var idMatery = 0
    private var type = 0
    private var isAudioExist = false
    private var isImageExist = false

    private var imageUri: Uri? = null

    private var mediaPlayer: MediaPlayer? = null
    private val TAG = UploadLessonQActivity::class.simpleName


    companion object {
        const val TYPE = "type"
        const val REQUEST_ADD = 30
        const val REQUEST_EDIT = 40
        const val EXTRA_DATA_QUESTION = "extra_data_question"
        const val EXTRA_DATA_MATERY_ID = "extra_data_matery_id"
        const val TYPE_VOWEL = "type_vowel"
        private const val REQUEST_CODE_PERMISSIONS = 111
        private const val REQUEST_CODE_SELECT_IMAGE = 333
        private const val REQUEST_CODE_SELECT_AUDIO = 444
        private const val AUDIO = 200
        private const val MUST_PICK_IMAGE = "Gambar soal harus dipilih, tidak boleh kosong!"
        private const val MUST_PICK_AUDIO = "Audio soal harus dipilih, tidak boleh kosong!"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityUploadLessonQBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareView()
    }

    private fun prepareView() {
        loader(true) //Kasih notif klu sdg cek apa data ada
        with(binding){
            btnBack.setOnClickListener(this@UploadLessonQActivity)
            btnSimpan.setOnClickListener(this@UploadLessonQActivity)
            pickImage.setOnClickListener(this@UploadLessonQActivity)
            pickAudio.setOnClickListener(this@UploadLessonQActivity)
            btnReselectImg.setOnClickListener(this@UploadLessonQActivity)
            btnReselectAudio.setOnClickListener(this@UploadLessonQActivity)
            audioPreviewUpload.setOnClickListener(this@UploadLessonQActivity)

            //cek jika ada datanya
            if(intent.extras != null){
                idMatery = intent.getIntExtra(EXTRA_DATA_MATERY_ID,0)
                val isTypeVowel = intent.getBooleanExtra(TYPE_VOWEL,false)
                if(isTypeVowel){
                    loader(false)
                }else{
                    //untuk data voka
                    val question = intent.getParcelableExtra<QuestionStudyClass>(EXTRA_DATA_QUESTION)
                    if(question == null){
                        getQuestions(idMatery)
                    }else{
                        prepareViewWithData(question)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onClick(view: View?) {
        with(binding){
            when(view){
                btnSimpan -> saveQuestion()
                pickImage -> selectImage()
                pickAudio -> selectAudio()
                btnReselectImg -> {
                    isImageExist = false
                    imgPreviewUpload.visibility = View.GONE
                    pickImage.visibility = View.VISIBLE
                    tvGambar.visibility = View.VISIBLE
                    btnReselectImg.visibility = View.GONE
                }
                btnReselectAudio -> {
                    isAudioExist = false
                    audioPreviewUpload.visibility = View.GONE
                    pickAudio.visibility = View.VISIBLE
                    tvSuara.visibility = View.VISIBLE
                    btnReselectAudio.visibility = View.GONE

                    //clear audio
                    releaseAudio()
                    audioPath = null
                }
                audioPreviewUpload -> {
                    //play audio
                    mediaPlayer?.start()
                }
                btnBack -> finish()
                else -> {}
            }
        }
    }

    private fun prepareViewWithData(item: Any? = null) {
        with(binding){
            if(item != null){
                when(item){
                    is Question -> {
                        idQuestion = item.id
                        edtTeksJawaban.setText(item.teksJawaban)

                        //image
                        Glide.with(this@UploadLessonQActivity)
                            .load(ApiConfig.URL_IMAGE + item.gambar)
                            .error(R.drawable.no_profile_images)
                            .into(imgPreviewUpload)
                        imgPreviewUpload.visibility = View.VISIBLE
                        btnReselectImg.visibility = View.VISIBLE
                        pickImage.visibility = View.GONE
                        isImageExist = true

                        //audio
                        audioPreviewUpload.visibility = View.VISIBLE
                        btnReselectAudio.visibility = View.VISIBLE
                        pickAudio.visibility = View.GONE
                        tvNamaFileAudio.text = item.suara

                        //Prepare Voice
                        mediaPlayer = MediaPlayer()
                        val urlAudio = ApiConfig.URL_SOUNDS + item.suara
                        prepareMediaPlayer(urlAudio)
                    }
                    is QuestionStudyClass -> {
                        idQuestion = item.id!!
                        edtTeksJawaban.setText(item.teksJawaban)

                        //image
                        Glide.with(this@UploadLessonQActivity)
                            .load(ApiConfig.URL_IMAGE + item.gambar)
                            .error(R.drawable.no_profile_images)
                            .into(imgPreviewUpload)
                        imgPreviewUpload.visibility = View.VISIBLE
                        btnReselectImg.visibility = View.VISIBLE
                        pickImage.visibility = View.GONE
                        isImageExist = true

                        //audio
                        audioPreviewUpload.visibility = View.VISIBLE
                        btnReselectAudio.visibility = View.VISIBLE
                        pickAudio.visibility = View.GONE
                        tvNamaFileAudio.text = item.suara

                        //Prepare Voice
                        mediaPlayer = MediaPlayer()
                        val urlAudio = ApiConfig.URL_SOUNDS + item.suara
                        prepareMediaPlayer(urlAudio)
                    }
                }
            }
        }
        loader(false)
    }

    private fun saveQuestion() {
        with(binding) {
            val teksJawaban = edtTeksJawaban.text.toString().trim()
             when {
                    teksJawaban.isEmpty() -> {
                        showMessage(
                            this@UploadLessonQActivity,
                            TITLE_WARNING,
                            "Teks jawaban soal tidak boleh kosong!",
                            style = MotionToast.TOAST_WARNING
                        )
                        return@with
                    }
                    !isImageExist -> {
                     showMessage(
                         activity = this@UploadLessonQActivity,
                         title = TITLE_WARNING,
                         message = MUST_PICK_IMAGE,
                         style = MotionToast.TOAST_WARNING
                     )
                     return@with
                 }
                    !isAudioExist -> {
                        showMessage(
                            activity = this@UploadLessonQActivity,
                            title = TITLE_WARNING,
                            message = MUST_PICK_AUDIO,
                            style = MotionToast.TOAST_WARNING
                        )
                        return@with
                    }
                     else -> {
                         Log.d(TAG,"idQuestion = $idQuestion")
                        var params = HashMap<String, RequestBody>()
                        params.put("id", createPartFromString(idQuestion.toString()))
                        params.put("id_materi", createPartFromString(idMatery.toString()))
                        params.put("teks_jawaban", createPartFromString(teksJawaban))
                        when{
                            isAudioExist && audioPath == null && isImageExist && imagePath == null -> storeLessonQuestion(reqFileImageEmpty(),reqFileAudioEmpty(), params)
                            isAudioExist && audioPath == null -> storeLessonQuestion(reqFileImage(),reqFileAudioEmpty(),params)
                            isImageExist && imagePath == null -> storeLessonQuestion(reqFileImageEmpty(),reqFileAudio(),params)
                            else -> storeLessonQuestion(reqFileImage(),reqFileAudio(),params)
                        }
                    }
                }
            }
    }

    private fun storeLessonQuestion(
        bodyImage: MultipartBody.Part,
        bodyAudio: MultipartBody.Part,
        params: HashMap<String,RequestBody>
    ) {
        viewModel.uploadLessonQ(bodyImage,bodyAudio,params).observe(this) { response ->
            loader(false)
            if (response.data != null) {
                if (response.code == 200) {
                    showMessage(
                        this@UploadLessonQActivity,
                        TITLE_SUCESS,
                        "Berhasil menyimpan soal",
                        style = MotionToast.TOAST_SUCCESS
                    )
                    finish()
                } else {
                    showMessage(
                        this@UploadLessonQActivity,
                        TITLE_ERROR,
                        response.message ?: "",
                        style = MotionToast.TOAST_ERROR
                    )
                }
            } else {
                showMessage(
                    this@UploadLessonQActivity,
                    UtilsCode.TITLE_ERROR,
                    "soal belajar gagal disimpan",
                    style = MotionToast.TOAST_ERROR
                )
            }
        }
    }

    private fun getQuestions(materyId:Int) { //PASTI SATU KECUALI VOKAL
        viewModel.questions(materyId).observe(this) { response ->
            loader(false)
            if (response.data != null) {
                if (!response.data.isEmpty()) {
                    if (response.code == 200) {
                        val results = response.data
                        prepareViewWithData(results.get(0))
                    }
                } else {
                    prepareViewWithData()
                }
            } else {
                showMessage(
                    this,
                    TITLE_ERROR,
                    style = MotionToast.TOAST_ERROR
                )
            }
        }
    }

    private fun releaseAudio() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun selectAudio(){
        permission()
        mediaPlayer = MediaPlayer()
        val mimeTypes = arrayOf("audio/wav", "audio/m4a", "audio/mp3","audio/amr")
//                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI).apply {
//            type = "audio/*" //klu ditambahkan, muncul pesan "tidak ada app yg ditemukan untk aksi tersebut..". tp wajib ada klu pk ACTION_GET_CONTENT
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        resultLauncherVoice.launch(Intent.createChooser(intent, "Pilih 1 Audio"))
//        resultLauncherVoice.launch(intent)
    }

    private fun selectImage() {
        permission()
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        resultLauncherImage.launch(Intent.createChooser(intent, "Pilih 1 Gambar"))
    }

    private fun reqFileImage(): MultipartBody.Part {
        val fileImage = File(imagePath!!)
        val reqFileImage =
            fileImage.asRequestBody("image/jpeg/jpg/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "gambar", fileImage.name, reqFileImage
        )
    }

    private fun reqFileImageEmpty(): MultipartBody.Part {
        val reqFileImage = ""
            .toRequestBody("image/jpeg/jpg/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("gambar", "", reqFileImage)
    }

    private fun reqFileAudio(): MultipartBody.Part {
        val fileAudio = File(audioPath)
        val reqFileAudio = UploadRequestBody(fileAudio, "audio/mp3/wav/m4a/amr")
        return MultipartBody.Part.createFormData("suara", fileAudio.name, reqFileAudio)
    }


        private fun reqFileAudioEmpty(): MultipartBody.Part {
            val reqFileAudio = ""
                .toRequestBody("audio/mp3/wav/m4a/amr".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("suara", "", reqFileAudio)
        }

        private fun getPathImage(contentUri: Uri): String? {
            val filePath: String?
            val cursor = contentResolver?.query(contentUri, null, null, null, null)
            if (cursor == null) {
                filePath = contentUri.path
            } else {
                cursor.moveToFirst()
                val index = cursor.getColumnIndex("_data")
                filePath = cursor.getString(index)
                cursor.close()
            }
            return filePath
        }

        private fun getFilePath(type: Int, uri: Uri): String {
            lateinit var filePathColumn: Array<String>
            lateinit var filePath: String
            when (type) {
                AUDIO -> {
                    filePathColumn = arrayOf(MediaStore.Audio.Media.DATA)
                }
            }
            val cursor = contentResolver?.query(
                uri,
                filePathColumn, null, null, null
            )
            if (cursor != null) {
                cursor.moveToFirst()
                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                filePath = cursor.getString(columnIndex)
                cursor.close()
            } else {
                filePath = uri.path.toString()
            }
            return filePath
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
                isAudioExist = true
            } catch (e: Exception) {
                Log.e(TAG, "prepareMediaPlayer: ${e.message}")
                isAudioExist = false
            }
        }

        private var resultLauncherImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val selectedImage = data.data
                        if (selectedImage != null) {
                            with(binding) {
                                imageUri = data?.data
                                imagePath = getPathImage(imageUri!!)
                                imgPreviewUpload.visibility = View.VISIBLE
                                btnReselectImg.visibility = View.VISIBLE
                                pickImage.visibility = View.GONE
                                imgPreviewUpload.setImageURI(imageUri)
                                isImageExist = true
                            }
                        }
                    }
                }
            }


        private var resultLauncherVoice =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    if (data != null) {
                        val selectedVoice = data.data

                        if (selectedVoice != null) {
                            audioPath = getFilePath(AUDIO, selectedVoice)

                            with(binding) {
                                // visible player Audio
                                audioPreviewUpload.visibility = View.VISIBLE
                                btnReselectAudio.visibility = View.VISIBLE
                                pickAudio.visibility = View.GONE
                                tvNamaFileAudio.text = getFilePath(AUDIO, selectedVoice)
                                prepareMediaPlayer(audioPath!!)

                                Log.d(TAG, "Audio Path: $audioPath")
                            }
                        }
                    }
                }
            }

        // permission camera, write file, read file , and image
        private fun permission() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    ), REQUEST_CODE_PERMISSIONS
                )
            }
        }

        private fun loader(state: Boolean) {
            with(binding) {
                if (state) {
                    pbLoader.visibility = android.view.View.VISIBLE
                } else {
                    pbLoader.visibility = android.view.View.GONE
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            releaseAudio()
        }

}