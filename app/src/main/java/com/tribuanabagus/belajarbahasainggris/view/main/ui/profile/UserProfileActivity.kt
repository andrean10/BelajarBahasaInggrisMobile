package com.tribuanabagus.belajarbahasainggris.view.main.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ActivityUserProfileBinding
import com.tribuanabagus.belajarbahasainggris.local_db.User
import com.tribuanabagus.belajarbahasainggris.model.user.Data
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TITLE_WARNING
import com.tribuanabagus.belajarbahasainggris.utils.createPartFromString
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.auth.viewmodel.AuthViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import www.sanju.motiontoast.MotionToast
import java.io.File

class UserProfileActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var binding: ActivityUserProfileBinding
    private val viewModel by viewModels<AuthViewModel>()

    private lateinit var user: User

    private var isImageExist = false
    private var imageUri: Uri? = null
    private var imagePath: String? = null

//    private lateinit var audioRaw: AssetFileDescriptor
//    lateinit var mediaPlayer: MediaPlayer
    private var isReady: Boolean = false

    private val TAG = UserProfileActivity::class.simpleName

//    private lateinit var BackgroundSound: BackgroundSound


    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 111
        private const val REQUEST_CODE_SELECT_IMAGE = 333
        private const val MUST_PICK_IMAGE = "Gambar soal harus dipilih, tidak boleh kosong!"
        private const val FULLNAME_NOT_NULL = "Nama tidak boleh kosong!"
        private const val USERNAME_NOT_NULL = "Username tidak boleh kosong!"
        private const val PASSWORD_NOT_NULL = "Password tidak boleh kosong!"
        private const val MIN_COUNTER_LENGTH_PASS = "Minimal 5 karakter password"
        const val TYPE = "type"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        mediaPlayer = MediaPlayer()
//        audioRaw = applicationContext.resources.openRawResourceFd(R.raw.music_app)
//
//        prepareMediaPlayer()

        prepareView()
    }

    private fun prepareView() {
        with(binding){
            prepareViewWithData(UserPreference(this@UserProfileActivity))

            edtFullname.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length!! == 0) {
                        binding.tiFullname.error = FULLNAME_NOT_NULL
                    } else {
                        binding.tiFullname.error = null
                    }
                }
            })
            edtUsername.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length!! == 0) {
                        binding.tiUsername.error = USERNAME_NOT_NULL
                    } else {
                        binding.tiUsername.error = null
                    }
                }
            })
            pickImage.setOnClickListener(this@UserProfileActivity)
            btnUpdateProfile.setOnClickListener(this@UserProfileActivity)
            btnEditPsw.setOnClickListener(this@UserProfileActivity)
            btnBack.setOnClickListener(this@UserProfileActivity)
        }
    }

    override fun onClick(view: View?) {
        with(binding){
            when(view){
                pickImage -> selectImage()
                btnBack -> finish()
                btnEditPsw -> {
                    val intent = Intent (this@UserProfileActivity,ChangePasswordActivity::class.java)
                    startActivity(intent)
                }
                btnUpdateProfile -> {
                    loader(true)
                    val fullName = edtFullname.text.toString().trim()
                    val email = edtUsername.text.toString().trim()

                    when{
                        !isImageExist -> {
                            showMessage(
                                activity = this@UserProfileActivity,
                                title = TITLE_WARNING,
                                message = MUST_PICK_IMAGE,
                                style = MotionToast.TOAST_WARNING
                            )
                            return@with
                            loader(false)
                        }
                        fullName.isEmpty() -> {
                            tiFullname.error = FULLNAME_NOT_NULL
                        }
                        email.isEmpty() -> tiUsername.error = USERNAME_NOT_NULL
                        else -> {
                            var params = HashMap<String, RequestBody>()
                            params.put("id", createPartFromString(user.id.toString()))
                            params.put("nama", createPartFromString(fullName))
                            params.put("email", createPartFromString(email))
                            params.put("role", createPartFromString(user.role.toString()))

                            when{
                                isImageExist && imagePath == null -> updateProfile(reqFileImageEmpty(),params)
                                else -> updateProfile(reqFileImage(),params)
                            }

                        }
                    }
                }
            }
        }
    }

    private fun updateProfile(bodyImage: MultipartBody.Part,params: HashMap<String,RequestBody>) {
        viewModel.updateProfile(bodyImage,params).observe(this) { response ->
            loader(false)
            if (response.data != null) {
                if (response.code == 200) {
                    showMessage(
                        this@UserProfileActivity,
                        UtilsCode.TITLE_SUCESS,
                        response.message ?: "",
                        style = MotionToast.TOAST_SUCCESS
                    )
                    val result = response.data
                    saveDataToPreference(result)
                    finish()
                } else {
                    showMessage(
                        this@UserProfileActivity,
                        UtilsCode.TITLE_ERROR,
                        response.message ?: "",
                        style = MotionToast.TOAST_ERROR
                    )
                }
            } else {
                showMessage(
                    this@UserProfileActivity,
                    UtilsCode.TITLE_ERROR,
                    response.message ?: "",
                    style = MotionToast.TOAST_ERROR
                )
            }
        }
    }

    private fun saveDataToPreference(data: Data) {
        UserPreference(this@UserProfileActivity).apply {
            setUser(
                User(
                    id = data?.id,
                    nama = data?.nama,
                    role = data.role,
                    gambar = data?.gambar,
                    email = data?.email,
                    password = data?.password
                )
            )
        }
    }

    fun prepareViewWithData(preference: UserPreference){
        user = preference.getUser()
        with(binding){
            Glide.with(this@UserProfileActivity)
                .load(ApiConfig.URL_IMAGE + user.gambar)
                .error(R.drawable.no_profile_images)
                .into(imgProfile)
            isImageExist = true
            edtFullname.setText(user.nama)
            edtUsername.setText(user.email)
        }
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

    private var resultLauncherImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val selectedImage = data.data
                    if (selectedImage != null) {
                        with(binding) {
                            imageUri = data?.data
                            imagePath = getPathImage(imageUri!!)

                            //circle imgview hanya bs load dg glide/picasso (?)
                            Glide.with(this@UserProfileActivity)
                                .load(imageUri)
                                .error(R.drawable.no_profile_images)
                                .into(imgProfile)
                            isImageExist = true
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
                    Manifest.permission.READ_EXTERNAL_STORAGE
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

//    private fun prepareMediaPlayer() {
//        val attribute = AudioAttributes.Builder()
//            .setUsage(AudioAttributes.USAGE_MEDIA)
//            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//            .build()
//        try {
//            mediaPlayer?.setAudioAttributes(attribute)
//            mediaPlayer?.setDataSource(audioRaw.fileDescriptor,audioRaw.startOffset,audioRaw.length)
//            mediaPlayer?.prepare()
//        } catch (e: Exception) {
//            Log.e(TAG, "prepareMediaPlayer: ${e.message}")
//        }
//
//        mediaPlayer?.setOnPreparedListener {
//            isReady = true
//            mediaPlayer?.start()
//        }
//        mediaPlayer?.setOnErrorListener { _, _, _ -> false }
//    }

//    override fun onStart() {
//        super.onStart()
//        BackgroundSound.mediaPlayer.start()
//        Log.d(TAG,"onstart")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        BackgroundSound.mediaPlayer.pause()
//        Log.d(TAG,"onstop")
//    }
//
//    override fun onDestroy() {
//        BackgroundSound.mediaPlayer.release()
//        super.onDestroy()
//
//        Log.d(TAG,"ondestroy")
//    }

}