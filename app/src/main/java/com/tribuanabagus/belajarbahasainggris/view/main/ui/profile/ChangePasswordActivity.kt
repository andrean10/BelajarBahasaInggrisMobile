package com.tribuanabagus.belajarbahasainggris.view.main.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ActivityChangePasswordBinding
import com.tribuanabagus.belajarbahasainggris.local_db.User
import com.tribuanabagus.belajarbahasainggris.model.users.ResultsUserManage
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.auth.viewmodel.AuthViewModel
import www.sanju.motiontoast.MotionToast

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var userPreference: UserPreference

    companion object {
        private const val OLD_PASSWORD_NOT_NULL = "Password Lama tidak boleh kosong!"
        private const val OLD_PASSWORD_NOT_EQUALS = "Password lama tidak sesuai!"
        private const val NEW_PASSWORD_NOT_NULL = "Password baru tidak boleh kosong!"
        private const val CONFIRM_PASSWORD_NOT_NULL = "Konfirmasi password tidak boleh kosong!"
        private const val CONFIRM_PASSWORD_NOT_EQUALS =
            "Konfirmasi password tidak sesuai dengan password baru!"
        private const val MIN_COUNTER_LENGTH_PASS = "Minimal 5 karakter password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareView()
    }

    private fun prepareView() {
        userPreference = UserPreference(this)
        val oldPasswordFromDB = userPreference.getUser().password ?: ""

        with(binding) {
            edtOldPsw.addTextChangedListener(object : TextWatcher {
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
                    tiOldPsw.error = when {
                        s?.length!! < 5 -> MIN_COUNTER_LENGTH_PASS
                        s.isNullOrEmpty() -> OLD_PASSWORD_NOT_NULL
                        oldPasswordFromDB.isNotEmpty() && oldPasswordFromDB != s.toString() -> OLD_PASSWORD_NOT_EQUALS
                        else -> null
                    }
                }
            })

            edtNewPsw.addTextChangedListener(object : TextWatcher {
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
                    tiNewPsw.error = when {
                        s?.length!! < 5 -> MIN_COUNTER_LENGTH_PASS
                        s.isNullOrEmpty() -> NEW_PASSWORD_NOT_NULL
                        else -> null
                    }
                }
            })


            edtConfirPsw.addTextChangedListener(object : TextWatcher {
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
                    val newPassword = edtNewPsw.text.toString()
                    tiConfirmPsw.error = when {
                        s?.length!! < 5 -> MIN_COUNTER_LENGTH_PASS
                        s.isNullOrEmpty() -> CONFIRM_PASSWORD_NOT_NULL
                        newPassword.isNotEmpty() && newPassword != s.toString() -> CONFIRM_PASSWORD_NOT_EQUALS
                        else -> null
                    }
                }
            })
            btnBack.setOnClickListener(this@ChangePasswordActivity)
            btnUpdatePsw.setOnClickListener(this@ChangePasswordActivity)
        }
    }

    override fun onClick(view: View?) {
        with(binding) {
            when (view) {
                btnBack -> finish()
                btnUpdatePsw -> updatePassword()
            }
        }
    }

    private fun updatePassword() {
        loader(true)
        with(binding) {
            val userId = userPreference.getUser().id ?: 0
            val oldPsw = edtOldPsw.text.toString().trim()
            val newPsw = edtNewPsw.text.toString().trim()
            val confirPsw = edtConfirPsw.text.toString().trim()

            when {
                oldPsw.isEmpty() -> {
                    tiOldPsw.error = OLD_PASSWORD_NOT_NULL
                    loader(false)
                    return@with
                }
                newPsw.isEmpty() -> {
                    tiNewPsw.error = NEW_PASSWORD_NOT_NULL
                    loader(false)
                    return@with

                }
                confirPsw.isEmpty() -> {
                    tiConfirmPsw.error = CONFIRM_PASSWORD_NOT_NULL
                    loader(false)
                    return@with
                }
                else -> {
                    changePassword(userId, newPsw)
                }
            }
        }

    }

    private fun changePassword(id: Int, newPassword: String) {
        viewModel.updatePassword(id, newPassword).observe(this) { response ->
            loader(false)
            if (response != null) {
                if (response.status == 200) {
                    showMessage(
                        this,
                        UtilsCode.TITLE_SUCESS,
                        response.message,
                        style = MotionToast.TOAST_SUCCESS
                    )
                    val result = response.results
                    if (result != null) {
                        saveDataToPreference(result)
                        finish()
                    }
                } else {
                    showMessage(
                        this,
                        UtilsCode.TITLE_ERROR,
                        response.message,
                        style = MotionToast.TOAST_ERROR
                    )
                }
            } else {
                showMessage(
                    this,
                    UtilsCode.TITLE_ERROR,
                    getString(R.string.failed_description),
                    style = MotionToast.TOAST_ERROR
                )
            }
        }
    }

    private fun saveDataToPreference(data: ResultsUserManage) {
        UserPreference(this@ChangePasswordActivity).apply {
            setUser(
                User(
                    id = data.id,
                    nama = data.nama,
                    role = data.roleId,
                    email = data.email,
                    password = data.password,
                    gambar = data.gambar
                )
            )
        }
    }

    private fun loader(state: Boolean) {
        with(binding) {
            if (state) {
                pbLoader.visibility = View.VISIBLE
            } else {
                pbLoader.visibility = View.GONE
            }
        }
    }
}