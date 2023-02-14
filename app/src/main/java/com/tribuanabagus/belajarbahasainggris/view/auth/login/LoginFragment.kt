package com.tribuanabagus.belajarbahasainggris.view.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kontakanprojects.apptkslb.local_db.Login
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentLoginBinding
import com.tribuanabagus.belajarbahasainggris.local_db.User
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.ROLE_ADMIN
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.ROLE_SISWA
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TAG
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TITLE_ERROR
import com.tribuanabagus.belajarbahasainggris.utils.showMessage
import com.tribuanabagus.belajarbahasainggris.view.auth.viewmodel.AuthViewModel
import com.tribuanabagus.belajarbahasainggris.view.dialog.LoadingDialogFragment
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.TeacherActivity
import www.sanju.motiontoast.MotionToast


class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()
    private val loginValid = true
    private var roleId: Int = 0

    private lateinit var loadingDialogFragment: LoadingDialogFragment

    companion object {
        private const val USERNAME_NOT_NULL = "Username tidak boleh kosong!"
        private const val PASSWORD_NOT_NULL = "Password tidak boleh kosong!"
        private const val MIN_COUNTER_LENGTH_PASS = "Minimal 5 karakter password"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        loadingDialogFragment = LoadingDialogFragment()

        roleId = LoginFragmentArgs.fromBundle(arguments as Bundle).role
        binding.btnRegister.visibility = if (roleId != ROLE_SISWA) View.GONE else View.VISIBLE
        // Build a GoogleSignInClient with the options specified by gso.
        with(binding) {
            edtUsername.addTextChangedListener(textWatcherUsername)
            edtPassword.addTextChangedListener(textWatcherPsw)
            btnRegister.setOnClickListener(this@LoginFragment)
            btnLogin.setOnClickListener(this@LoginFragment)
        }
    }

    private val textWatcherUsername = object : TextWatcher {
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
    }
    private val textWatcherPsw = object : TextWatcher {
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
            if (s?.length!! < 5) {
                binding.tiPassword.error = MIN_COUNTER_LENGTH_PASS
            } else if (s.isNullOrEmpty()) {
                binding.tiPassword.error = PASSWORD_NOT_NULL
            } else {
                binding.tiPassword.error = null
            }
        }
    }

    override fun onClick(view: View?) {
        with(binding) {
            when (view) {
                btnRegister -> {
                    val toRegister =
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment().apply {
                            role = roleId
                        }
                    findNavController().navigate(toRegister)
                }
                btnLogin -> {
                    val email = edtUsername.text.toString().trim()
                    val password = edtPassword.text.toString().trim()

                    when {
                        email.isEmpty() -> tiUsername.error = USERNAME_NOT_NULL
                        password.isEmpty() -> tiPassword.error = PASSWORD_NOT_NULL
                        else -> {
                            loader(true)
                            val params = HashMap<String, Any>()
                            params["email"] = email
                            params["password"] = password
                            login(params)
                        }
                    }
                }
            }
        }
    }

    private fun login(params: HashMap<String, Any>) {
        val hasObserver = viewModel.login(params).hasObservers()
        viewModel.login(params).observe(viewLifecycleOwner) { result ->
            loader(false)
            if (result != null) {
                if (result.status == 200) {
                    val data = result.results
                    UserPreference(requireContext()).apply {
                        setUser(
                            User(
                                id = data?.id,
                                nama = data?.nama,
                                role = data?.role?.id ?: 0,
                                roleName = data?.role?.nama.toString(),
                                gambar = data?.gambar,
                                email = data?.email,
                                password = data?.password
                            )
                        )
                        setLogin(Login(loginValid))
                    }

                    Log.d(TAG, "login: ${data?.role?.id}")

                    when (data?.role?.id) {
                        ROLE_ADMIN -> {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    TeacherActivity::class.java
                                )
                            )
                        }
                        ROLE_SISWA -> {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    StudentActivity::class.java
                                )
                            )
                        }
                    }
                } else {
                    showMessage(
                        requireActivity(),
                        TITLE_ERROR,
                        message = result.message ?: "",
                        style = MotionToast.TOAST_ERROR
                    )
                }
            } else {
                showMessage(
                    requireActivity(),
                    TITLE_ERROR,
                    style = MotionToast.TOAST_ERROR
                )
            }
        }
        if (hasObserver) viewModel.login(params).removeObservers(this)
    }

    override fun onPause() {
        super.onPause()
        with(binding) {
            edtUsername.removeTextChangedListener(textWatcherUsername)
            edtPassword.removeTextChangedListener(textWatcherPsw)
        }
    }

    private fun loader(state: Boolean) {
        if (state) {
            loadingDialogFragment.show(parentFragmentManager, TAG)
        } else {
            loadingDialogFragment.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}