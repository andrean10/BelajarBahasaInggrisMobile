package com.tribuanabagus.belajarbahasainggris.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.session.UserPreference
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.ROLE_ADMIN
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.ROLE_SISWA
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TIME_DELAY_SCREEN
import com.tribuanabagus.belajarbahasainggris.view.auth.AuthActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity
import com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.TeacherActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_splash)

        activityScope.launch {
            delay(TIME_DELAY_SCREEN)

            // check session auth
            val userPreference = UserPreference(this@SplashActivity)
            if (userPreference.getLogin()) {
                if (userPreference.getUser().role == ROLE_ADMIN) {
                    startActivity(Intent(this@SplashActivity, TeacherActivity::class.java))
                    finish()
                } else if (userPreference.getUser().role == ROLE_SISWA) {
                    startActivity(Intent(this@SplashActivity, StudentActivity::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this@SplashActivity, StudentActivity::class.java))
                finish()
            }
        }
    }
}